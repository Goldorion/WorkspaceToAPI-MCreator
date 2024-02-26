package net.goldorion.workspace2api;

import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.workspace.MCreatorLoadedEvent;
import net.mcreator.ui.init.L10N;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

@SuppressWarnings("FieldCanBeLocal")
public class Workspace2API extends JavaPlugin {
    public static final Logger LOG = LogManager.getLogger("Workspace2API");

    public Workspace2API(Plugin plugin) {
        super(plugin);

        addListener(MCreatorLoadedEvent.class, event -> {
            JMenu menu = new JMenu(L10N.t("workspace2api.menu.main"));

            JMenuItem menuItem = new JMenuItem(new GeneratePluginAction(event.getMCreator().actionRegistry));
            menu.add(menuItem);

            event.getMCreator().getMainMenuBar().add(menu);
        });

        LOG.info("Workspace2API loaded");
    }
}