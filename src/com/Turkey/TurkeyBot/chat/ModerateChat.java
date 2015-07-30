package com.Turkey.TurkeyBot.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.files.ChatSettings;
import com.Turkey.TurkeyBot.files.ResponseSettings;

public class ModerateChat
{

	private String[] message;

	private String[] blackList;

	private List<String> emotes = new ArrayList<String>();

	public static boolean Moderate = true;

	private List<String> bypass = new ArrayList<String>();

	public ModerateChat()
	{

		if(!TurkeyBot.bot.getProfile().chatSettings.getSetting("WordBlackList").equalsIgnoreCase(""))
			blackList = TurkeyBot.bot.getProfile().chatSettings.getSetting("WordBlackList").split(",");
		else
			blackList = new String[0];
		try
		{
			URL url = new URL("https://api.twitch.tv/kraken/chat/turkey2349/emoticons");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			String result = "";
			while((line = reader.readLine()) != null)
			{
				result += line + "\n";
			}
			reader.close();

			int index = result.indexOf("regex");
			while(index > -1)
			{
				result = result.substring(index + 8);
				emotes.add(result.substring(0, result.indexOf("\"")));
				index = result.indexOf("regex");
			}
		} catch(Exception e)
		{
		}
		;
	}

	/**
	 * Returns if the given message is valid to be said in chat or if the chat message should be filtered
	 * 
	 * @param message
	 *            The chat message to parse through.
	 * @param sender
	 *            the username that sent the message
	 * @return if the message is ok or false if it should be filtered
	 */
	public boolean isValidChat(String m, String sender)
	{
		message = m.split(" ");

		if(TurkeyBot.bot.isMod(sender) || this.checkForImmunity(sender) || !Moderate)
		{
			return true;
		}

		ErrorType error;
		ResponseSettings response = TurkeyBot.bot.getProfile().spamResponseFile;
		if((error = passesWordCheck()) != ErrorType.None)
		{
			if(error == ErrorType.Caps)
				TurkeyBot.bot.sendMessage(response.getSetting("CapsMessage"));
			if(error == ErrorType.Length)
				TurkeyBot.bot.sendMessage(response.getSetting("LengthMessage"));
			if(error == ErrorType.BadWord)
				TurkeyBot.bot.sendMessage(response.getSetting("BlockedWordMessage"));
			if(error == ErrorType.BlacklistWord)
				TurkeyBot.bot.sendMessage("");
			if(error == ErrorType.Emotes)
				TurkeyBot.bot.sendMessage(response.getSetting("EmotesMessage"));
			if(error == ErrorType.Sybols)
				TurkeyBot.bot.sendMessage(response.getSetting("SymbolsMessage"));
			TurkeyBot.bot.sendMessage("/timeout " + sender + " 1");
			return false;
		}
		if((error = passesLinkCheck()) != ErrorType.None)
		{
			TurkeyBot.bot.sendMessage(response.getSetting("LinkMessage"));
			TurkeyBot.bot.sendMessage("/timeout " + sender + " 1");
			return false;
		}

		return true;
	}

