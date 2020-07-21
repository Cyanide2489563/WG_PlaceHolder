package team.dungeoncraft.wg_placeholder.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.dungeoncraft.wg_placeholder.WG_PlaceHolder;

public class CommandManager implements CommandExecutor {

    private final WG_PlaceHolder plugin;

    public CommandManager(WG_PlaceHolder plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wgpapi") && checkSender(sender)) {
            if (args.length == 0) {
                sender.sendMessage("指令錯誤, 輸入 /wgpapi help");
            }

            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage("/wgpapi help - 顯示插件指令列表");
                    sender.sendMessage("/wgpapi reload - 重新載入區域 placeholder");
                    sender.sendMessage("/wgpapi list - 顯示 placeholder 列表");
                    sender.sendMessage("/wgpapi add <regionID> <name> - 新增區域 placeholder");
                    sender.sendMessage("/wgpapi rename <regionID> <name> - 重新命名區域 placeholder");
                    sender.sendMessage("/wgpapi delete <regionID> - 刪除 placeholder");
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage("重新載入完成");
                }
                if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(plugin.getRegions().toString());
                }
                if (args[0].equalsIgnoreCase("add")) {
                    if (checkRegionId(sender, args) && checkRegionName(sender, args)) {
                        String regionID = args[1];
                        String regionName = args[2];
                        if (!plugin.addRegionPlaceHolder(regionID, regionName)) {
                            sender.sendMessage("新增失敗");
                        }
                        sender.sendMessage(String.format("已新增區域：%s 的 placeholder：%s", regionID, regionName));
                    }
                }
                if (args[0].equalsIgnoreCase("rename")) {
                    if (checkRegionId(sender, args) && checkRegionName(sender, args)) {
                        String regionID = args[1];
                        String regionName = args[2];
                        if (!plugin.renameRegionPlaceHolder(regionID, regionName)) {
                            sender.sendMessage("重新命名失敗");
                        }
                        sender.sendMessage(String.format("已重命名區域：%s 的 placeholder：%s", regionID, regionName));
                    }
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (checkRegionId(sender, args)) {
                        String regionID = args[1];
                        if (!plugin.deleteRegionPlaceHolder(regionID)) {
                            sender.sendMessage("刪除失敗");
                        }
                        sender.sendMessage(String.format("已刪除區域：%s", regionID));
                    }
                }
            }
        }
        return false;
    }

    public boolean checkSender(CommandSender sender) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("這個指令只能由玩家發送");
            return false;
        }
        if (!sender.isOp()) {
            sender.sendMessage("這個指令只能由管理員使用");
            return false;
        }
        return true;
    }

    public boolean checkRegionId(CommandSender sender, String args[]) {
        if (args.length < 2) {
            sender.sendMessage("請輸入區域 ID");
            return false;
        } else if (plugin.isContainer(args[1])) {
            sender.sendMessage("區域 ID 重複");
            return false;
        }
        return true;
    }

    public boolean checkRegionName(CommandSender sender, String args[]) {
        if (args.length < 3) {
            sender.sendMessage("請輸入 placeholder");
            return false;
        }
        return true;
    }
}
