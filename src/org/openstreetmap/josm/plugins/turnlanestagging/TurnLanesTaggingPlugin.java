// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging;

import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

/**
 *
 * @author ruben
 */
public class TurnLanesTaggingPlugin extends Plugin {
LaunchAction action;

    public TurnLanesTaggingPlugin(PluginInformation info) {
        super(info);
        action = new LaunchAction();
        MainMenu.add(MainApplication.getMenu().dataMenu, action);
    }
}
