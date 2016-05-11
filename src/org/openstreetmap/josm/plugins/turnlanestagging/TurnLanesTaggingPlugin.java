package org.openstreetmap.josm.plugins.turnlanestagging;

import javax.swing.JMenuItem;
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
         Main.main.menu.editMenu.add(new JMenuItem(new AddTagAction()));
    }
}



