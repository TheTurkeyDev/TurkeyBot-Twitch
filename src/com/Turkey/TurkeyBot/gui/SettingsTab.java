package com.Turkey.TurkeyBot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

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
		Object[] settings = Gui.getBot().settings.getSettings().toArray();
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
			text.setText(Gui.getBot().settings.getSetting(settingsName));
			super.add(text);
			components.add(text);

			if(settingsName.equalsIgnoreCase("autocurrencydelay") || settingsName.equalsIgnoreCase("trackfollowers") || settingsName.equalsIgnoreCase("announcedelay"))
			{
				boolean running = false;

				if(settingsName.equalsIgnoreCase("autocurrencydelay") && Gui.getBot().currencyTrack != null)
					running = Gui.getBot().currencyTrack.isRunning();
				if(settingsName.equalsIgnoreCase("trackfollowers") && Gui.getBot().followersFile != null)
					running = Gui.getBot().followersFile.isRunning();
				if(settingsName.equalsIgnoreCase("announcedelay") && Gui.getBot().announcer != null)
					running = Gui.getBot().announcer.isRunning();

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
				Gui.getBot().settings.setSetting(comp.getName(), ((JTextArea) comp).getText());
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

			if(b.getName().equalsIgnoreCase("autocurrencydelay") && Gui.getBot().currencyTrack != null)
			{
				if(b.getText().equalsIgnoreCase("Stop"))
				{
					Gui.getBot().currencyTrack.stopThread();
				}
				else if(b.getText().equalsIgnoreCase("Start"))
				{
					Gui.getBot().currencyTrack.initCurrencyThread();
				}
			}
			if(b.getName().equalsIgnoreCase("trackfollowers") && Gui.getBot().followersFile != null)
			{
				if(b.getText().equalsIgnoreCase("Stop"))
				{
					Gui.getBot().followersFile.stopFollowerTracker();
				}
				else if(b.getText().equalsIgnoreCase("Start"))
				{
					Gui.getBot().followersFile.initFollowerTracker();
				}
			}
			if(b.getName().equalsIgnoreCase("announcedelay") && Gui.getBot().announcer != null)
			{
				if(b.getText().equalsIgnoreCase("Stop"))
				{
					Gui.getBot().announcer.stop();
				}
				else if(b.getText().equalsIgnoreCase("Start"))
				{
					Gui.getBot().announcer.initAutoAnnouncemer();
				}
			}
		}
	}

}
