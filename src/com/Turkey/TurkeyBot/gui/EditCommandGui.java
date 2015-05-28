package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.commands.Command;

public class EditCommandGui implements ActionListener
{
	private JFrame popup;
	private Command command;

	private JLabel commandLabel;
	private JTextField commandName;

	private JPanel responses;
	private JScrollPane responsesScroller;
	private JLabel responseLabel;
	private JLabel response;
	private JTextField editResponse;
	private JButton responseEdit;
	private JButton responseAdd;

	private int editing = -1;

	private JLabel permissionLabel;

	private JComboBox<String> permSelect;

	private JButton save;

	public EditCommandGui(Command c)
	{
		command = c;
		popup = new JFrame();
		Dimension size = new Dimension(400, 300);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.setSize(size);
		popup.setPreferredSize(size);
		popup.setLayout(null);
		popup.setTitle("Editing Command: " + command.getName());
		popup.setVisible(true);
		popup.setLocationRelativeTo(null);

		commandLabel = new JLabel("Command");
		commandLabel.setLocation(10,5);
		commandLabel.setSize(75, 25);
		popup.add(commandLabel);

		commandName = new JTextField(command.getName());
		commandName.setLocation(85,5);
		commandName.setSize(250, 25);
		commandName.setEditable(false);
		popup.add(commandName);

		permissionLabel = new JLabel("Permission level");
		permissionLabel.setLocation(10,50);
		permissionLabel.setSize(75, 25);
		popup.add(permissionLabel);

		permSelect = new JComboBox<String>(TurkeyBot.getPermissions());
		permSelect.setLocation(125,50);
		permSelect.setSize(100, 25);
		String perm = command.getPermissionLevel();
		if(perm.equalsIgnoreCase("User"))
			permSelect.setSelectedIndex(0);
		else if(perm.equalsIgnoreCase("Mod"))
			permSelect.setSelectedIndex(1);
		else if(perm.equalsIgnoreCase("Streamer"))
			permSelect.setSelectedIndex(2);
		popup.add(permSelect);

		responseLabel = new JLabel("Responses:");
		responseLabel.setLocation(10, 90);
		responseLabel.setSize(75, 25);
		popup.add(responseLabel);

		if(command.canEdit())
		{
			responseAdd = new JButton("Add");
			responseAdd.setName("Add");
			responseAdd.setLocation(125,90);
			responseAdd.setSize(100, 25);
			responseAdd.addActionListener(this);
			popup.add(responseAdd);
		}

		ArrayList<String> list = command.getResponses();

		responses = new JPanel();
		responses.setSize(400, 100);
		responses.setLocation(0, 0);
		responses.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for(int i = 0; i < list.size(); i++)
		{
			if(i != editing)
			{
				response = new JLabel(list.get(i));
				response.setLocation(i,0);
				response.setSize(4,1);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 0.8;
				gbc.gridx = 0;
				gbc.gridy = i;
				responses.add(response,gbc);

				if(command.canEdit())
				{
					responseEdit = new JButton("Edit");
					responseEdit.setName("Edit " + i);
					responseEdit.setLocation(i,5);
					responseEdit.setSize(1, 1);
					responseEdit.addActionListener(this);
					gbc.fill = GridBagConstraints.HORIZONTAL;
					gbc.weightx = 0.2;
					gbc.gridx = 4;
					gbc.gridy = i;
					responses.add(responseEdit,gbc);
				}
			}
			else
			{
				editResponse = new JTextField(list.get(i));
				editResponse.setLocation(i,0);
				editResponse.setSize(4,1);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 0.8;
				gbc.gridx = 0;
				gbc.gridy = i;
				responses.add(editResponse,gbc);

				responseEdit = new JButton("Done");
				responseEdit.setName("Done " + i);
				responseEdit.setLocation(i,5);
				responseEdit.setSize(1, 1);
				responseEdit.addActionListener(this);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 0.2;
				gbc.gridx = 4;
				gbc.gridy = i;
				responses.add(responseEdit,gbc);
			}
		}

		responsesScroller = new JScrollPane(responses);
		responsesScroller.setLocation(10, 125);
		responsesScroller.setSize(350, 100);
		responsesScroller.setVisible(true);
		popup.add(responsesScroller);

		save = new JButton("Save");
		save.setName("Save");
		save.setLocation(150,230);
		save.setSize(100, 25);
		save.addActionListener(this);
		popup.add(save);
	}

	public void refresh()
	{
		popup.remove(responsesScroller);

		ArrayList<String> list = command.getResponses();

		responses = new JPanel();
		responses.setSize(400, 100);
		responses.setLocation(0, 0);
		responses.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for(int i = 0; i < list.size(); i++)
		{
			if(i != editing)
			{
				response = new JLabel(list.get(i));
				response.setLocation(i,0);
				response.setSize(4,1);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 0.8;
				gbc.gridx = 0;
				gbc.gridy = i;
				responses.add(response,gbc);

				if(command.canEdit())
				{
					responseEdit = new JButton("Edit");
					responseEdit.setName("Edit " + i);
					responseEdit.setLocation(i,5);
					responseEdit.setSize(1, 1);
					responseEdit.addActionListener(this);
					gbc.fill = GridBagConstraints.HORIZONTAL;
					gbc.weightx = 0.2;
					gbc.gridx = 4;
					gbc.gridy = i;
					responses.add(responseEdit,gbc);
				}
			}
			else
			{
				editResponse = new JTextField(list.get(i));
				editResponse.setLocation(i,0);
				editResponse.setSize(4,1);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 0.8;
				gbc.gridx = 0;
				gbc.gridy = i;
				responses.add(editResponse,gbc);

				responseEdit = new JButton("Done");
				responseEdit.setName("Done " + i);
				responseEdit.setLocation(i,5);
				responseEdit.setSize(1, 1);
				responseEdit.addActionListener(this);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 0.2;
				gbc.gridx = 4;
				gbc.gridy = i;
				responses.add(responseEdit,gbc);
			}
		}

		responsesScroller = new JScrollPane(responses);
		responsesScroller.setLocation(10, 125);
		responsesScroller.setSize(350, 100);
		responsesScroller.setVisible(true);
		popup.add(responsesScroller);

		popup.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton button = (JButton) e.getSource();
			if(button.getName().equalsIgnoreCase(save.getName()))
			{
				command.setPermissionLevel((String)permSelect.getSelectedItem());
				command.getFile().updateCommand();
				if(TurkeyBot.bot.settings.getSettingAsBoolean("outputchanges"))
					TurkeyBot.bot.sendMessage("Command !" + command.getName() + " was successfully edited");
				popup.dispose();
			}
			else if(button.getName().contains("Edit"))
			{
				if(editing != -1)
					command.editResponse(editing, editResponse.getText());
				editing = Integer.parseInt(button.getName().substring(5));
				refresh();
			}
			else if(button.getName().contains("Done"))
			{
				if(editResponse.getText().equals(""))
				{
					command.removeResponse(Integer.parseInt(button.getName().substring(5)));
					editing = -1;
					refresh();
				}
				else
				{
					command.editResponse(Integer.parseInt(button.getName().substring(5)), editResponse.getText());
					editing = -1;
					refresh();
				}
			}
			else if(button.getName().contains("Add"))
			{
				editing = command.getNumberOfResponses();
				command.addResponse("");
				refresh();
			}
		}
	}
}
