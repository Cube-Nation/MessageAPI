package org.lichtspiele.MessageAPI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.lichtspiele.CustomConfigurationFileAPI.CustomConfigurationRegistry;
import org.lichtspiele.CustomConfigurationFileAPI.exception.CustomConfigurationFileNotFoundException;
import org.lichtspiele.MessageAPI.exception.TranslationFileNotFoundException;
import org.lichtspiele.MessageAPI.exception.TranslationNotFoundException;


public class Translation {

	private String default_locale	= "en_US";
	
	private String locale_path		= "locale";
	
	private String locale			= null;
	
	private JavaPlugin plugin 		= null;
		
	private YamlConfiguration data	= null;
	
	public Translation(JavaPlugin plugin) throws TranslationFileNotFoundException {
		this.plugin = plugin;
		this.loadTranslation();
	}
	
	private void loadTranslation() throws TranslationFileNotFoundException {
		String _locale = this.plugin.getConfig().getString("locale");

		YamlConfiguration yc = null;
		String[] language_files = { _locale, this.default_locale };
				
		for ( String file : language_files ) {
						
			String locale_file = this.locale_path + java.lang.System.getProperty("file.separator") + file + ".yml";
			try {
				yc = CustomConfigurationRegistry.get(locale_file);
			} catch (CustomConfigurationFileNotFoundException e) {
				continue;
			}			

			if (yc != null) {
				this.locale = file;
				break;
			}
		}

		if (yc == null)
			throw new TranslationFileNotFoundException("Could not find a suitable locale file");
		
		this.data = yc;
	}
	
	public String getLocale() {
		return this.locale;
	}
	
	public YamlConfiguration getTranslation() {
		return this.data;
	}
	
	public String getTranslation(String path) throws TranslationNotFoundException {
		String s;
		try {
			s = this.data.getString(path);
		} catch (NullPointerException e) {
			throw new TranslationNotFoundException(path);
		}
		if (s == null)
			throw new TranslationNotFoundException(path);
		 
		s = ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}
	
	public String getTranslation(String path, String[] args) throws TranslationNotFoundException {
		String s = this.getTranslation(path);
		
		 // check even		
    	if (args.length % 2 == 1)
            return s;
        
    	return this.applyArgs(s, args);
	}
	
	public String[] getTranslationStrings(String path, String[] args) throws TranslationNotFoundException {
		List<String> out = this.getTranslationList(path, args);
		
		String[] s = new String[args.length];
		s = out.toArray(s);
		return s;
	}

	@SuppressWarnings("unchecked")
	public List<String> getTranslationList(String path, String[] args) throws TranslationNotFoundException {
		List<String> list = null;
		try {
			list = (List<String>) this.data.getList(path);
		} catch (NullPointerException e) {
			throw new TranslationNotFoundException(path);
		}
		
		if (list == null || list.size() == 0) throw new TranslationNotFoundException(path);
		
		// create a copy!
		List<String> out = new ArrayList<String>();
		for (int i = 1; i <= list.size(); i++) {
			out.add(this.applyArgs(list.get(i-1), args));
		}
		
		return out;
	}
	
	private String applyArgs(String s, String[] args) {
        for(int i = 0; i < args.length; i++){
            s = s.replaceAll("%" + args[i] + "%", args[i+1]);
            i++;
        }
		return ChatColor.translateAlternateColorCodes('&', s);		
	}
	
}