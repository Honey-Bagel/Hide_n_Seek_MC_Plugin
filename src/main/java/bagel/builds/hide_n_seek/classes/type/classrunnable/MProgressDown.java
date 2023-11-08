package bagel.builds.hide_n_seek.classes.type.classrunnable;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.type.classutil.MarionetteTask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MProgressDown extends BukkitRunnable {
    Main main;
    MarionetteTask mtask;
    Player player;


    public MProgressDown(Main main, MarionetteTask mtask, Player player) {
        this.main = main;
        this.mtask = mtask;
        this.player = player;
    }

    public void start() {
            runTaskTimer(main, 0, 60);
    }

    @Override
    public void run() {
        if(!mtask.isWinding()) {
            double temp = mtask.decrementProgress();
            mtask.setBarProgress(temp);
            if(temp <= 0) {
                mtask.end(false);
            }
        }
    }
}
