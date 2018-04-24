package scripts.JProgressiveWoodcutter.tasks;

import org.tribot.api.General;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;
import scripts.JProgressiveWoodcutter.data.TreeInfo;
import scripts.JProgressiveWoodcutter.framework.Task;
import scripts.heyimjamie.abc3.ABC2;
import scripts.heyimjamie.sleep.Sleep;
import scripts.heyimjamie.webwalker_logic.WebWalker;
import scripts.heyimjamie.webwalker_logic.local.walker_engine.WalkingCondition;
import scripts.heyimjamie.webwalker_logic.local.walker_engine.interaction_handling.AccurateMouse;
import sun.reflect.generics.tree.Tree;

public class HandleWoodcutting extends Task {

    private long lastWoodcuttingWaitTime;
    private long averageWoodcuttingWaitTime;
    private long totalWoodcuttingWaitTime;
    private long totalWoodcuttingInstances;

    private RSObject tree;
    private RSObject[] trees;

    @Override
    public String status() {
        return "Woodcutting.";
    }

    @Override
    public boolean canProcess() {
        return Inventory.find(Filters.Items.nameContains(" axe")).length > 0;
    }

    @Override
    public void process() {

        switch (getTaskState()) {

            case WALK_TO_TREE_AREA:
                WebWalker.walkTo(TreeInfo.getTreeArea().getRandomTile(), () -> TreeInfo.getTreeArea().contains(Player.getPosition()) ? WalkingCondition.State.EXIT_OUT_WALKER_SUCCESS : WalkingCondition.State.CONTINUE_WALKER);
                break;

            case MOVE_TO_PREDICTED:

                if (ABC2.shouldMoveAnticipated()) {
                    RSObject[] stump = Objects.findNearest(10, "Tree stump");
                    if (stump.length >= 1)
                        ABC2.moveToAnticipated(stump[1]);
                }
                waitForTree();
                break;

            case CUT_TREE:
                if (tree != null)
                    if (AccurateMouse.click(tree, "Chop down")) {
                        ABC2.get().generateSupportingTrackerInfo((int) averageWoodcuttingWaitTime);
                        Sleep.waitCondition(() -> Player.getAnimation() != -1, 3000);
                    }
                break;

            case IDLE_WHILE_CUTTING:
                long timeStarted = System.currentTimeMillis();
                while (Player.getAnimation() != -1) {
                    ABC2.handleIdleActions();
                }
                updateStatistics(timeStarted);
                break;
        }
    }

    private enum TaskState {
        WALK_TO_TREE_AREA,
        MOVE_TO_PREDICTED,
        CUT_TREE,
        IDLE_WHILE_CUTTING
    }

    private TaskState getTaskState() {

        if (!TreeInfo.getTreeArea().contains(Player.getPosition())) {
            return TaskState.WALK_TO_TREE_AREA;
        }

        trees = Objects.findNearest(10, TreeInfo.getTreeName());

        if (trees.length == 0) {
            return TaskState.MOVE_TO_PREDICTED;
        }

        tree = ABC2.get().getNextTarget(trees);

        if (Player.getAnimation() == -1 && tree != null) {
            return TaskState.CUT_TREE;
        }

        return TaskState.IDLE_WHILE_CUTTING;
    }

    private void updateStatistics(long timeStarted) {
        lastWoodcuttingWaitTime = System.currentTimeMillis() - timeStarted;
        totalWoodcuttingInstances++;
        totalWoodcuttingWaitTime += lastWoodcuttingWaitTime;
        averageWoodcuttingWaitTime = totalWoodcuttingWaitTime / totalWoodcuttingInstances;
    }

    private void waitForTree(){
        long idleStartTime = System.currentTimeMillis();
        while (trees.length == 0) {
            trees = Objects.find(10, TreeInfo.getTreeName());
            ABC2.handleIdleActions();
            General.sleep(50, 100);
        }
        long idleStopTime = System.currentTimeMillis();
        ABC2.get().generateAndSleep((int) (idleStopTime - idleStartTime));
    }

}
