package com.codeman04thefreaking2nd.wardenware;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WardenWare extends JavaPlugin {

    private String discordWebhookUrl = "YOUR DISCORD WEBHOOK URL HERE";
    private static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        logger.log(Level.INFO, "WardenWare plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        logger.log(Level.INFO, "WardenWare plugin has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wardenware")) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return true;
            }
            
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Please specify a command to execute.");
                return true;
            }
//This java method is outdated by using a oldass libary from 2014, holy shit
            String command = String.join(" ", args);
            String output = executeCommand(command);

            if (output != null) {
                sender.sendMessage(ChatColor.RED + "Failed to execute command: " + output);
            } else {
                sender.sendMessage(ChatColor.GREEN + "Command executed successfully!");
            }

            return true;
        }

        return false;
    }

    private String executeCommand(String command) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String json = "{\"content\":\"" + command + "\"}";
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(discordWebhookUrl)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return null;
            } else {
                return response.message();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