	/**
	 * Parses through the words of the message and checks for any flags in the chat message. Checks for Caps, length, Blacklisted words/ Astrixs, Emotes and Symbols.
	 * 
	 * @return The Error Type of the chat message. ErrorType.None if no flag is raised.
	 */
	public ErrorType passesWordCheck()
	{
		// TODO: Fix emotes check as not all emotes are checked for.
		int caps = 0;
		int letters = 0;
		int symbols = 0;
		int charecters = 0;
		int numofemotes = 0;
		for(String word : message)
		{
			if(word.contains("***"))
				return ErrorType.BadWord;
			if(isBlackListed(word))
				return ErrorType.BlacklistWord;
			if(emotes.contains(word))
			{
				numofemotes++;
			}
			else
			{
				for(char letter : word.toCharArray())
				{
					if(letter >= 65 && letter <= 90)
						caps++;
					if((letter >= 65 && letter <= 90) || (letter >= 97 && letter <= 122))
					{
						letters++;
					}
					else if(!(letter >= 48 && letter <= 57))
					{
						symbols++;
					}
					charecters++;
				}
			}
		}
		ChatSettings chat = TurkeyBot.bot.getProfile().chatSettings;
		int capsMax = Integer.parseInt(chat.getSetting("MaxCaps"));
		int capsMin = Integer.parseInt(chat.getSetting("MinimumCaps"));
		int capsPercent = Integer.parseInt(chat.getSetting("MaxpercentofCaps"));
		if(capsMax != -1 && caps > capsMax)
			return ErrorType.Caps;
		if((capsMin != -1 && caps > capsMin) && (capsPercent != -1 && (((double) caps / (double) letters) * 100) > capsPercent))
			return ErrorType.Caps;

		int emotesMax = Integer.parseInt(chat.getSetting("MaxEmotes"));
		int emotesMin = Integer.parseInt(chat.getSetting("MinimumEmotes"));
		int emotesPercent = Integer.parseInt(chat.getSetting("MaxpercentofEmotes"));

		if(emotesMax != -1 && numofemotes > emotesMax)
			return ErrorType.Emotes;
		if((emotesMin != -1 && numofemotes > emotesMin) && (emotesPercent != -1 && (((double) numofemotes / (double) message.length) * 100) > emotesPercent))
			return ErrorType.Emotes;

		if(letters > Integer.parseInt(chat.getSetting("MaxMessageLength")))
			return ErrorType.Length;

		int symbolsMax = Integer.parseInt(chat.getSetting("MaxSymbols"));
		int symbolsMin = Integer.parseInt(chat.getSetting("MinimumSymbols"));
		int symbolsPercent = Integer.parseInt(chat.getSetting("MaxpercentofSymbols"));

		if(symbolsMax != -1 && symbols > symbolsMax)
			return ErrorType.Sybols;
		if((symbolsMin != -1 && symbols > symbolsMin) && (symbolsPercent != -1 && (((double) symbols / (double) charecters) * 100) > symbolsPercent))
			return ErrorType.Sybols;

		return ErrorType.None;
	}

	/**
	 * Checks for possible links in the chat message.
	 * 
	 * @return ErrorType.Link if a link is used or ErrorType.None is chat message has no links.
	 */
	public ErrorType passesLinkCheck()
	{
		if(!Boolean.parseBoolean(TurkeyBot.bot.getProfile().chatSettings.getSetting("BlockLinks")))
		{
			return ErrorType.None;
		}
		for(String word : message)
		{
			if(word.contains("."))
			{
				String wordFixed = word;
				if(word.contains("http://"))
					wordFixed = word.substring(word.indexOf("http://"));
				else if(word.contains("https://"))
					wordFixed = word.substring(word.indexOf("https://"));
				if(word.contains("www."))
					wordFixed = word.substring(word.indexOf("www."));

				int index = wordFixed.indexOf(".");
				if(index > 0 && index < wordFixed.length() - 1)
				{
					UrlValidator validator = new UrlValidator();
					if(!wordFixed.contains("http://") && !wordFixed.contains("https://"))
						wordFixed = "http://" + wordFixed;
					if(validator.isValid(wordFixed))
						return ErrorType.Link;
				}
			}
		}
		return ErrorType.None;
	}

	/**
	 * Checks if the given word is on the black list.
	 * 
	 * @param tocheck
	 *            The word to check against the black list.
	 * @return If the word is black listed or not.
	 */
	public boolean isBlackListed(String tocheck)
	{
		for(String blackword : blackList)
		{
			if(blackword.equalsIgnoreCase(tocheck))
				return true;
		}
		return false;
	}

	/**
	 * Possible error Types on a given chat message
	 *
	 */
	public enum ErrorType
	{
		Caps, Link, Length, BadWord, BlacklistWord, Sybols, Emotes, None;
	}

	/**
	 * Adds the specified user name to a list for people who will bypass the chat modertion check.
	 * 
	 * @param name
	 *            The username to add to the list.
	 */
	public void giveImmunityTo(String name)
	{
		bypass.add(name);
	}

	/**
	 * Checks to see if the given user name is able to bypass the chat filter. Auto removes the username from the list if the name is on the list.
	 * 
	 * @param name
	 *            The username to check to see if they bypass the filter.
	 * @return If the given username can bypass the chat filter.
	 */
	public boolean checkForImmunity(String name)
	{
		if(bypass.contains(name))
			bypass.remove(name);
		else
			return false;
		return true;
	}
}
