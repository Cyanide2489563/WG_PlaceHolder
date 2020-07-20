package team.dungeoncraft.wg_placeholder.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import team.dungeoncraft.wg_placeholder.WG_PlaceHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    private final WG_PlaceHolder plugin;
    private static final List<String> COMMANDS = Arrays.asList("help", "reload", "list", "add", "delete", "rename");
    private static final List<String> BLANK = Arrays.asList("", "");

    public CommandTabCompleter(WG_PlaceHolder plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], COMMANDS, new ArrayList<>());
            }
            if (args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("delete")) {
                if (args.length > 2) return BLANK;
                return plugin.getRegionIds();
            }
        }
        return BLANK;
    }
}
