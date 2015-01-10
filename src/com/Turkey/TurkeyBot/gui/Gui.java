package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.files.Followers;

public class Gui extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private static Tab currentTab;

	//Tabs classes
	private static ConsoleTab console;
	private Viewers viewers;
	private CommandsTab listcommands;
	private SettingsTab settingsTab;
	private ChatSettingsTab chatSettingsTab;
	private SpamResponseTab spamResponseTab;
	private AccountSettingsTab accountSettingsTab;

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem item;

	private static TurkeyBot bot;

	// constructor
	public Gui(TurkeyBot b)
	{
		bot = b;
		console = new ConsoleTab(this);
		viewers = new Viewers(this);
		listcommands = new CommandsTab(this);
		settingsTab = new SettingsTab(this);
		chatSettingsTab = new ChatSettingsTab(this);
		spamResponseTab = new SpamResponseTab(this);
		accountSettingsTab = new AccountSettingsTab(this);
		Dimension size = new Dimension(800, 600);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(size);
		super.setLayout(null);
		super.setTitle("TurkeyBot");
		super.setVisible(true);

		currentTab = console;
		currentTab.load();

		// Menu Bar
		menuBar = new JMenuBar();

		// Console Menu Item
		menu = new JMenu("Console");
		menuBar.add(menu);

		//Console Items
		item = new JMenuItem("Console");
		item.addActionListener(this);
		menu.add(item);

		// Viewers
		menu = new JMenu("Viewers");
		menuBar.add(menu);

		// Viewers Menu Item
		item = new JMenuItem("Viewers");
		item.addActionListener(this);
		menu.add(item);

		//Settings
		menu = new JMenu("Settings");
		menuBar.add(menu);

		//Settings Menu Item
		item = new JMenuItem("Settings");
		item.addActionListener(this);
		menu.add(item);

		//Chat Settings Menu Item
		item = new JMenuItem("Chat Settings");
		item.addActionListener(this);
		menu.add(item);

		//Bot Response Settings Menu Item
		item = new JMenuItem("Response Settings");
		item.addActionListener(this);
		menu.add(item);

		//Bot Account Settings Menu Item
		item = new JMenuItem("Account Settings");
		item.addActionListener(this);
		menu.add(item);

		//Commands
		menu = new JMenu("Commands");
		menuBar.add(menu);

		// List Commands Menu Item
		item = new JMenuItem("List Commands");
		item.addActionListener(this);
		menu.add(item);

		// Add Command Menu Item
		item = new JMenuItem("Add Command");
		item.addActionListener(this);
		menu.add(item);



		super.setJMenuBar(menuBar);
		super.setVisible(true);

		super.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) 
			{
				if(!bot.settings.getSettingAsBoolean("SilentJoinLeave") && !bot.getChannel().equalsIgnoreCase(""))
					bot.sendMessage("GoodBye!!!");
				System.exit(0);
				Followers.run = false;
			}	
		});

		b.connectToTwitch();
		//ConsoleTab.output(Level.Alert, "Type /connect to connect to twitch!");
	}


	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JMenuItem)
		{
			JMenuItem eItem = (JMenuItem) e.getSource();
			if(eItem.getText().equalsIgnoreCase("Console"))
			{
				currentTab.unLoad();
				currentTab = console;
				currentTab.load();
			}
			else if(eItem.getText().equalsIgnoreCase("Viewers"))
			{
				currentTab.unLoad();
				currentTab = viewers;
				currentTab.load();
			}
			else if(eItem.getText().equalsIgnoreCase("List commands"))
			{
				currentTab.unLoad();
				currentTab = listcommands;
				currentTab.load();
			}
			else if(eItem.getText().equalsIgnoreCase("Add command"))
			{
				new AddCommandGui();
			}
			else if(eItem.getText().equalsIgnoreCase("Settings"))
			{
				currentTab.unLoad();
				currentTab = settingsTab;
				currentTab.load();
			}
			else if(eItem.getText().equalsIgnoreCase("Chat Settings"))
			{
				currentTab.unLoad();
				currentTab = chatSettingsTab;
				currentTab.load();
			}
			else if(eItem.getText().equalsIgnoreCase("Response Settings"))
			{
				currentTab.unLoad();
				currentTab = spamResponseTab;
				currentTab.load();
			}
			else if(eItem.getText().equalsIgnoreCase("Account Settings"))
			{
				currentTab.unLoad();
				currentTab = accountSettingsTab;
				currentTab.load();
			}
		}

	}

	public static TurkeyBot getBot()
	{
		return bot;
	}

	public static void reloadTab()
	{
		currentTab.unLoad();
		currentTab.load();
	}
}