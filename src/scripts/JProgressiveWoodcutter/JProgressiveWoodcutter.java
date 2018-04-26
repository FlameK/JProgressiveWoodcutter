package scripts.JProgressiveWoodcutter;

import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import scripts.JProgressiveWoodcutter.data.Vars;
import scripts.JProgressiveWoodcutter.framework.Task;
import scripts.JProgressiveWoodcutter.tasks.HandleBanking;
import scripts.JProgressiveWoodcutter.tasks.HandleWoodcutting;
import scripts.heyimjamie.dataformatting.ValueFormatting;

import java.awt.*;
import java.util.ArrayList;

@ScriptManifest(category = "Woodcutting", name = "JProgressiveWoodcutter", authors = "HeyImJamie")
public class JProgressiveWoodcutter extends Script implements Painting {

    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    public void run() {

        addTasks();

        while (Vars.get().shouldRun){

            handleTasks();

        }

    }

    private void addTasks(){
        tasks.add(new HandleBanking());
        tasks.add(new HandleWoodcutting());
    }

    private void handleTasks(){
        for (Task t : tasks){
            if (t.canProcess()){
                Vars.get().status = t.status();
                t.process();
                General.sleep(100, 250);
                break;
            }
        }
    }

    public static void stopScript(String reason){
        General.println("Script stopped : " + reason);
        Vars.get().shouldRun = false;
    }

    @Override
    public void onPaint(Graphics g) {
        g.setColor(Color.WHITE);

        g.drawString("JProgressiveWoodcutter", 10, 250);
        g.drawString("Runtime: " + ValueFormatting.formatTime(getRunningTime()), 10, 265);
        g.drawString("Status: " + Vars.get().status, 10, 280);
    }
}
