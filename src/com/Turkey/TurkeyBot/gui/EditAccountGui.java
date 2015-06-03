package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Turkey.TurkeyBot.TurkeyBot;

public class EditAccountGui implements ActionListener
{
	private JFrame popup;
	private String accountName;
	
	private Entry<String, String> account;

	private JLabel accountNameLabel;
	private JTextField accountNameField;
	
	private JLabel usernameLabel;
	private JTextField usernameField;
	
	private JLabel oAuthLabel;
	private JPasswordField oAuthField;

	private JButton save;

	public EditAccountGui(String name)
	{
		accountName = name;
		account = TurkeyBot.bot.accountSettingsFile.getAccountFromDisplayName(accountName);
		popup = new JFrame();
		Dimension size = new Dimension(400, 300);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setSize(size);
		popup.setPreferredSize(size);
		popup.setLayout(null);
		popup.setTitle("Editing Account: " + accountName);
		popup.setVisible(true);
		popup.setLocationRelativeTo(null);

		accountNameLabel = new JLabel("Display Name");
		accountNameLabel.setLocation(10,5);
		accountNameLabel.setSize(75, 25);
		popup.add(accountNameLabel);

		accountNameField = new JTextField(accountName);
		accountNameField.setLocation(125,5);
		accountNameField.setSize(150, 25);
		accountNameField.setEditable(false);
		popup.add(accountNameField);
		
		usernameLabel = new JLabel("Username");
		usernameLabel.setLocation(10,45);
		usernameLabel.setSize(75, 25);
		popup.add(usernameLabel);

		usernameField = new JTextField(account.getKey());
		usernameField.setLocation(125,45);
		usernameField.setSize(150, 25);
		popup.add(usernameField);
		
		oAuthLabel = new JLabel("oAuth:");
		oAuthLabel.setLocation(10,85);
		oAuthLabel.setSize(75, 25);
		popup.add(oAuthLabel);

		oAuthField = new JPasswordField(account.getValue());
		oAuthField.setLocation(125,85);
		oAuthField.setSize(250, 25);
		popup.add(oAuthField);


		save = new JButton("Save");
		save.setName("Save");
		save.setLocation(150,230);
		save.setSize(100, 25);
		save.addActionListener(this);
		popup.add(save);
	}

	public void refresh()
	{
		popup.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(save))
		{
			if(!this.usernameField.getText().equalsIgnoreCase("") && this.oAuthField.getPassword().length != 0)
			{
				String oAuth = "";
				for(char c: oAuthField.getPassword())
					oAuth+=c;
				TurkeyBot.bot.accountSettingsFile.addAccount(this.accountName, this.usernameField.getText(), oAuth);
			}
			popup.dispose();
			Gui.reloadTab();
		}
	}
}
