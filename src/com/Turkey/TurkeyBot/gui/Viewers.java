package com.Turkey.TurkeyBot.gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jibble.pircbot.User;

public class Viewers extends Tab
{

	private User[] viewers;
	public static final String[] columnNames = {
		"User Name", "Rank", "Following since", ""
	};

	private JTable table;
	private JScrollPane scroller;

	public Viewers(JFrame jframe)
	{
		super(jframe);
	}

	public void load()
	{
		viewers = Gui.getBot().getViewers();
		String[][] rows;

		if(viewers.length == 0)
		{
			rows = new String[1][4];
			rows[0][0] = "No viewers in chat!";
			rows[0][1] = "";
			rows[0][2] = "";
			rows[0][3] = "";
		}
		else
		{
			rows = new String[viewers.length][4];

			for(int y = 0; y < viewers.length; y++)
			{
				for(int x = 0; x < 4; x++)
				{
					if(x == 0)
						rows[y][x] = viewers[y].getNick();
					else if(x == 1)
						rows[y][x] = Gui.getBot().getPermLevel(viewers[y].getNick());
					else if(x == 2)
					{
						String response =Gui.getBot().followersFile.getSetting(viewers[y].getNick());
						rows[y][x] = response != null ? response : "Not following";
					}
					else if(x == 3)
					{
						rows[y][x] = "";
					}
				}
			}
		}
		table = new JTable(rows, columnNames)
		{
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {                
				return false;        
			};
		};
		scroller = new JScrollPane(table);
		scroller.setLocation(40, 0);
		scroller.setSize(700, 500);
		scroller.setVisible(true);
		frame.add(scroller);
	}


	public void unLoad()
	{
		scroller.setVisible(false);
	}
}