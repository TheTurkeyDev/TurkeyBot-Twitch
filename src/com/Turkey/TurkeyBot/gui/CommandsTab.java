package com.Turkey.TurkeyBot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.Commands.Command;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class CommandsTab extends Tab implements ActionListener
{
	List<JComponent> components = new ArrayList<JComponent>();

	public CommandsTab(JFrame jframe)
	{
		super(jframe);
	}

	public void load()
	{
		Object[] temp = Gui.getBot().getCommands();
		String[] commands = new String[temp.length];

		for(int i = 0; i < temp.length; i++)
		{
			String com = (String) temp[i];
			commands[i] = com;
		}
		Arrays.sort(commands);

		JLabel label;
		JButton editbutton;
		JButton enablebutton;
		JButton disablebutton;
		JButton deletebutton;
		int x = 10;
		int row = 0;
		for(int i = 0; i < commands.length; i++)
		{
			String commandName = commands[i];

			if((row*25)+20 > (frame.getHeight()-100))
			{
				x+=325;
				row = 0;
			}

			label = new JLabel(commandName);
			label.setLocation(x, (row*25) + 20);
			label.setSize(200, 25);
			label.setVisible(true);
			frame.add(label);
			components.add(label);

			Command command = TurkeyBot.getCommandFromName(commandName);
			
			editbutton = new JButton();
			editbutton.setName("Edit " + commandName);
			editbutton.setLocation(x + 100, (row*25) + 20);
			editbutton.setSize(60, 25);
			editbutton.setVisible(true);
			editbutton.setText("Edit");
			editbutton.addActionListener(this);
			frame.add(editbutton);
			components.add(editbutton);
			
			if(command.canEdit())
			{
				deletebutton = new JButton();
				deletebutton.setName("Delete " + commandName);
				deletebutton.setLocation(x + 240, (row*25) + 20);
				deletebutton.setSize(70, 25);
				deletebutton.setVisible(true);
				deletebutton.setText("Delete");
				deletebutton.addActionListener(this);
				frame.add(deletebutton);
				components.add(deletebutton);
			}
			
			if(!command.isEnabled())
			{
				enablebutton = new JButton();
				enablebutton.setName("Enable " + commandName);
				enablebutton.setLocation(x + 160, (row*25) + 20);
				enablebutton.setSize(80, 25);
				enablebutton.setVisible(true);
				enablebutton.setText("Enable");
				enablebutton.addActionListener(this);
				frame.add(enablebutton);
				components.add(enablebutton);
			}
			else
			{
				disablebutton = new JButton();
				disablebutton.setName("Disable " + commandName);
				disablebutton.setLocation(x + 160, (row*25) + 20);
				disablebutton.setSize(80, 25);
				disablebutton.setVisible(true);
				disablebutton.setText("Disable");
				disablebutton.addActionListener(this);
				frame.add(disablebutton);
				components.add(disablebutton);
			}

			row++;
		}
		frame.repaint();
	}

	public void unLoad()
	{
		for(JComponent comp: components)
		{
			comp.setVisible(false);
			frame.remove(comp);
		}
		components.clear();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton button = ((JButton)e.getSource());
			Command command;
			try{
				command = TurkeyBot.getCommandFromName(button.getName().substring(button.getName().indexOf(" ") + 1));
			}catch(Exception ex){ConsoleTab.output(Level.Error, "Failed to get the correct command that goes with that edit button!"); return;};
			if(button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Edit"))
			{
				new EditCommandGui(command);
			}
			else if(button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Enable"))
			{
				command.enable();
				command.getFile().enableCommand();
				unLoad();
				load();
			}
			else if(button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Disable"))
			{
				command.disable();
				command.getFile().disableCommand();
				unLoad();
				load();
			}
			else if(button.getName().substring(0, button.getName().indexOf(" ")).equalsIgnoreCase("Delete"))
			{
				Gui.getBot().removeCommand(command);
				unLoad();
				load();
			}
		}
	}

}
