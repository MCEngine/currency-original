package io.github.mcengine.common.currency.tabcompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides tab completion for the /currency command.
 * Supports completion for subcommands and relevant arguments like player names and coin types.
 */
public class MCEngineCurrencyCommonTabCompleter implements TabCompleter {

    /**
     * List of valid subcommands for the /currency command.
     * Used to provide suggestions when typing the first argument.
     */
    private static final List<String> ACTIONS = Arrays.asList("add", "cash", "check", "pay");

    /**
     * List of supported currency types.
     * Used to provide suggestions when typing the currency type in commands.
     */
    private static final List<String> COIN_TYPES = Arrays.asList("coin", "copper", "silver", "gold");

    /**
     * Handles tab completion for the /currency command.
     *
     * @param sender The source of the command.
     * @param command The command that was executed.
     * @param alias The alias used.
     * @param args The arguments passed to the command.
     * @return A list of suggestions for tab completion.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1 -> {
                return filter(ACTIONS, args[0]);
            }
            case 2 -> {
                return switch (args[0].toLowerCase()) {
                    case "add", "pay" -> getOnlinePlayerNames(args[1]);
                    case "check", "cash" -> filter(COIN_TYPES, args[1]);
                    default -> Collections.emptyList();
                };
            }
            case 3 -> {
                return switch (args[0].toLowerCase()) {
                    case "add" -> filter(COIN_TYPES, args[2]);
                    case "pay", "cash" -> Collections.singletonList("<amount>");
                    default -> Collections.emptyList();
                };
            }
            case 4 -> {
                return switch (args[0].toLowerCase()) {
                    case "add" -> Collections.singletonList("<amount>");
                    case "pay" -> filter(COIN_TYPES, args[3]);
                    default -> Collections.emptyList();
                };
            }
            case 5 -> {
                return args[0].equalsIgnoreCase("pay") ? Collections.singletonList("<note>") : Collections.emptyList();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    /**
     * Retrieves a list of online player names that match the given prefix.
     *
     * @param prefix The prefix to match against player names.
     * @return A list of matching player names.
     */
    private List<String> getOnlinePlayerNames(String prefix) {
        List<String> matches = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
                matches.add(player.getName());
            }
        }
        return matches;
    }

    /**
     * Filters a list of options based on a given prefix.
     *
     * @param options The list of options to filter.
     * @param prefix The prefix to match.
     * @return A list of filtered options that start with the prefix.
     */
    private List<String> filter(List<String> options, String prefix) {
        List<String> filtered = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(prefix.toLowerCase())) {
                filtered.add(option);
            }
        }
        return filtered;
    }
}
