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
public class PharaohSceptreReplacer implements Replacer
{

	private static final String SCEPTRE_DIALOG_HEADER = "Where would you like to teleport to?";

	private final List<TeleportReplacement> replacements = new ArrayList<>(4);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsConfig config)
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
}
