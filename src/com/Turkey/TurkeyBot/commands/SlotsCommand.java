package com.Turkey.TurkeyBot.commands;

import java.util.Random;

import com.Turkey.TurkeyBot.TurkeyBot;

public class SlotsCommand extends Command
{
	public SlotsCommand(String n)
	{
		super(n, "");
	}

	private String[] emotes = { "BibleThump", "BloodTrail", "FrankerZ", "MrDestructoid", "SSSsss", "PJSalt", "Kappa" };
	private int trys;

	public void oncommand(TurkeyBot bot, String channel, String sender, String login, String hostname, String message)
	{
		if(bot.getProfile().currency.getCurrencyFor(sender) > 0)
		{
			bot.getProfile().currency.addCurrencyFor(sender, -1);
			trys++;
			Random r = new Random();

			int slotOne = r.nextInt(emotes.length);
			int slotTwo = r.nextInt(emotes.length);
			int slotThree = r.nextInt(emotes.length);

			if(slotOne == slotTwo && slotOne == slotThree)
			{
				bot.sendMessage(bot.capitalizeName(sender) + " Your slots result is-------------- " + emotes[slotOne] + " " + emotes[slotTwo] + " " + emotes[slotThree] + " and you have won " + trys + " " + bot.getProfile().getCurrencyName() + "!!!");
				trys = 0;
			}
			else
			{
				String correctGrammar = "tries";
				if(trys < 2) correctGrammar = "try";
				bot.sendMessage(bot.capitalizeName(sender) + " Your slots result is-------------- " + emotes[slotOne] + " " + emotes[slotTwo] + " " + emotes[slotThree] + " and you have lost! The slots have not been won in " + trys + " " + correctGrammar + "!");
			}
		}
	}

	public boolean canEdit()
	{
		return false;
	}
}
