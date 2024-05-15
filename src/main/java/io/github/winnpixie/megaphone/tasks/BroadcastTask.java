package io.github.winnpixie.megaphone.tasks;

import io.github.winnpixie.commons.spigot.MathHelper;
import io.github.winnpixie.megaphone.Config;
import io.github.winnpixie.megaphone.Megaphone;

public class BroadcastTask implements Runnable {
    private final Megaphone plugin;

    private int messageIndex;

    public BroadcastTask(Megaphone plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!Config.ENABLED) return;
        if (Config.MESSAGES.isEmpty()) return;

        if (Config.RANDOMIZE) {
            messageIndex = MathHelper.getRandomInt(0, Config.MESSAGES.size());
        } else {
            messageIndex = (messageIndex + 1) % Config.MESSAGES.size();
        }

        plugin.broadcast(messageIndex);
    }
}
