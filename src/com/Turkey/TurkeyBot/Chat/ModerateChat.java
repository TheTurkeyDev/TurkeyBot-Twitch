package com.Turkey.TurkeyBot.Chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;

import com.Turkey.TurkeyBot.TurkeyBot;

public class ModerateChat
{
	private TurkeyBot bot;

	private String[] message;

	private String[] blackList;

	private List<String> emotes = new ArrayList<String>();

	public ModerateChat(TurkeyBot b)
	{
		bot = b;

		blackList = bot.chatSettings.getSetting("WordBlackList").split(",");
		try{
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
				result=result.substring(index+8);
				emotes.add(result.substring(0,result.indexOf("\"")));
				index = result.indexOf("regex");
			}
		}catch(Exception e){};
	}

	public boolean isValidChat(String m, String sender)
	{
		message = m.split(" ");
		if(bot.isMod(sender))
		{
			return true;
		}
		else if(bot.checkForImmunity(sender))
		{
			return true;
		}

		ErrorType error;

		if((error = passesWordCheck()) != ErrorType.None)
		{
			if(error == ErrorType.Caps)
				bot.sendMessage(bot.spamResponseFile.getSetting("CapsMessage"));
			if(error == ErrorType.Length)
				bot.sendMessage(bot.spamResponseFile.getSetting("LengthMessage"));
			if(error == ErrorType.BlockedWord)
				bot.sendMessage(bot.spamResponseFile.getSetting("BlockedWordMessage"));
			if(error == ErrorType.Emotes)
				bot.sendMessage(bot.spamResponseFile.getSetting("EmotesMessage"));
			bot.sendMessage("/timeout "+ sender + " 1");
			return false;
		}
		if((error = passesLinkCheck()) != ErrorType.None)
		{
			bot.sendMessage(bot.spamResponseFile.getSetting("LinkMessage"));
			bot.sendMessage("/timeout "+ sender + " 1");
			return false;
		}

		return true;
	}

	public ErrorType passesWordCheck()
	{	
		int caps = 0;
		int letters = 0;
		int symbols = 0;
		int charecters = 0;
		int numofemotes = 0;
		for(String word: message)
		{
			if(word.equalsIgnoreCase("***"))
			{
				return ErrorType.BlockedWord;
			}
			if(emotes.contains(word))
				numofemotes++;
			for(char letter: word.toCharArray())
			{
				if(letter >= 65 && letter <= 90)
					caps++;
				if((letter >= 65 && letter <= 90) || (letter >= 97 && letter <= 122))
				{
					letters++;
				}
				else
				{
					symbols++;
				}
				charecters++;
			}
		}

		int capsMax = Integer.parseInt(bot.chatSettings.getSetting("CapsTypedMaximum"));
		int capsMin = Integer.parseInt(bot.chatSettings.getSetting("CapsTypedMinmumforAffect"));
		int capsPercent = Integer.parseInt(bot.chatSettings.getSetting("MaxPercentOfCapsUsed"));

		if(capsMax != -1 && caps > capsMax)
			return ErrorType.Caps;
		if(( capsMin != -1 && caps > capsMin) && (capsPercent != -1 && (((double) caps / (double)letters)*100) > capsPercent))
			return ErrorType.Caps;

		int emotesMax = Integer.parseInt(bot.chatSettings.getSetting("EmotesTypedMaximum"));
		int emotesMin = Integer.parseInt(bot.chatSettings.getSetting("EmotesTypedMinmumforAffect"));
		int emotesPercent = Integer.parseInt(bot.chatSettings.getSetting("MaxPercentOfEmotesUsed"));

		if(emotesMax != -1 && numofemotes > emotesMax)
			return ErrorType.Emotes;
		if(( emotesMin != -1 && numofemotes > emotesMin) && (emotesPercent != -1 && (((double) numofemotes / (double)message.length)*100) > emotesPercent))
			return ErrorType.Emotes;

		if(letters > Integer.parseInt(bot.chatSettings.getSetting("MaxMessageLength")))
			return ErrorType.Length;
		
		int symbolsMax = Integer.parseInt(bot.chatSettings.getSetting("CapsTypedMaximum"));
		int symbolsMin = Integer.parseInt(bot.chatSettings.getSetting("CapsTypedMinmumforAffect"));
		int symbolsPercent = Integer.parseInt(bot.chatSettings.getSetting("MaxPercentOfCapsUsed"));

		if(symbolsMax != -1 && symbols > symbolsMax)
			return ErrorType.Sybols;
		if(( symbolsMin != -1 && symbols > symbolsMin) && (symbolsPercent != -1 && (((double) symbols / (double)charecters)*100) > symbolsPercent))
			return ErrorType.Sybols;
		return ErrorType.None;
	}

	public ErrorType passesLinkCheck()
	{
		if(!Boolean.parseBoolean(bot.chatSettings.getSetting("BlockLinks")))
		{
			return ErrorType.None;
		}
		for(String word: message)
		{
			if(word.contains("."))
			{
				int index = word.indexOf(".");
				if(index > 0 && index < word.length()-1)
				{
					UrlValidator validator = new UrlValidator();
					if(!word.contains("http://") && !word.contains("https://"))
						word="http://"+word;
					if(validator.isValid(word))
						return ErrorType.Link;
				}
			}
		}
		return ErrorType.None;
	}

	public boolean isBlackListed(String tocheck)
	{
		for(String blackword: blackList)
		{
			if(blackword.equalsIgnoreCase(tocheck))
				return true;
		}
		return false;
	}

	public enum ErrorType
	{
		Caps,
		Link,
		Length,
		BlockedWord,
		Sybols,
		Emotes,
		None;
	}
}