package com.duckblade.osrs;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(name = "Easy Pharaoh Sceptre")
@Singleton
public class EasyPharaohSceptre extends Plugin
{

	private static final String SCEPTRE_DIALOG_HEADER = "Where would you like to teleport to?";

	private static final Map<String, String> DEFAULT_DIALOG_REPLACEMENTS = ImmutableMap.<String, String>builder()
		.put("Jalsavrah.", "Pyramid Plunder.")
		.put("Jaleustrophos.", "Agility Pyramid.")
		.put("Jaldraocht.", "Ancients Pyramid.")
		.put("Jaltevas.", "Necropolis.")
		.build();
	
	private final Map<String, String> replacementMap = new HashMap<>();

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EasyPharaohSceptreConfig config;

	@Override
	protected void startUp() throws Exception
	{
		buildReplacementMap();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e)
	{
		if (e.getGroup().equals(EasyPharaohSceptreConfig.CONFIG_GROUP))
		{
			buildReplacementMap();
		}
	}
	
	private void buildReplacementMap()
	{
		replacementMap.clear();
		if (config.customReplacements())
		{
			replacementMap.put("Jalsavrah.", config.replacementJalsavrah());
			replacementMap.put("Jaleustrophos.", config.replacementJaleustrophos());
			replacementMap.put("Jaldraocht.", config.replacementJaldraocht());
			replacementMap.put("Jaltevas.", config.replacementJaltevas());
		}
		else
		{
			replacementMap.putAll(DEFAULT_DIALOG_REPLACEMENTS);
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded e)
	{
		if (e.getGroupId() == WidgetInfo.DIALOG_OPTION_OPTIONS.getGroupId())
		{
			clientThread.invokeLater(this::replaceDialogOptions);
		}
	}

	private void replaceDialogOptions()
	{
		Widget root = client.getWidget(WidgetInfo.DIALOG_OPTION_OPTIONS);
		if (root == null)
		{
			return;
		}

		Widget[] children = root.getChildren();
		if (children == null || children.length < 5)
		{
			return;
		}

		if (!SCEPTRE_DIALOG_HEADER.equals(children[0].getText()))
		{
			return;
		}

		for (Widget child : children)
		{
			// in case custom replacement somehow errors
			try
			{
				String replacement = replacementMap.get(child.getText());
				if (replacement != null)
				{
					child.setText(replacement);
				}
			}
			catch (Exception e)
			{
				log.error("Failed to replace dialog option on widget ID {}.", child.getId(), e);
			}
		}
	}
	
	@Provides
	public EasyPharaohSceptreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EasyPharaohSceptreConfig.class);
	}
	
}
