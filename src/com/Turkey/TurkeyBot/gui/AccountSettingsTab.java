package com.Turkey.TurkeyBot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import com.Turkey.TurkeyBot.SecretStuff;

public class AccountSettingsTab extends Tab implements ActionListener
{
	private static final long serialVersionUID = 1L;

	List<JComponent> components = new ArrayList<JComponent>();

	JButton save;

	JLabel namelabel;
	JLabel oAuthlabel;
	JTextArea nametext;
	JPasswordField oAuthtext;

	JLabel info;

	public AccountSettingsTab()
	{
		save = new JButton("Save");
		save.setName("Save");
		save.setLocation((super.getWidth()/2)- 50, super.getHeight() - 100);
		save.setSize(100, 25);
		save.addActionListener(this);
		super.add(save);

		namelabel = new JLabel("AccountName");
		namelabel.setLocation(0, 20);
		namelabel.setSize(200, 25);
		namelabel.setVisible(true);
		super.add(namelabel);
		components.add(namelabel);

		nametext = new JTextArea();
		nametext.setName("AccountName");
		nametext.setLocation(100, 25);
		nametext.setSize(200, 15);
		nametext.setVisible(true);
		nametext.setText(Gui.getBot().accountSettingsFile.getSetting("AccountName"));
		super.add(nametext);
		components.add(nametext);

		oAuthlabel = new JLabel("AccountOAuth");
		oAuthlabel.setLocation(0, 40);
		oAuthlabel.setSize(200, 25);
		oAuthlabel.setVisible(true);
		super.add(oAuthlabel);
		components.add(oAuthlabel);

		oAuthtext = new JPasswordField();
		oAuthtext.setName("AccountOAuth");
		oAuthtext.setLocation(100, 45);
		oAuthtext.setSize(200, 15);
		oAuthtext.setVisible(true);
		oAuthtext.setText(Gui.getBot().accountSettingsFile.getSetting("AccountOAuth"));
		super.add(oAuthtext);
		components.add(oAuthtext);
	}

	public void load()
	{
		super.setVisible(true);
	}

	public void unLoad()
	{
		super.setVisible(false);
	}

	/**
	 * Saves settings of the bot from this tab
	 */
	public void saveSettings()
	{
		Gui.getBot().accountSettingsFile.setSetting("AccountName", nametext.getText());
		String pass = "";
		char[] chars = oAuthtext.getPassword();
		for(char c: chars)
			pass+=c;
		Gui.getBot().accountSettingsFile.setSetting("AccountOAuth", pass);
		SecretStuff.oAuth = pass;
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