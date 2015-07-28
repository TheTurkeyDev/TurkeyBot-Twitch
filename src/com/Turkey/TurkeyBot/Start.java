package com.Turkey.TurkeyBot;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.Turkey.TurkeyBot.gui.ProfilesGui;

public class Start
{
	public static void main(String[] args)
	{
		new ProfilesGui();
		checkVersion();
	}

	/**
	 * Checks for an update to the bot.
	 */
	private static void checkVersion()
	{
		String result = "";
		try
		{
			URL url = new URL("http://theprogrammingturkey.com/API/versionCheck.php?application=TurkeyBot");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while((line = reader.readLine()) != null)
			{
				result += line;
			}
			reader.close();
		} catch(Exception e)
		{
			ConsoleTab.output(Level.Error, "Could not connect to the server to check for the current version of TurkeyBot");
			return;
		}

		if(!result.equalsIgnoreCase(TurkeyBot.VERSION))
		{
			JFrame popup = new JFrame();
			Dimension size = new Dimension(350, 150);
			popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			popup.setSize(size);
			popup.setPreferredSize(size);
			popup.setLayout(null);
			popup.setTitle("Out of Date!!!");
			popup.setLocationRelativeTo(null);

			JLabel accountNameLabel = new JLabel("Your version of TurkeyBot is currently out of date!");
			accountNameLabel.setLocation(25, 10);
			accountNameLabel.setSize(400, 25);
			popup.add(accountNameLabel);

			accountNameLabel = new JLabel("Go to http://theprogrammingturkey.com/TurkeyBot.php");
			accountNameLabel.setLocation(10, 40);
			accountNameLabel.setSize(400, 25);
			popup.add(accountNameLabel);

			accountNameLabel = new JLabel("To get the latest version!");
			accountNameLabel.setLocation(100, 75);
			accountNameLabel.setSize(400, 25);
			popup.add(accountNameLabel);
			popup.setVisible(true);
			popup.revalidate();
		}
	}
}