package scripts.JProgressiveWoodcutter.tasks;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import scripts.JProgressiveWoodcutter.JProgressiveWoodcutter;
import scripts.JProgressiveWoodcutter.data.TreeInfo;
import scripts.JProgressiveWoodcutter.data.Vars;
import scripts.JProgressiveWoodcutter.framework.Task;
import scripts.heyimjamie.banking.JBanking;
import scripts.heyimjamie.sleep.Sleep;
import scripts.heyimjamie.webwalker_logic.WebWalker;
import scripts.heyimjamie.webwalker_logic.local.walker_engine.WalkingCondition;
import sun.reflect.generics.tree.Tree;

import java.util.HashMap;

public class HandleBanking extends Task {
    @Override
    public String status() {
        return "Banking.";
    }

    @Override
    public boolean canProcess() {
        return Vars.get().bankCache.size() == 0 || Inventory.isFull();
    }

    @Override
    public void process() {
        switch (getTaskState()){

            case WALK_TO_BANK:
                WebWalker.walkToBank(() -> Banking.isInBank() ? WalkingCondition.State.EXIT_OUT_WALKER_SUCCESS : WalkingCondition.State.CONTINUE_WALKER);
                break;

            case OPEN_BANK:
                if (Banking.openBank()){
                    Sleep.waitCondition(() -> Banking.isBankScreenOpen(), 3000);
                }
                break;

            case SET_BANK_CACHE:
                Vars.get().bankCache = JBanking.setBankCache();
                break;

            case UPGRADE_AXE:
                RSItem[] unwantedAxes = Inventory.find(Filters.Items.nameContains(" axe").combine(Filters.Items.nameNotContains(TreeInfo.getBestAxe()), false));
                String bestAxe = TreeInfo.getBestAxe();
                if (unwantedAxes.length > 0) {
                    General.println("Depositing unwanted axes.");
                    for (RSItem axe : unwantedAxes) {
                        Banking.depositItem(axe, 0);
                    }
                }

                if (Banking.withdraw(1, bestAxe))
                    Sleep.waitCondition(() -> Inventory.find(bestAxe).length > 0, 2000);
                break;

            case DEPOSIT_ITEMS:
                JBanking.depositAllExcept(Inventory.find(Filters.Items.nameNotContains(" axe")));
                Vars.get().bankCache = JBanking.setBankCache();
                break;

        }
    }

    private enum TaskState{
        WALK_TO_BANK,
        OPEN_BANK,
        SET_BANK_CACHE,
        UPGRADE_AXE,
        DEPOSIT_ITEMS
    }

    private TaskState getTaskState(){

        if (!Banking.isInBank()){
            return TaskState.WALK_TO_BANK;
        }

        if (!Banking.isBankScreenOpen()){
            return TaskState.OPEN_BANK;
        }

        if (Vars.get().bankCache.size() == 0){
            return TaskState.SET_BANK_CACHE;
        }

        if (Inventory.getCount(TreeInfo.getBestAxe()) == 0){
            return TaskState.UPGRADE_AXE;
        }

        return TaskState.DEPOSIT_ITEMS;
    }

}
