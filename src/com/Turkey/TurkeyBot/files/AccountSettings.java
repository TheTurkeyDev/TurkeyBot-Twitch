package com.Turkey.TurkeyBot.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.Turkey.TurkeyBot.util.CustomEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AccountSettings extends JsonFile
{
	private Map<String, Entry<String, String>> accounts;

	public AccountSettings() throws IOException
	{
		accounts = new HashMap<String, Entry<String, String>>();
		gson = new Gson();
		file = new File(System.getProperty("user.home") + File.separator + "TurkeyBot" + File.separator + "AccountSettings.json");
		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
			mainFile = new JsonObject();

			this.updateFile();
		}
		loadfile();
	}

	private void loadfile() throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String result = "";
		String line = "";
		while((line = reader.readLine()) != null)
		{
			result += line;
		}
		reader.close();
		JsonElement obj = TurkeyBot.json.parse(result);
		if(obj == null) return;
		mainFile = obj.getAsJsonObject();
		for(Entry<String, JsonElement> elements : mainFile.entrySet())
		{
			String userName = elements.getValue().getAsJsonObject().get("AccountUserName").getAsString();
			String oAuth = elements.getValue().getAsJsonObject().get("AccountoAuth").getAsString();
			Entry<String, String> entry = new CustomEntry<String, String>(userName, oAuth);
			accounts.put(elements.getKey(), entry);
		}
	}

	private void updateFile()
	{
		try
		{
			FileOutputStream outputStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(outputStream);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(mainFile);
			writer.append(json);
			writer.close();
			outputStream.close();
		} catch(IOException ex)
		{
			ConsoleTab.output(Level.Error, "Could not write to json file for the account settings");
		}
	}

	public void addAccount(String display, String userName, String oAuth)
	{
		Entry<String, String> entry = new CustomEntry<String, String>(userName, oAuth);
		accounts.put(display, entry);

		JsonObject accountInfo = new JsonObject();
		accountInfo.addProperty("AccountUserName", userName);
		accountInfo.addProperty("AccountoAuth", oAuth);
		this.mainFile.add(display, accountInfo);

		this.updateFile();
	}

	public Map<String, Entry<String, String>> getAccounts()
	{
		return this.accounts;
	}

	public Entry<String, String> getAccountFromDisplayName(String name)
	{
		return accounts.get(name);
	}
}