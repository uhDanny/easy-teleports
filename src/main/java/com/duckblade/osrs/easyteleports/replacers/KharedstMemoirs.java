package com.duckblade.osrs.easyteleports.replacers;

import com.duckblade.osrs.easyteleports.EasyTeleportsConfig;
import com.duckblade.osrs.easyteleports.TeleportReplacement;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class KharedstMemoirs implements Replacer
{

	private static final String KHAREDST_DIALOG_HEADER = "What would you like to remember?";

	private final List<TeleportReplacement> replacements = new ArrayList<>(5);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsConfig config)
	{
		this.enabled = config.enableKharedstsMemoirs();
		replacements.clear();

		// equipped
		replacements.add(new TeleportReplacement("Lunch by the Lancalliums", config.replacementLancalliums()));
		replacements.add(new TeleportReplacement("The Fisher's Flute", config.replacementFishers()));
		replacements.add(new TeleportReplacement("History and Hearsay", config.replacementHistory()));
		replacements.add(new TeleportReplacement("Jewellery of Jubilation", config.replacementJubilation()));
		replacements.add(new TeleportReplacement("A Dark Disposition", config.replacementDisposition()));

		// dialog
		replacements.add(new TeleportReplacement("'Lunch by the Lancalliums' - Hosidius", config.replacementLancalliums()));
		replacements.add(new TeleportReplacement("'The Fisher's Flute' - Piscarilius", config.replacementFishers()));
		replacements.add(new TeleportReplacement("'History and Hearsay' - Shayzien", config.replacementHistory()));
		replacements.add(new TeleportReplacement("'Jewellery of Jubilation' - Lovakengj", config.replacementJubilation()));
		replacements.add(new TeleportReplacement("'A Dark Disposition' - Arceuus", config.replacementDisposition()));
	}

	@Override
	public List<TeleportReplacement> getReplacements()
	{
		return ImmutableList.copyOf(replacements);
	}

	@Override
	public boolean isApplicableToDialog(Widget root)
	{
		Widget[] children = root.getChildren();
		return children != null &&
			children.length >= 5 &&
			KHAREDST_DIALOG_HEADER.equals(children[0].getText());
	}

	@Override
	public EquipmentInventorySlot getEquipmentSlot()
	{
		return EquipmentInventorySlot.SHIELD;
	}

	@Override
	public boolean isApplicableToInventory(int itemId)
	{
		return itemId == ItemID.KHAREDSTS_MEMOIRS || itemId == ItemID.BOOK_OF_THE_DEAD;
	}
}