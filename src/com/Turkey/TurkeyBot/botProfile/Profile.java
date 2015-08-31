package com.Turkey.TurkeyBot.botProfile;

import java.io.File;
import java.io.IOException;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.chat.AutoAnnouncement;
import com.Turkey.TurkeyBot.chat.ModerateChat;
import com.Turkey.TurkeyBot.files.AccountSettings;
import com.Turkey.TurkeyBot.files.AnnouncementFile;
import com.Turkey.TurkeyBot.files.ChatSettings;
import com.Turkey.TurkeyBot.files.CurrencyFile;
import com.Turkey.TurkeyBot.files.Followers;
import com.Turkey.TurkeyBot.files.ResponseSettings;
import com.Turkey.TurkeyBot.files.SettingsFile;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.Turkey.TurkeyBot.util.CurrencyThread;

public class Profile
{
	private String profileName;

	private File profileFolder;

	public CurrencyFile currency;
	public SettingsFile settings;
	public ChatSettings chatSettings;
	public ResponseSettings spamResponseFile;
	public AccountSettings accountSettingsFile;
	public Followers followersFile;
	public CurrencyThread currencyTrack;
	public AnnouncementFile announceFile;

	public ModerateChat chatmoderation;
	public AutoAnnouncement announcer;

	private String currencyName;

	public Profile(String name)
	{
		this.profileName = name;
		profileFolder = new File("C:" + File.separator + "TurkeyBot" + File.separator + name);
	}

	public String getProfileName()
	{
		return this.profileName;
	}

	public File getProfileFolder()
	{
		return this.profileFolder;
	}

	public void loadProfile()
	{
		try
		{
			accountSettingsFile = new AccountSettings();
			currency = new CurrencyFile();
			settings = new SettingsFile();
			chatSettings = new ChatSettings();
			spamResponseFile = new ResponseSettings();
			announceFile = new AnnouncementFile();
		} catch(IOException e)
		{
			ConsoleTab.output(Level.Error, "Failed to load profile files!");
			ConsoleTab.output(Level.Error, e.getMessage());
		}

		chatmoderation = new ModerateChat();

		currencyName = settings.getSetting("CurrencyName");
		if(!settings.getSetting("AutoCurrencyDelay").equalsIgnoreCase("-1"))
		{
			try
			{
				currencyTrack = new CurrencyThread(Integer.parseInt(settings.getSetting("AutoCurrencyDelay")), Integer.parseInt(settings.getSetting("AutoCurrencyAmount")));
				currencyTrack.initCurrencyThread();
			} catch(NumberFormatException e)
			{
				TurkeyBot.bot.disconnectFromChannel();
				ConsoleTab.output(Level.Error, "Your Auto Currency Settings are invalid!");
				return;
			}
		}

		if(!settings.getSetting("AnnounceDelay").equals("-1"))
		{
			announcer = new AutoAnnouncement();
		}
	}

	public String getCurrencyName()
	{
		return this.currencyName;
	}
}