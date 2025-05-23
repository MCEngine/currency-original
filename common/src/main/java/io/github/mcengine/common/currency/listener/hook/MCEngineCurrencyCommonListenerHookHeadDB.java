package io.github.mcengine.common.currency.listener.hook;

import io.github.mcengine.api.currency.MCEngineCurrencyApi;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Listener for handling HeadDB cash items that allow players to deposit currency
 * into their account by right-clicking the item.
 */
public class MCEngineCurrencyCommonListenerHookHeadDB implements Listener {

    private final MCEngineCurrencyApi currencyApi;

    /**
     * Namespaced key for identifying if an item is a cash item.
     */
    private static final NamespacedKey CASH_KEY = new NamespacedKey("mcengine", "cash");

    /**
     * Namespaced key for identifying the coin type stored in the item.
     */
    private static final NamespacedKey COIN_TYPE_KEY = new NamespacedKey("mcengine", "coin_type");

    /**
     * Namespaced key for identifying the amount of currency stored in the item.
     */
    private static final NamespacedKey AMOUNT_KEY = new NamespacedKey("mcengine", "amount");

    /**
     * Constructor for initializing the currency listener with the provided currency API.
     *
     * @param currencyApi the currency API used for depositing coins
     */
    public MCEngineCurrencyCommonListenerHookHeadDB(MCEngineCurrencyApi currencyApi) {
        this.currencyApi = currencyApi;
    }

    /**
     * Handles player interaction events to check if a player is right-clicking
     * with a valid HeadDB cash item and deposits the contained currency into
     * the player's account.
     *
     * @param event the PlayerInteractEvent triggered by player interaction
     */
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        // Only process if the interaction is with the main hand
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // Check if the item is a cash item
        if (!meta.getPersistentDataContainer().has(CASH_KEY, PersistentDataType.BYTE)) return;

        // Retrieve coin type and amount from the item
        String coinType = meta.getPersistentDataContainer().get(COIN_TYPE_KEY, PersistentDataType.STRING);
        Double amount = meta.getPersistentDataContainer().get(AMOUNT_KEY, PersistentDataType.DOUBLE);

        if (coinType == null || amount == null) return;

        // Deposit the money to the player's account
        currencyApi.addCoin(player.getUniqueId(), coinType, amount);
        player.sendMessage(ChatColor.GREEN + "Deposited " + amount + " " + coinType + " from cash item.");

        // Remove one item from the stack
        item.setAmount(item.getAmount() - 1);
    }
}
