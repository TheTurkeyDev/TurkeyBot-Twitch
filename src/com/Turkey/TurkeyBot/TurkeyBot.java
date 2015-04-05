package com.Turkey.TurkeyBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.Turkey.TurkeyBot.chat.AutoAnnouncement;
import com.Turkey.TurkeyBot.chat.ModerateChat;
import com.Turkey.TurkeyBot.commands.AddCommand;
import com.Turkey.TurkeyBot.commands.AddResponse;
import com.Turkey.TurkeyBot.commands.AutoTurtleCommand;
import com.Turkey.TurkeyBot.commands.BypassCommand;
import com.Turkey.TurkeyBot.commands.Command;
import com.Turkey.TurkeyBot.commands.CurrencyCommand;
import com.Turkey.TurkeyBot.commands.DeleteCommand;
import com.Turkey.TurkeyBot.commands.EditCommand;
import com.Turkey.TurkeyBot.commands.EditPermission;
import com.Turkey.TurkeyBot.commands.FunWayBotCommand;
import com.Turkey.TurkeyBot.commands.MooBotCommand;
import com.Turkey.TurkeyBot.commands.NightBotCommand;
import com.Turkey.TurkeyBot.commands.SlotsCommand;
import com.Turkey.TurkeyBot.commands.StatusCommand;
import com.Turkey.TurkeyBot.commands.UpdateTitleCommand;
import com.Turkey.TurkeyBot.commands.WinnerCommand;
import com.Turkey.TurkeyBot.commands.upTimeCommand;
import com.Turkey.TurkeyBot.files.AccountSettings;
import com.Turkey.TurkeyBot.files.AnnouncementFile;
import com.Turkey.TurkeyBot.files.ChatSettings;
import com.Turkey.TurkeyBot.files.CurrencyFile;
import com.Turkey.TurkeyBot.files.Followers;
import com.Turkey.TurkeyBot.files.ResponseSettings;
import com.Turkey.TurkeyBot.files.SettingsFile;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.Turkey.TurkeyBot.gui.Gui;
import com.Turkey.TurkeyBot.gui.KeyWordRaffleTab;
import com.Turkey.TurkeyBot.gui.QuestionRaffleTab;
import com.Turkey.TurkeyBot.util.CurrencyThread;
import com.Turkey.TurkeyBot.util.HTTPConnect;
import com.Turkey.TurkeyBot.util.KeyWordRaffle;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TurkeyBot extends PircBot
{
	public static final String VERSION = "Beta 1.2.7";

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
	public CurrencyThread currencyTrack;
	public AnnouncementFile announceFile;

	private ModerateChat chatmoderation;
	public AutoAnnouncement announcer;

	private String[] mods;
	private ArrayList<String> viewers;

	private List<String> bypass = new ArrayList<String>();

	public static JsonParser json;

	private boolean connected = false;

	/**
	 * Initializes the Chat side of the bot
	 * @throws Exception
	 */
	public TurkeyBot() throws Exception
	{
		//this.setVerbose(true);
		json = new JsonParser();
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
			announceFile = new AnnouncementFile();
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
		commands.put("!addResponse".toLowerCase(), new AddResponse("AddResponse"));
		commands.put("!editPermission".toLowerCase(), new EditPermission("EditPermission"));
		commands.put("!deleteCommand".toLowerCase(), new DeleteCommand("DeleteCommand"));
		commands.put("!commandstatus".toLowerCase(), new StatusCommand("commandStatus"));
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
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String result = "";
				String line = "";
				while((line = reader.readLine()) != null)
				{
					result += line;
				}
				reader.close();

				String name = f.getName().substring(0, f.getName().indexOf("."));
				JsonObject obj = json.parse(result).getAsJsonObject();

				if(obj.get("LoadFile").getAsBoolean())
				{
					Command c = new Command(name, obj.get("Responses").getAsJsonObject().get("0").getAsString());
					JsonObject responses = obj.get("Responses").getAsJsonObject();
					for(int i = 1; i < obj.get("Number_Of_Responses").getAsInt(); i++)
					{
						c.addResponse(responses.get("" + i).getAsString());
					}
					this.addCommand(c);
				}
				else
				{
					Command c = getCommandFromName("!" + name);
					if(c== null)
						c = commands.get(("!"+currencyName.replaceAll(" ", "")).toLowerCase());
					c.getFile().updateCommand();
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
		String[] args = message.split(" ");
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
		else if(args[0].equalsIgnoreCase("!moderate") && (sender.equalsIgnoreCase(stream.substring(1)) || sender.equalsIgnoreCase("turkey2349")))
		{		
			if(args.length < 2)
			{
				this.sendMessage(this.capitalizeName(sender) + ": invalid use of that command! Try !moderate <true:false>");
			}
			else
			{
				if(args[1].equalsIgnoreCase("true"))
				{
					ModerateChat.Moderate = true;
					this.sendMessage(this.capitalizeName(sender) + "TurkeyBot is now moderating the chat!");
					return;
				}
				else if(args[1].equalsIgnoreCase("false"))
				{
					ModerateChat.Moderate = false;
					this.sendMessage(this.capitalizeName(sender) + "TurkeyBot is no longer moderating the chat!");
					return;
				}
				this.sendMessage(this.capitalizeName(sender) + "The argument was not true or false");
			}
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
			if(args.length != 2)
			{
				sendMessage("Invalid arguments");
			}
			else
			{
				int ammount;
				try{
					ammount = Integer.parseInt(args[1]);
				}catch(NumberFormatException e){sendMessage("Invalid Integer"); return;}
				for(User user: getUsers(channel))
				{
					currency.addCurrencyFor(user.getNick(), ammount);
				}
				sendMessage("Gave " + ammount + " " + currencyName + " to everyone!");
			}
		}

		if(KeyWordRaffleTab.getCurrentRaffle() != null && KeyWordRaffleTab.getCurrentRaffle().isRunning())
		{
			KeyWordRaffle raffle = KeyWordRaffleTab.getCurrentRaffle();
			if(raffle.getKeyWord().equalsIgnoreCase(message))
				raffle.addEntry(sender.toLowerCase());
		}

		if(QuestionRaffleTab.isRunning())
		{
			if(QuestionRaffleTab.getAnswer().equalsIgnoreCase(message))
			{
				this.sendMessage(this.capitalizeName(sender) + " Has guessed the correct answer! The answer was: " + QuestionRaffleTab.getAnswer());
				QuestionRaffleTab.end();
			}
		}
	}

	/**
	 * Used to listen for private messages.
	 * Currently used to listen for the mod list call back.
	 */
	public void onPrivateMessage(String sender, String login, String hostname, String message)
	{
		try{
			if(message.contains("The moderators"))
			{
				message = message.substring(message.indexOf(":") + 2);
				message += ", " + stream.substring(stream.indexOf("#") + 1);
				mods = message.split(", ");
				ConsoleTab.output(Level.Info, "TurkeyBot has received the list of Mods for this channel!");
			}
		}catch(Exception e){ConsoleTab.output(Level.Error, "An Error Has occured while getting the mods of this channel");};
	}

	/**
	 * Called when someone join the channel that the bot is in.
	 */
	public void onJoin(String channel, String sender, String login, String hostname) 
	{
		if(!viewers.contains(sender))
		{
			viewers.add(sender);
			Gui.reloadTab();
		}
	}

	/**
	 * Called when someone join the channel that the bot is in.
	 */
	public void onPart(String channel, String sender, String login, String hostname) 
	{
		if(viewers.contains(sender))
		{
			viewers.remove(sender);
			Gui.reloadTab();
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
		if(stream != "" || !this.settings.getSettingAsBoolean("isSilent"))
			this.sendMessage(stream, msg);
	}

	/**
	 * Connects the bot to the Twitch servers.
	 */
	public void connectToTwitch()
	{
		ConsoleTab.output(Level.Info, "Connecting to twitch....");
		if(!accountSettingsFile.getSetting("AccountName").replaceAll(" ", "").equals(""))
		{
			try{
				botName = accountSettingsFile.getSetting("AccountName");
				setName(botName);
				connect("irc.twitch.tv", 6667, SecretStuff.oAuth);
				connected = true;
			}catch(Exception e){connected = false; ConsoleTab.output(Level.Error, "Could not connect to Twitch! \n" + e.getMessage());return;}
			ConsoleTab.output(Level.Info, "Connected!");
		}
		else
		{
			ConsoleTab.output(Level.Important, "No account entered for the bot to connect to!");
			ConsoleTab.output(Level.Important, "Please enter this info into: Settings -> Acount Settings");
			ConsoleTab.output(Level.Important, "Then connect the bot to twitch by entering /connect");
		}
		//connectToChannel("turkey2349");
	}

	public void onDisconnect()
	{
		disconnectFromTwitch();
		ConsoleTab.output(Level.Important, "This may be due to incorrect account information! Please check to make sure these are correct!");
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
		ConsoleTab.clearConsole();
		if(!connected)
		{
			ConsoleTab.output(Level.Alert, "You must first connect to twitch with /connect!");
			return;
		}
		if(stream!="")
			disconnectFromChannel();
		stream = "#"+channel.toLowerCase();
		joinChannel(stream);
		ConsoleTab.output(Level.Info, "Connected to " + stream.substring(1) + "'s channel!");

		try
		{
			followersFile = new Followers(this);
			if(settings.getSettingAsBoolean("TrackFollowers"))
				followersFile.initFollowerTracker();
		} catch (IOException e){ConsoleTab.output(Level.Error, "Unable to create the Followers File!");
		} catch (IllegalStateException e){disconnectFromChannel();ConsoleTab.output(Level.Error, "The channel you tried to connect to is invalid!");return;}
		if(!settings.getSetting("AutoCurrencyDelay").equalsIgnoreCase("-1"))
		{
			try
			{
				currencyTrack = new CurrencyThread(Integer.parseInt(settings.getSetting("AutoCurrencyDelay")), Integer.parseInt(settings.getSetting("AutoCurrencyAmount")), this);
				currencyTrack.initCurrencyThread();
			} catch (NumberFormatException e){disconnectFromChannel();ConsoleTab.output(Level.Error, "Your Auto Currency Settings are invalid!");return;}
		}
		if(!settings.getSetting("AnnounceDelay").equals("-1"))
		{
			announcer = new AutoAnnouncement(this);
		}
		if(!settings.getSettingAsBoolean("SilentJoinLeave"))
		{
			if(!botName.equalsIgnoreCase("TurkeyChatBot"))
				this.sendMessage("Hello I am TurkeyBot! errrr I mean, I am " + botName.substring(0, 1).toUpperCase() + botName.substring(1) + "!");
			else
				this.sendMessage("Hello I am TurkeyBot");
		}
		else
			ConsoleTab.output(Level.Alert, "Connected to the channel silently!");
		this.sendMessage(stream, "/mods");
		loadViewers();
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
		if(followersFile != null)
			followersFile.stopFollowerTracker();
		if(currencyTrack != null)
			currencyTrack.stopThread();
		if(announcer != null)
			announcer.stop();
		stream = "";
		mods = new String[0];
		this.viewers.clear();
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
	public String getChannel(boolean includeSymbol)
	{
		if(includeSymbol || stream == "")
			return stream;
		return stream.substring(1);
	}

	/**
	 * Gets the list of users currently in the chat.
	 * @return List of current Viewers.
	 */
	public ArrayList<String> getViewers()
	{
		return viewers;
	}

	/**
	 * Loads all of the viewers for the the current channel that the bot is in.
	 */
	public void loadViewers()
	{
		JsonObject obj = json.parse(HTTPConnect.GetResponsefrom("https://tmi.twitch.tv/group/user/" + this.getChannel(false) + "/chatters")).getAsJsonObject();
		viewers = new ArrayList<String>();
		obj = obj.get("chatters").getAsJsonObject();
		JsonArray mods = obj.get("moderators").getAsJsonArray();
		JsonArray staff = obj.get("staff").getAsJsonArray();
		JsonArray admins = obj.get("admins").getAsJsonArray();
		JsonArray globalMod = obj.get("global_mods").getAsJsonArray();
		JsonArray watchers = obj.get("viewers").getAsJsonArray();
		for(int i = 0; i < mods.size(); i++)
			viewers.add(mods.get(i).getAsString());
		for(int i = 0; i < staff.size(); i++)
			viewers.add(staff.get(i).getAsString());
		for(int i = 0; i < admins.size(); i++)
			viewers.add(admins.get(i).getAsString());
		for(int i = 0; i < globalMod.size(); i++)
			viewers.add(globalMod.get(i).getAsString());
		for(int i = 0; i < watchers.size(); i++)
			viewers.add(watchers.get(i).getAsString());
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
		if(mods == null)
			return false;
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
		for(String user: getViewers())
		{
			if(user.startsWith(un))
			{
				return user;
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
		return new String[]{"User", "Mod", "Streamer"};
	}
}