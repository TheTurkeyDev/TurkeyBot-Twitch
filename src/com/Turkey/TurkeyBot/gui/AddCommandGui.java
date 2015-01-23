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

public class AddCommandGui implements ActionListener
{
	private JFrame popup;

	private JLabel commandLabel;
	//private JLabel note;
	private JTextField commandName;
	private JLabel responseLabel;
	private TextArea response;

	private JLabel permissionLabel;
	private JComboBox<?> permSelect;

	private JButton addCommand;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AddCommandGui()
	{
		popup = new JFrame();
		Dimension size = new Dimension(400, 300);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setSize(size);
		popup.setLayout(null);
		popup.setTitle("New Command");
		popup.setVisible(true);
		popup.setResizable(false);

		commandLabel = new JLabel("Command    !");
		commandLabel.setLocation(10,5);
		commandLabel.setSize(75, 25);
		popup.add(commandLabel);

		commandName = new JTextField();
		commandName.setLocation(85,5);
		commandName.setSize(250, 25);
		popup.add(commandName);

		responseLabel = new JLabel("Output");
		responseLabel.setLocation(10,50);
		responseLabel.setSize(75, 25);
		popup.add(responseLabel);

		permissionLabel = new JLabel("Permission Level");
		permissionLabel.setLocation(10,110);
		permissionLabel.setSize(150, 25);
		popup.add(permissionLabel);

		permSelect = new JComboBox(TurkeyBot.getPermissions());
		permSelect.setLocation(125,110);
		permSelect.setSize(100, 25);
		permSelect.setSelectedIndex(0);
		popup.add(permSelect);

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
			if(commandName.getText().equalsIgnoreCase("") ||  response.getText().equalsIgnoreCase(""))
				error("You have blank feilds");
			else if(commandName.getText().contains("!"))
				error("No ! mark needed in the command feild!");
			else
			{
				Command command = new Command(commandName.getText(), response.getText());
				command.setPermissionLevel((String)permSelect.getSelectedItem());
				Gui.getBot().addCommand(command);
				if(Gui.getBot().settings.getSettingAsBoolean("outputchanges"))
				{
					Gui.getBot().sendMessage("Added Command " + "!" + commandName.getText());
				}
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
