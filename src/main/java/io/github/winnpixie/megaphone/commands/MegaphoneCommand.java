package io.github.winnpixie.megaphone.commands;

import io.github.winnpixie.hukkit.MathHelper;
import io.github.winnpixie.hukkit.TextHelper;
import io.github.winnpixie.hukkit.commands.HukkitCommand;
import io.github.winnpixie.hukkit.configs.adapters.BukkitAdapter;
import io.github.winnpixie.megaphone.Config;
import io.github.winnpixie.megaphone.Megaphone;
import io.github.winnpixie.megaphone.tasks.BroadcastTask;
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

public class MegaphoneCommand extends HukkitCommand<Megaphone> {
    private final BaseComponent[] reloadedMessage = new ComponentBuilder("Announcements reloaded!")
            .color(ChatColor.GREEN)
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
            sender.spigot().sendMessage(Errors.MISSING_ARGUMENTS);
            return true;
        }

        switch (args[0].toLowerCase()) {
            // Reload
            case "reload", "rl" -> {
                getPlugin().broadcastTask.cancel();

                var adapter = (BukkitAdapter) getPlugin().configManager.getAdapter();
                adapter.setConfig(getPlugin().getConfig());
                getPlugin().configManager.load();

                getPlugin().broadcastTask = getPlugin().getServer().getScheduler().runTaskTimer(getPlugin(),
                        new BroadcastTask(getPlugin()),
                        0L,
                        (long) (20.00 * Config.INTERVAL));

                sender.spigot().sendMessage(reloadedMessage);
            }
            // Announce @ <idx>
            case "announce", "a" -> {
                if (args.length < 2) {
                    sender.spigot().sendMessage(Errors.MISSING_ARGUMENTS);
                    return true;
                }

                if (!MathHelper.isInt(args[1])) {
                    sender.spigot().sendMessage(Errors.INVALID_ARGUMENTS);
                    return true;
                }

                if (Config.MESSAGES.isEmpty()) {
                    return true;
                }

                var msgIdx = Integer.parseInt(args[1]);
                if (msgIdx > Config.MESSAGES.size()) {
                    sender.spigot().sendMessage();
                    return true;
                }

                var msg = Config.formatMessage(Config.MESSAGES.get(msgIdx - 1));
                getPlugin().getServer().spigot().broadcast(TextComponent.fromLegacyText(TextHelper.formatColors(msg)));
            }
            default -> sender.spigot().sendMessage(Errors.INVALID_ARGUMENTS);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length < 2) {
            return Arrays.asList("reload", "announce");
        }

        return null;
    }
}
