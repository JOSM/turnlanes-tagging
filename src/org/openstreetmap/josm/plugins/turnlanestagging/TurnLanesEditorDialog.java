// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Tag;
import org.openstreetmap.josm.gui.ExtendedDialog;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.gui.tagging.TagEditorModel;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes.BuildTurnLanes;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.TagEditor;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsData;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsTableModel;
import org.openstreetmap.josm.plugins.turnlanestagging.util.Util;
import org.openstreetmap.josm.tools.ImageProvider;

public class TurnLanesEditorDialog extends ExtendedDialog {

    // Unique instance
    private static TurnLanesEditorDialog instance = null;
    Map<String, String> tags = new HashMap<>();

    //constructor
    protected TurnLanesEditorDialog() {
        super(MainApplication.getMainFrame(), "", null, false, false);
        build();
    }

    public static TurnLanesEditorDialog getInstance() {
        if (instance == null) {
            instance = new TurnLanesEditorDialog();
        }
        return instance;
    }

    public static final Dimension PREFERRED_SIZE = new Dimension(750, 700);
    public static final Dimension MIN_SIZE = new Dimension(630, 600);
    private TagEditor tagEditor = null;
    private BuildTurnLanes buildTurnLanes = null;
    private JButton jbOk = null;
    private OKAction okAction = null;
    private CancelAction cancelAction = null;

    // Last Editions
    List<BRoad> lastEdits = new ArrayList<>();
    OsmPrimitive saveSelected = null;

