package io.github.mcengine.common.currency.item;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private static final NamespacedKey CASH_KEY = new NamespacedKey("mcengine", "cash");
    private static final NamespacedKey COIN_TYPE_KEY = new NamespacedKey("mcengine", "coin_type");

    public static ItemStack createCashItem(String headId, String coinType, double amount) {
        HeadDatabaseAPI hdb = new HeadDatabaseAPI();

        ItemStack item = hdb.getItemHead(headId);
        if (item == null) {
            item = new ItemStack(Material.PAPER); // fallback
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Cash: " + capitalize(coinType));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "coin type : " + ChatColor.WHITE + capitalize(coinType));
            lore.add(ChatColor.GRAY + "amount : " + ChatColor.WHITE + amount);
            meta.setLore(lore);

            // Set persistent data
            meta.getPersistentDataContainer().set(CASH_KEY, PersistentDataType.BYTE, (byte) 1);
            meta.getPersistentDataContainer().set(COIN_TYPE_KEY, PersistentDataType.STRING, coinType);
            meta.getPersistentDataContainer().set(new NamespacedKey("mcengine", "amount"), PersistentDataType.DOUBLE, amount);

            item.setItemMeta(meta);
        }

        return item;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }    
}
