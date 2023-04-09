package io.github.winnpixie.megaphone.tasks;

import io.github.winnpixie.hukkit.MathHelper;
import io.github.winnpixie.hukkit.TextHelper;
import io.github.winnpixie.megaphone.Config;
import io.github.winnpixie.megaphone.Megaphone;
import net.md_5.bungee.api.chat.TextComponent;

public class BroadcastTask implements Runnable {
    private final Megaphone plugin;

    private int messageIndex;

    public BroadcastTask(Megaphone plugin) {
        this.plugin = plugin;
    }


    @Override
    public void run() {
        if (Config.MESSAGES.isEmpty()) return;

        if (Config.RANDOMIZE) {
            messageIndex = MathHelper.getRandomInt(0, Config.MESSAGES.size());
        } else {
            messageIndex = (messageIndex + 1) % Config.MESSAGES.size();
        }

        var msg = Config.formatMessage(Config.MESSAGES.get(messageIndex));
        plugin.getServer().spigot().broadcast(TextComponent.fromLegacyText(TextHelper.formatColors(msg)));
    }
}
