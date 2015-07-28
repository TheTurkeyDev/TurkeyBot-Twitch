package com.Turkey.TurkeyBot.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.files.AnnouncementFile;

public class AnnouncementTab extends Tab implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private List<JComponent> components = new ArrayList<JComponent>();
	private JPanel commandspanel = new JPanel();
	private JScrollPane scroller;
	private GridBagConstraints gbc = new GridBagConstraints();

	public AnnouncementTab()
	{
		commandspanel.setSize(800, 550);
		commandspanel.setLocation(0, 0);
		commandspanel.setLayout(new GridBagLayout());
	}

	public void load()
	{
		if (TurkeyBot.bot.getProfile() == null) return;
		AnnouncementFile file = TurkeyBot.bot.announceFile;
		List<String> temp = file.getAnnouncements();

		JLabel label;
		JButton editbutton;
		JButton enablebutton;
		JButton disablebutton;
		JButton deletebutton;
		int x = 1;
		int row = 0;
		for (int i = 0; i < temp.size(); i++)
		{
			String announcement = temp.get(i);

			if ((row * 25) + 20 > (commandspanel.getHeight() - 100))
			{
				x += 1;
				row = 0;
			}

			label = new JLabel(announcement);
			gbc.gridx = x * 4;
			gbc.gridy = row;
			label.setSize(200, 25);
			label.setVisible(true);
			commandspanel.add(label, gbc);
			components.add(label);

			editbutton = new JButton();
			editbutton.setName("Edit " + i);
			gbc.gridx = (x * 4) + 1;
			gbc.gridy = row;
			editbutton.setSize(60, 25);
			editbutton.setVisible(true);
			editbutton.setText("Edit");
			editbutton.addActionListener(this);
			commandspanel.add(editbutton, gbc);
			components.add(editbutton);

			deletebutton = new JButton();
			deletebutton.setName("Delete " + i);
			gbc.gridx = (x * 4) + 3;
			gbc.gridy = row;
			deletebutton.setSize(70, 25);
			deletebutton.setVisible(true);
			deletebutton.setText("Delete");
			deletebutton.addActionListener(this);
			commandspanel.add(deletebutton, gbc);
			components.add(deletebutton);

			if (!file.isEnabled(announcement))
			{
				enablebutton = new JButton();
				enablebutton.setName("Enable " + i);
				gbc.gridx = (x * 4) + 2;
				gbc.gridy = row;
				enablebutton.setSize(80, 25);
				enablebutton.setVisible(true);
				enablebutton.setText("Enable");
				enablebutton.addActionListener(this);
				commandspanel.add(enablebutton, gbc);
				components.add(enablebutton);
			}
			else
			{
				disablebutton = new JButton();
				disablebutton.setName("Disable " + i);
				gbc.gridx = (x * 4) + 2;
				gbc.gridy = row;
				disablebutton.setSize(80, 25);
				disablebutton.setVisible(true);
				disablebutton.setText("Disable");
				disablebutton.addActionListener(this);
				commandspanel.add(disablebutton, gbc);
				components.add(disablebutton);
			}

			row++;
		}
		scroller = new JScrollPane(commandspanel);
		scroller.setLocation(0, 0);
		scroller.setSize(800, 540);
		scroller.setVisible(true);
		super.add(scroller);
		super.setVisible(true);
	}

	public void unLoad()
	{
		if (TurkeyBot.bot.getChannel(false).equalsIgnoreCase("")) return;
		for (JComponent comp : components)
		{
			comp.setVisible(false);
			scroller.remove(comp);
		}
		super.remove(scroller);
		super.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof JButton)
		{
			JButton button = ((JButton) e.getSource());
			AnnouncementFile file = TurkeyBot.bot.announceFile;
			if (button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Edit"))
			{
				new EditAnnouncementGui(Integer.parseInt(button.getName().substring(5)));
			}
			else if (button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Enable"))
			{
				file.enableAnnouncement(Integer.parseInt(button.getName().substring(7)));
				unLoad();
				load();
			}
			else if (button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Disable"))
			{
				file.disableAnnouncement(Integer.parseInt(button.getName().substring(8)));
				unLoad();
				load();
			}
			else if (button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Delete"))
			{
				file.removeAnnouncement(Integer.parseInt(button.getName().substring(7)));
				unLoad();
				load();
			}
		}
	}

}
