package bagel.builds.hide_n_seek.classes.type;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.event.VentEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.UUID;

public class MangleClass extends ClassType {
    private Main main;
    private boolean inVent;

    public MangleClass(Main main, Animatronic animatronic, UUID uuid) {
        super(main, Animatronic.MANGLE, uuid);
        this.main = main;
    }

    @Override
    public void start(int addCooldown) {

        skinUtil.changeSkin(Animatronic.MANGLE.getValue(), Animatronic.MANGLE.getSignature());
    }

    @Override
    public void reset() {
        if(inVent) {
            Bukkit.getPlayer(uuid).setWalkSpeed(0.5f);
        } else {
            Bukkit.getPlayer(uuid).setWalkSpeed(0.2f);
        }
        super.remove();
    }

    @EventHandler
    public void onVentChange(VentEvent e) {
        Player player = e.getPlayer();

        if(e.getInVent()) {
                player.setWalkSpeed(1f);
                inVent = true;
        } else {
            player.setWalkSpeed(0.2f);
            inVent = false;
        }
    }
}
