package scripts.JProgressiveWoodcutter.data;

import org.tribot.api2007.types.RSPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class Vars {

    private Vars() {}
    private static final Vars VARS = new Vars();
    public static Vars get() { return VARS; }

    public boolean shouldRun = true;

    public String status;

    public HashMap<String, Integer> bankCache = null;

}
