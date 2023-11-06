package bagel.builds.hide_n_seek.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class HatTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        List<String> results = new ArrayList<>();

        if(args.length == 1) {
            results.add("santa");
            results.add("catears");
        }

        return results;
    }
}
