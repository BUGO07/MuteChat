package cf.bugodev.mutechat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class MuteChat extends JavaPlugin implements Listener {
	
	private volatile boolean isChatEnabled = true;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if (!isChatEnabled) {
			if(!(event.getPlayer().hasPermission(getConfig().getString("bypass-permission")))) {
				event.setCancelled(true);
			}
		}
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("mutechat")) {
        	if (!(sender.hasPermission(getConfig().getString("mute-permission")))) {
        		
        		sender.sendMessage(getConfig().getString("mute-permission-message"));
        		return true;
        	}
        	
        	isChatEnabled = !isChatEnabled;
        	for(String message : getConfig().getStringList(isChatEnabled ? "unmute-message" : "mute-message")) {
        		getServer().broadcastMessage(message.replace("&", "§").replace("%user%", sender.getName()));
        	}
            
        }
        
        if (cmd.getName().equalsIgnoreCase("mutechatconfig")) {
        	try {
        		reloadConfig();
        		sender.sendMessage(getConfig().getString("reload-message").replace("&", "§"));
        	} catch (Exception error) {
        		error.printStackTrace();
        		sender.sendMessage(ChatColor.RED + "There was an error while trying to reload the config.");
        		sender.sendMessage(ChatColor.RED + "Please check the logs to see why this error occured.");
        	}
        }

        return true;
    }
	
	
}
