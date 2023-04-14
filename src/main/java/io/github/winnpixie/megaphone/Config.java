package io.github.winnpixie.megaphone;

import io.github.winnpixie.hukkit.configs.Link;

import java.util.List;

public class Config {
    @Link(path = "announcer.enabled")
    public static boolean ENABLED;

    @Link(path = "announcer.interval")
    public static double INTERVAL;

    @Link(path = "announcer.random-order")
    public static boolean RANDOMIZE;

    @Link(path = "announcer.prefix")
    public static String PREFIX;

    @Link(path = "announcer.suffix")
    public static String SUFFIX;

    @Link(path = "announcer.messages")
    public static List<String> MESSAGES;

    public static String formatMessage(String message) {
        return "%s%s%s".formatted(Config.PREFIX, message, Config.SUFFIX);
    }
}
