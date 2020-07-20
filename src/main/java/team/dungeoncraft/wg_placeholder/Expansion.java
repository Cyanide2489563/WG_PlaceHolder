package team.dungeoncraft.wg_placeholder;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Expansion extends PlaceholderExpansion {

    private final WG_PlaceHolder plugin;

    Expansion(WG_PlaceHolder plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "WGPapi";
    }

    @Override
    public String getAuthor() {
        return "Cyanide";
    }

    @Override
    public String getVersion() {
        return plugin.getVERSION();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("current")) {
            Location location = player.getLocation();
            assert location.getWorld() != null;
            ApplicableRegionSet set = WorldGuard.getInstance().getPlatform().getRegionContainer()
                    .get(BukkitAdapter.adapt(location.getWorld())).getApplicableRegions(BukkitAdapter.asBlockVector(location));
            for (ProtectedRegion region : set) {
                if (region == null) continue;
                return plugin.getRegionName(region.getId());
            }
            return "未知區域";
        }
        for (World world : Bukkit.getWorlds()) {
            ProtectedRegion region = WorldGuard.getInstance().getPlatform().getRegionContainer()
                    .get(BukkitAdapter.adapt(world)).getRegion(identifier);
            if (region == null) continue;
            return plugin.getRegionName(region.getId());
        }
        return "未知區域";
    }
}