    protected void build() {
        //Parameters for Dialog
        getContentPane().setLayout(new BorderLayout());
        setModal(false);
        setSize(PREFERRED_SIZE);
        setTitle(tr("Turn Lanes Editor"));
        setLocation(90, 90);
        pack();
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

        setMinimumSize(MIN_SIZE);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(380);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(buildButtonRowPanel(), BorderLayout.SOUTH);
        getRootPane().registerKeyboardAction(cancelAction, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickButton");
        this.getRootPane().getActionMap().put("clickButton", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                jbOk.doClick();
            }
        });
    }

    //Build Buttons
    protected JPanel buildButtonRowPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jbOk = new JButton(okAction = new OKAction());
        pnl.add(jbOk);
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
                    addTagOnRoad((BRoad) evt.getNewValue());
                    jbOk.requestFocus();
                    jbOk.setSelected(true);
                }
            }
        });
        return buildTurnLanes;
    }

    public PresetsTableModel getPressetTableModel() {
        return buildTurnLanes.getModel();
    }

    public void startEditSession() {
        if (waySelected() != null) {
            tags = waySelected().getKeys();
        }
        tagEditor.getModel().clearAppliedPresets();
        tagEditor.getModel().initFromJOSMSelection();
        getModel().ensureOneTag();
        setRoadProperties();
    }

    //Buton Actions
    class CancelAction extends AbstractAction {

        CancelAction() {
            putValue(NAME, tr("Cancel"));
            putValue(SMALL_ICON, ImageProvider.get("cancel"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            putValue(SHORT_DESCRIPTION, tr("Abort tag editing and close dialog"));
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (waySelected() != null) {
                waySelected().setKeys(tags);
                waySelected().setModified(false);
            }
            setVisible(false);
        }
    }

    class OKAction extends AbstractAction implements PropertyChangeListener {

        OKAction() {
            putValue(NAME, tr("OK"));
            putValue(SMALL_ICON, ImageProvider.get("ok"));
            putValue(SHORT_DESCRIPTION, tr("Apply edited tags and close dialog"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl ENTER"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            run();
            // Add on table
            buildTurnLanes.addLastEditInTable();
            buildTurnLanes.clearSelection();
        }

        public void run() {
            tagEditor.stopEditing();
            setVisible(false);
            waySelected().setKeys(tags);
            tagEditor.getModel().updateJOSMSelection();
            MainApplication.getMainFrame().repaint(); // repaint all
            saveSelected = null;
        }

        @Override
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
        //Collection<OsmPrimitive> selection = Main.getLayerManager().getEditDataSet().getSelected();
        // for (OsmPrimitive element : selection) {
        if (waySelected().hasDirectionKeys()) {
            return true;
        }
        //  }
        return false;
    }

    public void setRoadProperties() {
        //Set the selection Roads
        PresetsData presetsData = new PresetsData();
        BRoad bRoad = new BRoad();
        //set as unidirectional as first

        OsmPrimitive element = waySelected();
        if (element.hasKey("oneway")) {
            if (element.get("oneway").equals("yes")) {
                bRoad.setName("Unidirectional");
            } else if (element.get("oneway").equals("no")) {
                bRoad.setName("Bidirectional");
            }
        } else {
            bRoad.setName("Bidirectional");
        }

        for (String key : element.keySet()) {
            if (key.equals("turn:lanes")) {
                bRoad.getLanesUnid().setStringLanes("unid", element.get(key));
                bRoad.setName("Unidirectional");
                if (element.hasKey("lanes") && Util.isInt(element.get("lanes"))
                        && Integer.valueOf(element.get("lanes")) != bRoad.getLanesUnid().getLanes().size()) {
                    new Notification(tr(" The number of lanes has fixed according number of turns")).show();
                }
            } else if (key.equals("lanes") && Util.isInt(element.get(key))
                    && !element.hasKey("turn:lanes") && element.hasDirectionKeys()) {
                bRoad = presetsData.defaultRoadUnidirectional(Integer.valueOf(element.get(key)));
                bRoad.setName("Unidirectional");
            } else if (key.equals("lanes") && Util.isInt(element.get(key)) && !element.hasKey("turn:lanes")
                    && !(element.hasKey("turn:lanes:forward") || element.hasKey("turn:lanes:both_ways") ||
                         element.hasKey("turn:lanes:backward") || element.hasKey("lanes:forward") ||
                         element.hasKey("lanes:both_ways") || element.hasKey("lanes:backward"))) {
                bRoad = presetsData.defaultRoadUnidirectional(Integer.valueOf(element.get(key)));
                bRoad.setName("Unidirectional");
            } else if (key.equals("turn:lanes:forward")) {
                if (Util.isRightHandTraffic()) {
                    bRoad.getLanesC().setStringLanes("forward", element.get(key));
                    bRoad.getLanesC().setType("forward");
                    bRoad.setName("Bidirectional");
                    if (element.hasKey("lanes:forward") && Util.isInt(element.get("lanes:forward"))
                            && Integer.valueOf(element.get("lanes:forward")) != bRoad.getLanesC().getLanes().size()) {
                        new Notification(tr(" The number of lanes:forward has fixed according number of turns")).show();
                    }
                } else {
                    bRoad.getLanesA().setStringLanes("forward", element.get(key));
                    bRoad.getLanesA().setType("forward");
                    bRoad.setName("Bidirectional");
                    if (element.hasKey("lanes:forward") && Util.isInt(element.get("lanes:forward"))
                            && Integer.valueOf(element.get("lanes:forward")) != bRoad.getLanesA().getLanes().size()) {
                        new Notification(tr(" The number of lanes:forward has fixed according number of turns")).show();
                    }
                }
            } else if (key.equals("turn:lanes:both_ways")) {
                bRoad.getLanesB().setStringLanes("both_ways", element.get(key));
                bRoad.getLanesB().setType("both_ways");
                bRoad.setName("Bidirectional");
                if (element.hasKey("lanes:both_ways") && Util.isInt(element.get("lanes:both_ways"))
                        && Integer.valueOf(element.get("lanes:both_ways")) != bRoad.getLanesB().getLanes().size()) {
                    new Notification(tr(" The number of lanes:both_ways has fixed according number of turns")).show();
                }
            } else if (key.equals("turn:lanes:backward")) {
                if (Util.isRightHandTraffic()) {
                    bRoad.getLanesA().setStringLanes("backward", element.get(key));
                    bRoad.getLanesA().setType("backward");
                    bRoad.setName("Bidirectional");
                    if (element.hasKey("lanes:backward") && Util.isInt(element.get("lanes:backward"))
                            && Integer.valueOf(element.get("lanes:backward")) != bRoad.getLanesA().getLanes().size()) {
                        new Notification(tr(" The number of lanes:backward has fixed according number of turns")).show();
                    }
                } else {
                    bRoad.getLanesC().setStringLanes("backward", element.get(key));
                    bRoad.getLanesC().setType("backward");
                    bRoad.setName("Bidirectional");
                    if (element.hasKey("lanes:backward") && Util.isInt(element.get("lanes:backward"))
                            && Integer.valueOf(element.get("lanes:backward")) != bRoad.getLanesC().getLanes().size()) {
                        new Notification(tr(" The number of lanes:backward has fixed according number of turns")).show();
                    }
                }
            } else if (key.equals("lanes:forward") && Util.isInt(element.get(key)) && !element.hasKey("turn:lanes:forward")) {
                if (Util.isRightHandTraffic()) {
                    bRoad.setLanesC(presetsData.defaultLanes("forward", Integer.valueOf(element.get(key))));
                    bRoad.getLanesC().setType("forward");
                    bRoad.setName("Bidirectional");
                } else {
                    bRoad.setLanesA(presetsData.defaultLanes("forward", Integer.valueOf(element.get(key))));
                    bRoad.getLanesA().setType("forward");
                    bRoad.setName("Bidirectional");
                }
            } else if (key.equals("lanes:both_ways") && Util.isInt(element.get(key)) && !element.hasKey("turn:lanes:both_ways")) {
                bRoad.setLanesB(presetsData.defaultLanes("both_ways", Integer.valueOf(element.get(key))));
                bRoad.getLanesB().setType("both_ways");
                bRoad.setName("Bidirectional");
            } else if (key.equals("lanes:backward") && Util.isInt(element.get(key)) && !element.hasKey("turn:lanes:backward")) {
                if (Util.isRightHandTraffic()) {
                    bRoad.setLanesA(presetsData.defaultLanes("backward", Integer.valueOf(element.get(key))));
                    bRoad.getLanesA().setType("backward");
                    bRoad.setName("Bidirectional");
                } else {
                    bRoad.setLanesC(presetsData.defaultLanes("backward", Integer.valueOf(element.get(key))));
                    bRoad.getLanesC().setType("backward");
                    bRoad.setName("Bidirectional");
                }
            }

            //Notifications
            if (key.equals("oneway") && element.get(key).equals("-1")) {
                new Notification(tr("check the right direction of the way")).show();
            }
        }

        lastEdits = buildTurnLanes.getListLastEditsRoads();
        if (bRoad.getName().equals("Unidirectional")) {
            if (bRoad.getLanesUnid().getLanes().size() > 0) {
                buildTurnLanes.setLanesByRoadUnidirectional(bRoad);
            } else if (!lastEdits.isEmpty() && lastEdits.get(0).getName().equals("Unidirectional")) {
                buildTurnLanes.setLastEdit();
            } else {
                buildTurnLanes.startDefaultUnidirectional();
            }
        } else if (bRoad.getLanesA().getLanes().size() > 0 ||
                   bRoad.getLanesB().getLanes().size() > 0 ||
                   bRoad.getLanesC().getLanes().size() > 0) {
            if (bRoad.getLanesA().getLanes().isEmpty()) {
                bRoad.setLanesA(presetsData.defaultLanes("forward", 1));
            }
            if (bRoad.getLanesC().getLanes().isEmpty()) {
                bRoad.setLanesC(presetsData.defaultLanes("backward", 1));
            }
            buildTurnLanes.setLanesByRoadBidirectional(bRoad);
        } else if (!lastEdits.isEmpty() && lastEdits.get(0).getName().equals("Bidirectional")) {
            buildTurnLanes.setLastEdit();
        } else {
            buildTurnLanes.startDefaultBidirectional();
        }
    }

    public void addTagOnRoad(BRoad bRoad) {
        //Clear
        tagEditor.getModel().delete("turn:lanes");
        tagEditor.getModel().delete("lanes");
        tagEditor.getModel().delete("turn:lanes:forward");
        tagEditor.getModel().delete("lanes:forward");
        tagEditor.getModel().delete("turn:lanes:both_ways");
        tagEditor.getModel().delete("lanes:both_ways");
        tagEditor.getModel().delete("turn:lanes:backward");
        tagEditor.getModel().delete("lanes:backward");

        if (bRoad.getName().equals("Unidirectional")) {
            if (Util.isEmptyturnlane(bRoad.getLanesUnid().getTagturns())) {
                if (bRoad.isNone()) {
                    tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes",
                            Util.setNoneOnEmpty(bRoad.getLanesUnid().getTagturns())));
                } else {
                    tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes",
                            bRoad.getLanesUnid().getTagturns()));
                }
            }
            tagEditor.getModel().applyKeyValuePair(new Tag("lanes",
                    String.valueOf(bRoad.getLanesUnid().getLanes().size())));
        } else {
            if (!bRoad.getLanesA().getLanes().isEmpty()) {
                if (bRoad.getLanesA().getType().equals("forward")) {
                    if (Util.isEmptyturnlane(bRoad.getLanesA().getTagturns())) {
                        if (bRoad.isNone()) {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:forward",
                                    Util.setNoneOnEmpty(bRoad.getLanesA().getTagturns())));
                        } else {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:forward",
                                    bRoad.getLanesA().getTagturns()));
                        }
                    }
                    tagEditor.getModel().applyKeyValuePair(new Tag("lanes:forward",
                            String.valueOf(bRoad.getLanesA().getLanes().size())));
                } else {
                    if (Util.isEmptyturnlane(bRoad.getLanesA().getTagturns())) {
                        if (bRoad.isNone()) {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:backward",
                                    Util.setNoneOnEmpty(bRoad.getLanesA().getTagturns())));
                        } else {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:backward",
                                    bRoad.getLanesA().getTagturns()));
                        }
                    }
                    tagEditor.getModel().applyKeyValuePair(new Tag("lanes:backward",
                            String.valueOf(bRoad.getLanesA().getLanes().size())));
                }
            }
            if (!bRoad.getLanesB().getLanes().isEmpty()) {
                if (Util.isEmptyturnlane(bRoad.getLanesB().getTagturns())) {
                    if (bRoad.isNone()) {
                        tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:both_ways",
                                Util.setNoneOnEmpty(bRoad.getLanesB().getTagturns())));
                    } else {
                        tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:both_ways",
                                bRoad.getLanesB().getTagturns()));
                    }
                }
                tagEditor.getModel().applyKeyValuePair(new Tag("lanes:both_ways",
                        String.valueOf(bRoad.getLanesB().getLanes().size())));
            }
            if (!bRoad.getLanesC().getLanes().isEmpty()) {
                if (bRoad.getLanesC().getType().equals("backward")) {
                    if (Util.isEmptyturnlane(bRoad.getLanesC().getTagturns())) {
                        if (bRoad.isNone()) {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:backward",
                                    Util.setNoneOnEmpty(bRoad.getLanesC().getTagturns())));
                        } else {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:backward",
                                    bRoad.getLanesC().getTagturns()));
                        }
                    }
                    tagEditor.getModel().applyKeyValuePair(new Tag("lanes:backward",
                            String.valueOf(bRoad.getLanesC().getLanes().size())));
                } else {
                    if (Util.isEmptyturnlane(bRoad.getLanesC().getTagturns())) {
                        if (bRoad.isNone()) {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:forward",
                                    Util.setNoneOnEmpty(bRoad.getLanesC().getTagturns())));
                        } else {
                            tagEditor.getModel().applyKeyValuePair(new Tag("turn:lanes:forward",
                                    bRoad.getLanesC().getTagturns()));
                        }
                    }
                    tagEditor.getModel().applyKeyValuePair(new Tag("lanes:forward",
                            String.valueOf(bRoad.getLanesC().getLanes().size())));
                }
            }
            tagEditor.getModel().applyKeyValuePair(new Tag("lanes",
                    String.valueOf(bRoad.getNumLanesBidirectional())));
        }
        tagEditor.repaint();

        setPreview(waySelected());
    }

    public void setPreview(OsmPrimitive osmps) {
        if (osmps != null) {
            osmps.setKeys(tagEditor.getModel().getTags());
        }
    }

    public void resetPreview(OsmPrimitive osmprs) {
        if (osmprs != null) {
            osmprs.setKeys(tags);
            osmprs.setModified(false);
            saveSelected = null;
        }
    }

    public OsmPrimitive waySelected() {
        if (saveSelected != null && !MainApplication.getLayerManager().getEditDataSet().getSelected().contains(saveSelected)) {
            resetPreview(saveSelected);
        }

        Collection<OsmPrimitive> selection = MainApplication.getLayerManager().getEditDataSet().getSelected();
        for (OsmPrimitive element : selection) {
            saveSelected = element;
            return element;
        }
        return null;
    }

    public void setEnableOK(boolean active) {
        jbOk.setEnabled(active);
    }
}