package com.Turkey.TurkeyBot.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.Turkey.TurkeyBot.TurkeyBot;


public class BotFile
{
	protected TurkeyBot bot;
	protected InputStream iStream;
	protected File file;
	protected Properties properties;
	
	public BotFile(TurkeyBot b, String path) throws IOException
	{
		bot = b;
		properties = new Properties();
		file = new File(path);
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		iStream = new FileInputStream(file);
		properties.load(iStream);
	}
	
	public void setSetting(String key, Object o)
	{
		properties.setProperty(key, o.toString());
		save();
	}
	
	public String getSetting(String key)
	{
		return properties.getProperty(key);
	}
	
	public void save()
	{
		try
		{
			properties.store(new FileOutputStream(file),"");
		} catch(Exception e){}
	}
}
