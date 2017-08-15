package com.Turkey.TurkeyBot.gui;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.commands.ConsoleCommands;

public class ConsoleTab extends Tab implements KeyListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	public static TextArea consoleWindow;
	public JTextField consoleEntry;
	public JLabel streamToJoinLabel;
	public JTextField streamToJoin;
	public JButton joinButton;
	public TextArea viewersList;

	private ArrayList<String> pastEntries = new ArrayList<String>();
	private String currentString = "";
	int timesUp = 0;

	public ConsoleTab()
	{
		streamToJoinLabel = new JLabel("Join: ");
		streamToJoinLabel.setLocation(10, 25);
		streamToJoinLabel.setSize(100, 25);
		super.add(streamToJoinLabel);

		streamToJoin = new JTextField();
		streamToJoin.setLocation(50, 25);
		streamToJoin.setSize(300, 25);
		streamToJoin.setBackground(Color.black);
		streamToJoin.setForeground(Color.white);
		super.add(streamToJoin);

		joinButton = new JButton("Join");
		joinButton.setLocation(375, 25);
		joinButton.setSize(75, 25);
		joinButton.addActionListener(this);
		super.add(joinButton);

		// console tab
		consoleWindow = new TextArea();
		consoleWindow.setLocation(10, 75);
		consoleWindow.setSize(600, 425);
		consoleWindow.setEditable(false);
		consoleWindow.setBackground(Color.black);
		consoleWindow.setForeground(Color.white);
		super.add(consoleWindow);

		consoleEntry = new JTextField();
		consoleEntry.setLocation(10, 500);
		consoleEntry.setSize(600, 25);
		consoleEntry.setBackground(Color.black);
		consoleEntry.setForeground(Color.white);
		consoleEntry.addKeyListener(this);
		super.add(consoleEntry);

		viewersList = new TextArea();
		viewersList.setLocation(625, 25);
		viewersList.setSize(160, 500);
		viewersList.setEditable(false);
		viewersList.setBackground(Color.black);
		viewersList.setForeground(Color.white);
		super.add(viewersList);
	}

	public void load()
	{
		if(TurkeyBot.bot.getChannel(false).equals(""))
		{
			joinButton.setText("Join");
			streamToJoin.setEditable(true);
			viewersList.append("Viewers: 0 \n");
			viewersList.append("---------------------------------- \n");
			viewersList.append("You are not in a channel \n");
		}
		else
		{
			joinButton.setText("Leave");
			streamToJoin.setEditable(false);
			viewersList.append("Viewers: " + TurkeyBot.bot.getViewers().size() + " \n");
			viewersList.append("---------------------------------- \n");
			for(String s : TurkeyBot.bot.getViewers())
			{
				viewersList.append(s + " \n");
			}
		}

		super.setVisible(true);
	}

	public void unLoad()
	{
		viewersList.setText("");
		super.setVisible(false);
	}

	/**
	 * Outputs the given message to the console.
	 * 
	 * @param level
	 *            The level of the message. Added onto the beginning of the message in the console window.
	 * @param message
	 *            The message to be added to the console window.
	 */
	public static void output(Level level, String message)
	{
		if(level == Level.Chat)
		{
			consoleWindow.append(message + "\n");
		}
		else
		{
			consoleWindow.append("[" + level.getLevel() + "]: " + message + "\n");
		}
	}

	public void keyPressed(KeyEvent e)
	{
		if(e.getSource() == consoleEntry)
		{
			// output("" + e.getKeyCode());
			boolean cancel = true;
			if(e.getKeyChar() == KeyEvent.VK_ENTER)
			{
				String[] args = consoleEntry.getText().split(" ");
				ConsoleCommands.onCommand(args);
				if(pastEntries.size() > 100)
				{
					pastEntries.remove(0);
				}
				pastEntries.add(consoleEntry.getText());
				consoleEntry.setText("");
			}
			else if(e.getKeyCode() == 38)
			{
				timesUp++;
				if(timesUp > pastEntries.size())
					timesUp--;
				if(pastEntries.size() > 0)
				{
					consoleEntry.setText(pastEntries.get(pastEntries.size() - timesUp));
				}
				cancel = false;
			}
			else if(e.getKeyCode() == 40)
			{
				timesUp--;
				if(timesUp < 0)
					timesUp++;
				if(pastEntries.size() > 0 && timesUp > 0)
				{
					consoleEntry.setText(pastEntries.get(pastEntries.size() - timesUp));
				}
				else if(timesUp == 0)
				{
					consoleEntry.setText(currentString);
				}
				cancel = false;
			}
			else if(e.getKeyCode() == 18)
			{
				String[] args = consoleEntry.getText().split(" ");
				String fullName = TurkeyBot.bot.getFullUserName(args[args.length - 1]);
				if(fullName != "")
				{
					args[args.length - 1] = fullName;
					String msg = "";
					for(String s : args)
					{
						msg += s + " ";
					}
					consoleEntry.setText(msg);
				}
			}
			if(cancel)
			{
				currentString = consoleEntry.getText();
				timesUp = 0;
			}
			cancel = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(joinButton))
		{
			TurkeyBot bot = TurkeyBot.bot;
			if(bot.getChannel(false).equals(""))
			{
				if(!streamToJoin.getText().equals(""))
				{
					bot.connectToChannel(streamToJoin.getText());
					Gui.reloadTab();
				}
			}
			else
			{
				bot.disconnectFromChannel();
				Gui.reloadTab();
			}
		}
	}

	/**
	 * Clears the console window.
	 */
	public static void clearConsole()
	{
		consoleWindow.setText("");
	}

	public enum Level
	{
		None(""), Chat("Chat"), Whisper("Whisper"), Info("Info"), Important("IMPORTANT"), Alert("Alert"), Warning("Warning"), DeBug("DeBug"), Error("ERROR");

		private String level;

		private Level(String level)
		{
			this.level = level;
		}

		public String getLevel()
		{
			return level;
		}
	}
}