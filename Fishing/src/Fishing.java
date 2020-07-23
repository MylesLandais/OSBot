import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

@ScriptManifest(author = "Alexei",
                name = "Fishing Script",
                info = "Lumbridge Fishing",
                version = 0.1,
                logo = "")

public final class Fishing extends Script  {


    private Position LUMBRIDGE_FISHING = new Position(3241, 3154, 0);

    private String STATUS = "start";
    private NPC FISHING_SPOT;

    public final void onStart() {
        log("This will be printed to the logger when the script starts");
    }

    @Override
    public final int onLoop() throws InterruptedException {
        int fishing_level = getSkills().getDynamic(Skill.FISHING);
        log(STATUS);
        log(fishing_level);

/*
        if (!myPlayer().isAnimating()){
            getNpcs().closest("1530").interact("Net");
        }
*/
        // basic lumby fish
        String NPC_ID = "1530";
        FISHING_SPOT = getNpcs().closest("Fishing Spot");

        if(!myPlayer().isAnimating() && FISHING_SPOT != null){
            sleep(200);
            if (FISHING_SPOT.interact("Net")) {
                new ConditionalSleep( random(1500, 3000) ){
                    @Override
                    public boolean condition() throws InterruptedException {
                        return FISHING_SPOT != null;
                    }
                }.sleep();
            }
        }
        //end fishing or missing fishing net
        if(inventory.isFull() || !inventory.contains("Small fishing net")) {
            if (!Banks.LUMBRIDGE_UPPER.contains(myPosition())) {
                getWalking().webWalk(Banks.LUMBRIDGE_UPPER);
            }
        }
        //AT BANK
        // BANKING LOGIC
        // Inventory full of fish
        String tool = "Small fishing net";
        if(Banks.LUMBRIDGE_UPPER.contains(myPosition()) ){
            if (getBank() != null) {
                if (!getBank().open()) {
                    getBank().open();
                }
                if (getBank().open()) {
                    getBank().depositAllExcept("Small fishing net");
                }
                if(!inventory.contains("Small fishing net")){
                    getBank().withdraw(tool,1);
                }
                if(getBank().open()){
                    getBank().close();
                }
                getWalking().webWalk(LUMBRIDGE_FISHING);
            }
        }

        // leveling 1-20 can be done with small fishing net in lumby
        /*if (fishing_level < 20){
            //pick a fishing spot.
            Inventory inv = getInventory();
            // check small fishing net
            if( myPosition() == LUMBRIDGE_FISHING){
                STATUS = "LUMB_FISH";
            }
            if (inv.contains("Small Fishing Net") || (myPosition() != LUMBRIDGE_FISHING)) {
                STATUS = "Walking to fish";
            }
            if(!inv.contains(("Small Fishing Net"))){
                STATUS = "BANK - INVENTORY DOES NOT CONTAIN NET";
                getWalking().webWalk(Banks.LUMBRIDGE_UPPER);
                // TODO Add grabbing small net from bank
            }
        }
*/

        return 0;
    }
}
