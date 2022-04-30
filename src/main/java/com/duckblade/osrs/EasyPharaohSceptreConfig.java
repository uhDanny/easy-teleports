package com.duckblade.osrs;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(EasyPharaohSceptreConfig.CONFIG_GROUP)
public interface EasyPharaohSceptreConfig extends Config
{
	
	String CONFIG_GROUP = "easypharaohsceptre";
	
	@ConfigItem(
		keyName = "customReplacements",
		name = "Custom Replacements",
		description = "Enable custom replacements for teleports, sourced from the config section below.",
		position = 1
	)
	default boolean customReplacements()
	{
		return false;
	}
	
	@ConfigSection(
		name = "Custom Replacements",
		description = "Custom replacements for teleport locations",
		position = 2,
		closedByDefault = true
	)
	String SECTION_CUSTOM_REPLACEMENTS = "sectionCustomReplacements";
	
	@ConfigItem(
		keyName = "replacementJalsavrah",
		name = "Jalsavrah",
		description = "Replace Jalsavrah",
		section = SECTION_CUSTOM_REPLACEMENTS,
		position = 3
	)
	default String replacementJalsavrah()
	{
		return "Sophanem.";
	}
	
	@ConfigItem(
		keyName = "replacementJaleustrophos",
		name = "Jaleustrophos",
		description = "Replace Jaleustrophos",
		section = SECTION_CUSTOM_REPLACEMENTS,
		position = 4
	)
	default String replacementJaleustrophos()
	{
		return "Agi Pyramid.";
	}
	
	@ConfigItem(
		keyName = "replacementJaldraocht",
		name = "Jaldraocht",
		description = "Replace Jaldraocht",
		section = SECTION_CUSTOM_REPLACEMENTS,
		position = 5
	)
	default String replacementJaldraocht()
	{
		return "Azzanandra's Pyramid.";
	}
	
	@ConfigItem(
		keyName = "replacementJaltevas",
		name = "Jaltevas",
		description = "Replace Jaltevas",
		section = SECTION_CUSTOM_REPLACEMENTS,
		position = 6
	)
	default String replacementJaltevas()
	{
		return "Tombs of Amascut.";
	}

}
