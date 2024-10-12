package be.isach.ultracosmetics.command.subcommands;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.command.SubCommand;
import be.isach.ultracosmetics.config.MessageManager;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.util.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

/**
 * Give {@link be.isach.ultracosmetics.command.SubCommand SubCommand}.
 *
 * @author iSach
 * @since 12-21-2015
 */
public class SubCommandGive extends SubCommand {

    public SubCommandGive(UltraCosmetics ultraCosmetics) {
        super("give", "Gives Ammo/Keys", "key [amount] [player] OR /uc give ammo <type> <amount> [player]", ultraCosmetics);
    }

    @Override
    protected void onExeAnyone(CommandSender sender, String[] args) {
        if (args.length < 2 || (!args[1].toLowerCase(Locale.ROOT).startsWith("k") && !args[1].toLowerCase(Locale.ROOT).startsWith("a"))) {
            badUsage(sender);
            return;
        }

        boolean givingKey = args[1].toLowerCase(Locale.ROOT).startsWith("k");
        if (!givingKey && args.length < 4) {
            badUsage(sender);
            return;
        }

        // TODO: support offline players? Maybe with OfflineUltraPlayer?
        Player target;

        int targetArg = givingKey ? 3 : 4;
        if (args.length <= targetArg) {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                error(sender, "You must specify a player.");
                return;
            }
        } else {
            target = Bukkit.getPlayer(args[targetArg]);
            if (target == null) {
                error(sender, "Player " + args[3] + " not found!");
                return;
            }
        }

        if (givingKey) {
            int keys = 1;
            if (args.length > 2) { // if amount arg supplied
                if (!MathUtils.isInteger(args[2])) {
                    error(sender, args[2] + " isn't a number!");
                    return;
                }
                keys = Integer.parseInt(args[2]);
            }

            // negative keys is fine, see comment on addAmmo
            addKeys(target, keys);

            sender.sendMessage(ChatColor.GREEN.toString() + keys + " treasure keys given to " + target.getName());
            return;
        }

        // Giving ammo. /uc give ammo <type> <amount> [player]
        if (args.length < 4) {
            badUsage(sender, "/uc give ammo <gadget> <amount> [player]");
            return;
        }
        GadgetType gadgetType = CosmeticType.valueOf(Category.GADGETS, args[2].toUpperCase(Locale.ROOT));
        if (gadgetType == null) {
            MessageManager.send(sender, "Invalid-Gadget");
            return;
        }

        if (!gadgetType.isEnabled()) {
            error(sender, "This gadget isn't enabled!");
            return;
        }

        if (!MathUtils.isInteger(args[3])) {
            error(sender, args[3] + " isn't a number!");
            return;
        }

        // I don't think there's anything wrong with allowing giving of negative ammo,
        // otherwise there's no way to take ammo. If someone takes more ammo than
        // a user has, that's on them I guess...
        int ammo = Integer.parseInt(args[3]);

        addAmmo(gadgetType, target, ammo);
        sender.sendMessage(ChatColor.GREEN.toString() + ammo + " " + gadgetType.toString().toLowerCase(Locale.ROOT) + " ammo given to " + target.getName());
    }

    private void addKeys(Player player, int amount) {
        ultraCosmetics.getPlayerManager().getUltraPlayer(player).addKeys(amount);
    }

    private void addAmmo(GadgetType gadgetType, Player player, int ammo) {
        ultraCosmetics.getPlayerManager().getUltraPlayer(player).addAmmo(gadgetType, ammo);
    }

    @Override
    protected void tabComplete(CommandSender sender, String[] args, List<String> options) {
        if (args.length == 2) {
            options.add("ammo");
            options.add("key");
        } else if (args.length == 3 && args[1].equalsIgnoreCase("ammo")) {
            for (CosmeticType<?> gadgetType : CosmeticType.enabledOf(Category.GADGETS)) {
                options.add(gadgetType.getConfigName());
            }
        } else if ((args.length == 4 && args[1].equalsIgnoreCase("key"))
                || (args.length == 5 && args[1].equalsIgnoreCase("ammo"))) {
            addPlayers(options);
        }
    }
}
