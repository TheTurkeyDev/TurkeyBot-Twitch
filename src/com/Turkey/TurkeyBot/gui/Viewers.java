package com.Turkey.TurkeyBot.gui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Viewers extends Tab
{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> viewers;
	public static final String[] columnNames = {
		"User Name", "Rank", "Following since", ""
	};

	private JTable table;
	private JScrollPane scroller;

	public Viewers()
	{
	}

	public void load()
	{
		viewers = Gui.getBot().getViewers();
		String[][] rows;

		if(viewers == null || viewers.size() == 0)
		{
			rows = new String[1][4];
			rows[0][0] = "No viewers in chat!";
			rows[0][1] = "";
			rows[0][2] = "";
			rows[0][3] = "";
		}
		else
		{
			rows = new String[viewers.size()][4];

			for(int y = 0; y < viewers.size(); y++)
			{
				for(int x = 0; x < 4; x++)
				{
					if(x == 0)
						rows[y][x] = viewers.get(y);
					else if(x == 1)
						rows[y][x] = Gui.getBot().getPermLevel(viewers.get(y));
					else if(x == 2)
					{
						String response = Gui.getBot().followersFile.getSetting(viewers.get(y));
						response= response!=null ? response.replaceAll("\"", ""): null;
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
						format.setTimeZone(TimeZone.getTimeZone("GMT"));
						String parse = "";
						try
						{
							parse = response != null ? format.parse(response).toString() : "Not following";
						} catch (ParseException e)
						{parse = "Error";}
						rows[y][x] = parse;
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
		super.add(scroller);
		super.setVisible(true);
	}


	public void unLoad()
	{
		super.setVisible(false);
	}
}