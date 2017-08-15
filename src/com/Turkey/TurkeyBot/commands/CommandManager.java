package com.Turkey.TurkeyBot.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.Turkey.TurkeyBot.TurkeyBot;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CommandManager
{

	private static HashMap<String, Command> commands = new HashMap<String, Command>();

	private static String lastCommand = "";
	private static long lastCommandTime = 0;

	/**
	 * Loads the commands for TurkeyBot
	 */
	public static void loadCommands()
	{
		commands.clear();
		commands.put("!slots".toLowerCase(), new SlotsCommand("Slots"));
		commands.put(("!" + TurkeyBot.bot.getProfile().getCurrencyName().replaceAll(" ", "")).toLowerCase(), new CurrencyCommand("Currency"));
		commands.put("!upTime".toLowerCase(), new UptimeCommand("Uptime"));
		commands.put("!Winner".toLowerCase(), new WinnerCommand("Winner"));
		commands.put("!bypass".toLowerCase(), new BypassCommand("Bypass"));
		commands.put("!addCommand".toLowerCase(), new AddCommand("AddCommand"));
		commands.put("!editCommand".toLowerCase(), new EditCommand("EditCommand"));
		commands.put("!addResponse".toLowerCase(), new AddResponse("AddResponse"));
		commands.put("!editPermission".toLowerCase(), new EditPermission("EditPermission"));
		commands.put("!deleteCommand".toLowerCase(), new DeleteCommand("DeleteCommand"));
		// commands.put("!commandstatus".toLowerCase(), new StatusCommand("commandStatus"));
		commands.put("!setTitle".toLowerCase(), new UpdateTitleCommand("SetTitle"));
		commands.put("!turkeybot".toLowerCase(), new TurkeyBotCommand("TurkeyBot"));
		commands.put("!nightbot".toLowerCase(), new NightBotCommand("NightBot"));
		commands.put("!moobot".toLowerCase(), new MooBotCommand("MooBot"));
		commands.put("!funwaybot".toLowerCase(), new FunWayBotCommand("Funwaybot"));
		commands.put("!autoTurtle".toLowerCase(), new AutoTurtleCommand("autoTurtle"));

		File filesfolder = new File(System.getProperty("user.home") + File.separator + "TurkeyBot" + File.separator + TurkeyBot.bot.getProfile().getProfileName() + File.separator + "commands");
		for(String s : filesfolder.list())
		{
			if(!s.contains(".json"))
				continue;
			try
			{
				File f = new File(filesfolder.getAbsolutePath() + File.separator + s);
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String result = "";
				String line = "";
				while((line = reader.readLine()) != null)
				{
					result += line;
				}
				reader.close();

				String name = f.getName().substring(0, f.getName().indexOf("."));
				JsonElement elem = TurkeyBot.json.parse(result);
				if(elem == null)
				{
					ConsoleTab.output(Level.Error, "Command file " + f.getName() + " filed to load!");
				}
				JsonObject obj = elem.getAsJsonObject();

				if(obj.get("LoadFile").getAsBoolean())
				{
					Command c = new Command(name, obj.get("Responses").getAsJsonObject().get("0").getAsString());
					c.setPermissionLevel(obj.get("PermLevel").getAsString());
					c.setCommandCooldown(obj.get("CoolDown") == null ? 0 : obj.get("CoolDown").getAsInt());
					if(!obj.get("Enabled").getAsBoolean())
						c.disable();
					JsonObject responses = obj.get("Responses").getAsJsonObject();
					for(int i = 1; i < obj.get("Number_Of_Responses").getAsInt(); i++)
					{
						c.addResponse(responses.get("" + i).getAsString());
					}
					CommandManager.addCommand(c);
				}
				else
				{
					Command c = getCommandFromName("!" + name);
					if(c == null)
						c = commands.get(("!" + TurkeyBot.bot.getProfile().getCurrencyName().replaceAll(" ", "")).toLowerCase());

					c.setPermissionLevel(obj.get("PermLevel").getAsString());
					c.setCommandCooldown(obj.get("CoolDown") == null ? 0 : obj.get("CoolDown").getAsInt());
					if(!obj.get("Enabled").getAsBoolean())
						c.disable();

					c.getFile().updateCommand();
				}

			} catch(IOException e)
			{
			}
		}
	}

	public static void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		int index = message.indexOf(" ");

		if(index < 1)
			index = message.length();

		if(commands.containsKey(message.substring(0, index).toLowerCase()))
		{
			Command command = commands.get(message.substring(0, index).toLowerCase());
			if(command.isEnabled() && TurkeyBot.bot.hasPermission(sender, command.getPermissionLevel()) && (!lastCommand.equalsIgnoreCase(command.getName()) || (lastCommandTime == 0 || System.currentTimeMillis() - lastCommandTime > 1000)))
			{
				if(command.runcommand())
				{
					lastCommand = command.getName();
					lastCommandTime = System.currentTimeMillis();
					command.oncommand(TurkeyBot.bot, channel, sender, login, hostname, message);
				}
			}
		}
	}

	/**
	 * Gets the the command class for the given command name.
	 * 
	 * @param name
	 *            The name of a command to be returned.
	 * @return The command for the given name. Null if not found.
	 */
	public static Command getCommandFromName(String name)
	{
		return commands.get(name.toLowerCase());
	}

	/**
	 * Adds a command to the command list and auto generates its properties file.
	 * 
	 * @param command
	 *            The command to be added to the bot.
	 */
	public static void addCommand(Command command)
	{
		commands.put("!" + command.getName().toLowerCase(), command);
		command.getFile().updateCommand();
	}

	/**
	 * Removes a command and its file from the bot.
	 * 
	 * @param command
	 *            The command to be removed from the bot.
	 */
	public static void removeCommand(Command command)
	{
		commands.remove(("!" + command.getName()).toLowerCase());
		command.getFile().removeCommand();
		if(TurkeyBot.bot.getProfile().settings.getSettingAsBoolean("outputchanges"))
		{
			TurkeyBot.bot.sendMessage("Removed command " + command.getName());
		}
	}

	/**
	 * Gets the current list of all of the commands.
	 * 
	 * @return
	 */
	public static Object[] getCommands()
	{
		return commands.keySet().toArray();
	}
}
