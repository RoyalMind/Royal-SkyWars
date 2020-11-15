package net.royalmind.skywars;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    public static String translate(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> translate(final List<String> message) {
        final List<String> temp = new ArrayList<>();
        for (String s : message) {
            temp.add(translate(s));
        }
        return temp;
    }
}
