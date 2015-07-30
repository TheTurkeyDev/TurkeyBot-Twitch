package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.botProfile.ProfileManager;

public class ProfilesGui implements ActionListener
{
	private JFrame frame;

	private JTextArea nametext;

	private JButton load;
	private JButton add;
	private JButton edit;

	private JLabel selectedAccount;
	private static JComboBox<?> profiles;

	public ProfilesGui()
	{
		ProfileManager.instance.loadProfiles();

		frame = new JFrame();
		Dimension size = new Dimension(600, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setLayout(null);
		frame.setTitle("Profile Selection");
		frame.setLocationRelativeTo(null);
		
		load = new JButton("Load Profile");
		load.setName("Load");
		load.setLocation((frame.getWidth() / 2) - 25, 150);
		load.setSize(135, 25);
		load.addActionListener(this);
		frame.add(load);

		add = new JButton("Add Profile");
		add.setName("Add");
		add.setLocation((frame.getWidth() / 2) - 10, 100);
		add.setSize(100, 25);
		add.addActionListener(this);
		frame.add(add);
		
		nametext = new JTextArea("Profile Name");
		nametext.setName("ProfileName");
		nametext.setLocation((frame.getWidth() / 2) - 160, 100);
		nametext.setSize(120, 15);
		nametext.setVisible(true);
		nametext.setToolTipText("Profile Name");
		frame.add(nametext);

		edit = new JButton("Edit");
		edit.setName("Edit");
		edit.setLocation((frame.getWidth() / 2) + 125, 150);
		edit.setSize(100, 25);
		edit.addActionListener(this);
		frame.add(edit);

		selectedAccount = new JLabel("Selected Profile:");
		selectedAccount.setLocation((frame.getWidth() / 2) - 275, 150);
		selectedAccount.setSize(200, 25);
		selectedAccount.setVisible(true);
		frame.add(selectedAccount);

		loadProfiles();
		
		frame.setVisible(true);
	}
	
	public void loadProfiles()
	{
		profiles = new JComboBox<>(ProfileManager.instance.getProfileNames());
		profiles.setLocation((frame.getWidth() / 2) - 160, 150);
		profiles.setSize(120, 25);
		frame.add(profiles);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton button = (JButton) e.getSource();
			if(button.getName().equalsIgnoreCase("Load"))
			{
				try
				{
					new Gui(new TurkeyBot(ProfileManager.instance.getProfileFromName((String) profiles.getSelectedItem())));
				} catch(Exception e1)
				{
					e1.printStackTrace();
				}
				frame.dispose();
			}
			else if(button.getName().equalsIgnoreCase("Add"))
			{
				if(!nametext.getText().equalsIgnoreCase("Profile Name") && !nametext.getText().replaceAll(" ", "").equalsIgnoreCase(""))
				{
					ProfileManager.instance.addProfile(nametext.getText());
					frame.remove(profiles);
					this.loadProfiles();
				}
			}
		}
	}
}