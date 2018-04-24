package scripts.JProgressiveWoodcutter.data;

import org.tribot.api2007.Skills;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public enum TreeInfo {

    NORMAL(1, "Tree", new RSArea(new RSTile(3173, 3374, 0), new RSTile(3156, 3402, 0))),
    OAK(15, "Oak", new RSArea(new RSTile(3171, 3421, 0), new RSTile(3158, 3410, 0))),
    WILLOW(30, "Willow", new RSArea(new RSTile(3082, 3238, 0), new RSTile(3091, 3225, 0))),
    YEW(60, "Yew", new RSArea(new RSTile(3203, 3506, 0), new RSTile(3224, 3498, 0)));

    int levelRequired;
    String treeName;
    RSArea treeArea;

    TreeInfo(int levelRequired, String treeName, RSArea treeArea) {
        this.levelRequired = levelRequired;
        this.treeName = treeName;
        this.treeArea = treeArea;
    }

    public static String getTreeName() {
        int woodcuttingLevel = Skills.getActualLevel(Skills.SKILLS.WOODCUTTING);
        return woodcuttingLevel < 15 ? NORMAL.treeName : woodcuttingLevel < 30 ? OAK.treeName : woodcuttingLevel < 60 ? WILLOW.treeName : YEW.treeName;
    }

    public static RSArea getTreeArea() {
        int woodcuttingLevel = Skills.getActualLevel(Skills.SKILLS.WOODCUTTING);
        return woodcuttingLevel < 15 ? NORMAL.treeArea : woodcuttingLevel < 30 ? OAK.treeArea : woodcuttingLevel < 60 ? WILLOW.treeArea : YEW.treeArea;
    }
}
