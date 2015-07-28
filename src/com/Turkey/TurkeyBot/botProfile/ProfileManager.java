package com.Turkey.TurkeyBot.botProfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager
{
	public static ProfileManager instance = new ProfileManager();

	private List<Profile> profiles = new ArrayList<Profile>();

	public void loadProfiles()
	{
		File file = new File("C:" + File.separator + "TurkeyBot");

		if(!file.exists())
		{
			file.getParentFile().mkdirs();
			try
			{
				file.createNewFile();
				File deafult = new File("C:" + File.separator + "TurkeyBot" + File.separator + "Default");
				deafult.createNewFile();
			} catch(IOException e)
			{
			}
		}

		for(File f : file.listFiles())
		{
			if(f.isDirectory())
			{
				Profile prof = new Profile(f.getName());
				profiles.add(prof);
			}
		}
	}

	public List<Profile> getProfiles()
	{
		return this.profiles;
	}

	public String[] getProfileNames()
	{
		String[] names = new String[this.profiles.size()];
		for(int i = 0; i < this.profiles.size(); i++)
			names[i] = this.profiles.get(i).getProfileName();
		return names;
	}

	public Profile getProfileFromName(String name)
	{
		for(Profile p : this.profiles)
			if(p.getProfileName().equalsIgnoreCase(name))
				return p;
		return null;
	}
}