package bagel.builds.hide_n_seek.util;

import bagel.builds.hide_n_seek.Main;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GameSettingsConfig implements Listener {
    private Main main;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private File file;
    private FileConfiguration fileConfig;
    private ItemStack book;
    private int curGameTime;

    public GameSettingsConfig(Main main) {
        this.main = main;
        this.file = null;
        this.fileConfig = null;
        saveConfig();
    }

    public void reloadConfig() throws UnsupportedEncodingException {
        if(file == null) {
            file = new File(main.getDataFolder(), "game-settings.yml");
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);

        Reader defConfigStream = new InputStreamReader(main.getResource("game-settings.yml"),"UTF8");
        if(defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() throws UnsupportedEncodingException {
        if(fileConfig == null) {
            reloadConfig();
        }
        return fileConfig;
    }

    public void saveConfig() {
        if(fileConfig == null || file == null) {
            return;
        }
        try {
            getConfig().save(file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void saveDefaultConfig() {
        if(file == null) {
            file = new File(main.getDataFolder(), "game-settings.yml");
        }
            main.saveResource("game-settings.yml", true);
    }

    public void giveBook(Player player) {
        ItemStack settingBook = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = settingBook.getItemMeta();
        bookMeta.setDisplayName(ChatColor.DARK_BLUE + "Game Settings");
        bookMeta.setLore(Arrays.asList(ChatColor.GRAY + "Change game settings and start game"));
        bookMeta.setLocalizedName("game settings");
        settingBook.setItemMeta(bookMeta);
        if(!player.getInventory().contains(settingBook)) {
            player.getInventory().addItem(settingBook);
        }
    }

    @EventHandler
    public void onBookInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().hasLocalizedName() && player.getInventory().getItemInMainHand().getItemMeta().getLocalizedName().equalsIgnoreCase("game settings") && (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            openBook(player);
        }
    }

    public void openBook(Player player) {
        main.adventure().player(player).openBook(createBook(player));
    }

    public Book createBook(Player player) {

        Component title = Component.text("Game Settings");
        Component author = Component.text("FNAF");
        Collection<Component> page = getPages(player);
        Book mybook = Book.book(title, author, page);
        //nightmare mode
        return mybook;

//        return Book.builder()
//                .author(miniMessage.deserialize("FNAF"))
//                .title(miniMessage.deserialize("Game Settings"))
//                .pages(getPages(player))
//                .build();

    }

    private List<Component> getPages(Player player) {
        TagResolver tagResolver = TagResolver.resolver(
                TagResolver.resolver("hide-time", (args, context) -> {
                    final String time = args.popOr("time expected").value();
                    return Tag.styling(ClickEvent.runCommand("/setting sethidetime" + time));
                }),
                TagResolver.resolver("dupes", (args, context) -> {
                    final String value = args.popOr("Value expected").value();
                    return Tag.styling(ClickEvent.runCommand("/setting allowdupes" + value));
                }),
                TagResolver.resolver("nightmare", (args, context) -> {
                    final String value = args.popOr("Value expected").value();
                    return Tag.styling(ClickEvent.runCommand("/setting nightmare" + value));
                }),
                TagResolver.resolver("cooldown", (args, context) -> {
                    final String addition = args.popOr("value expected").value();
                    return Tag.styling(ClickEvent.runCommand("/setting cooldown" + addition));
                }),
                TagResolver.resolver(Placeholder.component("player", Component.text(player.getName()))),
                TagResolver.resolver("game-time", (args, context) -> {
                    final String time = args.popOr("time expected").value();
                    return Tag.styling(ClickEvent.runCommand("/setting setgametime" + time));
                })
                );
        List<String> pages = new ArrayList<>();
        pages.addAll(page1);
        pages.addAll(page2);
        switch(curGameTime) {

        }
        return (pages.stream()
                .map(page -> miniMessage.deserialize(page, tagResolver))
                .collect(Collectors.toList()));
    }
    private static List<String> page1 = List.of("""
            <gold>GameManager: <underlined><player></underlined></gold>
    
            <black><bold>Allow Duplicates</bold> 
            <green><dupes: true>true</green> | <red><dupes: false>false</red>
            
            <black><bold>Hiding Time: </bold></black>
            <gray><hide-time: 2>2 <hide-time: 4>4 <hide-time: 6>6 <hide-time: 8>8 </gray>
            
            <black><bold>Game Time:</bold></black>
            <gray><game-time: 15>15 <game-time: 20>20 <game-time: 25>25 <game-time: 30>30 </gray>
            """
    );
    private static List<String> page2 = List.of("""
            <dark_red><bold>Nightmare mode: </bold></dark_red>
            <green><nightmare: true>On</green> | <red><nightmare: false>Off</red>
            
            <black><bold>Cooldown():</bold><black>
            <gray> <cooldown: -5>-5 <cooldown: -1>-1 <cooldown: 0>reset <cooldown: 1>+1 <cooldown: 5>+5</gray>
            
            <gold><bold><click:run_command:/start>Start</bold></gold>
            """);



    public File getFile() { return file; }
    public FileConfiguration getFileConfig() { return fileConfig; }


}
