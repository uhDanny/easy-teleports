package com.duckblade.osrs.easyteleports;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(EasyTeleportsConfig.CONFIG_GROUP)
public interface EasyTeleportsConfig extends Config
{

	String CONFIG_GROUP = "easypharaohsceptre";
	int POSITION_FLAGS = 100;
	int POSITION_PHARAOHS_SCEPTRE = POSITION_FLAGS + 100;
	int POSITION_KHAREDSTS_MEMOIRS = POSITION_PHARAOHS_SCEPTRE + 100;
	int POSITION_XERICS_TALISMAN = POSITION_KHAREDSTS_MEMOIRS + 100;
	int POSITION_RING_OF_DUELING = POSITION_XERICS_TALISMAN + 100;

	@ConfigSection(
		name = "Toggles",
		description = "Turn teleport replacements on or off for specific items.",
		position = POSITION_FLAGS
	)
	String SECTION_ENABLE_FLAGS = "enableFlags";

	@ConfigItem(
		section = SECTION_ENABLE_FLAGS,
		keyName = "enablePharaohSceptre",
		name = "Pharaoh's sceptre",
		description = "Replace teleport entries on the Pharaoh's sceptre with new names.",
		position = POSITION_FLAGS + (POSITION_PHARAOHS_SCEPTRE / 100)
	)
	default boolean enablePharaohSceptre()
	{
		return true;
	}

	@ConfigItem(
		section = SECTION_ENABLE_FLAGS,
		keyName = "enableXericsTalisman",
		name = "Xeric's talisman",
		description = "Replace teleport entries on the Xeric's talisman with new names.",
		position = POSITION_FLAGS + (POSITION_XERICS_TALISMAN / 100)
	)
	default boolean enableXericsTalisman()
	{
		return false;
	}

	@ConfigItem(
		section = SECTION_ENABLE_FLAGS,
		keyName = "enableKharedstsMemoirs",
		name = "Kharedst's memoirs",
		description = "Replace teleport entries on the Kharedst's memoirs / Book of the dead with new names.",
		position = POSITION_FLAGS + (POSITION_KHAREDSTS_MEMOIRS / 100)
	)
	default boolean enableKharedstsMemoirs()
	{
		return false;
	}

	@ConfigItem(
		section = SECTION_ENABLE_FLAGS,
		keyName = "enableRingOfDueling",
		name = "Ring of dueling",
		description = "Replace teleport entries on the Ring of Dueling with new names.",
		position = POSITION_FLAGS + (POSITION_RING_OF_DUELING / 100)
	)
	default boolean enableRingOfDueling()
	{
		return false;
	}

	@ConfigSection(
		name = "Pharaoh's sceptre",
		description = "Replacement text for the Pharaoh's sceptre teleport locations.",
		position = 200,
		closedByDefault = true
	)
	String SECTION_PHARAOHS_SCEPTRE = "sectionCustomReplacements"; // legacy config name

	@ConfigItem(
		keyName = "replacementJalsavrah",
		name = "Jalsavrah",
		description = "Replace Jalsavrah",
		section = SECTION_PHARAOHS_SCEPTRE,
		position = POSITION_PHARAOHS_SCEPTRE + 1
	)
	default String replacementJalsavrah()
	{
		return "Pyramid Plunder";
	}

	@ConfigItem(
		keyName = "replacementJaleustrophos",
		name = "Jaleustrophos",
		description = "Replace Jaleustrophos",
		section = SECTION_PHARAOHS_SCEPTRE,
		position = POSITION_PHARAOHS_SCEPTRE + 2
	)
	default String replacementJaleustrophos()
	{
		return "Agility Pyramid";
	}

	@ConfigItem(
		keyName = "replacementJaldraocht",
		name = "Jaldraocht",
		description = "Replace Jaldraocht",
		section = SECTION_PHARAOHS_SCEPTRE,
		position = POSITION_PHARAOHS_SCEPTRE + 3
	)
	default String replacementJaldraocht()
	{
		return "Ancients Pyramid";
	}

	@ConfigItem(
		keyName = "replacementJaltevas",
		name = "Jaltevas",
		description = "Replace Jaltevas",
		section = SECTION_PHARAOHS_SCEPTRE,
		position = POSITION_PHARAOHS_SCEPTRE + 4
	)
	default String replacementJaltevas()
	{
		return "Necropolis";
	}

	@ConfigSection(
		name = "Kharedst's memoirs",
		description = "Replacement text for the Kharedst's memoirs teleport locations.",
		position = POSITION_KHAREDSTS_MEMOIRS,
		closedByDefault = true
	)
	String SECTION_KHAREDSTS_MEMOIRS = "sectionKharedstsMemoirs";

	@ConfigItem(
		keyName = "replacementLancalliums",
		name = "Lunch by the Lancalliums",
		description = "Replace Lunch by the Lancalliums",
		section = SECTION_KHAREDSTS_MEMOIRS,
		position = POSITION_KHAREDSTS_MEMOIRS + 1
	)
	default String replacementLancalliums()
	{
		return "<col=00ff00>Hosidius";
	}

