package io.github.mcengine.spigotmc.currency;

import io.github.mcengine.api.currency.MCEngineCurrencyApi;
import io.github.mcengine.common.currency.command.MCEngineCurrencyCommonCommand;
import io.github.mcengine.common.currency.listener.hook.MCEngineCurrencyCommonListenerHookHeadDB;
import io.github.mcengine.common.currency.listener.MCEngineCurrencyCommonListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * The {@code MCEngineCurrency} class is a SpigotMC plugin for handling a currency system.
 * It integrates with multiple database types (SQLite, MySQL) and provides API utilities
 * for managing currency-related operations.
 */
public class MCEngineCurrency extends JavaPlugin {

    private static MCEngineCurrency instance;
    private MCEngineCurrencyApi currencyApi;

    /**
     * Called when the plugin is enabled.
     * 
     * <p>This method performs the following actions:</p>
     * <ul>
     *   <li>Saves the default configuration file if it doesn't already exist.</li>
     *   <li>Retrieves the SQL type from the configuration, defaulting to "sqlite" if not set.</li>
     *   <li>Initializes the {@code MCEngineCurrencyApi} with the selected SQL type.</li>
     *   <li>Initializes the database connection.</li>
     *   <li>Registers event listeners and command executors for currency handling.</li>
     *   <li>Logs the successful enable message or disables the plugin if an exception occurs.</li>
     * </ul>
     */
    @Override
    public void onEnable() {
        instance = this;
        // Save default config if not already present
        saveDefaultConfig();

        // Read SQL type from config (default to sqlite)
        String sqlType = getConfig().getString("database.type", "sqlite");
        boolean hookHeadDB = getConfig().getBoolean("hook.HeadDB.enable", false);

        try {
            // Initialize currency API
            currencyApi = new MCEngineCurrencyApi(this, sqlType);
            currencyApi.initDB();

            // Register listener and command using the shared API
            getServer().getPluginManager().registerEvents(
                new MCEngineCurrencyCommonListener(currencyApi), this
            );
            getCommand("currency").setExecutor(
                new MCEngineCurrencyCommonCommand(this, currencyApi)
            );
            if (hookHeadDB) {
                // Pass currencyApi to the hook listener instead of "this"
                getServer().getPluginManager().registerEvents(new MCEngineCurrencyCommonListenerHookHeadDB(currencyApi), this);
            }

            getLogger().info("has been enabled using SQL type: " + sqlType);
        } catch (Exception e) {
            getLogger().severe("Failed to initialize MCEngineCurrency: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Called when the plugin is disabled.
     * 
     * <p>This method safely disconnects from the database by calling {@code disConnect()}
     * on the {@code currencyApi}. It logs the result of the disconnection process
     * and catches any exceptions that may occur.</p>
     */
    @Override
    public void onDisable() {
        if (currencyApi != null) {
            try {
                currencyApi.disConnect();
                getLogger().info("Database connection closed successfully.");
            } catch (Exception e) {
                getLogger().severe("Failed to close the database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /*
     * Return instance of MCEngineCurrency
     */
    public static MCEngineCurrency getInstance() {
        return instance;
    }

    /*
     * Return instance of currencyApi
     */
    public static MCEngineCurrencyApi getApi() {
        return instance.currencyApi;
    }
}
