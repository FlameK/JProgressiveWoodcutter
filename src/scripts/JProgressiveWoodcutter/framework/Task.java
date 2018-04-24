package scripts.JProgressiveWoodcutter.framework;

/**
 * Created by jamie on 23/04/2018.
 */
public abstract class Task {

    public abstract String status();

    public abstract boolean canProcess();

    public abstract void process();


}
