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
import net.runelite.api.widgets.Widget;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RingOfDueling implements Replacer
{

	private static final String RING_OF_DUELING_DIALOGUE_HEADER = "Where would you like to teleport to?";

	private final List<TeleportReplacement> replacements = new ArrayList<>(5);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsConfig config)
	{
		this.enabled = config.enableRingOfDueling();

		replacements.clear();
		replacements.add(new TeleportReplacement("PvP Arena", config.replacementPvPArena()));
		replacements.add(new TeleportReplacement("Al Kharid PvP Arena", config.replacementPvPArena()));
		replacements.add(new TeleportReplacement("Castle Wars", config.replacementCastleWars()));
		replacements.add(new TeleportReplacement("Castle Wars Arena", config.replacementCastleWars()));
		replacements.add(new TeleportReplacement("Ferox Enclave", config.replacementFeroxEnclave()));
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
			RING_OF_DUELING_DIALOGUE_HEADER.equals(children[0].getText());
	}

	@Override
	public boolean isApplicableToJewelleryBox()
	{
		return true;
	}

	@Override
	public EquipmentInventorySlot getEquipmentSlot()
	{
		return EquipmentInventorySlot.RING;
	}
}