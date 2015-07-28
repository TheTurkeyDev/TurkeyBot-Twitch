package com.Turkey.TurkeyBot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.Turkey.TurkeyBot.TurkeyBot;

public class SettingsTab extends Tab implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private List<JComponent> components = new ArrayList<JComponent>();

	private JButton save;
	public SettingsTab()
	{
	}

	public void load()
	{
		if (TurkeyBot.bot.getProfile() == null) return;
		Object[] settings = TurkeyBot.bot.settings.getSettings().toArray();
		JLabel label;
		JTextArea text;
		JButton button;

		save = new JButton("Save");
		save.setName("Save");
		save.setLocation((super.getWidth()/2)- 50, super.getHeight() - 100);
		save.setSize(100, 25);
		save.addActionListener(this);
		super.add(save);	

		int x = 10;
		int row = 0;
		for(int i = 0; i < settings.length; i++)
		{
			String settingsName = (String) settings[i];

			if((row*25)+20 > super.getHeight())
			{
				x+=1000;
				row = 0;
			}

			label = new JLabel(settingsName);
			label.setLocation(x, (row*25) + 20);
			label.setSize(200, 25);
			label.setVisible(true);
			super.add(label);
			components.add(label);

			text = new JTextArea();
			text.setName(settingsName);
			text.setLocation(x + 200, (row*25) + 25);
			text.setSize(60, 15);
			text.setVisible(true);
			text.setText(TurkeyBot.bot.settings.getSetting(settingsName));
			super.add(text);
			components.add(text);

			if(settingsName.equalsIgnoreCase("autocurrencydelay") || settingsName.equalsIgnoreCase("trackfollowers") || settingsName.equalsIgnoreCase("announcedelay"))
			{
				boolean running = false;

				if(settingsName.equalsIgnoreCase("autocurrencydelay") && TurkeyBot.bot.currencyTrack != null)
					running = TurkeyBot.bot.currencyTrack.isRunning();
				if(settingsName.equalsIgnoreCase("trackfollowers") && TurkeyBot.bot.followersFile != null)
					running = TurkeyBot.bot.followersFile.isRunning();
				if(settingsName.equalsIgnoreCase("announcedelay") && TurkeyBot.bot.announcer != null)
					running = TurkeyBot.bot.announcer.isRunning();

				button = new JButton(running?"Stop":"Start");
				button.setLocation(x+275, (row*25) + 20);
				button.setName(settingsName);
				button.setSize(75, 25);
				button.setVisible(true);
				button.addActionListener(this);
				super.add(button);
				components.add(button);
			}
			
			row++;
		}
		super.setVisible(true);
	}

	public void unLoad()
	{
		if(TurkeyBot.bot.getChannel(false).equalsIgnoreCase(""))
			return;
		for(JComponent comp: components)
		{
			comp.setVisible(false);
			super.remove(comp);
		}
		save.setVisible(false);
		super.remove(save);
		components.clear();
		super.setVisible(false);
	}

	/**
	 * Saves settings of the bot from this tab
	 */
	public void saveSettings()
	{
		for(JComponent comp: components)
		{
			if(comp instanceof JTextArea)
			{
				TurkeyBot.bot.settings.setSetting(comp.getName(), ((JTextArea) comp).getText());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(save))
		{
			saveSettings();
		}
		else if(e.getSource() instanceof JButton)
		{
			JButton b = (JButton) e.getSource();

			if(b.getName().equalsIgnoreCase("autocurrencydelay") && TurkeyBot.bot.currencyTrack != null)
			{
				if(b.getText().equalsIgnoreCase("Stop"))
				{
					TurkeyBot.bot.currencyTrack.stopThread();
				}
				else if(b.getText().equalsIgnoreCase("Start"))
				{
					TurkeyBot.bot.currencyTrack.initCurrencyThread();
				}
				Gui.reloadTab();
			}
			if(b.getName().equalsIgnoreCase("trackfollowers") && TurkeyBot.bot.followersFile != null)
			{
				if(b.getText().equalsIgnoreCase("Stop"))
				{
					TurkeyBot.bot.followersFile.stopFollowerTracker();
				}
				else if(b.getText().equalsIgnoreCase("Start"))
				{
					TurkeyBot.bot.followersFile.initFollowerTracker();
				}
				Gui.reloadTab();
			}
			if(b.getName().equalsIgnoreCase("announcedelay") && TurkeyBot.bot.announcer != null)
			{
				if(b.getText().equalsIgnoreCase("Stop"))
				{
					TurkeyBot.bot.announcer.stop();
				}
				else if(b.getText().equalsIgnoreCase("Start"))
				{
					TurkeyBot.bot.announcer.initAutoAnnouncemer();
				}
				Gui.reloadTab();
			}
		}
	}

}
