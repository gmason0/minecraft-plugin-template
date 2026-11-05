package io.github.gmason0.example;

import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin
{

	@Override
	public void onEnable()
	{

		suggestPaper();
	}

	private void suggestPaper()
	{
		if (isPaper())
		{
			return;
		}

		getLogger().warning(getName() + " recommends using Paper.");
	}

	private boolean isPaper()
	{
		try
		{
			Class.forName("io.papermc.paper.ServerBuildInfo");
			return true;
		}
		catch (ClassNotFoundException e)
		{
			return false;
		}
	}
}
