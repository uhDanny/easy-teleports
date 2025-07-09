package com.uhdanny.osrs.easyteleportsplus.replacers;

import com.uhdanny.osrs.easyteleportsplus.EasyTeleportsPlusConfig;
import com.uhdanny.osrs.easyteleportsplus.TeleportReplacement;
import java.util.List;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.widgets.Widget;

public interface Replacer
{

	void onConfigChanged(EasyTeleportsPlusConfig config);

	boolean isEnabled();

	List<TeleportReplacement> getReplacements();

	default boolean isApplicableToDialog(Widget root)
	{
		return false;
	}

	default boolean isApplicableToAdventureLog(Widget root)
	{
		return false;
	}

	default boolean isApplicableToJewelleryBox()
	{
		return false;
	}

	default boolean isApplicableToInventory(int itemId)
	{
		return false;
	}

	default EquipmentInventorySlot getEquipmentSlot()
	{
		return null;
	}

}