package com.Turkey.TurkeyBot.Commands;

import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.Turkey.TurkeyBot.TurkeyBot;

public class MathCommand extends Command
{
	public MathCommand(String n)
	{
		super(n, "");
	}

	private static int answer = 0;
	
	public static boolean isMathQuestion = false;
	
	public void oncommand(TurkeyBot bot,String channel, String sender, String login, String hostname, String message)
	{
		isMathQuestion = true;
		String[] operations = new String[4];
		int[] numbers = new int[5];
		Random r = new Random();
		for(int i = 0; i < operations.length; i++)
		{
			int rand = r.nextInt(3);
			if(rand == 0)
				operations[i] = "+";
			else if(rand == 1)
				operations[i] = "-";
			else if(rand == 2)
				operations[i] = "*";
		}
		for(int i = 0; i < numbers.length; i++)
		{
			numbers[i] = r.nextInt(13);
		}
		
		String question = numbers[0] + "" + operations[0] + "" + numbers[1] + "" + operations[1] + "" + numbers[2] + "" + operations[2] + "" + numbers[3] + "" + operations[3] + "" + numbers[4];
		bot.sendMessage("The Question is: " + question);
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
	    try
		{
	    	String ans = String.valueOf(engine.eval(question));
	    	answer = Integer.parseInt(ans.substring(0, ans.indexOf(".")));
		} catch (ScriptException e){bot.sendMessage("Script Error!");}
	}
	
	public static int getAnswer()
	{
		return answer;
	}
	
	public boolean canUseByDefault()
	{
		return false;
	}
	
	public boolean canEdit()
	{
		return false;
	}
	
	public String getPermissionLevel()
	{
		return "Mod";
	}
}
