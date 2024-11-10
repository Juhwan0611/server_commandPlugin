package com.juhwan.server_commandPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class Server_commandPlugin extends JavaPlugin implements CommandExecutor {
    private Set<String> operators = new HashSet<>();

    @Override
    public void onEnable() {
        // Register commands
        this.getCommand("운영자등록").setExecutor(this);
        this.getCommand("도움").setExecutor(this);
        this.getCommand("공지").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("운영자등록")) {
            if (sender.isOp()) { // Check if the sender has OP permissions
                if (args.length == 1) {
                    String username = args[0];
                    operators.add(username);
                    sender.sendMessage(ChatColor.GREEN + username + " has been registered as an operator.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /운영자등록 <Username>");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("도움")) {
            if (args.length > 0) {
                String message = String.join(" ", args);
                for (String operatorName : operators) {
                    Player operator = Bukkit.getPlayer(operatorName);
                    if (operator != null && operator.isOnline()) {
                        operator.sendMessage(ChatColor.AQUA + "[HELP REQUEST] " + sender.getName() + ": " + message);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Your help request has been sent to the operators.");
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /도움 <message>");
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("공지")) {
            if (sender instanceof Player && operators.contains(sender.getName())) { // Check if the sender is a registered operator and is a player
                if (args.length > 0) {
                    String message = String.join(" ", args);
                    String announcement = ChatColor.BOLD + "\n" +
                            ChatColor.WHITE + "공지 : " + message + "\n";
                    Bukkit.broadcastMessage(announcement);
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /공지 <message>");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            }
            return true;
        }
        return false;
    }
}
