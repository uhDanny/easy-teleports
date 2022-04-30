package com.duckblade.osrs;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EasyPharaohSceptreTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EasyPharaohSceptre.class);
		RuneLite.main(args);
	}
}