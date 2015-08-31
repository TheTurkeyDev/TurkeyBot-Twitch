package com.Turkey.TurkeyBot.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.util.KeyWordRaffle;

public class KeyWordRaffleTab extends Tab implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JButton startEntry;
	private JButton endEntry;
	private JButton pickWinner;
	private JLabel winnerLabel;
	private JLabel winnerName;
	private JLabel keywordLabel;
	private JTextField keywordField;
	private JTextArea entered;

	private static KeyWordRaffle currentRaffle;

	public KeyWordRaffleTab()
	{
		keywordLabel = new JLabel("Key Word:");
		keywordLabel.setLocation(25, 25);
		keywordLabel.setSize(75, 25);
		super.add(keywordLabel);

		keywordField = new JTextField();
		keywordField.setLocation(100, 25);
		keywordField.setSize(200,25);
		super.add(keywordField);

		entered = new JTextArea();
		entered.setLocation(25, 100);
		entered.setSize(300,400);
		entered.setEditable(false);
		super.add(entered);

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

		pickWinner = new JButton("Pick Winner");
		pickWinner.setLocation(375, 125);
		pickWinner.setSize(200, 25);
		pickWinner.addActionListener(this);
		super.add(pickWinner);

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
		entered.setText("Users Entered Into the Raffle: \n");

		if(currentRaffle != null && currentRaffle.isRunning())
		{
			startEntry.setVisible(false);
			pickWinner.setVisible(false);
			endEntry.setVisible(true);
			keywordField.setEditable(false);
		}
		else
		{
			endEntry.setVisible(false);
			startEntry.setVisible(true);
			pickWinner.setVisible(true);
			keywordField.setEditable(true);
		}

		if(currentRaffle != null && currentRaffle.getEntries().size() > 0)
			for(String name: currentRaffle.getEntries())
				entered.append(TurkeyBot.bot.capitalizeName(name) + " \n");

		super.setVisible(true);
	}

	public void unLoad()
	{
		entered.setText("");
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
				currentRaffle = new KeyWordRaffle(keywordField.getText());
				currentRaffle.setRunning(true);
				TurkeyBot.bot.sendMessage("A Raffle has started! To enter simply type: " + currentRaffle.getKeyWord());
				Gui.reloadTab();
			}
			else
			{
				error("You have not set your keyword for the Raffle entry!");
			}
		}
		else if(e.getSource().equals(endEntry))
		{
			currentRaffle.setRunning(false);
			TurkeyBot.bot.sendMessage("The Raffle has ended with " + currentRaffle.getEntries().size() + " entries!");
			Gui.reloadTab();
		}
		else if(e.getSource().equals(pickWinner))
		{
			if(currentRaffle != null)
			{
				if(currentRaffle.getEntries().size() > 0)
				{
					String winner = currentRaffle.getRandomEntry();
					winnerName.setText(winner);
					TurkeyBot.bot.sendMessage(TurkeyBot.bot.capitalizeName(winner) + " has won the raffle! Congrats!");
				}
				else
					error("No has entered the raffle!");
			}
		}
	}


	public static KeyWordRaffle getCurrentRaffle()
	{
		return currentRaffle;
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
