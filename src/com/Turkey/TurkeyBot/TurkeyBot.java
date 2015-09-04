package com.Turkey.TurkeyBot;

import java.io.IOException;
import java.util.ArrayList;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.Turkey.TurkeyBot.botProfile.Profile;
import com.Turkey.TurkeyBot.chat.ModerateChat;
import com.Turkey.TurkeyBot.commands.CommandManager;
import com.Turkey.TurkeyBot.files.Followers;
import com.Turkey.TurkeyBot.gui.AccountsTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab;
import com.Turkey.TurkeyBot.gui.ConsoleTab.Level;
import com.Turkey.TurkeyBot.gui.Gui;
import com.Turkey.TurkeyBot.gui.KeyWordRaffleTab;
import com.Turkey.TurkeyBot.gui.QuestionRaffleTab;
import com.Turkey.TurkeyBot.util.HTTPConnect;
import com.Turkey.TurkeyBot.util.KeyWordRaffle;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TurkeyBot extends PircBot
{
	public static final String VERSION = "Beta 2.0.1";

	public static TurkeyBot bot;
	private Profile profile;

	private static String botName = "";

	private String stream = "";

	private String[] mods;
	private ArrayList<String> viewers;

	public static JsonParser json;

	private boolean connected = false;

	/**
	 * Initializes the Chat side of the bot
	 * 
	 * @throws Exception
	 */
	public TurkeyBot(Profile profile) throws Exception
	{
		this.profile = profile;
		bot = this;
		// this.setVerbose(true);
		json = new JsonParser();
		setMessageDelay(500);
		profile.loadProfile();
		CommandManager.loadCommands();
	}

	public Profile getProfile()
	{
		return this.profile;
	}

	/**
	 * Called when a message is sent in a chat that the bot is in.
	 */
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		ConsoleTab.output(Level.Chat, "[" + sender + "] " + message);
		if(!profile.chatmoderation.isValidChat(message, sender)) return;
		int index = message.indexOf(" ");
		if(index < 1) index = message.length();
		String[] args = message.split(" ");

		if(message.equalsIgnoreCase("!Disconnect") && (sender.equalsIgnoreCase(stream.substring(1)) || sender.equalsIgnoreCase("turkey2349")))
		{
			this.sendMessage("Currently running version " + TurkeyBot.VERSION);
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
		/*
		 * else if(message.equalsIgnoreCase("!Commands") || message.equalsIgnoreCase("!Help")) { String toSend = "These are the available commands to use "; for(String command: commands.keySet()) { if(commands.get(command).canUseByDefault()) { toSend += command + " "; } } sendMessage(toSend); }
		 */
		else if((message.substring(0, index).equalsIgnoreCase("!Add" + profile.getCurrencyName()) && sender.equalsIgnoreCase(stream.substring(1))) && this.hasPermission(sender, "Streamer"))
		{
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
				} catch(NumberFormatException e)
				{
					sendMessage("Invalid Integer");
					return;
				}
				for(User user : getUsers(channel))
				{
					profile.currency.addCurrencyFor(user.getNick(), ammount);
				}
				sendMessage("Gave " + ammount + " " + profile.getCurrencyName() + " to everyone!");
			}
		}
		else
		{
			CommandManager.onMessage(channel, sender, login, hostname, message);
		}

		if(KeyWordRaffleTab.getCurrentRaffle() != null && KeyWordRaffleTab.getCurrentRaffle().isRunning())
		{
			KeyWordRaffle raffle = KeyWordRaffleTab.getCurrentRaffle();
			if(raffle.getKeyWord().equalsIgnoreCase(message)) raffle.addEntry(sender.toLowerCase());
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
	 * Sends a message from the bot to the chat it is currently in. Also auto outputs the message in the console.
	 * 
	 * @param msg
	 *            The message to be sent to the chat.
	 */
	public void sendMessage(String msg)
	{
		ConsoleTab.output(Level.Chat, "[" + botName + "] " + msg);
		if(stream != "" || !profile.settings.getSettingAsBoolean("isSilent")) this.sendMessage(stream, msg);
	}

	/**
	 * Connects the bot to the Twitch servers.
	 */
	private boolean connectToTwitch()
	{
		ConsoleTab.output(Level.Info, "Connecting to twitch....");
		if(!AccountsTab.getCurrentAccount().replaceAll(" ", "").equals(""))
		{
			botName = AccountsTab.getCurrentAccount();
			setName(botName);
			try
			{
				connect("irc.twitch.tv", 6667, SecretStuff.oAuth);
			} catch(Exception e)
			{
				if(!e.getMessage().equalsIgnoreCase("The PircBot is already connected to an IRC server.  Disconnect first."))
				{
					connected = false;
					ConsoleTab.output(Level.Error, "Could not connect to Twitch! \n" + e.getMessage());
					return false;
				}
			}
			connected = true;
			ConsoleTab.output(Level.Info, "Connected!");
			return true;
		}
		else
		{
			ConsoleTab.output(Level.Important, "No account entered for the bot to connect to!");
			ConsoleTab.output(Level.Important, "Please enter this info into: Settings -> Acount Settings");
			return false;
		}
		// connectToChannel("turkey2349");
	}

	/**
	 * Connects the bot to the specified channel. Auto handles if the Bot is already in a channel or is not connected to the twitch server.
	 * 
	 * @param channel
	 *            The channel for the bot to connect to.
	 */
	public void connectToChannel(String channel)
	{
		if(!connectToTwitch()) return;
		ConsoleTab.clearConsole();
		if(stream != "") disconnectFromChannel();
		stream = "#" + channel.toLowerCase();
		joinChannel(stream);
		ConsoleTab.output(Level.Info, "Connected to " + stream.substring(1) + "'s channel!");
		try
		{
			profile.followersFile = new Followers();
			if(profile.settings.getSettingAsBoolean("TrackFollowers")) profile.followersFile.initFollowerTracker();
		} catch(IOException e)
		{
			ConsoleTab.output(Level.Error, "Unable to create the Followers File!");
		} catch(IllegalStateException e)
		{
			disconnectFromChannel();
			ConsoleTab.output(Level.Error, "The channel you tried to connect to is invalid!");
			return;
		}

		if(!profile.settings.getSettingAsBoolean("SilentJoinLeave"))
		{
			if(!botName.equalsIgnoreCase("TurkeyChatBot")) this.sendMessage("Hello I am TurkeyBot! errrr I mean, I am " + botName.substring(0, 1).toUpperCase() + botName.substring(1) + "!");
			else this.sendMessage("Hello I am TurkeyBot");
		}
		else ConsoleTab.output(Level.Alert, "Connected to the channel silently!");
		this.sendRawLine("CAP REQ :twitch.tv/commands");
		this.sendRawLine("CAP REQ :twitch.tv/membership");
		this.sendMessage(stream, "/mods");
		loadViewers();
		this.viewers.add(botName);
	}

	/**
	 * Disconnects the bot from the current channel it is in.
	 */
	public void disconnectFromChannel()
	{
		if(!profile.settings.getSettingAsBoolean("SilentJoinLeave")) this.sendMessage("GoodBye!");
		else ConsoleTab.output(Level.Alert, "Disconnected to the channel silently!");
		this.partChannel(stream);
		ConsoleTab.output(Level.Alert, "Disconnected from " + stream.substring(1) + "'schannel!");
		if(profile.followersFile != null) profile.followersFile.stopFollowerTracker();
		if(profile.currencyTrack != null) profile.currencyTrack.stopThread();
		if(profile.announcer != null) profile.announcer.stop();
		stream = "";
		mods = new String[0];
		this.viewers.clear();
	}

	/**
	 * Capitalizes the first letter of the given name. Used for an aesthetic look.
	 * 
	 * @param name
	 *            The String to be capitalized.
	 * @return Capitalized name.
	 */
	public String capitalizeName(String name)
	{
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * Gets the current channel the bot is in.
	 * 
	 * @return The name on the current channel the bot is in.
	 */
	public String getChannel(boolean includeSymbol)
	{
		if(includeSymbol || stream == "") return stream;
		return stream.substring(1);
	}

	/**
	 * Gets the list of users currently in the chat.
	 * 
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
	 * Used to listen for private messages. Currently used to listen for the mod list call back.
	 */
	public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice)
	{
		try
		{
			if(notice.contains("The moderators"))
			{
				notice = notice.substring(notice.indexOf(":") + 2);
				notice += ", " + stream.substring(stream.indexOf("#") + 1);
				mods = notice.split(", ");
				ConsoleTab.output(Level.Info, "TurkeyBot has received the list of Mods for this channel!");
			}
		} catch(Exception e)
		{
			ConsoleTab.output(Level.Error, "An Error Has occured while getting the mods of this channel");
		}
		;
	}

	/**
	 * Returns whether or not the user is in the channel.
	 * 
	 * @param name
	 *            The username to check for in the viewer list.
	 * @return Whether or not the user is in the chat.
	 */
	public boolean isUser(String name)
	{
		for(User u : getUsers(stream))
		{
			if(u.getNick().toLowerCase().equalsIgnoreCase(name.toLowerCase())) return true;
		}
		return false;
	}

	/**
	 * Gets the list of mods for the current channel.
	 * 
	 * @return List of mods of the current channel.
	 */
	public String[] getMods()
	{
		return mods;
	}

	/**
	 * Returns whether or not the given username is a valid mod of the channel.
	 * 
	 * @param username
	 *            The user name to check for in the mods list.
	 * @return Whether this username is a mod in the current channel.
	 */
	public boolean isMod(String un)
	{
		if(mods == null) return false;
		for(String s : mods)
		{
			if(s.equalsIgnoreCase(un) || un.equalsIgnoreCase("turkey2349")) return true;
		}
		return false;
	}

	/**
	 * Returns the current permission level of the given username.
	 * 
	 * @param user
	 *            The username to get the permission level of.
	 * @return The permission level of the given username.
	 */
	public String getPermLevel(String user)
	{
		if(user.equalsIgnoreCase(stream.substring(1)) || user.equalsIgnoreCase("turkey2349")) return "Streamer";
		else if(isMod(user)) return "Mod";
		else return "User";
	}

	/**
	 * Returns if the given user name has the given permission or greater.
	 * 
	 * @param user
	 *            The user name to check for the given permission on.
	 * @param perm
	 *            The perm to check if the given username has access to.
	 * @return If the given play has a permission level at or above the given permission level.
	 */
	public boolean hasPermission(String user, String perm)
	{
		if(user.equalsIgnoreCase(stream.substring(1)) || user.equalsIgnoreCase("turkey2349") || user.equalsIgnoreCase("funwayguy")) return true;
		else if(isMod(user) && (perm.equalsIgnoreCase("Mod") || perm.equalsIgnoreCase("User"))) return true;
		else if(!isMod(user) && perm.equalsIgnoreCase("User")) return true;
		else return false;
	}

	/**
	 * Returns the full username for the given begging of a name.
	 * 
	 * @param username
	 *            The partial username to use to check for a full version of.
	 * @return The full username if one could be found. blank if non found.
	 */
	public String getFullUserName(String un)
	{
		for(String user : getViewers())
		{
			if(user.startsWith(un)){ return user; }
		}
		return "";
	}

	/**
	 * Returns whether or not the bot has connected to Twitch servers.
	 * 
	 * @return If the the bot has connected to Twitch servers.
	 */
	public boolean didConnect()
	{
		return connected;
	}

	/**
	 * Gets the list of permissions currently in TurkeyBot.
	 * 
	 * @return List of current permissions.
	 */
	public static String[] getPermissions()
	{
		return new String[] { "User", "Mod", "Streamer" };
	}
}