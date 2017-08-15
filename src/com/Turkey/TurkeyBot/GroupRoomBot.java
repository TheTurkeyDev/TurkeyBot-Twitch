package com.Turkey.TurkeyBot;

import org.jibble.pircbot.PircBot;

import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class GroupRoomBot extends PircBot
{
	public static GroupRoomBot groupBot = new GroupRoomBot();
	public String groupRoom = "#_turkey2349_1446931024590";
	
	public void connectToGroup()
	{
		try
		{
			System.out.println("here");
			connect("192.16.64.180", 6667, SecretStuff.oAuth);
			System.out.println(super.getServer());
		} catch(Exception e)
		{
			e.printStackTrace();
			if(!e.getMessage().equalsIgnoreCase("The PircBot is already connected to an IRC server.  Disconnect first."))
			{
				ConsoleTab.output(Level.Error, "Could not connect to Group Room! \n" + e.getMessage());
				return;
			}
		}
		System.out.println(super.getServer());
		super.joinChannel(groupRoom);
		super.sendMessage(groupRoom, "test");
		System.out.println(super.getServer());
	}
}
