package io.github.winnpixie.megaphone;

import io.github.winnpixie.hukkit.Hukkit;
import io.github.winnpixie.hukkit.TextHelper;
import io.github.winnpixie.hukkit.configs.AnnotatedConfigurationManager;
import io.github.winnpixie.hukkit.configs.adapters.BukkitAdapter;
import io.github.winnpixie.megaphone.commands.MegaphoneCommand;
import io.github.winnpixie.megaphone.tasks.BroadcastTask;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Megaphone extends JavaPlugin {
    public final AnnotatedConfigurationManager configManager = new AnnotatedConfigurationManager();

    public BukkitTask broadcastTask;

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        configManager.setAdapter(new BukkitAdapter(super.getConfig())).linkClass(Config.class).load();

        this.broadcastTask = getServer().getScheduler().runTaskTimer(this, new BroadcastTask(this), 0L,
                (long) (20.00 * Config.INTERVAL));

        Hukkit.addCommand(new MegaphoneCommand(this), this);

        getLogger().info("Megaphone init DONE");
    }

    @Override
    public void onDisable() {
        getLogger().info("Megaphone unload DONE");
    }

    public void broadcast(int index) {
        var msg = TextHelper.formatColors(Config.formatMessage(Config.MESSAGES.get(index)));

        getServer().spigot().broadcast(TextComponent.fromLegacyText(msg));
    }
}
