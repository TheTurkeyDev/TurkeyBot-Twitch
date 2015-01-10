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
	
	public SettingsFile(TurkeyBot b) throws IOException
	{
		super(b,"C:" + File.separator + "TurkeyBot" + File.separator + "properties" + File.separator + propName);
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
	
	public boolean getSettingAsBoolean(String key)
	{
		try{
			boolean bool = Boolean.parseBoolean(properties.getProperty(key));
			return bool;
		}catch(Exception e){ConsoleTab.output(Level.Error, "Value for " + key + " is not a boolean!");return false;}
	}
	
	public Set<Object> getSettings()
	{
		return properties.keySet();
	}
}