	@ConfigItem(
		keyName = "replacementFishers",
		name = "The Fisher's Flute",
		description = "Replace The Fisher's Flute",
		section = SECTION_KHAREDSTS_MEMOIRS,
		position = POSITION_KHAREDSTS_MEMOIRS + 2
	)
	default String replacementFishers()
	{
		return "<col=00ffff>Port Piscarilius";
	}

	@ConfigItem(
		keyName = "replacementHistory",
		name = "History and Hearsay",
		description = "Replace History and Hearsay",
		section = SECTION_KHAREDSTS_MEMOIRS,
		position = POSITION_KHAREDSTS_MEMOIRS + 3
	)
	default String replacementHistory()
	{
		return "<col=ff0000>Shayzien";
	}

	@ConfigItem(
		keyName = "replacementJubilation",
		name = "Jewellery of Jubilation",
		description = "Replace Jewellery of Jubilation",
		section = SECTION_KHAREDSTS_MEMOIRS,
		position = POSITION_KHAREDSTS_MEMOIRS + 4
	)
	default String replacementJubilation()
	{
		return "<col=ff8800>Lovakengj";
	}

	@ConfigItem(
		keyName = "replacementDisposition",
		name = "A Dark Disposition",
		description = "Replace A Dark Disposition",
		section = SECTION_KHAREDSTS_MEMOIRS,
		position = POSITION_KHAREDSTS_MEMOIRS + 5
	)
	default String replacementDisposition()
	{
		return "<col=8800ff>Arceuus";
	}

	@ConfigSection(
		name = "Xeric's talisman",
		description = "Replacement text for the Xeric's talisman teleport locations.",
		position = POSITION_XERICS_TALISMAN,
		closedByDefault = true
	)
	String SECTION_XERICS_TALISMAN = "sectionXericsTalisman";

	@ConfigItem(
		keyName = "replacementLookout",
		name = "Xeric's Look-out",
		description = "Replace Xeric's Look-out",
		section = SECTION_XERICS_TALISMAN,
		position = POSITION_XERICS_TALISMAN + 1
	)
	default String replacementLookout()
	{
		return "Shayzien";
	}

	@ConfigItem(
		keyName = "replacementGlade",
		name = "Xeric's Glade",
		description = "Replace Xeric's Glade",
		section = SECTION_XERICS_TALISMAN,
		position = POSITION_XERICS_TALISMAN + 2
	)
	default String replacementGlade()
	{
		return "Hosidius";
	}

	@ConfigItem(
		keyName = "replacementInferno",
		name = "Xeric's Inferno",
		description = "Replace Xeric's Inferno",
		section = SECTION_XERICS_TALISMAN,
		position = POSITION_XERICS_TALISMAN + 3
	)
	default String replacementInferno()
	{
		return "Lovakengj";
	}

	@ConfigItem(
		keyName = "replacementHeart",
		name = "Xeric's Heart",
		description = "Replace Xeric's Heart",
		section = SECTION_XERICS_TALISMAN,
		position = POSITION_XERICS_TALISMAN + 4
	)
	default String replacementHeart()
	{
		return "Kourend Castle";
	}

	@ConfigItem(
		keyName = "replacementHonour",
		name = "Xeric's Honour",
		description = "Replace Xeric's Honour",
		section = SECTION_XERICS_TALISMAN,
		position = POSITION_XERICS_TALISMAN + 5
	)
	default String replacementHonour()
	{
		return "Chambers of Xeric";
	}

	@ConfigSection(
		name = "Ring of dueling",
		description = "Replacement text for the ring of dueling teleport locations.",
		position = POSITION_RING_OF_DUELING,
		closedByDefault = true
	)
	String SECTION_RING_OF_DUELING = "sectionRingOfDueling";

	@ConfigItem(
		keyName = "replacementPvPArena",
		name = "PvP Arena",
		description = "Replace PvP Arena",
		section = SECTION_RING_OF_DUELING,
		position = POSITION_RING_OF_DUELING + 1
	)
	default String replacementPvPArena()
	{
		return "Duel Arena";
	}

	@ConfigItem(
		keyName = "replacementCastleWars",
		name = "Castle Wars",
		description = "Replace Castle Wars",
		section = SECTION_RING_OF_DUELING,
		position = POSITION_RING_OF_DUELING + 2
	)
	default String replacementCastleWars()
	{
		return "Castle Wars";
	}

	@ConfigItem(
		keyName = "replacementFeroxEnclave",
		name = "Ferox Enclave",
		description = "Replace Ferox Enclave",
		section = SECTION_RING_OF_DUELING,
		position = POSITION_RING_OF_DUELING + 3
	)
	default String replacementFeroxEnclave()
	{
		return "Ferox Enclave";
	}
}
