package com.Turkey.TurkeyBot.chat;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class AutoAnnouncement implements Runnable
{

	public static boolean run = true;
	private int delay = 0;
	private TurkeyBot bot;
	private Thread thread;

	public AutoAnnouncement(TurkeyBot bot)
	{
		this.bot = bot;
		try{
			delay = Integer.parseInt(bot.settings.getSetting("AnnounceDelay"));
		}catch(NumberFormatException e){ConsoleTab.output(Level.Error, "The Announcement time is not set as a integer!");return;}
		initAutoAnnouncemer();
	}
	
	public void initAutoAnnouncemer()
	{
		run = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Starts the thread the controls the Auto Announcement
	 */
	@Override
	public void run()
	{
		while(run)
		{
			try
			{
				synchronized(this)
				{
					this.wait(delay*1000);
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
				this.stop();
			}
			makeAnnouncement();
		}
	}

	private void makeAnnouncement()
	{
		String msg =  bot.announceFile.getRandomAnnouncement();
		if(msg != "")
			bot.sendMessage(msg);
	}

	public void stop()
	{
		run = false;
		try
		{
			thread.join();
		} catch (InterruptedException e)
		{
			
		}
	}
	
	public boolean isRunning()
	{
		return run;
	}
}
