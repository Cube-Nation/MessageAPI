package org.lichtspiele.MessageAPI;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.lichtspiele.CustomConfigurationFileAPI.CustomConfigurationFileAPIPlugin;
import org.lichtspiele.MessageAPI.exception.NoSuchPluginException;

public class MessageAPIPlugin extends JavaPlugin {
	
	public static MessageAPIPlugin instance	= null;
	
	/*
	 * instance stuff
	 */
	private void setInstance(MessageAPIPlugin instance) {
		MessageAPIPlugin.instance = (MessageAPIPlugin) instance;
	}
	
	public static MessageAPIPlugin getInstance() {
		return instance;
	}
	
	/*
	 * actions that happen when the plugin starts up
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	public void onEnable() {			
		try {
			this.getCustomConfigurationFileAPIPlugin();
		} catch (NoSuchPluginException e) {
			this.disable(e);
		}
		
		this.setInstance(this);
	}
	
	
	/*
	 * external plugins
	 */
	public JavaPlugin getPlugin(String name) throws NoSuchPluginException {
		JavaPlugin p = (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin(name);
		if (p == null) throw new NoSuchPluginException(name);
		
		return p;
	}
	
	public CustomConfigurationFileAPIPlugin getCustomConfigurationFileAPIPlugin() throws NoSuchPluginException {
		return (CustomConfigurationFileAPIPlugin) getPlugin("CustomConfigurationFileAPI");
	}
	
	/*
	 * logging stuff
	 */
	public void log(Level level, String message) {
		Logger.getLogger("Minecraft").log(
			level,
			ChatColor.stripColor(String.format("[MessageAPI] %s", message))
		);
	}
	
	public void log(Level level, String message, Throwable t) {
		log(
			level,
			ChatColor.stripColor(String.format("[MessageAPI] %s", message)),
			t
		);		
	}
	
	
	/*
	 * disable plugin stuff
	 */
	public void disable(Exception e) {
		log(Level.SEVERE, "Unrecoverable error: " + e.getMessage());
		log(Level.SEVERE, "Disabling plugin");
        this.getPluginLoader().disablePlugin(this);	
	}

	public void disable(Exception e, CommandSender sender) {
		sender.sendMessage("[MessageAPI] Unrecoverable error. Disabling plugin");
		this.disable(e);
	}	
	
}
