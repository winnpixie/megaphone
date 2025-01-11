package io.github.winnpixie.megaphone.commands.admin;

import io.github.winnpixie.commons.spigot.MathHelper;
import io.github.winnpixie.commons.spigot.TextHelper;
import io.github.winnpixie.commons.spigot.commands.BaseCommand;
import io.github.winnpixie.commons.spigot.commands.CommandErrors;
import io.github.winnpixie.commons.spigot.configurations.adapters.BukkitConfigurationAdapter;
import io.github.winnpixie.megaphone.Config;
import io.github.winnpixie.megaphone.Megaphone;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MegaphoneCommand extends BaseCommand<Megaphone> {
    private final BaseComponent reloadedMessage = new ComponentBuilder("Announcements reloaded!")
            .color(ChatColor.GREEN)
            .build();
    private final BaseComponent usageMessage = new ComponentBuilder("=== Megaphone ===")
            .color(ChatColor.GOLD)
            .append("\n/megaphone reload|rl - Reloads the plugin configuration from file.", ComponentBuilder.FormatRetention.NONE)
            .append("\n/megaphone announce|a <index> - Broadcasts the message at <index>, starting from 1.")
            .append("\n/megaphone say <message> - Broadcasts <message> as if it were a standard announcement.")
            .build();

    public MegaphoneCommand(Megaphone plugin) {
        super(plugin, "megaphone");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("megaphone.command") && !sender.isOp()) {
            sender.spigot().sendMessage(CommandErrors.LACKS_PERMISSIONS);
            return true;
        }

        if (args.length < 1) {
            sender.spigot().sendMessage(usageMessage);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
            case "rl":
                reloadConfiguration(sender);
                break;
            case "announce":
            case "a":
                announceAt(sender, args);
                break;
            case "say":
                say(sender, args);
                break;
            default:
                sender.spigot().sendMessage(CommandErrors.INVALID_ARGUMENTS);
                break;
        }

        return true;
    }

    private void reloadConfiguration(CommandSender sender) {
        if (!sender.hasPermission("megaphone.command.reload") && !sender.isOp()) {
            sender.spigot().sendMessage(CommandErrors.LACKS_PERMISSIONS);
            return;
        }

        getPlugin().stopBroadcast();

        getPlugin().reloadConfig();

        BukkitConfigurationAdapter adapter = (BukkitConfigurationAdapter) getPlugin().configuration.getAdapter();
        adapter.setConfiguration(getPlugin().getConfig());
        getPlugin().configuration.load();

        getPlugin().startBroadcast();

        sender.spigot().sendMessage(reloadedMessage);
    }

    private void announceAt(CommandSender sender, String[] args) {
        if (!sender.hasPermission("megaphone.command.announce") && !sender.isOp()) {
            sender.spigot().sendMessage(CommandErrors.LACKS_PERMISSIONS);
            return;
        }

        if (args.length < 2) {
            sender.spigot().sendMessage(CommandErrors.MISSING_ARGUMENTS);
            return;
        }

        if (!MathHelper.isInt(args[1])) {
            sender.spigot().sendMessage(CommandErrors.INVALID_ARGUMENTS);
            return;
        }

        if (Config.MESSAGES.isEmpty()) {
            return;
        }

        int msgIdx = Integer.parseInt(args[1]);
        if (msgIdx > Config.MESSAGES.size()) {
            sender.spigot().sendMessage(CommandErrors.INVALID_ARGUMENTS);
            return;
        }

        getPlugin().broadcast(msgIdx - 1);
    }

    private void say(CommandSender sender, String[] args) {
        if (!sender.hasPermission("megaphone.command.say") && !sender.isOp()) {
            sender.spigot().sendMessage(CommandErrors.LACKS_PERMISSIONS);
            return;
        }

        if (args.length < 2) {
            sender.spigot().sendMessage(CommandErrors.MISSING_ARGUMENTS);
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        message = TextHelper.formatText(Config.formatMessage(message));

        getPlugin().getServer().spigot().broadcast(TextComponent.fromLegacy(message));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length < 2) {
            return Arrays.asList("reload", "announce");
        }

        return null;
    }
}
