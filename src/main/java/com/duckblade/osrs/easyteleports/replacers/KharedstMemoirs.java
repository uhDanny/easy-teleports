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

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class KharedstMemoirs implements Replacer
{

	private final List<TeleportReplacement> replacements = new ArrayList<>(5);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsConfig config)
	{
		this.enabled = config.enableKharedstsMemoirs();

		replacements.clear();
		replacements.add(new TeleportReplacement("Lunch by the Lancalliums", config.replacementLancalliums()));
		replacements.add(new TeleportReplacement("The Fisher's Flute", config.replacementFishers()));
		replacements.add(new TeleportReplacement("History and Hearsay", config.replacementHistory()));
		replacements.add(new TeleportReplacement("Jewellery of Jubilation", config.replacementJubilation()));
		replacements.add(new TeleportReplacement("A Dark Disposition", config.replacementDisposition()));
	}

	@Override
	public List<TeleportReplacement> getReplacements()
	{
		return ImmutableList.copyOf(replacements);
	}

	@Override
	public EquipmentInventorySlot getEquipmentSlot()
	{
		return EquipmentInventorySlot.SHIELD;
	}
}