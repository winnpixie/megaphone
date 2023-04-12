package io.github.winnpixie.megaphone.commands;

import io.github.winnpixie.hukkit.MathHelper;
import io.github.winnpixie.hukkit.commands.BaseCommand;
import io.github.winnpixie.hukkit.commands.CommandErrors;
import io.github.winnpixie.hukkit.configs.adapters.BukkitAdapter;
import io.github.winnpixie.megaphone.Config;
import io.github.winnpixie.megaphone.Megaphone;
import io.github.winnpixie.megaphone.tasks.BroadcastTask;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MegaphoneCommand extends BaseCommand<Megaphone> {
    private final BaseComponent[] reloadedMessage = new ComponentBuilder("Announcements reloaded!")
            .color(ChatColor.GREEN)
            .create();
    private final BaseComponent[] usageMessage = new ComponentBuilder("=== Megaphone ===")
            .color(ChatColor.GOLD)
            .append("\n/megaphone reload - Reloads the plugin configuration from file.", ComponentBuilder.FormatRetention.NONE)
            .append("\n/megaphone announce <index> - Broadcasts the message at <index>, starting from 1.")
            .create();

    public MegaphoneCommand(Megaphone plugin) {
        super(plugin, "megaphone");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("megaphone.command") && !sender.isOp()) {
            return true;
        }

        if (args.length < 1) {
            sender.spigot().sendMessage(usageMessage);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload", "rl" -> reloadConfiguration(sender);
            case "announce", "a" -> announceAt(sender, args);
            default -> sender.spigot().sendMessage(CommandErrors.INVALID_ARGUMENTS);
        }

        return true;
    }

    private void reloadConfiguration(CommandSender sender) {
        getPlugin().broadcastTask.cancel();
        getPlugin().reloadConfig();

        var adapter = (BukkitAdapter) getPlugin().configManager.getAdapter();
        adapter.setConfig(getPlugin().getConfig());
        getPlugin().configManager.load();

        getPlugin().broadcastTask = sender.getServer().getScheduler().runTaskTimer(getPlugin(),
                new BroadcastTask(getPlugin()), 0L, (long) (20.00 * Config.INTERVAL));

        sender.spigot().sendMessage(reloadedMessage);
    }

    private void announceAt(CommandSender sender, String[] args) {
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

        var msgIdx = Integer.parseInt(args[1]);
        if (msgIdx > Config.MESSAGES.size()) {
            sender.spigot().sendMessage(CommandErrors.INVALID_ARGUMENTS);
            return;
        }

        getPlugin().broadcast(msgIdx - 1);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length < 2) {
            return Arrays.asList("reload", "announce");
        }

        return null;
    }
}
