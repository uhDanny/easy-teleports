package com.uhdanny.osrs;

import com.uhdanny.osrs.easyteleportsplus.EasyTeleportsPlusPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EasyTeleportsPlusPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EasyTeleportsPlusPlugin.class);
		RuneLite.main(args);
	}
}