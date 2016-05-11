package org.openstreetmap.josm.plugins.turnlanestagging;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.SelectionChangedListener;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import static org.openstreetmap.josm.gui.mappaint.mapcss.ExpressionFactory.Functions.tr;
import org.openstreetmap.josm.tools.Shortcut;

/**
 *
 * @author ruben
 */
public class AddTagAction extends JosmAction implements SelectionChangedListener {
    public AddTagAction() {
        super(tr("Add Turn Lanes Tag"),
                null,
                tr("Add Turn Lanes Tag"),
                Shortcut.registerShortcut("turnlanetag", tr("turnlanetag"),
                        KeyEvent.VK_M, Shortcut.ALT_CTRL), true);
        DataSet.addSelectionListener(this);
        setEnabled(false);
    }

    protected void launchEditor() {
        if (!isEnabled()) {
            return;
        }
        TagEditorDialog dialog = TagEditorDialog.getInstance();
//        dialog.startEditSession();
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        launchEditor();
    }

    @Override
    public void selectionChanged(Collection<? extends OsmPrimitive> newSelection) {
        setEnabled(newSelection != null && newSelection.size() > 0);
    }
}
