package team.dungeoncraft.wg_placeholder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import team.dungeoncraft.wg_placeholder.command.CommandManager;
import team.dungeoncraft.wg_placeholder.command.CommandTabCompleter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class WG_PlaceHolder extends JavaPlugin {

    private final String VERSION = "0.0.1";
    private static WG_PlaceHolder plugin;
    private YamlConfiguration config;
    private File configFile = new File(this.getDataFolder() + File.separator + "config.yml");
    private final Map<String, String> regions = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Expansion(this).register();
            loadConfig();
            Objects.requireNonNull(this.getCommand("wgpapi")).setExecutor(new CommandManager(this));
            this.getCommand("wgpapi").setTabCompleter(new CommandTabCompleter(this));
        }
    }

    @Override
    public void onDisable() {
        saveRegionPlaceHolder();
    }

    public WG_PlaceHolder getPlugin() {
        return plugin;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        this.getLogger().info("正在載入區域 PlaceHolder");
        for (String regionID : config.getKeys(false)) {
            regions.put(regionID, config.getString(regionID));
            this.getLogger().info(String.format("已載入區域：%s 的 PlaceHolder：%s", regionID, config.getString(regionID)));
        }
        this.getLogger().info("區域 PlaceHolder 載入完成");
    }

    public void reloadConfig() {
        this.getLogger().info("重新載入設定檔");
        regions.clear();
        loadConfig();
        this.getLogger().info("重新載入完成");
    }

    public Map<String, String> getRegions() {
        return regions;
    }

    public List<String> getRegionIds() {
        List<String> regionIds = new ArrayList<>(regions.size());
        regionIds.addAll(regions.keySet());
        return regionIds;
    }

    public boolean isContainer(String regionID) {
        return regions.containsKey(regionID);
    }

    public String getRegionName(String regionID) {
        String regionName = regions.get(regionID);
        return regionName == null ? "未知區域" : ChatColor.translateAlternateColorCodes('&', regionName);
    }

    public boolean addRegionPlaceHolder(String regionID, String regionName) {
        regions.put(regionID, regionName);
        saveRegionPlaceHolder();
        return regions.containsKey(regionID);
    }

    public boolean deleteRegionPlaceHolder(String regionID) {
        regions.remove(regionID);
        config.set(regionID, null);
        saveRegionPlaceHolder();
        return !regions.containsKey(regionID);
    }

    public boolean renameRegionPlaceHolder(String regionID, String regionName) {
        regions.replace(regionID, regionName);
        saveRegionPlaceHolder();
        return regions.containsKey(regionID);
    }

    public void saveRegionPlaceHolder() {
        for (String regionId : regions.keySet()) {
            config.set(regionId, regions.get(regionId));
        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
