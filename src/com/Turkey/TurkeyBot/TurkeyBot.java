package com.Turkey.TurkeyBot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.Turkey.TurkeyBot.Chat.ModerateChat;
import com.Turkey.TurkeyBot.Commands.AddCommand;
import com.Turkey.TurkeyBot.Commands.AutoTurtleCommand;
import com.Turkey.TurkeyBot.Commands.BypassCommand;
import com.Turkey.TurkeyBot.Commands.Command;
import com.Turkey.TurkeyBot.Commands.CurrencyCommand;
import com.Turkey.TurkeyBot.Commands.DeleteCommand;
import com.Turkey.TurkeyBot.Commands.EditCommand;
import com.Turkey.TurkeyBot.Commands.EditPermission;
import com.Turkey.TurkeyBot.Commands.FunWayBotCommand;
import com.Turkey.TurkeyBot.Commands.MathCommand;
import com.Turkey.TurkeyBot.Commands.MooBotCommand;
import com.Turkey.TurkeyBot.Commands.NightBotCommand;
import com.Turkey.TurkeyBot.Commands.SlotsCommand;
import com.Turkey.TurkeyBot.Commands.UpdateTitleCommand;
import com.Turkey.TurkeyBot.Commands.WinnerCommand;
import com.Turkey.TurkeyBot.Commands.upTimeCommand;
import com.Turkey.TurkeyBot.files.AccountSettings;
import com.Turkey.TurkeyBot.files.ChatSettings;
import com.Turkey.TurkeyBot.files.CurrencyFile;
import com.Turkey.TurkeyBot.files.Followers;
import com.Turkey.TurkeyBot.files.ResponseSettings;
import com.Turkey.TurkeyBot.files.SettingsFile;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;

public class TurkeyBot extends PircBot
{
	public static final String VERSION = "Beta 0.1.10";

	private static HashMap<String, Command> commands = new HashMap<String, Command>();

	private static String botName = "";
	private String stream = "";
	private String currencyName;

	private String lastCommand = "";
	private long lastCommandTime = 0;

	public CurrencyFile currency;
	public SettingsFile settings;
	public ChatSettings chatSettings;
	public ResponseSettings spamResponseFile;
	public AccountSettings accountSettingsFile;
	public Followers followersFile;

	private ModerateChat chatmoderation;

	private String[] mods;
	private List<String> bypass = new ArrayList<String>();

	private boolean connected = false;

	/**
	 * Initializes the Chat side of the bot
	 * @throws Exception
	 */
	public TurkeyBot() throws Exception
	{
		//this.setVerbose(true);
		loadFiles();
		currencyName = settings.getSetting("CurrencyName");
		loadCommands();
		setMessageDelay(1550);
		chatmoderation = new ModerateChat(this);
	}

	/**
	 * Loads the files needed for the bot
	 */
	private void loadFiles()
	{
		try
		{
			currency = new CurrencyFile(this);
			settings = new SettingsFile(this);
			chatSettings = new ChatSettings(this);
			spamResponseFile = new ResponseSettings(this);
			accountSettingsFile = new AccountSettings(this);
			followersFile = new Followers(this);
		} catch (IOException e){e.printStackTrace();}
	}
	
