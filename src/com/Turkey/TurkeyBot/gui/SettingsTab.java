package com.Turkey.TurkeyBot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class SettingsTab extends Tab implements ActionListener
{
	List<JComponent> components = new ArrayList<JComponent>();

	JButton save;
	public SettingsTab(JFrame jframe)
	{
		super(jframe);
	}

	public void load()
	{
		Object[] settings = Gui.getBot().settings.getSettings().toArray();
		JLabel label;
		JTextArea text;

		save = new JButton("Save");
		save.setName("Save");
		save.setLocation((frame.getWidth()/2)- 50, frame.getHeight() - 100);
		save.setSize(100, 25);
		save.addActionListener(this);
		frame.add(save);	

		int x = 10;
		int row = 0;
		for(int i = 0; i < settings.length; i++)
		{
			String settingsName = (String) settings[i];

			if((row*25)+20 > frame.getHeight())
			{
				x+=1000;
				row = 0;
			}

			label = new JLabel(settingsName);
			label.setLocation(x, (row*25) + 20);
			label.setSize(200, 25);
			label.setVisible(true);
			frame.add(label);
			components.add(label);

			text = new JTextArea();
			text.setName(settingsName);
			text.setLocation(x + 100, (row*25) + 25);
			text.setSize(60, 15);
			text.setVisible(true);
			text.setText(Gui.getBot().settings.getSetting(settingsName));
			frame.add(text);
			components.add(text);

			row++;
		}
		frame.repaint();
	}

	public void unLoad()
	{
		for(JComponent comp: components)
		{
			comp.setVisible(false);
			frame.remove(comp);
		}
		save.setVisible(false);
		frame.remove(save);
		components.clear();
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
