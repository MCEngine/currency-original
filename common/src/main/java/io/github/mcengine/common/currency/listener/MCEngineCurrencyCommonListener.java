package io.github.mcengine.common.currency.listener;

import io.github.mcengine.api.currency.MCEngineCurrencyApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Listener class for handling currency-related events in MCEngine.
 * This listener checks if a player exists in the currency database upon joining
 * and initializes their data if necessary.
 */
public class MCEngineCurrencyCommonListener implements Listener {
    private final MCEngineCurrencyApi currencyApi;

    /**
     * Constructs a new listener for handling player currency data.
     *
     * @param currencyApi The currency API instance used for database interactions.
     */
    public MCEngineCurrencyCommonListener(MCEngineCurrencyApi currencyApi) {
        this.currencyApi = currencyApi;
    }

    /**
     * Event handler for when a player joins the server.
     * Checks if the player exists in the currency database and initializes their data if not.
     *
     * @param event The PlayerJoinEvent triggered when a player joins.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        // Check if the player exists in the database
        if (!currencyApi.checkIfPlayerExists(playerUUID)) {
            // Initialize player data with default values
            currencyApi.initPlayerData(playerUUID);
        }
    }
}
