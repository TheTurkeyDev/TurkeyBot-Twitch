package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.Commands.Command;

public class EditCommandGui implements ActionListener
{
	private JFrame popup;
	private Command command;

	private JLabel commandLabel;
	private JTextField commandName;
	private JLabel responseLabel;
	private TextArea response;

	private JLabel permissionLabel;
	@SuppressWarnings("rawtypes")
	private JComboBox permSelect;

	private JButton save;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EditCommandGui(Command c)
	{
		command = c;
		popup = new JFrame();
		Dimension size = new Dimension(400, 300);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setSize(size);
		popup.setLayout(null);
		popup.setTitle("Editing Command: " + command.getName());
		popup.setVisible(true);

		commandLabel = new JLabel("Command");
		commandLabel.setLocation(10,5);
		commandLabel.setSize(75, 25);
		popup.add(commandLabel);

		commandName = new JTextField(command.getName());
		commandName.setLocation(85,5);
		commandName.setSize(250, 25);
		commandName.setEditable(false);
		popup.add(commandName);

		responseLabel = new JLabel("Output");
		responseLabel.setLocation(10,50);
		responseLabel.setSize(75, 25);
		popup.add(responseLabel);

		response = new TextArea(command.getReponse());
		response.setLocation(85,50);
		response.setSize(250, 40);
		if(!command.canEdit())
			response.setEditable(false);
		popup.add(response);

		permissionLabel = new JLabel("Permission level");
		permissionLabel.setLocation(10,110);
		permissionLabel.setSize(75, 25);
		popup.add(permissionLabel);
		
		permSelect = new JComboBox(TurkeyBot.getPermissions());
		permSelect.setLocation(125,110);
		permSelect.setSize(100, 25);
		String perm = command.getPermissionLevel();
		if(perm.equalsIgnoreCase("User"))
			permSelect.setSelectedIndex(0);
		else if(perm.equalsIgnoreCase("Mod"))
			permSelect.setSelectedIndex(1);
		else if(perm.equalsIgnoreCase("Streamer"))
			permSelect.setSelectedIndex(2);
		popup.add(permSelect);



		save = new JButton("Save");
		save.setName("Save");
		save.setLocation(150,225);
		save.setSize(100, 25);
		save.addActionListener(this);
		popup.add(save);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(save))
		{
			command.setResponse(response.getText());
			command.setPermissionLevel((String)permSelect.getSelectedItem());
			command.getFile().updateCommand();
			Gui.getBot().sendMessage("Command !" + command.getName() + " was successfully edited");
			popup.dispose();
		}
	}
}
