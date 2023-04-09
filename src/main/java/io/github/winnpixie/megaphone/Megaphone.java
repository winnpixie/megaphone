package io.github.winnpixie.megaphone;

import io.github.winnpixie.hukkit.Hukkit;
import io.github.winnpixie.hukkit.configs.AnnotatedConfigurationManager;
import io.github.winnpixie.hukkit.configs.adapters.BukkitAdapter;
import io.github.winnpixie.megaphone.commands.MegaphoneCommand;
import io.github.winnpixie.megaphone.tasks.BroadcastTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Megaphone extends JavaPlugin {
    public final AnnotatedConfigurationManager configManager = new AnnotatedConfigurationManager();

    public BukkitTask broadcastTask;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        configManager.setAdapter(new BukkitAdapter(this.getConfig())).linkClass(Config.class).load();

        this.broadcastTask = getServer().getScheduler().runTaskTimer(this,
                new BroadcastTask(this),
                0L,
                (long) (20.00 * Config.INTERVAL));

        Hukkit.addCommand(new MegaphoneCommand(this), this);

        getLogger().info("Megaphone init DONE");
    }

    @Override
    public void onDisable() {
        getLogger().info("Megaphone unload DONE");
    }
}
