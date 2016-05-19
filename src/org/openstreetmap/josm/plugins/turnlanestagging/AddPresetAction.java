/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.turnlanestagging;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.command.ChangePropertyCommand;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.SelectionChangedListener;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import static org.openstreetmap.josm.gui.mappaint.mapcss.ExpressionFactory.Functions.tr;
import static org.openstreetmap.josm.tools.I18n.trn;
import org.openstreetmap.josm.tools.Shortcut;

/**
 *
 * @author ruben
 */
public class AddPresetAction extends JosmAction implements SelectionChangedListener {

    public AddPresetAction(String turnslanes, Integer num) {
        super(tr(turnslanes),
                null,
                tr(turnslanes),
                Shortcut.registerShortcut("turnlanetag:" + turnslanes, tr("turnlanetag:" + turnslanes), num, Shortcut.ALT_CTRL_SHIFT), true);
        DataSet.addSelectionListener(this);
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        addTurnLanesTag();
        Main.main.undoRedo.add(new SequenceCommand(tr("revert tags"), addTurnLanesTag()));
    }

    protected Command addTurnLanesTag() {
        List<Command> commands = new ArrayList<>();
        LinkedList<Way> ways = new LinkedList<>(Main.main.getCurrentDataSet().getSelectedWays());
        if (ways.size() == 1) {
            commands.add(new ChangePropertyCommand(ways, "lanes", "3"));
            commands.add(new ChangePropertyCommand(ways, "turn:lanes", "right|left|right"));
        }
        if (!commands.isEmpty()) {
            String title1 = trn("Pasting {0} tag", "Pasting {0} tags", 2, 2);
            String title2 = trn("to {0} primitive", "to {0} primtives", 2, 2);
            return new SequenceCommand(
                    title1 + " " + title2,
                    commands
            );
        }
        return null;
    }

    @Override
    public void selectionChanged(Collection<? extends OsmPrimitive> newSelection) {
        setEnabled(newSelection != null && newSelection.size() == 1 && isRoad());
    }

    public boolean isRoad() {
        Collection<OsmPrimitive> selection = Main.main.getCurrentDataSet().getSelected();
        for (OsmPrimitive element : selection) {
            for (String key : element.keySet()) {
                if (key.equals("highway")) {
                    return true;
                }
            }
        }
        return false;
    }
}
