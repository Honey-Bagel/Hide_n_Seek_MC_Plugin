package bagel.builds.hide_n_seek.manager;

import bagel.builds.hide_n_seek.Main;
import bagel.builds.hide_n_seek.classes.Animatronic;
import bagel.builds.hide_n_seek.classes.Hider;
import bagel.builds.hide_n_seek.classes.Team;
import bagel.builds.hide_n_seek.classes.type.*;
import bagel.builds.hide_n_seek.enums.GameState;
import bagel.builds.hide_n_seek.instance.Game;
import bagel.builds.hide_n_seek.instance.LiveGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class GameManager {

    private Main main;
    private List<UUID> players;
    private VentManager ventManager;
    private HashMap<UUID, Animatronic> animatronicsMap;
    private HashMap<UUID, Hider> hiderMap;
    private HashMap<UUID, Team> teams;
    private HashMap<UUID, ClassType> classtypes;
    private List<Game> games = new ArrayList<>();
    private Player gameController;
    private GameState state;
    private LiveGame liveGame;
    private static int gameTime;
    private static int hideTime;
    private static int additionalCooldown;
    private static boolean allowDupes;
    private static boolean nightmare;

    public GameManager(Main main) {
        this.main = main;

        this.players = new ArrayList<>();
        this.animatronicsMap = new HashMap<>();
        this.hiderMap = new HashMap<>();
        this.teams = new HashMap<>();
        this.classtypes = new HashMap<>();

        this.ventManager = new VentManager(this);

        this.state = GameState.WAITING;
    }

    public void start() {
        //start countdown
        loadSettings();
        this.liveGame = new LiveGame(main, this, gameTime, hideTime);
        for(UUID uuid : players) {
            if(!teams.containsKey(uuid)) {
                sendGameTitle("Not all players selecter a team/class", "");
                return;
            }
        }
        sendGameTitle("starting game", "");
        liveGame.start();
    }

    public void reset() {
        if(state.equals(GameState.LIVE) || state.equals(GameState.HIDING)) {
            //teleport all players to lobby
            //give gamecontroller back settings book
            //clear kits and inventories
            for(UUID uuid : players){
                Player player = Bukkit.getPlayer(uuid);
                if(getTeam(player).equals(Team.ANIMATRONIC)) {
                    removeAnimatronic(player);
                } else if(getTeam(player).equals(Team.HIDER)) {
                    removeHider(player);
                }
                removeTeam(player);
//                player.getInventory().clear();
                if(gameController.equals(player)) {
                    //Give game controller back settings book
                    main.getGameSettingsConfig().giveBook(player);
                }
            }
        }
        sendGameTitle("", "");
        state = GameState.WAITING;
        liveGame.cancelTasks();
        main.getGameSettingsConfig().saveDefaultConfig();
    }


    /* Players */
    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        ventManager.addPlayer(player);
        if(players.size() == 1) {
            gameController = player;
            main.getGameSettingsConfig().giveBook(player);
        }
        main.getCameraManager().addPlayerCamera(player);
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        ventManager.removePlayer(player);
        removeTeam(player);
        main.getCameraManager().removePlayerCamera(player);
        if(gameController.equals(player)) {
            gameController = Bukkit.getPlayer(players.get((int) Math.random()*players.size()));
        }
        if(state == GameState.LIVE || state == GameState.HIDING) {
            if(hiderMap.size() == 0) {
                sendGameTitle(ChatColor.RED + "Not enough Hiders", ChatColor.GRAY + "Game reset");
                reset();
            } else if(animatronicsMap.size() == 0) {
                sendGameTitle(ChatColor.RED + "Not enough Animatronics", ChatColor.GRAY + "Game reset");
                reset();
            }
        }

    }



    /* Info */

    public Main getMain() { return main; }
    public List<UUID> getPlayers() { return players; }
    public void setState(GameState newstate) {
        this.state = newstate;
        System.out.println(this.state);
    }
    public void sendGameTitle(String title, String subTitle) {
        for(UUID uuid : players) {
            Bukkit.getPlayer(uuid).sendTitle(title, subTitle);
        }
    }
    public GameState getState() { return state; }
    public LiveGame getLiveGame() { return liveGame; }

    /* Animatronic Hashmap */
    public HashMap<UUID, Animatronic> getAnimatronicsMap() { return animatronicsMap; }

    public void setAnimatronic(Player player, Animatronic animatronic) {
        removeAnimatronic(player);
        animatronicsMap.put(player.getUniqueId(), animatronic);

        if(animatronic == Animatronic.MANGLE) {
            classtypes.put(player.getUniqueId(), new MangleClass(main, animatronic, player.getUniqueId()));
            classtypes.get(player.getUniqueId()).start(additionalCooldown);
        } else if(animatronic.equals(Animatronic.FOXY)) {
            classtypes.put(player.getUniqueId(), new FoxyClass(main, animatronic, player.getUniqueId()));
            classtypes.get(player.getUniqueId()).start(additionalCooldown);
        } else if(animatronic.equals(Animatronic.BONNIE)) {
            classtypes.put(player.getUniqueId(), new BonnieClass(main, animatronic, player.getUniqueId()));
            classtypes.get(player.getUniqueId()).start(additionalCooldown);
        } else if(animatronic.equals(Animatronic.MARIONETTE)) {
            classtypes.put(player.getUniqueId(), new MarionetteClass(main, animatronic, player.getUniqueId()));
            classtypes.get(player.getUniqueId()).start(additionalCooldown);
        } else if(animatronic.equals(Animatronic.FREDDY)) {
            classtypes.put(player.getUniqueId(), new FreddyClass(main, animatronic, player.getUniqueId()));
            classtypes.get(player.getUniqueId()).start(additionalCooldown);
        }
    }
    public void removeAnimatronic(Player player) {
        if(animatronicsMap.containsKey(player.getUniqueId())) {
            animatronicsMap.remove(player.getUniqueId());
            if(classtypes.containsKey(player.getUniqueId())) {
                classtypes.get(player.getUniqueId()).reset();
                classtypes.remove(player.getUniqueId());
            }
        }
    }
    public Boolean isAnimatronicTaken(Animatronic animatronic) {
        if(allowDupes) return false;
        for(Animatronic a : animatronicsMap.values()) {
            if(a.equals(animatronic)) {
                return true;
            }
        }
        return false;
    }

    public void removeDupes() {
        for(UUID uuid1 : teams.keySet()) {
            for(UUID uuid2 : teams.keySet()) {
                if(!uuid1.toString().equalsIgnoreCase(uuid2.toString())) {
                    Team one = teams.get(uuid1);
                    Team two = teams.get(uuid2);
                    if(one.equals(two)){
                        removeTeam(Objects.requireNonNull(Bukkit.getPlayer(uuid2)));
                    }
                }
            }
        }
    }

    public Animatronic getAnimatronic(Player player) {
        if(animatronicsMap.containsKey(player.getUniqueId())) {
            return animatronicsMap.get(player.getUniqueId());
        } else { return null; }
    }

    /* Hider Hashmap */
    public HashMap<UUID, Hider> getHiderMap() { return hiderMap; }
    public void setHider(Player player, Hider hider) {
        removeHider(player);
        hiderMap.put(player.getUniqueId(), hider);
    }
    public void removeHider(Player player) {
        if(hiderMap.containsKey(player.getUniqueId())) {
            hiderMap.remove(player.getUniqueId());
            if(classtypes.containsKey(player.getUniqueId())) {
                classtypes.get(player.getUniqueId()).reset();
                classtypes.remove(player.getUniqueId());
            }
        }
    }
    public Hider getHider(Player player) {
        if(hiderMap.containsKey(player.getUniqueId())) {
            return hiderMap.get(player.getUniqueId());
        } else { return null; }
    }
    public Boolean isHiderTaken(Hider hider) {
        for(Hider h : hiderMap.values()) {
            if(h.equals(hider)) {
                return true;
            }
        }
        return false;
    }

    /* Team Hashmap */
    public HashMap<UUID, Team> getTeams() { return teams; }
    public void setTeam(Player player, Team team) {
        removeTeam(player);
        teams.put(player.getUniqueId(), team);
    }
    public void removeTeam(Player player) {
        if(teams.containsKey(player.getUniqueId())) {
            if(teams.get(player.getUniqueId()).equals(Team.ANIMATRONIC)) {
                removeAnimatronic(player);
            } else if(teams.get(player.getUniqueId()).equals(Team.HIDER)) {
                removeHider(player);
            }
            teams.remove(player.getUniqueId());
        }
    }
    public Team getTeam(Player player) {
        if(teams.containsKey(player.getUniqueId())) {
            return teams.get(player.getUniqueId());
        }
        return null;
    }

    /* Class Types Hashmap */
    public HashMap<UUID, ClassType> getClasstypes() { return classtypes; }

    public VentManager getVentManager() { return ventManager; }

    /* Util */
    public void sendAnimTitle(String title, String subString) {
        for(UUID uuid : animatronicsMap.keySet()) {
            Bukkit.getPlayer(uuid).sendTitle(title, subString);
        }
    }

    public void sendHiderTitle(String title, String subString) {
        for(UUID uuid : hiderMap.keySet()) {
            Bukkit.getPlayer(uuid).sendTitle(title, subString);
        }
    }

    public void loadSettings() {
        FileConfiguration config = null;
        try {
            config = main.getGameSettingsConfig().getConfig();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if(config != null) {
            gameTime = config.getInt("Game.game-time");
            hideTime = config.getInt("Game.hiding-time");
            allowDupes = config.getBoolean("Game.allow-duplicates");
            nightmare = config.getBoolean("Game.nightmare");
            additionalCooldown = config.getInt("Game.additional-cooldown");
        }
    }

//    public Game getGame(int id) {
//        for(Game game : games) {
//            if(game.getId() == id) {
//                return game;
//            }
//        }
//        return null;
//    }


}
