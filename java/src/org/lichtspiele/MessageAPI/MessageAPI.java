package org.lichtspiele.MessageAPI;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.command.ColouredConsoleSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.lichtspiele.MessageAPI.exception.TranslationFileNotFoundException;
import org.lichtspiele.MessageAPI.exception.TranslationNotFoundException;

public abstract class MessageAPI {
	
	protected Translation translation	 	= null;
	
	private String message_prefix			= null;

	private JavaPlugin plugin				= null;

	
	public MessageAPI(JavaPlugin plugin) throws TranslationFileNotFoundException {
		this.plugin 		= plugin;
		this.message_prefix	= plugin.getName();
		this.loadTranslation();		
	}

	private void loadTranslation() throws TranslationFileNotFoundException {
		this.translation = new Translation(this.plugin);
	}
	
	public void reloadTranslation() throws TranslationFileNotFoundException {
		this.loadTranslation();
	}
	
	
	public String getMessagePrefix() {
		return ChatColor.WHITE + "[" + ChatColor.AQUA + this.message_prefix.toString() + ChatColor.WHITE + "] " + ChatColor.RESET;
	}
	
	
	public void send(CommandSender sender, String message, boolean prefix) {
		if (prefix) message = this.getMessagePrefix() + message;
		
		if (sender instanceof Player || sender instanceof ColouredConsoleSender) {
			sender.sendMessage(message);
		} else {
			sender.sendMessage(ChatColor.stripColor(message));
		}
		
		//FIXME
		/*
		 * https://bukkit.org/threads/json-chat-lib-easily-create-and-send-json-messages-to-players.188165/
		 * 
		 *
		JSONChatMessage message = new JSONChatMessage("Hey, ", JSONChatColor.AQUA, null);
        JSONChatExtra extra = new JSONChatExtra("<Click This>", JSONChatColor.BLUE, Arrays.asList(JSONChatFormat.BOLD));
        extra.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "Example Hover Text");
        extra.setClickEvent(JSONChatClickEventType.RUN_COMMAND, "/example-command");
        message.addExtra(extra);
        message.sendToPlayer(event.getPlayer());
        
		 *
		 * https://github.com/bobacadodl/Bukkit-JSONMessageLib - 1.8 compat problem?
		 * 
		 * alt: http://dev.bukkit.org/bukkit-plugins/hovermessagesapi/
		 */
	}
	
	public void send(CommandSender sender, String message) {
		this.send(sender, message, true);
	}
	
	/*
	 * predefined messages
	 */
	public void reload(CommandSender sender) throws TranslationNotFoundException {
		this.send(sender, this.translation.getTranslation("reload"));
	}	
	
	public void version(CommandSender sender) throws TranslationNotFoundException {
		this.send(sender,
			this.translation.getTranslation("version", new String[] { "version", this.plugin.getDescription().getVersion() })
		);
	}
	
	public void insufficientPermission(CommandSender sender, String permission) throws TranslationNotFoundException {
		this.send(sender, 
			this.translation.getTranslation("missing_permission", new String[] {"permission", permission} )
		);
	}
	
	public void missingTranslation(CommandSender sender, String path) {
		this.send(sender,
			ChatColor.RED + "ERROR:" + 
			ChatColor.WHITE + " Translation not found for path " + 
			ChatColor.AQUA + path
		);
	}
	
	public void missingTranslationFile(CommandSender sender, String locale) {
		this.send(sender,
			ChatColor.RED + "ERROR:" +
			ChatColor.WHITE + " Translation file not found for locale  " +
			ChatColor.AQUA + locale
		);						
	}
	
	public void mustBePlayer(CommandSender sender) throws TranslationNotFoundException {
		this.send(sender, this.translation.getTranslation("must_be_ingame"));		
	}
	
	public void unknownWorld(CommandSender sender, String world) throws TranslationNotFoundException {
		this.send(sender,
			this.translation.getTranslation("unknown_world", new String[] {"world", world} )
		);
	}	
	
	public void invalidCommand(CommandSender sender, String command) throws TranslationNotFoundException {
		this.send(sender,
			this.translation.getTranslation("invalid_command", new String[] { "command", command } )				
		);
	}
	
	
	/*
	 * help
	 */
	public void helpTitle(CommandSender sender) throws TranslationNotFoundException {
		this.send(sender, this.translation.getTranslation("help.header"));
	}
	
	public void helpEntry(CommandSender sender, String command, String[] args, String desc_lang_key) throws TranslationNotFoundException {
		String args_txt = StringUtils.join(args, " ");
		if (args_txt.length() > 0) args_txt = " " + args_txt;
		
		this.send(sender,
			this.translation.getTranslation("help.entry", new String[] {
				"command",	command,
				"args",		args_txt.toString(),
				"desc",		this.translation.getTranslation(desc_lang_key)		
			}),
			false
		);	
	}
	
}