package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.Turkey.TurkeyBot.TurkeyBot;

public class QuestionRaffleTab extends Tab implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JButton startEntry;
	private JButton endEntry;
	private JLabel winnerLabel;
	private JLabel winnerName;
	private JLabel keywordLabel;
	private JTextField keywordField;
	
	private static String answer = "";
	private static boolean isRunning = false;

	public QuestionRaffleTab()
	{
		keywordLabel = new JLabel("Key Word:");
		keywordLabel.setLocation(25, 25);
		keywordLabel.setSize(75, 25);
		super.add(keywordLabel);

		keywordField = new JTextField();
		keywordField.setLocation(100, 25);
		keywordField.setSize(200,25);
		super.add(keywordField);

		startEntry = new JButton("Start Entry Period");
		startEntry.setLocation(375, 25);
		startEntry.setSize(200, 25);
		startEntry.addActionListener(this);
		super.add(startEntry);

		endEntry = new JButton("End Entry Period");
		endEntry.setLocation(375, 25);
		endEntry.setSize(200, 25);
		endEntry.addActionListener(this);
		super.add(endEntry);

		winnerLabel = new JLabel("Winner:");
		winnerLabel.setLocation(450, 175);
		winnerLabel.setSize(75, 25);
		super.add(winnerLabel);

		winnerName = new JLabel("");
		winnerName.setLocation(450, 200);
		winnerName.setSize(75, 25);
		super.add(winnerName);
	}

	public void load()
	{

		if(isRunning)
		{
			startEntry.setVisible(false);
			endEntry.setVisible(true);
			keywordField.setEditable(false);
		}
		else
		{
			endEntry.setVisible(false);
			startEntry.setVisible(true);
			keywordField.setEditable(true);
		}

		super.setVisible(true);
	}

	public void unLoad()
	{
		super.setVisible(false);
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(startEntry))
		{
			if(!keywordField.getText().equalsIgnoreCase(""))
			{
				winnerName.setText("");
				answer = keywordField.getText();
				isRunning = true;
				Gui.reloadTab();
			}
			else
			{
				error("You have not set your keyword for the Raffle entry!");
			}
		}
		else if(e.getSource().equals(endEntry))
		{
			isRunning = false;
			TurkeyBot.bot.sendMessage("The question raffle has ended with no one guessing the answer! The answer was: " + answer);
			Gui.reloadTab();
		}
	}


	public static String getAnswer()
	{
		return answer;
	}
	
	public static boolean isRunning()
	{
		return isRunning;
	}
	
	public static void end()
	{
		isRunning = false;
		answer = "";
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
		errorpopup.setLocationRelativeTo(startEntry);

		JLabel commandLabel = new JLabel(error);
		commandLabel.setLocation(10,15);
		commandLabel.setSize(300, 25);
		errorpopup.add(commandLabel);
	}
}
