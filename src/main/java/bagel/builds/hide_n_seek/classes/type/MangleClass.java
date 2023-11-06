package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.event.VentEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class MangleClass extends ClassType {
    private Main main;

    public MangleClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.MANGLE, uuid);
        this.main = main;
    }

    @Override
    public void start() {

    }

    @Override
    public void reset() {
        super.remove();
    }

    @EventHandler
    public void onVentChange(VentEvent e) {
        Player player = e.getPlayer();

        if(e.getInVent()) {
                player.setWalkSpeed(1f);
        } else {
            player.setWalkSpeed(0.2f);
        }
    }
}
