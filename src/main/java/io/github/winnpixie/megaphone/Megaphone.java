package io.github.winnpixie.megaphone;

import io.github.winnpixie.commons.spigot.SpigotHelper;
import io.github.winnpixie.commons.spigot.TextHelper;
import io.github.winnpixie.commons.spigot.configurations.AnnotatedConfigurator;
import io.github.winnpixie.commons.spigot.configurations.adapters.BukkitConfigurationAdapter;
import io.github.winnpixie.megaphone.commands.admin.MegaphoneCommand;
import io.github.winnpixie.megaphone.tasks.BroadcastTask;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Megaphone extends JavaPlugin {
    public final AnnotatedConfigurator configuration = new AnnotatedConfigurator();

    private BukkitTask broadcastTask;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        configuration.setAdapter(new BukkitConfigurationAdapter(super.getConfig())).linkClass(Config.class).load();

        SpigotHelper.addCommand(new MegaphoneCommand(this));

        startBroadcast();
    }

    public void startBroadcast() {
        stopBroadcast();

        this.broadcastTask = getServer().getScheduler().runTaskTimer(this, new BroadcastTask(this), 0L,
                (long) (20.00 * Config.INTERVAL));
    }

    public void stopBroadcast() {
        if (broadcastTask != null) broadcastTask.cancel();
    }

    public void broadcast(int index) {
        String message = TextHelper.formatText(Config.formatMessage(Config.MESSAGES.get(index)));

        getServer().spigot().broadcast(TextComponent.fromLegacy(message));
    }
}
