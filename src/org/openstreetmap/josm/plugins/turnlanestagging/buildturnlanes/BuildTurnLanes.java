// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.buildturnlanes;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ImageRenderer;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsData;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsTable;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.PresetsTableModel;
import org.openstreetmap.josm.plugins.turnlanestagging.util.Util;

/**
 *
 * @author ruben
 */
public class BuildTurnLanes extends JPanel {

    //Table
    private JScrollPane scrollPane = null;
    private PresetsTable presetsTable = null;
    private PresetsTableModel presetsTableModel = null;
    // Table for last edits
    private JScrollPane lastEditsScrollPane = null;
    private PresetsTable lastEditsTable = null;
    private PresetsTableModel lastEditsTableModel = null;
    // Config
    private static JCheckBox jcNone = null;

    //Main Content
    private JPanel pnlBuildTurnLanes = null;

    //Content directional options
    private JPanel pnlDirectionalOptions = null;
    private ButtonGroup btdirectional = null;
    private JPanel pnlUnidirectionalOptions = null;
    private JPanel pnlBidirectionalOptions = null;
    private JRadioButton jrbUnidirectional = null;
    private JRadioButton jrbBidirectional = null;

    //Content for directional
    private JPanel pnlContentDirectional = null;

    // Jtext for add the changes
    public static JTextField jtfChangeRoad = new JTextField();

    // Data to fill  the  table
    static List<BRoad> listPresetRoads = null;
    PresetsData presetsData = new PresetsData();
    private static List<BRoad> listLastEditsRoads = new ArrayList<>();

    //Road
    public static BRoad bRoad = new BRoad();
    // Bidirectional
    public TurnSelectionBidirectional turnSelectionBidirectional = null;

    //Unidirection
    public TurnSelectionUnidirectional turnSelectionUnidirectional = null;

    // Event Changed
    public static final String ROADCHANGED = "RoadChanged";

    GridBagConstraints gbc = new GridBagConstraints();

    //Constructor
    public BuildTurnLanes() {
        turnSelectionUnidirectional = new TurnSelectionUnidirectional();
        turnSelectionUnidirectional.addPropertyChangeListener(new LinesChangeUnidirectionalListener());
        turnSelectionBidirectional = new TurnSelectionBidirectional();
        turnSelectionBidirectional.addPropertyChangeListener(new LinesChangeBidirectionalListener());
        init();
    }

