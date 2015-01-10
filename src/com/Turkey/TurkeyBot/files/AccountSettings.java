package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import com.Turkey.TurkeyBot.SecretStuff;
import com.Turkey.TurkeyBot.TurkeyBot;

public class AccountSettings extends BotFile
{
	private static String propName = "AccountSettings.properties";

	public AccountSettings(TurkeyBot b) throws IOException
	{
		super(b, "C:" + File.separator + "TurkeyBot" + File.separator + "properties" + File.separator + propName);
		loadSettings();
	}

	public void loadSettings() throws IOException
	{
		Properties defaultproperties = new Properties();
		InputStream iiStream = SettingsFile.class.getResourceAsStream("/properties/AccountSettingsDefault.properties");
		defaultproperties.load(iiStream);

		for(Object o : defaultproperties.keySet())
		{
			String key = (String) o;
			if(!properties.containsKey(key))
			{
				properties.setProperty(key, defaultproperties.getProperty(key));
			}
		}
		if(properties.get("AccountoAuth") != null)
			SecretStuff.oAuth = (String) properties.get("AccountoAuth");
		
		save();
	}

	public Set<Object> getSettings()
	{
		return properties.keySet();
	}
}
