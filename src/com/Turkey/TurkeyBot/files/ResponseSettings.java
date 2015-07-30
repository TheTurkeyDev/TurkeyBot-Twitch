package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import com.Turkey.TurkeyBot.TurkeyBot;

public class ResponseSettings extends BotFile
{
	
	private static String propName = "SpamResponse.properties";
	
	public ResponseSettings() throws IOException
	{
		super("C:" + File.separator + "TurkeyBot" + File.separator + TurkeyBot.bot.getProfile().getProfileName() + File.separator + "properties" + File.separator + propName);
		loadSettings();
	}
	
	public void loadSettings() throws IOException
	{
		Properties defaultproperties = new Properties();
		InputStream iiStream = SettingsFile.class.getResourceAsStream("/properties/SpamResponse.properties");
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
	 * Gets all of the Keys of the settings in the file.
	 * @return The keys of the settings.
	 */
	public Set<Object> getSettings()
	{
		return properties.keySet();
	}
}
