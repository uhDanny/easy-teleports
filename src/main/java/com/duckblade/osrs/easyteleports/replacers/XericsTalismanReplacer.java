package com.duckblade.osrs.easyteleports.replacers;

import com.duckblade.osrs.easyteleports.EasyTeleportsConfig;
import static com.duckblade.osrs.easyteleports.EasyTeleportsConfig.POSITION_FLAGS;
import static com.duckblade.osrs.easyteleports.EasyTeleportsConfig.POSITION_XERICS_TALISMAN;
import static com.duckblade.osrs.easyteleports.EasyTeleportsConfig.SECTION_ENABLE_FLAGS;
import static com.duckblade.osrs.easyteleports.EasyTeleportsConfig.SECTION_XERICS_TALISMAN;
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
import net.runelite.client.config.ConfigItem;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class XericsTalismanReplacer implements Replacer
{

	private static final String TALISMAN_ADVENTURE_LOG_HEADER_PREFIX = "The talisman has";

	private final List<TeleportReplacement> replacements = new ArrayList<>(5);

	@Getter(onMethod = @__(@Override))
	private boolean enabled = false;

	@Override
	public void onConfigChanged(EasyTeleportsConfig config)
	{
		this.enabled = config.enableXericsTalisman();

		replacements.clear();
		replacements.add(new TeleportReplacement("Xeric's Lookout", config.replacementLookout()));
		replacements.add(new TeleportReplacement("Xeric's Glade", config.replacementGlade()));
		replacements.add(new TeleportReplacement("Xeric's Inferno", config.replacementInferno()));
		replacements.add(new TeleportReplacement("Xeric's Heart", config.replacementHeart()));
		replacements.add(new TeleportReplacement("Xeric's Honour", config.replacementHonour()));
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
			root.getText().startsWith(TALISMAN_ADVENTURE_LOG_HEADER_PREFIX);
	}

	@Override
	public EquipmentInventorySlot getEquipmentSlot()
	{
		return EquipmentInventorySlot.AMULET;
	}
}
