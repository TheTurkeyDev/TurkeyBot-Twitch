package com.Turkey.TurkeyBot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import com.Turkey.TurkeyBot.TurkeyBot;

public class AccountSettingsTab extends Tab implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JButton add;
	private JButton edit;

	private JLabel displaynamelabel;
	private JLabel namelabel;
	private JLabel oAuthlabel;
	private JTextArea nametext;
	private JTextArea displaynametext;
	private JPasswordField oAuthtext;
	private JLabel selectedAccount;
	
	private static JComboBox<?> accounts;

	public AccountSettingsTab()
	{
		add = new JButton("Add");
		add.setName("Add");
		add.setLocation((super.getWidth()/2)- 50, 100);
		add.setSize(100, 25);
		add.addActionListener(this);
		super.add(add);
		
		edit = new JButton("Edit");
		edit.setName("Edit");
		edit.setLocation((super.getWidth()/2) + 75, 150);
		edit.setSize(100, 25);
		edit.addActionListener(this);
		super.add(edit);

		displaynamelabel = new JLabel("Display Name (what you call the bot)");
		displaynamelabel.setLocation((super.getWidth()/2) - 275, 20);
		displaynamelabel.setSize(300, 25);
		displaynamelabel.setVisible(true);
		super.add(displaynamelabel);

		displaynametext = new JTextArea("Display Name");
		displaynametext.setName("DisplayName");
		displaynametext.setLocation((super.getWidth()/2) - 50, 25);
		displaynametext.setSize(200, 15);
		displaynametext.setVisible(true);
		displaynametext.setToolTipText("Display Name");
		super.add(displaynametext);
		
		namelabel = new JLabel("AccountName");
		namelabel.setLocation((super.getWidth()/2) - 150, 40);
		namelabel.setSize(200, 25);
		namelabel.setVisible(true);
		super.add(namelabel);

		nametext = new JTextArea("Account Username");
		nametext.setName("AccountName");
		nametext.setLocation((super.getWidth()/2) - 50, 45);
		nametext.setSize(200, 15);
		nametext.setVisible(true);
		nametext.setToolTipText("Account Username");
		super.add(nametext);

		oAuthlabel = new JLabel("AccountOAuth");
		oAuthlabel.setLocation((super.getWidth()/2) - 150, 60);
		oAuthlabel.setSize(100, 25);
		oAuthlabel.setVisible(true);
		super.add(oAuthlabel);

		oAuthtext = new JPasswordField();
		oAuthtext.setName("AccountOAuth");
		oAuthtext.setLocation((super.getWidth()/2) - 50, 65);
		oAuthtext.setSize(200, 15);
		oAuthtext.setVisible(true);
		oAuthtext.setToolTipText("Account oAuth");
		super.add(oAuthtext);
		
		accounts = new JComboBox<>(TurkeyBot.bot.accountSettingsFile.getAccounts().keySet().toArray());
		accounts.setLocation((super.getWidth()/2) - 60 , 150);
		accounts.setSize(120, 25);
		super.add(accounts);
		
		selectedAccount = new JLabel("Selected Account:");
		selectedAccount.setLocation((super.getWidth()/2) - 175, 150);
		selectedAccount.setSize(200, 25);
		selectedAccount.setVisible(true);
		super.add(selectedAccount);
	}

	public void load()
	{
		super.setVisible(true);
	}

	public void unLoad()
	{
		super.setVisible(false);
	}

	public void addAccount()
	{
		if(!this.nametext.getText().equalsIgnoreCase("") && !this.displaynametext.getText().equalsIgnoreCase("") && this.oAuthtext.getPassword().length != 0)
		{
			String oAuth = "";
			for(char c: oAuthtext.getPassword())
				oAuth+=c;
			TurkeyBot.bot.accountSettingsFile.addAccount(displaynametext.getText(), nametext.getText(), oAuth);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(add))
		{
			addAccount();
		}
	}
	
	public static String getCurrentAccount()
	{
		return TurkeyBot.bot.accountSettingsFile.getAccountFromDisplayName((String) accounts.getSelectedItem()).getKey();
	}

}