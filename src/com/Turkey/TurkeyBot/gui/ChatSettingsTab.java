package com.Turkey.TurkeyBot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatSettingsTab extends Tab implements ActionListener
{
	private List<JComponent> components = new ArrayList<JComponent>();

	private JButton save;
	private String[] groups = {"Caps", "Emotes", "Symbols", "other"};
	private JPanel panel;
	public ChatSettingsTab(JFrame jframe)
	{
		super(jframe);
	}

	public void load()
	{
		JLabel label;
		JTextArea text;

		save = new JButton("Save");
		save.setName("Save");
		save.setLocation((frame.getWidth()/2)- 50, frame.getHeight() - 100);
		save.setSize(100, 25);
		save.addActionListener(this);
		frame.add(save);
		
		for(int i = 0; i < groups.length; i++)
		{
			String s = groups[i];
			
			panel = new JPanel();
			panel.setLayout(null);
			panel.setLocation(10, (i*100) + 10);
			panel.setSize(400, 100);
			
			if(s.equalsIgnoreCase("other"))
			{
				label = new JLabel("Block Links");
				label.setLocation(0, 25);
				label.setSize(150, 25);
				panel.add(label);
				
				text = new JTextArea();
				text.setName("Block Links");
				text.setLocation(150, 25);
				text.setSize(60, 15);
				text.setText(Gui.getBot().chatSettings.getSetting("BlockLinks"));
				panel.add(text);
				
				label = new JLabel("Max Message Length");
				label.setLocation(0, 50);
				label.setSize(150, 25);
				panel.add(label);
				
				text = new JTextArea();
				text.setName("Max Message Length");
				text.setLocation(150, 50);
				text.setSize(60, 15);
				text.setText(Gui.getBot().chatSettings.getSetting("MaxMessageLength"));
				panel.add(text);
				
				label = new JLabel("Word Black List");
				label.setLocation(0, 75);
				label.setSize(150, 25);
				panel.add(label);
				
				text = new JTextArea();
				text.setName("Word Black List");
				text.setLocation(150, 75);
				text.setSize(60, 15);
				text.setText(Gui.getBot().chatSettings.getSetting("WordBlackList"));
				panel.add(text);
			}
			else
			{
				label = new JLabel("Minimum " + s);
				label.setLocation(0, 25);
				label.setSize(150, 25);
				panel.add(label);
				
				text = new JTextArea();
				text.setName("Minimum " + s);
				text.setLocation(150, 25);
				text.setSize(60, 15);
				text.setText(Gui.getBot().chatSettings.getSetting(s+"TypedMinmumforAffect"));
				panel.add(text);
				
				label = new JLabel("Max " + s);
				label.setLocation(0, 50);
				label.setSize(150, 25);
				panel.add(label);

				text = new JTextArea();
				text.setName("Max " + s);
				text.setLocation(150, 50);
				text.setSize(60, 15);
				text.setText(Gui.getBot().chatSettings.getSetting(s+"TypedMaximum"));
				panel.add(text);
				
				label = new JLabel("Max percent of " + s);
				label.setLocation(0, 75);
				label.setSize(150, 25);
				panel.add(label);

				text = new JTextArea();
				text.setName("Max percent of " + s);
				text.setLocation(150, 75);
				text.setSize(60, 15);
				text.setText(Gui.getBot().chatSettings.getSetting("MaxPercentOf" + s + "Used"));
				panel.add(text);
			}
			frame.add(panel);
			components.add(panel);
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

	public void saveSettings()
	{
		for(JComponent comp: components)
		{
			if(comp instanceof JTextArea)
			{
				Gui.getBot().chatSettings.setSetting(comp.getName(), ((JTextArea) comp).getText());
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