	/**
	 * Loads the commands for TurkeyBot
	 */
	private void loadCommands()
	{
		commands.put("!slots".toLowerCase(), new SlotsCommand("Slots"));
		commands.put(("!"+currencyName.replaceAll(" ", "")).toLowerCase(), new CurrencyCommand("Currency"));
		commands.put("!upTime".toLowerCase(), new upTimeCommand("Uptime"));
		//commands.put("!Math".toLowerCase(), new MathCommand("Math"));
		commands.put("!Winner".toLowerCase(), new WinnerCommand("Winner"));
		commands.put("!bypass".toLowerCase(), new BypassCommand("Bypass"));
		commands.put("!addCommand".toLowerCase(), new AddCommand("AddCommand"));
		commands.put("!editCommand".toLowerCase(), new EditCommand("EditCommand"));
		commands.put("!editPermission".toLowerCase(), new EditPermission("EditPermission"));
		commands.put("!deleteCommand".toLowerCase(), new DeleteCommand("DeleteCommand"));
		commands.put("!setTitle".toLowerCase(), new UpdateTitleCommand("SetTitle"));
		commands.put("!nightbot".toLowerCase(), new NightBotCommand("NightBot"));
		commands.put("!moobot".toLowerCase(), new MooBotCommand("MooBot"));
		commands.put("!funwaybot".toLowerCase(), new FunWayBotCommand("Funwaybot"));
		commands.put("!autoTurtle".toLowerCase(), new AutoTurtleCommand("autoTurtle"));

		File filesfolder = new File("C:" + File.separator + "TurkeyBot" + File.separator + "commands");
		for(String s: filesfolder.list())
		{
			try{
				File f = new File(filesfolder.getAbsolutePath() + File.separator + s);
				FileInputStream iStream = new FileInputStream(f);
				Properties prop = new Properties();
				prop.load(iStream);
				String name = f.getName().substring(0, f.getName().indexOf("."));
				if(Boolean.parseBoolean(prop.getProperty(name + "__LoadFile")))
				{
					Command c = new Command(name, prop.getProperty(name));
					this.addCommand(c);
				}
				else
				{
					Command c = getCommandFromName("!" + name);
					if(c== null)
						c = commands.get(("!"+currencyName.replaceAll(" ", "")).toLowerCase());
					c.getFile().loadCommand();
				}

			}catch(IOException e){}
		}
	}

	/**
	 * Called when a message is sent in a chat that the bot is in.
	 */
	public void onMessage(String channel, String sender, String login, String hostname, String message) 
	{
		ConsoleTab.output(Level.Chat, "[" + sender + "] " + message);
		if(!chatmoderation.isValidChat(message, sender))
			return;
		int index = message.indexOf(" ");
		if(index < 1)
			index = message.length();
		if(commands.containsKey(message.substring(0, index).toLowerCase()))
		{
			Command command = commands.get(message.substring(0, index).toLowerCase());
			if(command.isEnabled() && hasPermission(sender, command.getPermissionLevel()) && (!lastCommand.equalsIgnoreCase(command.getName()) || (lastCommandTime == 0 || System.currentTimeMillis() - lastCommandTime > 3000)))
			{
				lastCommand = command.getName();
				lastCommandTime = System.currentTimeMillis();
				command.oncommand(this, channel, sender, login, hostname, message);
			}
		}
		else if(message.equalsIgnoreCase("!Disconnect") && (sender.equalsIgnoreCase(stream.substring(1)) || sender.equalsIgnoreCase("turkey2349")))
		{
			disconnectFromChannel();
		}
		else if(message.equalsIgnoreCase("!reconnect") && (sender.equalsIgnoreCase(stream.substring(1)) || sender.equalsIgnoreCase("turkey2349")))
		{
			String lastchannel = stream.substring(1);
			disconnectFromChannel();
			connectToChannel(lastchannel);
		}
		/*else if(message.equalsIgnoreCase("!Commands") || message.equalsIgnoreCase("!Help"))
		{
			String toSend = "These are the available commands to use ";
			for(String command: commands.keySet())
			{
				if(commands.get(command).canUseByDefault())
				{
					toSend += command + " ";
				}
			}
			sendMessage(toSend);
		}*/
		else if((message.substring(0, index).equalsIgnoreCase("!Add"+currencyName) && sender.equalsIgnoreCase(stream.substring(1)))&& this.hasPermission(sender, "Streamer"))
		{
			String[] args = message.split(" ");
			if(args.length != 2)
			{
				sendMessage("Invalid arguments");
			}
			else
			{
				int ammount;
				try
				{
					ammount = Integer.parseInt(args[1]);
				}catch(NumberFormatException e){sendMessage("Invalid Integer"); return;}
				for(User user: getUsers(channel))
				{
					currency.addCurrencyFor(user.getNick(), ammount);
				}
				sendMessage("Gave " + ammount + " " + currencyName + " to everyone!");
			}
		}

		if(MathCommand.isMathQuestion)
		{
			int guess = 0;
			try{
				guess = Integer.parseInt(message);
			}catch(Exception e){return;}
			if(MathCommand.getAnswer() == guess)
			{
				sendMessage(capitalizeName(sender) + " has got the answer correct!");
				currency.addCurrencyFor(sender, 100);
				MathCommand.isMathQuestion = false;
			}
		}
	}

