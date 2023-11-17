package bagel.builds.hide_n_seek.npc;

import bagel.builds.hide_n_seek.Main;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class NPCManager {

    private List<NPC> npcList;
    private packetHandler packetHandler;

    public NPCManager(Main main) {
        packetHandler = new packetHandler();
        Bukkit.getPluginManager().registerEvents(new NPCListener(main, packetHandler, this), main);
        main.getCommand("npc").setExecutor(new NPCCommand(main, this));

        this.npcList = new ArrayList<>();
    }


    public void spawnNPCS() {

    }


    public void addNPC(NPC npc) {
        npcList.add(npc);
    }

    public List<NPC> getNpcList() { return npcList; }

}
