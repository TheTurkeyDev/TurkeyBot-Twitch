package com.Turkey.TurkeyBot.botProfile;

import java.io.File;

public class Profile
{
	private String profileName;

	private File profileFolder;

	public Profile(String name)
	{
		this.profileName = name;
		profileFolder = new File("C:" + File.separator + "TurkeyBot" + File.separator + name);
	}

	public String getProfileName()
	{
		return this.profileName;
	}

	public File getProfileFolder()
	{
		return this.profileFolder;
	}
}
