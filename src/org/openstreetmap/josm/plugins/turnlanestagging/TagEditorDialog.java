package org.openstreetmap.josm.plugins.turnlanestagging;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.gui.tagging.TagEditorModel;
import org.openstreetmap.josm.gui.tagging.ac.AutoCompletionManager;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.TagEditor;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.ac.KeyValuePair;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.PresetsTableModel;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.PresetSelector;
import org.openstreetmap.josm.plugins.turnlanestagging.util.Util;
import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ImageProvider;

public class TagEditorDialog extends JDialog {

    // Unique instance      
    static private TagEditorDialog instance = null;

    //constructor
    protected TagEditorDialog() {
        build();
    }

    static public TagEditorDialog getInstance() {
        if (instance == null) {
            instance = new TagEditorDialog();
        }
        return instance;
    }

    static public final Dimension PREFERRED_SIZE = new Dimension(800, 500);

    private TagEditor tagEditor = null;
    private AutoCompletionManager autocomplete = null;
    private PresetSelector presetSelector = null;
    private OKAction okAction = null;
    private CancelAction cancelAction = null;

    protected void build() {
        //Parameters for Dialog
        getContentPane().setLayout(new BorderLayout());
        setModal(true);
        setSize(PREFERRED_SIZE);
        setTitle(tr("Turn Lanes Editor"));

        // Preset Panel
        JPanel pnlPresetGrid = buildPresetGridPanel();
        pnlPresetGrid.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Tag Panel
        JPanel pnlTagGrid = buildTagGridPanel();

        //Split the Preset an dTag panel
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                pnlPresetGrid,
                pnlTagGrid
        );

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(250);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(buildButtonRowPanel(), BorderLayout.SOUTH);
        getRootPane().registerKeyboardAction(cancelAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    //Build Buttons
    protected JPanel buildButtonRowPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER));

        pnl.add(new JButton(okAction = new OKAction()));
        getModel().addPropertyChangeListener(okAction);

        pnl.add(new JButton(cancelAction = new CancelAction()));
        return pnl;
    }

    // Build tag grid
    protected JPanel buildTagGridPanel() {
        tagEditor = new TagEditor();
        return tagEditor;
    }

    public TagEditorModel getModel() {
        return tagEditor.getModel();
    }

    // Build preset grid
    protected JPanel buildPresetGridPanel() {
        presetSelector = new PresetSelector();
        presetSelector.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(presetSelector.jTF_CHANGED)) {
                    BRoad b = (BRoad) evt.getNewValue();
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes", b.getTagturns()));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes", String.valueOf(b.getNumLanes())));
                    //Add oneway =yes if missing on road
                    if (addOneway()) {
                        tagEditor.getModel().applyKeyValuePair(new KeyValuePair("oneway", "yes"));
                    }
                    tagEditor.repaint();
                }

            }
        });

        return presetSelector;

    }

    public PresetsTableModel getPressetTableModel() {
        return presetSelector.getModel();
    }

    public void startEditSession() {
        tagEditor.getModel().clearAppliedPresets();
        tagEditor.getModel().initFromJOSMSelection();
        getModel().ensureOneTag();

        //Set the selection Roads
        BRoad bRoad = new BRoad("selectRoad", new ArrayList<BLine>());
        Collection<OsmPrimitive> selection = Main.main.getCurrentDataSet().getSelected();
        int numLanes = 0;
        for (OsmPrimitive element : selection) {
            for (String key : element.keySet()) {
                if (key.equals("turn:lanes")) {
                    String value = element.get(key);
                    bRoad.setLanes(value);
                    bRoad.setName("selectRoad");
                }
                if (key.equals("lanes") && Util.isInt(element.get(key))) {
                    numLanes = Integer.valueOf(element.get(key));
                }
            }
        }
        if (bRoad.getNumLanes() > 0) {
            presetSelector.lanes(bRoad);
            if (numLanes == 0) {
                Util.notification(tr("Tag lanes is missing"));
            } else if (bRoad.getNumLanes() != numLanes) {
                Util.notification(tr("Number of lanes doesn't match with turn lanes"));
            }
        } else {
            presetSelector.setDefaultLanes();
        }
    }

    //Buton Actions
    class CancelAction extends AbstractAction {

        public CancelAction() {
            putValue(NAME, tr("Cancel"));
            putValue(SMALL_ICON, ImageProvider.get("cancel"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            putValue(SHORT_DESCRIPTION, tr("Abort tag editing and close dialog"));
        }

        public void actionPerformed(ActionEvent arg0) {
            setVisible(false);
        }
    }

    class OKAction extends AbstractAction implements PropertyChangeListener {

        public OKAction() {
            putValue(NAME, tr("OK"));
            putValue(SMALL_ICON, ImageProvider.get("ok"));
            putValue(SHORT_DESCRIPTION, tr("Apply edited tags and close dialog"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl ENTER"));
        }

        public void actionPerformed(ActionEvent e) {
            run();
        }

        public void run() {
            tagEditor.stopEditing();
            setVisible(false);
            tagEditor.getModel().updateJOSMSelection();
            DataSet ds = Main.main.getCurrentDataSet();
            ds.fireSelectionChanged();
            Main.parent.repaint(); // repaint all
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (!evt.getPropertyName().equals(TagEditorModel.PROP_DIRTY)) {
                return;
            }
            if (!evt.getNewValue().getClass().equals(Boolean.class)) {
                return;
            }
            setEnabled(true);
        }

    }

    public boolean addOneway() {
        Collection<OsmPrimitive> selection = Main.main.getCurrentDataSet().getSelected();
        for (OsmPrimitive element : selection) {
            for (String key : element.keySet()) {
                if (key.equals("oneway")) {
                    return false;
                }
            }
        }
        return true;
    }

}