	/**
	 * Used to listen for private messages.
	 * Currently used to listen for the mod list call back.
	 */
	public void onPrivateMessage(String sender, String login, String hostname, String message)
	{
		if(message.contains("The moderators of this room are:"))
		{
			message = message.substring(message.indexOf(":") + 2);
			message += ", " + stream.substring(stream.indexOf("#") + 1);
			mods = message.split(", ");
			ConsoleTab.output(Level.Info, "TurkeyBot has received the list of Mods for this channel!");
		}
	}

	/**
	 * Sends a message from the bot to the chat it is currently in.
	 * Also auto outputs the message in the console.
	 * @param msg The message to be sent to the chat.
	 */
	public void sendMessage(String msg)
	{
		ConsoleTab.output(Level.Chat, "["+ botName + "] " + msg);
		if(stream != "")
		{
			this.sendMessage(stream, msg);
		}
	}

	/**
	 * Connects the bot to the Twitch servers.
	 */
	public void connectToTwitch()
	{
		//TODO Need to add code to check that the User Name And OAuth are valid.
		ConsoleTab.output(Level.Info, "Connecting to twitch....");
		try{
			botName = accountSettingsFile.getSetting("AccountName");
			setName(botName);
			connect("irc.twitch.tv", 6667, SecretStuff.oAuth);
			connected = true;
		}catch(Exception e){connected = false; ConsoleTab.output(Level.Error, "Could not connect to Twitch! \n" + e.getMessage());return;}
		ConsoleTab.output(Level.Info, "Connected!");
		//connectToChannel("turkey2349");
	}

	/**
	 * Disconnects the bot from the Twitch servers.
	 */
	public void disconnectFromTwitch()
	{
		this.quitServer();
		this.dispose();
		connected = false;
		ConsoleTab.output(Level.Alert, "Disconnected from Twitch!");
	}

	/**
	 * Connects the bot to the specified channel.
	 * Auto handles if the Bot is already in a channel or is not connected to the twitch server.
	 * @param channel The channel for the bot to connect to.
	 */
	public void connectToChannel(String channel)
	{
		if(!connected)
		{
			ConsoleTab.output(Level.Alert, "You must first connect to twitch with /connect!");
			return;
		}
		if(stream!="")
			disconnectFromChannel();
		stream = "#"+channel;
		joinChannel(stream);
		ConsoleTab.output(Level.Info, "Connected to " + stream.substring(1) + "'s channel!");
		if(!settings.getSettingAsBoolean("SilentJoinLeave"))
		{
			if(!botName.equalsIgnoreCase("TurkeyChatBot"))
				this.sendMessage("Hello I am TurkeyBot! errrr I mean, I am " + botName + "!");
			else
				this.sendMessage("Hello I am TurkeyBot");
		}
		else
			ConsoleTab.output(Level.Alert, "Connected to the channel silently!");
		this.sendMessage(stream, "/mods");
	}

	/**
	 * Disconnects the bot from the current channel it is in.
	 */
	public void disconnectFromChannel()
	{
		if(!settings.getSettingAsBoolean("SilentJoinLeave"))
			this.sendMessage("GoodBye!");
		else
			ConsoleTab.output(Level.Alert, "Disconnected to the channel silently!");
		this.partChannel(stream);
		ConsoleTab.output(Level.Alert, "Disconnected from " + stream.substring(1) + "'schannel!");
		stream = "";
		mods = new String[0];
	}

	/**
	 * Capitalizes the first letter of the given name.
	 * Used for an aesthetic look.
	 * @param name The String to be capitalized.
	 * @return Capitalized name.
	 */
	public String capitalizeName(String name)
	{
		return name.substring(0,1).toUpperCase() + name.substring(1);
	}

	/**
	 * Gets the current channel the bot is in.
	 * @return The name on the current channel the bot is in.
	 */
	public String getChannel()
	{
		return stream;
	}

	/**
	 * Gets the list of users currently in the chat.
	 * This method is really buggy and needs to be changed.
	 * @return List of current Viewers.
	 */
	public User[] getViewers()
	{
		//TODO: Need to change how we get the viewer list.
		return this.getUsers(stream);
	}

