package org.openstreetmap.josm.plugins.turnlanestagging;

import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsData;

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
//        JMenu menu = new JMenu("Turn lanes tagging");
//        menu.add(new JMenuItem(new AddTagAction()));
//        menu.addSeparator();
//        PresetsData presetsData = new PresetsData();
//        List<BRoad> listpresets = presetsData.dataPreset();
        Main.main.menu.editMenu.add(new AddTagAction());
//        for (int i = 0; i < listpresets.size(); i++) {
//            menu.add(new AddPresetAction(listpresets.get(i).getNumLanes(), listpresets.get(i).getTagturns(), (i + 1)));
//        }
//        Main.main.menu.editMenu.add(menu);
    }
}
