package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.bidirectional.TurnSelectionBidirectional;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.unidirectional.TurnSelectionUnidirectional;

/**
 *
 * @author ruben
 */
public class BuildTurnLanes extends JPanel {

    //Table   
    private JScrollPane scrollPane = null;
    private PresetsTable presetsTable = null;
    private PresetsTableModel presetsTableModel = null;

    //Main Content
    private JPanel pnlBuildTurnLanes = null;

    //Content directional options
    private JPanel pnlDirectionalOptions = null;
    private ButtonGroup btdirectional = null;
    private JRadioButton jrbUnidirectional = null;
    private JRadioButton jrbBidirectional = null;

    //Content for directional
    private JPanel pnlContentDirectional = null;

    // Jtext for add the changes
    public static JTextField jtfChangeRoad = new JTextField();

    // Data to fill  the  table
    List<BRoad> listBRoads = null;
    PresetsData presetsData = new PresetsData();

    //Road
    public static BRoad bRoad = new BRoad();
    // Bidirectional
    public TurnSelectionBidirectional turnSelectionBidirectional = null;

    //Unidirection
    public TurnSelectionUnidirectional turnSelectionUnidirectional = null;

    // Event Changed
    public static final String ROADCHANGED = "RoadChanged";

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
        listBRoads = new ArrayList<>(presetsData.dataPreset());
        presetsTableModel = new PresetsTableModel(listBRoads);
        //print on table
        presetsTable = new PresetsTable(presetsTableModel);
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

    private class ClickAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                int rowNum = presetsTable.rowAtPoint(e.getPoint());
                if (listBRoads.get(rowNum).getName().equals("Unidirectional")) {
                    jrbUnidirectional.setSelected(true);

                    setLanesByRoadUnidirectional(listBRoads.get(rowNum));
//                    turnSelectionUnidirectional.lanes(listBRoads.get(rowNum));

                } else {
                    jrbBidirectional.setSelected(true);
                    setLanesByRoadBidirectinal(listBRoads.get(rowNum));

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
        pnlDirectionalOptions = new JPanel(new GridLayout(1, 2));
        pnlDirectionalOptions.setBorder(new SoftBevelBorder(BevelBorder.RAISED));

        btdirectional = new ButtonGroup();
        jrbUnidirectional = new JRadioButton("Unidirectional");
        jrbBidirectional = new JRadioButton("Bidirectional");
        btdirectional.add(jrbUnidirectional);
        btdirectional.add(jrbBidirectional);
        pnlDirectionalOptions.add(jrbUnidirectional);
        pnlDirectionalOptions.add(jrbBidirectional);
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
        //Title
        add(new JLabel("Select Preset Turn Lanes"), BorderLayout.NORTH);
        //Table
        add(buildPresetTable(), BorderLayout.CENTER);
        //turnlanes builder
        pnlBuildTurnLanes = new JPanel(new BorderLayout());
        pnlBuildTurnLanes.add(buildDirectionalOptions(), BorderLayout.NORTH);
        pnlContentDirectional = new JPanel();
        pnlBuildTurnLanes.add(pnlContentDirectional, BorderLayout.CENTER);
        pnlBuildTurnLanes.add(jtfChangeRoad, BorderLayout.SOUTH);

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
                jtfChangeRoad.setText(bRoad.getTagturns());
            }
        }

    }

    public static class LinesChangeBidirectionalListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(TurnSelectionBidirectional.LINESCHANGEDBIDIRECTIONAL)) {
                bRoad = (BRoad) evt.getNewValue();
                String t = bRoad.getLanesA().getTagturns() + "==" + bRoad.getLanesB().getTagturns() + "==" + bRoad.getLanesC().getTagturns();
                bRoad.setName("Bidirectional");
                jtfChangeRoad.setText(t);

            }

        }

    }

    private class SetRoadChangeRoadListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {

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
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionUnidirectional.setDefault(listBRoads.get(0));
        pnlContentDirectional.add(turnSelectionUnidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

    public void setLanesByRoadUnidirectional(BRoad road) {
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionUnidirectional.setDefault(road);
        turnSelectionUnidirectional.setDefault(bRoad);
        pnlContentDirectional.add(turnSelectionUnidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

    private void startDefaultBidirectional() {
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionBidirectional.setDefault(listBRoads.get(2));
        pnlContentDirectional.add(turnSelectionBidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

    private void setLanesByRoadBidirectinal(BRoad bRoad) {
        pnlContentDirectional.removeAll();
        pnlContentDirectional.setLayout(new GridLayout(1, 1));
        turnSelectionBidirectional.setDefault(bRoad);
        pnlContentDirectional.add(turnSelectionBidirectional);
        pnlContentDirectional.revalidate();
        pnlContentDirectional.repaint();
    }

}