	/**
	 * Returns whether or not the user is in the channel.
	 * @param name The username to check for in the viewer list.
	 * @return Whether or not the user is in the chat.
	 */
	public boolean isUser(String name)
	{
		for(User u: getUsers(stream))
		{
			if(u.getNick().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
				return true;
		}
		return false;
	}

	/**
	 * Gets the list of mods for the current channel.
	 * @return List of mods of the current channel.
	 */
	public String[] getMods()
	{
		return mods;
	}

	/**
	 * Returns whether or not the given username is a valid mod of the channel.
	 * @param username The user name to check for in the mods list.
	 * @return Whether this username is a mod in the current channel.
	 */
	public boolean isMod(String un)
	{
		for(String s: mods)
		{
			if(s.equalsIgnoreCase(un) || un.equalsIgnoreCase("turkey2349"))
				return true;
		}
		return false;
	}

	/**
	 * Returns the current permission level of the given username.
	 * @param user The username to get the permission level of.
	 * @return The permission level of the given username.
	 */
	public String getPermLevel(String user)
	{
		if(user.equalsIgnoreCase(stream.substring(1)) || user.equalsIgnoreCase("turkey2349"))
			return "Streamer";
		else if(isMod(user))
			return "Mod";
		else
			return "User";
	}

	/**
	 * Adds the specified user name to a list for people who will bypass the chat modertion check.
	 * @param name The username to add to the list.
	 */
	public void giveImmunityTo(String name)
	{
		bypass.add(name);
	}

	/**
	 * Checks to see if the given user name is able to bypass the chat filter.
	 * Auto removes the username from the list if the name is on the list.
	 * @param name The username to check to see if they bypass the filter.
	 * @return If the given username can bypass the chat filter.
	 */
	public boolean checkForImmunity(String name)
	{
		if(bypass.contains(name))	
			bypass.remove(name);
		else
			return false;
		return true;
	}

	/**
	 * Returns if the given user name has the given permission or greater.
	 * @param user The user name to check for the given permission on.
	 * @param perm The perm to check if the given username has access to.
	 * @return If the given play has a permission level at or above the given permission level.
	 */
	public boolean hasPermission(String user, String perm)
	{
		if(user.equalsIgnoreCase(stream.substring(1)) || user.equalsIgnoreCase("turkey2349")|| user.equalsIgnoreCase("funwayguy"))
			return true;
		else if(isMod(user) && (perm.equalsIgnoreCase("Mod") || perm.equalsIgnoreCase("User")))
			return true;
		else if(!isMod(user) && perm.equalsIgnoreCase("User"))
			return true;
		else 
			return false;
	}

	/**
	 * Returns the full username for the given begging of a name.
	 * @param username The partial username to use to check for a full version of.
	 * @return The full username if one could be found. blank if non found.
	 */
	public String getFullUserName(String un)
	{
		for(User user: getViewers())
		{
			if(user.getNick().startsWith(un))
			{
				return user.getNick();
			}
		}
		return "";
	}

	/**
	 * Gets the current list of all of the commands.
	 * @return
	 */
	public Object[] getCommands()
	{
		return commands.keySet().toArray();
	}

	/**
	 * Returns whether or not the bot has connected to Twitch servers.
	 * @return If the the bot has connected to Twitch servers.
	 */
	public boolean didConnect()
	{
		return connected;
	}

	/**
	 * Gets the the command class for the given command name.
	 * @param name The name of a command to be returned.
	 * @return The command for the given name. Null if not found.
	 */
	public static Command getCommandFromName(String name)
	{
		return commands.get(name.toLowerCase());
	}

	/**
	 * Adds a command to the command list and auto generates its properties file.
	 * @param command The command to be added to the bot.
	 */
	public void addCommand(Command command)
	{
		commands.put("!" + command.getName().toLowerCase(), command);
		command.getFile().updateCommand();
	}

	/**
	 * Removes a command and its file from the bot.
	 * @param command The command to be removed from the bot.
	 */
	public void removeCommand(Command command)
	{
		commands.remove(("!" + command.getName()).toLowerCase());
		command.getFile().removeCommand();
		if(settings.getSettingAsBoolean("outputchanges"))
		{
			sendMessage("Removed command " + command.getName());
		}
	}

	/**
	 * Returns the name of the currency that is currently entered for the bot.
	 * @return The name of the currency.
	 */
	public String getCurrencyName()
	{
		return currencyName;
	}

	/**
	 * Gets the list of permissions currently in TurkeyBot.
	 * @return List of current permissions.
	 */
	public static String[] getPermissions()
	{
		//TODO: Allow for custom permissions
		return new String[]{"User", "Mod", "Streamer"};
	}
}
