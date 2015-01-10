package com.Turkey.TurkeyBot.gui;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.Turkey.TurkeyBot.Commands.ConsoleCommands;


public class ConsoleTab extends Tab implements KeyListener
{

	public static TextArea consoleWindow;
	public static TextField consoleEntry;

	private ArrayList<String> pastEntries = new ArrayList<String>();
	private String currentString = "";
	int timesUp = 0;

	public ConsoleTab(JFrame jframe)
	{
		super(jframe);
		// console tab
		consoleWindow = new TextArea();
		consoleWindow.setLocation(50, 50);
		consoleWindow.setSize(700,425);
		consoleWindow.setEditable(false);
		consoleWindow.setBackground(Color.black);
		consoleWindow.setForeground(Color.white);
		consoleWindow.setVisible(false);
		frame.add(consoleWindow);

		consoleEntry = new TextField();
		consoleEntry.setLocation(50,475);
		consoleEntry.setSize(700, 25);
		consoleEntry.setBackground(Color.black);
		consoleEntry.setForeground(Color.white);
		consoleEntry.addKeyListener(this);
		consoleEntry.setVisible(false);
		frame.add(consoleEntry);
	}

	public void load()
	{
		consoleWindow.setVisible(true);
		consoleEntry.setVisible(true);
	}
	public void unLoad()
	{
		consoleWindow.setVisible(false);
		consoleEntry.setVisible(false);
	}

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
			//output("" + e.getKeyCode());
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
					consoleEntry.setText(pastEntries.get(pastEntries.size() - timesUp ));
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
					consoleEntry.setText(pastEntries.get(pastEntries.size() - timesUp ));
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
				String fullName = Gui.getBot().getFullUserName(args[args.length-1]);
				if(fullName != "")
				{
					args[args.length-1] = fullName;
					String msg = "";
					for(String s: args)
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

	@Override public void keyReleased(KeyEvent e){}@Override public void keyTyped(KeyEvent e){}


	public enum Level
	{
		None(""),
		Chat("Chat"),
		Info("Info"),
		Important("IMPORTANT"),
		Alert("Alert"),
		Warning("Warning"),
		Error("ERROR");

		private String level;

		private Level(String level) {
			this.level = level;
		}

		public String getLevel()
		{
			return level;
		}
	}

}
