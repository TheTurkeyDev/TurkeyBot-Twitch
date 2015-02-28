package com.Turkey.TurkeyBot.util;

import java.util.ArrayList;
import java.util.List;

import com.Turkey.TurkeyBot.gui.Gui;

public class KeyWordRaffle
{
	private boolean isRunning = false;
	private String keyWord;
	public List<String> entries;
	
	public KeyWordRaffle(String key)
	{
		keyWord = key;
		entries = new ArrayList<String>();
	}

	public boolean isRunning()
	{
		return isRunning;
	}

	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}
	
	public String getKeyWord()
	{
		return keyWord;
	}
	
	public void addEntry(String name)
	{
		entries.add(name);
		Gui.reloadTab();
	}
	
	public String getRandomEntry()
	{
		return entries.get((int)(Math.random() * entries.size()));
	}
	
	public List<String> getEntries()
	{
		return entries;
	}
}