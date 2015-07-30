package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.Turkey.TurkeyBot.TurkeyBot;

public class AddAnnouncementGui implements ActionListener
{
	private JFrame popup;

	private JLabel responseLabel;
	private TextArea response;

	private JButton addCommand;

	public AddAnnouncementGui()
	{
		popup = new JFrame();
		Dimension size = new Dimension(400, 300);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setSize(size);
		popup.setLayout(null);
		popup.setTitle("New Announcement");
		popup.setVisible(true);
		popup.setResizable(false);
		popup.setLocationRelativeTo(null);

		responseLabel = new JLabel("Output");
		responseLabel.setLocation(10,50);
		responseLabel.setSize(75, 25);
		popup.add(responseLabel);

		response = new TextArea();
		response.setLocation(85,50);
		response.setSize(250, 40);
		popup.add(response);

		addCommand = new JButton("Add");
		addCommand.setName("Add");
		addCommand.setLocation(150,225);
		addCommand.setSize(100, 25);
		addCommand.addActionListener(this);
		popup.add(addCommand);		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == addCommand)
		{
			if(response.getText().equalsIgnoreCase(""))
				error("You have blank feilds");
			else
			{
				TurkeyBot.bot.getProfile().announceFile.addAnnouncement(response.getText());
				TurkeyBot.bot.getProfile().announceFile.reloadAnnouncements();
				Gui.reloadTab();
				popup.dispose();
			}
		}
	}

	/**
	 * Called if an error occurs while changing the settings.
	 * @param error
	 */
	public void error(String error)
	{
		JFrame errorpopup = new JFrame();
		Dimension size = new Dimension(300, 75);
		errorpopup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		errorpopup.setSize(size);
		errorpopup.setLayout(null);
		errorpopup.setTitle("Error");
		errorpopup.setVisible(true);
		errorpopup.setResizable(false);

		JLabel commandLabel = new JLabel(error);
		commandLabel.setLocation(10,15);
		commandLabel.setSize(300, 25);
		errorpopup.add(commandLabel);
	}
}
