package com.Turkey.TurkeyBot.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.Turkey.TurkeyBot.gui.Gui;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AnnouncementFile
{
	private File file;
	private List<String> announcements;
	private List<String> enabledannouncements;
	private JsonObject obj;
	private Random r;

	public AnnouncementFile(TurkeyBot b) throws IOException
	{
		r = new Random();
		file = new File("C:" + File.separator + "TurkeyBot" + File.separator + b.getProfile().getProfileName() + File.separator + "properties" + File.separator + "Announcements.json");
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		loadAnnouncements();
	}

	private void loadAnnouncements() throws IOException
	{
		announcements = new ArrayList<String>();
		enabledannouncements = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String result = "";
		String line = "";
		while((line = reader.readLine()) != null)
		{
			result += line;
		}
		reader.close();

		try{
			obj = TurkeyBot.json.parse(result).getAsJsonObject();
		}catch(IllegalStateException e){
			obj = new JsonObject();
			JsonArray tempArray = new JsonArray();
			obj.add("Announcements", tempArray);
			return;
		}
		JsonArray tempArray = obj.get("Announcements").getAsJsonArray();
		for(int a = 0; a < tempArray.size(); a++)
		{
			JsonObject announcement = tempArray.get(a).getAsJsonObject();
			announcements.add(announcement.get("Announcement").getAsString());
			if(announcement.get("Enabled").getAsBoolean())
			{
				enabledannouncements.add(announcement.get("Announcement").getAsString());
			}
		}
	}

	/**
	 * Reloads the announcements File
	 */
	public void reloadAnnouncements()
	{
		try
		{
			loadAnnouncements();
			Gui.reloadTab();
		} catch (IOException e)
		{
			ConsoleTab.output(Level.Error, "Could not reload the Announcements File!");
		}
	}

	/**
	 * Removes the commands property file.
	 */
	public void removeAnnouncement(int i)
	{
		JsonArray tempArray = obj.get("Announcements").getAsJsonArray();
		JsonObject temp = tempArray.remove(i).getAsJsonObject();
		enabledannouncements.remove(temp.get("Announcement").getAsString());
		announcements.remove(temp.get("Announcement").getAsString());
		saveJson();
	}

	/**
	 * Adds the commands property file.
	 */
	public void addAnnouncement(String announcement)
	{
		JsonObject toAdd = new JsonObject();
		toAdd.addProperty("Announcement", announcement);
		toAdd.addProperty("Enabled", true);
		JsonArray tempArray = obj.get("Announcements").getAsJsonArray();
		tempArray.add(toAdd);
		saveJson();
	}

	/**
	 * Disables the command in the file.
	 */
	public void disableAnnouncement(int i)
	{
		JsonArray tempArray = obj.get("Announcements").getAsJsonArray();
		JsonObject temp = tempArray.get(i).getAsJsonObject();
		temp.addProperty("Enabled", false);
		enabledannouncements.remove(temp.get("Announcement").getAsString());
		saveJson();
	}

	/**
	 * Enables the command in the file.
	 */
	public void enableAnnouncement(int i)
	{
		JsonArray tempArray = obj.get("Announcements").getAsJsonArray();
		JsonObject temp = tempArray.get(i).getAsJsonObject();
		temp.addProperty("Enabled", true);
		enabledannouncements.add(temp.get("Announcement").getAsString());
		saveJson();
	}

	/**
	 * Saves the Announcement Json file
	 */
	public void saveJson()
	{
		try{
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(obj);
			FileOutputStream outputStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			writer.append(json);
			writer.close();
			outputStream.close();
		}catch(IOException ex){ConsoleTab.output(Level.Error, "Could not save the Announcements Json File!");}
	}

	public String getRandomAnnouncement()
	{
		if(enabledannouncements.size() > 0)
			return enabledannouncements.get(r.nextInt(enabledannouncements.size()));
		return "";
	}

	public String getAnnouncement(int i)
	{
		return obj.get("Announcements").getAsJsonArray().get(i).getAsJsonObject().get("Announcement").getAsString();
	}

	public List<String> getAnnouncements()
	{
		return announcements;
	}

	public boolean isEnabled(String announcement)
	{
		return enabledannouncements.contains(announcement);
	}
}