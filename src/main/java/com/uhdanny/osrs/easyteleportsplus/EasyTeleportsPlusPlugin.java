package com.uhdanny.osrs.easyteleportsplus;

import com.uhdanny.osrs.easyteleportsplus.replacers.*;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
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
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Easy Teleports Plus",
	description = "Replaces teleport menu entries with more recognizable names for various items.",
	tags = {"Pharaoh's", "Sceptre", "xeric's", "talisman", "kharedst's", "memoirs", "teleports", "Pendant of Ates"}
)
@Singleton
public class EasyTeleportsPlusPlugin extends Plugin
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

	private static final int ACTION_PARAM_1_INVENTORY = 9764864;

	private static final int GROUP_ID_JEWELLERY_BOX = 590;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EasyTeleportsPlusConfig config;

	@Inject
	private Set<Replacer> replacers;

	@Override
	public void configure(Binder binder)
	{
		Multibinder<Replacer> replacers = Multibinder.newSetBinder(binder, Replacer.class);
		replacers.addBinding().to(DiaryCape.class);
		replacers.addBinding().to(DrakansMedallion.class);
		replacers.addBinding().to(KharedstMemoirs.class);
		replacers.addBinding().to(PharaohSceptre.class);
		replacers.addBinding().to(RingOfDueling.class);
		replacers.addBinding().to(RingOfShadows.class);
		replacers.addBinding().to(SlayerRing.class);
		replacers.addBinding().to(XericsTalisman.class);
		replacers.addBinding().to(NecklaceOfPassage.class);
		replacers.addBinding().to(PendantOfAtes.class);
	}

	@Override
	protected void startUp()
	{
		propagateConfig();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e)
	{
		if (e.getGroup().equals(EasyTeleportsPlusConfig.CONFIG_GROUP))
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
		if (e.getGroupId() == InterfaceID.DIALOG_OPTION)
		{
			//InterfaceID.DIALOG_OPTION
			Widget chatbox = client.getWidget(ComponentID.DIALOG_OPTION_OPTIONS);
			clientThread.invokeLater(() -> replaceWidgetChildren(chatbox, Replacer::isApplicableToDialog, config.enableShadowedText()));
		}

		// the scroll thing that xeric's talisman uses
		// annoyingly, the header text and teleport entries share a groupId (187.0 vs 187.3),
		// but don't share a parent with that same groupId, their parent is 164.16
		if (e.getGroupId() == InterfaceID.ADVENTURE_LOG)
		{
			clientThread.invokeLater(() ->
			{
				Widget advLogHeader = getAdventureLogHeader();
				replaceWidgetChildren(InterfaceID.ADVENTURE_LOG, 3, (r, w) -> r.isApplicableToAdventureLog(advLogHeader));
			});
			return;
		}

		// jewellery box
		if (e.getGroupId() == GROUP_ID_JEWELLERY_BOX)
		{
			clientThread.invokeLater(() ->
			{
				Widget jewelleryBoxRoot = client.getWidget(GROUP_ID_JEWELLERY_BOX, 0);
				if (jewelleryBoxRoot == null)
				{
					return;
				}

				for (int i = 0; i < 6; i++)
				{
					replaceWidgetChildren(GROUP_ID_JEWELLERY_BOX, 2 + i, (r, w) -> r.isApplicableToJewelleryBox());
				}
			});
		}
	}

	private void replaceWidgetChildren(int groupId, int entriesChildId, BiPredicate<Replacer, Widget> filterSelector)
	{
		Widget root = client.getWidget(groupId, entriesChildId);
		if (root == null)
		{
			return;
		}

		replaceWidgetChildren(root, filterSelector);
	}

	private void replaceWidgetChildren(Widget root, BiPredicate<Replacer, Widget> filterSelector)
	{
		replaceWidgetChildren(root, filterSelector, false);
	}

	private void replaceWidgetChildren(Widget root, BiPredicate<Replacer, Widget> filterSelector, boolean shadowedText)
	{
		Widget[] children = root.getChildren();
		if (children == null)
		{
			return;
		}

		List<TeleportReplacement> applicableReplacements = getApplicableReplacements(r -> filterSelector.test(r, root));
		for (Widget child : children)
		{
			applyReplacement(applicableReplacements, child, Widget::getText, Widget::setText, shadowedText);
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded e)
	{
		if (e.getActionParam1() == ACTION_PARAM_1_INVENTORY)
		{
			List<TeleportReplacement> applicableReplacements = getApplicableReplacements(r -> r.isApplicableToInventory(e.getMenuEntry().getItemId()));
			clientThread.invokeLater(() -> applyReplacement(applicableReplacements, e.getMenuEntry(), MenuEntry::getOption, MenuEntry::setOption));
			return;
		}

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
		Widget adventureLogRoot = client.getWidget(ComponentID.ADVENTURE_LOG_CONTAINER);
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
	public EasyTeleportsPlusConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EasyTeleportsPlusConfig.class);
	}

	private static <T> void applyReplacement(List<TeleportReplacement> replacements, T entry, Function<T, String> getter, BiConsumer<T, String> setter)
	{
		applyReplacement(replacements, entry, getter, setter, false);
	}

	private static <T> void applyReplacement(List<TeleportReplacement> replacements, T entry, Function<T, String> getter, BiConsumer<T, String> setter, boolean shadowedText)
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
					if ((replacement.getReplacement().contains("</col>") || replacement.getReplacement().contains("<col=")) && shadowedText && entry instanceof Widget)
					{
						Widget wEntry = ((Widget) entry);
						wEntry.setTextShadowed(true);
						wEntry.revalidate();
					}
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

}