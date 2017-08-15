package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class SettingsFile extends BotFile
{
	private static String propName = "Settings.properties";
	
	public SettingsFile() throws IOException
	{
		super(System.getProperty("user.home") + File.separator + "TurkeyBot" + File.separator + TurkeyBot.bot.getProfile().getProfileName() + File.separator + "properties" + File.separator + propName);
		loadSettings();
	}
	
	public void loadSettings() throws IOException
	{
		Properties defaultproperties = new Properties();
		InputStream iiStream = SettingsFile.class.getResourceAsStream("/properties/DefaultSettings.properties");
		defaultproperties.load(iiStream);
		
		for(Object o : defaultproperties.keySet())
		{
			String key = (String) o;
			if(!properties.containsKey(key))
			{
				properties.setProperty(key, defaultproperties.getProperty(key));
			}
		}
		save();
	}
	
	/**
	 * Returns the given settings as a boolean
	 * @param key The key to get the setting/ value of.
	 * @return The boolean value of the given key.
	 */
	public boolean getSettingAsBoolean(String key)
	{
		try{
			boolean bool = Boolean.parseBoolean(properties.getProperty(key));
			return bool;
		}catch(Exception e){ConsoleTab.output(Level.Error, "Value for " + key + " is not a boolean!");return false;}
	}
	
	/**
	 * Gets all of the Keys of the settings in the file.
	 * @return The keys of the settings.
	 */
	public Set<Object> getSettings()
	{
		return properties.keySet();
	}
}
