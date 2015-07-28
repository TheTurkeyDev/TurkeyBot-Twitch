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

public class SpamResponseTab extends Tab implements ActionListener
{
	private static final long serialVersionUID = 1L;

	List<JComponent> components = new ArrayList<JComponent>();

	JButton save;
	public SpamResponseTab()
	{
	}

	public void load()
	{
		if (TurkeyBot.bot.getProfile() == null) return;
		Object[] settings = TurkeyBot.bot.spamResponseFile.getSettings().toArray();
		JLabel label;
		JTextArea text;

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
			text.setLocation(x + 150, (row*25) + 25);
			text.setSize(600, 15);
			text.setVisible(true);
			text.setText(TurkeyBot.bot.spamResponseFile.getSetting(settingsName));
			super.add(text);
			components.add(text);

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
				TurkeyBot.bot.spamResponseFile.setSetting(comp.getName(), ((JTextArea) comp).getText());
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
	}
}
