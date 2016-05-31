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
import javax.swing.AbstractAction;
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
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.TagEditor;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.ac.KeyValuePair;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsTableModel;
import org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes.BuildTurnLanes;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsData;
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

    static public final Dimension PREFERRED_SIZE = new Dimension(900, 600);

    private TagEditor tagEditor = null;
    private BuildTurnLanes buildTurnLanes = null;
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
        splitPane.setDividerLocation(350);
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
        buildTurnLanes = new BuildTurnLanes();
        buildTurnLanes.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BuildTurnLanes.ROADCHANGED)) {
                    BRoad b = (BRoad) evt.getNewValue();
                    //Clear
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes", null));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes", null));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes:forward", null));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes:forward", null));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes:both_ways", null));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes:both_ways", null));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes:backward", null));
                    tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes:backward", null));

                    if (b.getName().equals("Unidirectional")) {
                        tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes", b.getTagturns()));
                        tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes", String.valueOf(b.getNumLanes())));
                    } else {
                        if (!b.getLanesA().getLanes().isEmpty()) {
                            tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes:forward", b.getLanesA().getTagturns()));
                            tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes:forward", String.valueOf(b.getLanesA().getLanes().size())));
                        }
                        if (!b.getLanesB().getLanes().isEmpty()) {
                            tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes:both_ways", b.getLanesB().getTagturns()));
                            tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes:both_ways", String.valueOf(b.getLanesB().getLanes().size())));
                        }
                        if (!b.getLanesC().getLanes().isEmpty()) {
                            tagEditor.getModel().applyKeyValuePair(new KeyValuePair("turn:lanes:backward", b.getLanesC().getTagturns()));
                            tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes:backward", String.valueOf(b.getLanesC().getLanes().size())));
                        }
                        tagEditor.getModel().applyKeyValuePair(new KeyValuePair("lanes", String.valueOf(b.getNumLanesBidirectional())));
                    }
                    tagEditor.repaint();
                }
            }
        });
        return buildTurnLanes;
    }

    public PresetsTableModel getPressetTableModel() {
        return buildTurnLanes.getModel();
    }

    public void startEditSession() {
        tagEditor.getModel().clearAppliedPresets();
        tagEditor.getModel().initFromJOSMSelection();
        getModel().ensureOneTag();
        setRoadProperties();
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

    public void setRoadProperties() {
        //Set the selection Roads
        PresetsData presetsData = new PresetsData();
        BRoad bRoad = new BRoad("Unidirectional", new ArrayList<BLane>());
        Collection<OsmPrimitive> selection = Main.main.getCurrentDataSet().getSelected();
        int numLanes = 0;

        for (OsmPrimitive element : selection) {
            for (String key : element.keySet()) {

                Util.print(element.hasKey("turn:lanes:forward"));
                //Unidirectional
                if (key.equals("turn:lanes")) {
                    bRoad.setLanes(element.get(key));
                    bRoad.setName("Unidirectional");
                } else if (key.equals("lanes") && Util.isInt(element.get(key)) && !element.hasKey("lanes")) {
                    numLanes = Integer.valueOf(element.get(key));
                    //Aqui continuamos para set por numeor de lanes
//                    bRoad.setListLines(presetsData.);
                } //Bidirectional
                //on turn lanes
                else if (key.equals("turn:lanes:forward")) {
                    bRoad.getLanesA().setStringLanes("forward", element.get(key));
                    bRoad.getLanesA().setType("forward");
                    bRoad.setName("Bidirectional");
                } else if (key.equals("turn:lanes:both_ways")) {
                    bRoad.getLanesB().setStringLanes("both_ways", element.get(key));
                    bRoad.getLanesB().setType("both_ways");
                    bRoad.setName("Bidirectional");
                } else if (key.equals("turn:lanes:backward")) {
                    bRoad.getLanesC().setStringLanes("backward", element.get(key));
                    bRoad.getLanesC().setType("backward");
                    bRoad.setName("Bidirectional");
                } //in case the road has just lanes
                else if (key.equals("lanes:forward") && Util.isInt(element.get(key)) && !element.hasKey("turn:lanes:forward")) {
                    bRoad.setLanesA(presetsData.defaultLanes("forward", Integer.valueOf(element.get(key))));
                    bRoad.getLanesA().setType("forward");
                    bRoad.setName("Bidirectional");
                } else if (key.equals("lanes:both_ways") && Util.isInt(element.get(key)) && !element.hasKey("turn:lanes:both_ways")) {
                    bRoad.setLanesB(presetsData.defaultLanes("both_ways", Integer.valueOf(element.get(key))));
                    bRoad.getLanesB().setType("both_ways");
                    bRoad.setName("Bidirectional");
                } else if (key.equals("lanes:backward") && Util.isInt(element.get(key)) && !element.hasKey("turn:lanes:backward")) {
                    bRoad.setLanesC(presetsData.defaultLanes("backward", Integer.valueOf(element.get(key))));
                    bRoad.getLanesC().setType("backward");
                    bRoad.setName("Bidirectional");
                }
            }
        }

        if (bRoad.getName().equals("Unidirectional")) {
            if (bRoad.getNumLanes() > 0) {
                buildTurnLanes.setLanesByRoadUnidirectional(bRoad);
                if (numLanes == 0) {
                    Util.notification(tr("Tag lanes is missing"));
                } else if (bRoad.getNumLanes() != numLanes) {
                    Util.notification(tr("Number of lanes doesn't match with turn lanes"));
                }
            } else {
                buildTurnLanes.startDefaultUnidirectional();
            }
        } else {

            if (bRoad.getLanesA().getLanes().size() > 0 || bRoad.getLanesB().getLanes().size() > 0 || bRoad.getLanesC().getLanes().size() > 0) {
                buildTurnLanes.setLanesByRoadBidirectional(bRoad);
            } else {
                buildTurnLanes.startDefaultBidirectional();
            }
        }
    }
}
