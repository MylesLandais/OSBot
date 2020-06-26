import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Alexei",
                name = "Template Script",
                info = "Example skeleton for scripting",
                version = 0.1,
                logo = "")

public final class Template extends Script  {

    public final void onStart() {
        log("This will be printed to the logger when the script starts");
    }

    @Override
    public final int onLoop() throws InterruptedException {
        log("Welcome to my example script skeleton");
        return 0;
    }
}
