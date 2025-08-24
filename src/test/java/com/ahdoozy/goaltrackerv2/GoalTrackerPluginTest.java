package com.ahdoozy.goaltrackerv2;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GoalTrackerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GoalTrackerV2Plugin.class);
		RuneLite.main(args);
	}
}