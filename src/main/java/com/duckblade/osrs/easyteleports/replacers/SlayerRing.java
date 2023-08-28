package com.duckblade.osrs.easyteleports.replacers;

import com.duckblade.osrs.easyteleports.EasyTeleportsConfig;
import com.duckblade.osrs.easyteleports.TeleportReplacement;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.runelite.api.widgets.Widget;

public class SlayerRing implements Replacer
{

	private static final String SLAYER_RING_DIALOG_HEADER = "Select an Option";

	private final List<TeleportReplacement> replacements = new ArrayList<>(5);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsConfig config)
	{
		this.enabled = config.enableSlayerRing();

		replacements.clear();
		replacements.add(new TeleportReplacement("Teleport to the Stronghold Slayer Cave", config.replacementSlayerStronghold()));
		replacements.add(new TeleportReplacement("Teleport to the Morytania Slayer Tower", config.replacementSlayerTower()));
		replacements.add(new TeleportReplacement("Teleport to the Rellekka Slayer Caves", config.replacementSlayerRellekka()));
		replacements.add(new TeleportReplacement("Teleport to Tarn's Lair", config.replacementTarns()));
		replacements.add(new TeleportReplacement("Teleport to Dark Beasts", config.replacementDarkBeasts()));
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
			SLAYER_RING_DIALOG_HEADER.equals(children[0].getText());
	}

}