    // Build Table and add Actions
    protected JScrollPane buildPresetTable() {
        listPresetRoads = new ArrayList<>(presetsData.dataPreset());
        presetsTableModel = new PresetsTableModel(listPresetRoads, false);
        //print on table
        presetsTable = new PresetsTable(presetsTableModel);
        presetsTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
        presetsTable.setSelectionBackground(new Color(145, 195, 242));
        scrollPane = new JScrollPane(presetsTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        Dimension d = scrollPane.getViewport().getExtentSize();
                        presetsTable.adjustColumnWidth(d.width);
                    }
                }
        );
        // add the  click listener
        presetsTable.addMouseListener(new ClickAdapter());
        return scrollPane;
    }

    // Build Table and add Actions
    protected JScrollPane buildLastEditsTable() {
        List<BRoad> lastEditsRoads = new ArrayList<>();
        lastEditsTableModel = new PresetsTableModel(lastEditsRoads, false);
        //print on table
        lastEditsTable = new PresetsTable(lastEditsTableModel);
        lastEditsTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
        lastEditsTable.setSelectionBackground(new Color(252, 215, 121));
        lastEditsScrollPane = new JScrollPane(lastEditsTable);
        lastEditsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        lastEditsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        lastEditsScrollPane.addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        Dimension d = lastEditsScrollPane.getViewport().getExtentSize();
                        lastEditsTable.adjustColumnWidth(d.width);
                    }
                }
        );
        // add the  click listener
        lastEditsTable.addMouseListener(new ClickAdapterLastEditsTable());
        return lastEditsScrollPane;
    }

    private class ClickAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            BRoad br = new BRoad();
            if (e.getClickCount() == 1) {
                int rowNum = presetsTable.rowAtPoint(e.getPoint());
                if (listPresetRoads.get(rowNum).getName().equals("Unidirectional")) {
                    setLanesByRoadUnidirectional((BRoad) Util.clone(listPresetRoads.get(rowNum)));
                } else {
                    br = (BRoad) Util.clone(listPresetRoads.get(rowNum));
                    if (Util.isRightHandTraffic()) {
                        br.setLanesA(br.getLanesC());
                        br.setLanesC(((BRoad) Util.clone(listPresetRoads.get(rowNum))).getLanesA());
                    }
                    setLanesByRoadBidirectional(br);
                }
            }
        }
    }

    private class ClickAdapterLastEditsTable extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                int rowNum = lastEditsTable.rowAtPoint(e.getPoint());
                if (listLastEditsRoads.get(rowNum).getName().equals("Unidirectional")) {
                    setLanesByRoadUnidirectional((BRoad) Util.clone(listLastEditsRoads.get(rowNum)));
                } else {
                    setLanesByRoadBidirectional((BRoad) Util.clone(listLastEditsRoads.get(rowNum)));
                }
            }
        }
    }

    public PresetsTableModel getModel() {
        return (PresetsTableModel) presetsTable.getModel();
    }

    // Build Radio Butons and add Actions
    protected JPanel buildDirectionalOptions() {
        //Directional options
        pnlDirectionalOptions = new JPanel(new GridBagLayout());

        pnlUnidirectionalOptions = new JPanel(new BorderLayout());
        pnlBidirectionalOptions = new JPanel(new BorderLayout());
        pnlUnidirectionalOptions.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        pnlBidirectionalOptions.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        //pnlDirectionalOptions.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        btdirectional = new ButtonGroup();
        jrbUnidirectional = new JRadioButton(tr("Unidirectional road"));
        jrbUnidirectional.setToolTipText(tr("Build unidirectional road"));
        jrbBidirectional = new JRadioButton(tr("Bidirectional road"));
        jrbBidirectional.setToolTipText(tr("Build bidirectional road"));

        btdirectional.add(jrbUnidirectional);
        btdirectional.add(jrbBidirectional);
        pnlUnidirectionalOptions.add(jrbUnidirectional, BorderLayout.CENTER);
        pnlBidirectionalOptions.add(jrbBidirectional, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlDirectionalOptions.add(pnlUnidirectionalOptions, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlDirectionalOptions.add(pnlBidirectionalOptions, gbc);
        //None Option
        JPanel jpNoneOpt = new JPanel(new BorderLayout());
        jpNoneOpt.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        jcNone = new JCheckBox(tr("Use \"none\""));
        jcNone.setToolTipText(tr("use \"none\" instead of empty values"));
        jcNone.addActionListener(jcNoneActionListener);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jpNoneOpt.add(jcNone);
        pnlDirectionalOptions.add(jpNoneOpt, gbc);
        jrbUnidirectional.addActionListener(actionListenerUnidirectional);
        jrbBidirectional.addActionListener(actionListenerBidirectional);
        return pnlDirectionalOptions;
    }

    //Actions
    ActionListener actionListenerUnidirectional = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            startDefaultUnidirectional();
        }
    };

    ActionListener actionListenerBidirectional = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            startDefaultBidirectional();
        }
    };

    // Build the GUI for turn lanes
    protected void init() {
        setLayout(new BorderLayout());
        //Table
        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.addTab(tr("Preset turn lanes"), buildPresetTable());
        jTabbedPane.addTab(tr("Recently turn lanes edits"), buildLastEditsTable());
        add(jTabbedPane, BorderLayout.CENTER);
        //turnlanes builder
        pnlBuildTurnLanes = new JPanel(new BorderLayout());
        pnlBuildTurnLanes.add(buildDirectionalOptions(), BorderLayout.NORTH);
        pnlContentDirectional = new JPanel();
        pnlBuildTurnLanes.add(pnlContentDirectional, BorderLayout.CENTER);
        //comment
        //pnlBuildTurnLanes.add(jtfChangeRoad, BorderLayout.SOUTH);
        add(pnlBuildTurnLanes, BorderLayout.SOUTH);
        //road change event
        jtfChangeRoad.getDocument().addDocumentListener(new SetRoadChangeRoadListener());
        //Start Default
        jrbUnidirectional.setSelected(true);
        startDefaultUnidirectional();
    }

    public static class LinesChangeUnidirectionalListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(TurnSelectionUnidirectional.LINESCHANGED)) {
                bRoad = (BRoad) evt.getNewValue();
                jtfChangeRoad.setText(bRoad.getLanesUnid().getTagturns());
            }
        }

    }

    public static class LinesChangeBidirectionalListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(TurnSelectionBidirectional.LINESCHANGEDBIDIRECTIONAL)) {
                bRoad = (BRoad) evt.getNewValue();
                bRoad.setName("Bidirectional");
                jtfChangeRoad.setText(bRoad.getLanesA().getTagturns() + ";" +
                                      bRoad.getLanesB().getTagturns() + ";" +
                                      bRoad.getLanesC().getTagturns());
            }
        }
    }

    private class SetRoadChangeRoadListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            bRoad.setNone(jcNone.isSelected());
            firePropertyChange(ROADCHANGED, null, bRoad);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }

    //Start
    public void startDefaultUnidirectional() {
        jrbUnidirectional.setSelected(true);
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionUnidirectional.setDefault(presetsData.defaultRoadUnidirectional(4)); //default 4 lanes according data team
        pnlContentDirectional.add(turnSelectionUnidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

    public void setLanesByRoadUnidirectional(BRoad road) {
        jrbUnidirectional.setSelected(true);
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionUnidirectional.setDefault(road);
        pnlContentDirectional.add(turnSelectionUnidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

    public void startDefaultBidirectional() {
        jrbBidirectional.setSelected(true);
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionBidirectional.setDefault(
                presetsData.defaultRoadBidirectional("forward", 3, "backward", 2)); //default 3 and 2 lanes according data team
        pnlContentDirectional.add(turnSelectionBidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

    public void setLanesByRoadBidirectional(BRoad bRoad) {
        jrbBidirectional.setSelected(true);
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionBidirectional.setDefault(bRoad);
        pnlContentDirectional.add(turnSelectionBidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

    public void addLastEditInTable() {
        if (listLastEditsRoads.isEmpty()) {
            listLastEditsRoads.add(0, (BRoad) Util.clone(bRoad));
        } else {
            for (int i = 0; i < listLastEditsRoads.size(); i++) {
                if (bRoad.getName().equals("Bidirectional")) {
                    if (listLastEditsRoads.get(i).turns().equals(bRoad.turns())) {
                        listLastEditsRoads.remove(i);
                    }
                } else {
                    if (listLastEditsRoads.get(i).getLanesUnid().getTagturns().equals(bRoad.getLanesUnid().getTagturns())) {
                        listLastEditsRoads.remove(i);
                    }
                }
            }
            listLastEditsRoads.add(0, (BRoad) Util.clone(bRoad));
        }
        PresetsTableModel lasteditsTM = new PresetsTableModel(listLastEditsRoads, jcNone.isSelected()); //check aqui
        lastEditsTable.setModel(lasteditsTM);
        lastEditsTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
        Dimension d = scrollPane.getViewport().getExtentSize();
        lastEditsTable.adjustColumnWidth(d.width);
    }

    public void setLastEdit() {
        if (listLastEditsRoads.size() > 0) {
            if (listLastEditsRoads.get(0).getName().equals("Unidirectional")) {
                setLanesByRoadUnidirectional((BRoad) Util.clone(listLastEditsRoads.get(0)));
            } else {
                setLanesByRoadBidirectional((BRoad) Util.clone(listLastEditsRoads.get(0)));
            }
        } else {
            startDefaultUnidirectional();
        }
    }

    public void clearSelection() {
        presetsTable.clearSelection();
        presetsTable.getSelectionModel().clearSelection();
        lastEditsTable.clearSelection();
        lastEditsTable.getSelectionModel().clearSelection();
    }

    ActionListener jcNoneActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
            boolean selected = abstractButton.getModel().isSelected();
            //update the preset with none
            PresetsTableModel presetsTableModel = new PresetsTableModel(listPresetRoads, selected);
            presetsTable.setModel(presetsTableModel);
            presetsTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
            presetsTable.adjustColumnWidth(scrollPane.getViewport().getExtentSize().width);
            // Update last editions
            PresetsTableModel lasteditsTM = new PresetsTableModel(listLastEditsRoads, selected);
            lastEditsTable.setModel(lasteditsTM);
            lastEditsTable.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());
            lastEditsTable.adjustColumnWidth(scrollPane.getViewport().getExtentSize().width);
            //change turn in road
            bRoad.setNone(selected);
            if (bRoad.getName().equals("Unidirectional")) {
                jtfChangeRoad.setText(bRoad.getLanesUnid().getTagturns());
            } else {
                jtfChangeRoad.setText(bRoad.getLanesA().getTagturns() + ";" +
                                      bRoad.getLanesB().getTagturns() + ";" +
                                      bRoad.getLanesC().getTagturns());
            }
        }
    };

    public List<BRoad> getListLastEditsRoads() {
        return listLastEditsRoads;
    }
}
