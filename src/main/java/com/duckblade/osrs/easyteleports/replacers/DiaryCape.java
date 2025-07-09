package com.duckblade.osrs.easyteleports.replacers;

import com.duckblade.osrs.easyteleports.EasyTeleportsConfig;
import com.duckblade.osrs.easyteleports.TeleportReplacement;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;

public class DiaryCape implements Replacer
{

	private static final String ADVENTURE_LOG_HEADER = "Diary Masters";

	private final List<TeleportReplacement> replacements = new ArrayList<>(5);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsConfig config)
	{
		this.enabled = config.enableDiaryCape();

		replacements.clear();
		replacements.add(new TeleportReplacement("Ardougne: Two-pints", config.replacementArdougne()));
		replacements.add(new TeleportReplacement("Desert: Jarr", config.replacementDesert()));
		replacements.add(new TeleportReplacement("Falador: Sir Rebral", config.replacementFalador()));
		replacements.add(new TeleportReplacement("Fremennik: Thorodin", config.replacementFremennik()));
		replacements.add(new TeleportReplacement("Kandarin: Flax keeper", config.replacementKandarin()));
		replacements.add(new TeleportReplacement("Karamja: Pirate Jackie the Fruit", config.replacementKaramjaJackie()));
		replacements.add(new TeleportReplacement("Karamja: Kaleb Paramaya (retired)", config.replacementKaramjaKaleb()));
		replacements.add(new TeleportReplacement("Karamja: Jungle forester (retired)", config.replacementKaramjaForester()));
		replacements.add(new TeleportReplacement("Karamja: TzHaar-Mej (retired)", config.replacementKaramjaTzhaar()));
		replacements.add(new TeleportReplacement("Kourend & Kebos: Elise", config.replacementKourend()));
		replacements.add(new TeleportReplacement("Lumbridge & Draynor: Hatius Cosaintus", config.replacementLumbridge()));
		replacements.add(new TeleportReplacement("Morytania: Le-sabrè", config.replacementMorytania()));
		replacements.add(new TeleportReplacement("Varrock: Toby", config.replacementVarrock()));
		replacements.add(new TeleportReplacement("Wilderness: Lesser Fanatic", config.replacementWilderness()));
		replacements.add(new TeleportReplacement("Western Provinces: Elder Gnome child", config.replacementWestern()));
		replacements.add(new TeleportReplacement("Twiggy O'Korn", config.replacementTwiggy()));
	}

	@Override
	public List<TeleportReplacement> getReplacements()
	{
		return ImmutableList.copyOf(replacements);
	}

	@Override
	public boolean isApplicableToAdventureLog(Widget root)
	{
		return root != null &&
			root.getText() != null &&
			ADVENTURE_LOG_HEADER.equals(root.getText());
	}

	@Override
	public boolean isApplicableToInventory(int itemId)
	{
		return itemId == ItemID.ACHIEVEMENT_DIARY_CAPE || itemId == ItemID.ACHIEVEMENT_DIARY_CAPE_T;
	}
}