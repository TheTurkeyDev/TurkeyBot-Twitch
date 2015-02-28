package com.Turkey.TurkeyBot.util;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.Gui;

public class CurrencyThread implements Runnable
{

	private int delay;
	private int amount;
	private TurkeyBot bot;
	private boolean run;

	public CurrencyThread(int delay, int amount, TurkeyBot bot)
	{
		this.delay = delay;	
		this.amount = amount;
		this.bot = bot;
	}
	
	public void initCurrencyThread()
	{
		run = true;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		while(run)
		{
			try
			{
				synchronized(this)
				{
					this.wait(60000*delay);
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			giveCurrency();
		}
	}
	
	private void giveCurrency()
	{
		for(String viewer: bot.getViewers())
		{
			bot.currency.addCurrencyFor(viewer.toLowerCase(), amount);
		}
		Gui.reloadTab();
	}

	/**
	 * Stops the follower tracker for the bot in the channel
	 */
	public void stopThread()
	{
		run = false;
	}
}