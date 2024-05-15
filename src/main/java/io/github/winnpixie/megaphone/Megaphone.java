package io.github.winnpixie.megaphone;

import io.github.winnpixie.commons.spigot.SpigotHelper;
import io.github.winnpixie.commons.spigot.TextHelper;
import io.github.winnpixie.commons.spigot.configs.AnnotationConfiguration;
import io.github.winnpixie.commons.spigot.configs.adapters.BukkitAdapter;
import io.github.winnpixie.megaphone.commands.admin.MegaphoneCommand;
import io.github.winnpixie.megaphone.tasks.BroadcastTask;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Megaphone extends JavaPlugin {
    public final AnnotationConfiguration configuration = new AnnotationConfiguration();

    public BukkitTask broadcastTask;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        configuration.setAdapter(new BukkitAdapter(super.getConfig())).linkClass(Config.class).load();

        this.broadcastTask = getServer().getScheduler().runTaskTimer(this, new BroadcastTask(this), 0L,
                (long) (20.00 * Config.INTERVAL));

        SpigotHelper.addCommand(new MegaphoneCommand(this));

        getLogger().info("Init OK");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unload OK");
    }

    public void broadcast(int index) {
        String message = TextHelper.formatText(Config.formatMessage(Config.MESSAGES.get(index)));

        getServer().spigot().broadcast(TextComponent.fromLegacy(message));
    }
}
