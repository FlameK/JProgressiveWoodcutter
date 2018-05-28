package scripts.JProgressiveWoodcutter.tasks;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.ext.Filters;
import scripts.JProgressiveWoodcutter.data.TreeInfo;
import scripts.JProgressiveWoodcutter.framework.Task;

public class HandleDropping extends Task {
    @Override
    public String status() {
        return "Dropping Logs.";
    }

    @Override
    public boolean canProcess() {
        return TreeInfo.getTreeName().equals("Willow") && Inventory.isFull();
    }

    @Override
    public void process() {
        Inventory.dropAllExcept(new String[]{TreeInfo.getInventoryAxe()});
    }
}
