package scripts.JProgressiveWoodcutter.tasks;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.ext.Filters;
import scripts.JProgressiveWoodcutter.framework.Task;
import scripts.heyimjamie.banking.JBanking;
import scripts.heyimjamie.sleep.Sleep;
import scripts.heyimjamie.webwalker_logic.WebWalker;
import scripts.heyimjamie.webwalker_logic.local.walker_engine.WalkingCondition;

public class HandleBanking extends Task {
    @Override
    public String status() {
        return "Banking.";
    }

    @Override
    public boolean canProcess() {
        return Inventory.isFull();
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

            case DEPOSIT_ITEMS:
                JBanking.depositAllExcept(Inventory.find(Filters.Items.nameNotContains(" axe")));
                break;

        }
    }

    private enum TaskState{
        WALK_TO_BANK,
        OPEN_BANK,
        DEPOSIT_ITEMS
    }

    private TaskState getTaskState(){

        if (!Banking.isInBank()){
            return TaskState.WALK_TO_BANK;
        }

        if (!Banking.isBankScreenOpen()){
            return TaskState.OPEN_BANK;
        }

        return TaskState.DEPOSIT_ITEMS;
    }
}
