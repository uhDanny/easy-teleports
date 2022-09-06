package com.duckblade.osrs.easyteleports;

import com.duckblade.osrs.easyteleports.replacers.KharedstMemoirsReplacer;
import com.duckblade.osrs.easyteleports.replacers.PharaohSceptreReplacer;
import com.duckblade.osrs.easyteleports.replacers.Replacer;
import com.duckblade.osrs.easyteleports.replacers.RingOfDuelingReplacer;
import com.duckblade.osrs.easyteleports.replacers.XericsTalismanReplacer;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Provides;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Easy Teleports",
	tags = {"Pharaoh's", "Sceptre", "xeric's", "talisman", "kharedst's", "memoirs"}
)
@Singleton
public class EasyTeleportsPlugin extends Plugin
{

	private static final Map<Integer, EquipmentInventorySlot> ACTION_PARAM_1_TO_EQUIPMENT_SLOT =
		ImmutableMap.<Integer, EquipmentInventorySlot>builder()
			.put(25362447, EquipmentInventorySlot.HEAD)
			.put(25362448, EquipmentInventorySlot.CAPE)
			.put(25362449, EquipmentInventorySlot.AMULET)
			.put(25362457, EquipmentInventorySlot.AMMO)
			.put(25362450, EquipmentInventorySlot.WEAPON)
			.put(25362451, EquipmentInventorySlot.BODY)
			.put(25362452, EquipmentInventorySlot.SHIELD)
			.put(25362453, EquipmentInventorySlot.LEGS)
			.put(25362454, EquipmentInventorySlot.GLOVES)
			.put(25362455, EquipmentInventorySlot.BOOTS)
			.put(25362456, EquipmentInventorySlot.RING)
			.build();

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EasyTeleportsConfig config;

	@Inject
	private PharaohSceptreReplacer pharaohSceptreReplacer;

	@Inject
	private KharedstMemoirsReplacer kharedstMemoirsReplacer;

	@Inject
	private XericsTalismanReplacer xericsTalismanReplacer;

	@Inject
	private RingOfDuelingReplacer ringOfDuelingReplacer;

	private final Set<Replacer> replacers = new HashSet<>();

	private static <T> void applyReplacement(List<TeleportReplacement> replacements, T entry, Function<T, String> getter, BiConsumer<T, String> setter)
	{
		String entryText = null;
		try
		{
			entryText = getter.apply(entry);
			if (Strings.isNullOrEmpty(entryText))
			{
				return;
			}

			for (TeleportReplacement replacement : replacements)
			{
				if (entryText.contains(replacement.getOriginal()))
				{
					String newText = entryText.replace(replacement.getOriginal(), replacement.getReplacement());
					setter.accept(entry, newText);
				}
			}
		}
		catch (Exception e)
		{
			log.error("Failed to replace option [{}] on entry [{}]", entryText, entry.toString());
		}
	}

	@Override
	protected void startUp()
	{
		this.replacers.clear();
		this.replacers.add(pharaohSceptreReplacer);
		this.replacers.add(kharedstMemoirsReplacer);
		this.replacers.add(xericsTalismanReplacer);
		this.replacers.add(ringOfDuelingReplacer);

		propagateConfig();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e)
	{
		if (e.getGroup().equals(EasyTeleportsConfig.CONFIG_GROUP))
		{
			propagateConfig();
		}
	}

	private void propagateConfig()
	{
		this.replacers.forEach(r -> r.onConfigChanged(config));
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e)
	{
		// chatbox dialog
		if (e.getGroupId() == WidgetInfo.DIALOG_OPTION_OPTIONS.getGroupId())
		{
			clientThread.invokeLater(() -> replaceWidgetChildren(WidgetInfo.DIALOG_OPTION_OPTIONS, Replacer::isApplicableToDialog));
		}

		// the scroll thing that xeric's talisman uses
		// annoyingly, the header text and teleport entries share a groupId (187.0 vs 187.3),
		// but don't share a parent with that same groupId, their parent is 164.16
		if (e.getGroupId() == WidgetID.ADVENTURE_LOG_ID)
		{
			clientThread.invokeLater(() ->
			{
				Widget advLogHeader = getAdventureLogHeader();
				replaceWidgetChildren(WidgetID.ADVENTURE_LOG_ID, 3, (r, w) -> r.isApplicableToAdventureLog(advLogHeader));
			});
		}
	}

	private void replaceWidgetChildren(WidgetInfo widgetInfo, BiPredicate<Replacer, Widget> filterSelector)
	{
		replaceWidgetChildren(widgetInfo.getGroupId(), widgetInfo.getChildId(), filterSelector);
	}

	private void replaceWidgetChildren(int groupId, int entriesChildId, BiPredicate<Replacer, Widget> filterSelector)
	{
		Widget root = client.getWidget(groupId, entriesChildId);
		if (root == null)
		{
			return;
		}

		Widget[] children = root.getChildren();
		if (children == null)
		{
			return;
		}

		List<TeleportReplacement> applicableReplacements = getApplicableReplacements(r -> filterSelector.test(r, root));
		for (Widget child : children)
		{
			applyReplacement(applicableReplacements, child, Widget::getText, Widget::setText);
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded e)
	{
		EquipmentInventorySlot equipmentSlot = ACTION_PARAM_1_TO_EQUIPMENT_SLOT.get(e.getActionParam1());
		if (equipmentSlot != null)
		{
			List<TeleportReplacement> applicableReplacements = getApplicableReplacements(r -> r.getEquipmentSlot() == equipmentSlot);
			applyReplacement(applicableReplacements, e.getMenuEntry(), MenuEntry::getOption, MenuEntry::setOption);
		}
	}

	private List<TeleportReplacement> getApplicableReplacements(Predicate<Replacer> filter)
	{
		return replacers.stream()
			.filter(Replacer::isEnabled)
			.filter(filter)
			.flatMap(r -> r.getReplacements().stream())
			.collect(Collectors.toList());
	}

	private Widget getAdventureLogHeader()
	{
		Widget adventureLogRoot = client.getWidget(WidgetInfo.ADVENTURE_LOG);
		if (adventureLogRoot == null)
		{
			return null;
		}

		Widget[] children = adventureLogRoot.getChildren();
		if (children == null || children.length < 2)
		{
			return null;
		}

		return children[1];
	}

	@Provides
	public EasyTeleportsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EasyTeleportsConfig.class);
	}

}
