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
			text.setSize(60, 15);
			text.setVisible(true);
			text.setText(Gui.getBot().settings.getSetting(settingsName));
			super.add(text);
			components.add(text);

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
	}

}
