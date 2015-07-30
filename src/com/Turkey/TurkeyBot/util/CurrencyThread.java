package com.Turkey.TurkeyBot.util;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.Gui;

public class CurrencyThread implements Runnable
{

	private int delay;
	private int amount;
	private boolean run = false;
	private Thread thread;

	public CurrencyThread(int delay, int amount)
	{
		this.delay = delay;	
		this.amount = amount;
	}
	
	public void initCurrencyThread()
	{
		run = true;
		if(thread == null || !thread.isAlive())
		{
			thread = new Thread(this);
			thread.start();
		}
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
		try
		{
			thread.interrupt();
			thread.join();
		} catch (InterruptedException e){}
	}
	
	private void giveCurrency()
	{
		for(String viewer: TurkeyBot.bot.getViewers())
		{
			TurkeyBot.bot.getProfile().currency.addCurrencyFor(viewer.toLowerCase(), amount);
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
	
	public boolean isRunning()
	{
		return run;
	}
}