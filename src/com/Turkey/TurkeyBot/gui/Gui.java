package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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
	private AnnouncementTab announcementTab;

	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem item;

	private static TurkeyBot bot;

	/**
	 * Initiates the Gui side of the bot.
	 * @param b The instance of the bot.
	 */
	public Gui(TurkeyBot b)
	{
		bot = b;
		console = new ConsoleTab();
		viewers = new Viewers();
		listcommands = new CommandsTab();
		settingsTab = new SettingsTab();
		chatSettingsTab = new ChatSettingsTab();
		spamResponseTab = new SpamResponseTab();
		accountSettingsTab = new AccountSettingsTab();
		announcementTab = new AnnouncementTab();
		Dimension size = new Dimension(800, 600);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(size);
		super.setLayout(null);
		super.setTitle("TurkeyBot");
		super.setVisible(true);
		super.setLocationRelativeTo(null);

		ImageIcon icon = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo.jpg")));
		super.setIconImage(icon.getImage());

		// Menu Bar
		menuBar = new JMenuBar();

		// Console Menu Item
		menu = new JMenu("Chat");
		menuBar.add(menu);

		//Console Items
		item = new JMenuItem("Chat");
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

		//Announcements
		menu = new JMenu("Announcements");
		menuBar.add(menu);

		// List Announcements Menu Item
		item = new JMenuItem("List Announcements");
		item.addActionListener(this);
		menu.add(item);

		// Add Announcement Menu Item
		item = new JMenuItem("Add Announcement");
		item.addActionListener(this);
		menu.add(item);

		super.setJMenuBar(menuBar);
		currentTab = console;
		super.add(currentTab);
		currentTab.load();
		super.setVisible(true);

		super.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) 
			{
				if(!bot.settings.getSettingAsBoolean("SilentJoinLeave") && !bot.getChannel(false).equalsIgnoreCase(""))
					bot.sendMessage("GoodBye!!!");
				System.exit(0);
				Followers.run = false;
			}	
		});

		b.connectToTwitch();
		//ConsoleTab.output(Level.Alert, "Type /connect to connect to twitch!");
		this.revalidate();
	}


	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JMenuItem)
		{
			JMenuItem eItem = (JMenuItem) e.getSource();
			currentTab.unLoad();
			super.remove(currentTab);
			if(eItem.getText().equalsIgnoreCase("Chat"))
				currentTab = console;
			else if(eItem.getText().equalsIgnoreCase("Viewers"))
				currentTab = viewers;
			else if(eItem.getText().equalsIgnoreCase("List commands"))
				currentTab = listcommands;
			else if(eItem.getText().equalsIgnoreCase("Add command"))
				new AddCommandGui();
			else if(eItem.getText().equalsIgnoreCase("Settings"))
				currentTab = settingsTab;
			else if(eItem.getText().equalsIgnoreCase("Chat Settings"))
				currentTab = chatSettingsTab;
			else if(eItem.getText().equalsIgnoreCase("Response Settings"))
				currentTab = spamResponseTab;
			else if(eItem.getText().equalsIgnoreCase("Account Settings"))
				currentTab = accountSettingsTab;
			else if(eItem.getText().equalsIgnoreCase("List Announcements"))
				currentTab = announcementTab;
			else if(eItem.getText().equalsIgnoreCase("Add Announcement"))
				new AddAnnouncementGui();
			super.add(currentTab);
			currentTab.load();
			this.revalidate();
			this.repaint();
		}

	}

	/**
	 * Gets the instance of the bot
	 * @return
	 */
	public static TurkeyBot getBot()
	{
		return bot;
	}

	/**
	 * Reloads the current tab.
	 */
	public static void reloadTab()
	{
		currentTab.unLoad();
		currentTab.load();

	}
}