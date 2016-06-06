package org.openstreetmap.josm.plugins.turnlanestagging;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

/**
 *
 * @author ruben
 */
public class TurnLanesTaggingPlugin extends Plugin {

    public TurnLanesTaggingPlugin(PluginInformation info) {
        super(info);
        menu();
    }

    public void menu() {
        Main.main.menu.editMenu.add(new AddTagAction());
    }
}
