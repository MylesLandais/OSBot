import org.osbot.rs07.api.filter.Filter;
        import org.osbot.rs07.api.model.Entity;
        import org.osbot.rs07.api.model.NPC;
        import org.osbot.rs07.api.ui.Skill;
        import org.osbot.rs07.script.Script;
        import org.osbot.rs07.script.ScriptManifest;
        import org.osbot.rs07.utility.ConditionalSleep;

        import java.awt.*;

@ScriptManifest(name = "Open Minnows", version = 1.0, author = "Eliot", info = "Fish minnows", logo = "")
public class ElliotsBannedMinnowFishing extends Script {
    private long startTime;
    private ConditionalSleep flyingFishCS;
    private ConditionalSleep interactingCS;
    private ConditionalSleep runningCS;

    // Paint constants
    private final Color transBackground = new Color(0, 0, 0, 178);
    private final Color rsOrange = new Color(252, 155, 31);
    private final Font font = new Font("Helvetica", Font.PLAIN, 12);

    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
        getExperienceTracker().start(Skill.FISHING);
        flyingFishCS = new ConditionalSleep(1500) {
            @Override
            public boolean condition() throws InterruptedException {
                return myPlayer().getInteracting() != null &&
                        myPlayer().getInteracting().getModelHeight() > 9;
            }
        };
        interactingCS = new ConditionalSleep(5000) {
            @Override
            public boolean condition() throws InterruptedException {
                return myPlayer().getInteracting() != null;
            }
        };
        runningCS = new ConditionalSleep(3000) {
            @Override
            public boolean condition() throws InterruptedException {
                return getSettings().isRunning();
            }
        };
    }

    @Override
    public int onLoop() throws InterruptedException {
        if ((myPlayer().getInteracting() == null && !myPlayer().isMoving()) ||
                (myPlayer().getInteracting() != null && myPlayer().getInteracting().getHeight() > 9) ||
                getDialogues().isPendingContinuation()) {
            flyingFishCS.sleep();
            if (!getSettings().isRunning() && getSettings().getRunEnergy() > 30) {
                getSettings().setRunning(true);
                runningCS.sleep();
            } else {
                Entity fishingSpot = getNpcs().closest((Filter<NPC>) npc -> npc.getName().equals("Fishing spot") &&
                        npc.getModelHeight() < 10);
                if (fishingSpot != null) {
                    fishingSpot.interact("Small Net");
                    interactingCS.sleep();
                }
            }
        }
        return random(50, 200);
    }

    @Override
    public void onPaint(Graphics2D g) {
        // Set the font
        g.setFont(font);

        // Transparent box, holds paint strings
        g.setColor(transBackground);
        g.fillRect(1, 250, 225, 88);

        // Outside of transparent box
        g.setColor(rsOrange);
        g.drawRect(1, 250, 225, 88);

        // Draw paint info
        g.drawString("Open Minnows by Eliot", 10, 265);
        g.drawString("Runtime: " + formatTime(System.currentTimeMillis() - startTime), 10, 285);
        g.drawString("Fishing XP (p/h): " + getExperienceTracker().getGainedXP(Skill.FISHING) + " (" +
                getExperienceTracker().getGainedXPPerHour(Skill.FISHING) + ")", 10, 300);
        g.drawString("Fishing Level: " + getSkills().getStatic(Skill.FISHING) + " (" +
                getExperienceTracker().getGainedLevels(Skill.FISHING) + ")", 10, 315);
        g.drawString("Time to level: " + formatTime(getExperienceTracker().getTimeToLevel(Skill.FISHING)), 10, 330);
    }

    /**
     * Formats the runtime into human readable form
     *
     * @param time
     * @return string representing the runtime of the script
     */
    private String formatTime(final long time) {
        long s = time / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}