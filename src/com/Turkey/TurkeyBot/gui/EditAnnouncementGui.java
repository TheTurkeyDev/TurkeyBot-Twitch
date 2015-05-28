package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.Turkey.TurkeyBot.TurkeyBot;

public class EditAnnouncementGui implements ActionListener
{
	private JFrame popup;
	private int annoucementNum;

	private JLabel responseLabel;
	private JTextField editResponse;

	private JButton save;

	public EditAnnouncementGui(int i)
	{
		annoucementNum = i;
		popup = new JFrame();
		Dimension size = new Dimension(400, 300);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setSize(size);
		popup.setPreferredSize(size);
		popup.setLayout(null);
		popup.setTitle("Edit Announcement");
		popup.setVisible(true);
		popup.setLocationRelativeTo(null);

		responseLabel = new JLabel("Response");
		responseLabel.setLocation(10, 90);
		responseLabel.setSize(75, 25);
		popup.add(responseLabel);

		editResponse = new JTextField(TurkeyBot.bot.announceFile.getAnnouncement(annoucementNum));
		editResponse.setLocation(100,90);
		editResponse.setSize(300,25);
		popup.add(editResponse);

		save = new JButton("Save");
		save.setName("Save");
		save.setLocation(150,230);
		save.setSize(100, 25);
		save.addActionListener(this);
		popup.add(save);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton button = (JButton) e.getSource();
			if(button.getName().equalsIgnoreCase(save.getName()))
			{
				TurkeyBot.bot.announceFile.removeAnnouncement(annoucementNum);
				if(editResponse.getText() != "")
					TurkeyBot.bot.announceFile.addAnnouncement(editResponse.getText());
				TurkeyBot.bot.announceFile.reloadAnnouncements();
				popup.dispose();
			}
		}
	}
}
