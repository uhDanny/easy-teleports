package com.uhdanny.osrs.easyteleportsplus.replacers;

import com.uhdanny.osrs.easyteleportsplus.EasyTeleportsPlusConfig;
import com.uhdanny.osrs.easyteleportsplus.TeleportReplacement;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemMapping;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PharaohSceptre implements Replacer
{

	private static final String SCEPTRE_DIALOG_HEADER = "Where would you like to teleport to?";

	private final List<TeleportReplacement> replacements = new ArrayList<>(4);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsPlusConfig config)
	{
		this.enabled = config.enablePharaohSceptre();

		replacements.clear();
		replacements.add(new TeleportReplacement("Jalsavrah", config.replacementJalsavrah()));
		replacements.add(new TeleportReplacement("Jaleustrophos", config.replacementJaleustrophos()));
		replacements.add(new TeleportReplacement("Jaldraocht", config.replacementJaldraocht()));
		replacements.add(new TeleportReplacement("Jaltevas", config.replacementJaltevas()));
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
			SCEPTRE_DIALOG_HEADER.equals(children[0].getText());
	}

	@Override
	public EquipmentInventorySlot getEquipmentSlot()
	{
		return EquipmentInventorySlot.WEAPON;
	}

	@Override
	public boolean isApplicableToInventory(int itemId)
	{
		if (itemId == ItemID.PHARAOHS_SCEPTRE_UNCHARGED)
		{
			return false;
		}
		Collection<ItemMapping> itemMappings = ItemMapping.map(itemId);
		return itemMappings != null && itemMappings.contains(ItemMapping.ITEM_PHARAOHS_SCEPTRE);
	}
}