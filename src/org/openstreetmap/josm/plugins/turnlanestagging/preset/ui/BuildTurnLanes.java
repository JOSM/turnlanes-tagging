package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.bidirectional.TurnSelectionBidirectional;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.unidirectional.TurnSelectionUnidirectional;
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
    public static JTextField jTextField = new JTextField();

    // Data to fill  the  table
    List<BRoad> listBRoads = null;
    PresetsData presetsData = new PresetsData();

    //Value Road
//    BRoad valBRoad = new BRoad();
    //Event
    // Bidirectional
    TurnSelectionBidirectional turnSelectionBidirectional = null;

    //Unidirection
    public TurnSelectionUnidirectional turnSelectionUnidirectional = null;

    //Constructor
    public BuildTurnLanes() {
        build();
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
                lanes(listBRoads.get(rowNum));
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
            pnlContentDirectional.removeAll();
            pnlContentDirectional.setLayout(new GridLayout(1, 1));
            turnSelectionUnidirectional = new TurnSelectionUnidirectional();
            turnSelectionUnidirectional.addPropertyChangeListener(new RoadChangeListener());

            pnlContentDirectional.add(turnSelectionUnidirectional);
            pnlContentDirectional.revalidate();
            pnlContentDirectional.repaint();
        }
    };

    ActionListener actionListenerBidirectional = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            pnlContentDirectional.removeAll();
            pnlContentDirectional.setLayout(new GridLayout(1, 1));
            turnSelectionBidirectional = new TurnSelectionBidirectional();
            pnlContentDirectional.add(turnSelectionBidirectional);
            pnlContentDirectional.revalidate();
            pnlContentDirectional.repaint();
        }
    };

    // Build the GUI for turn lanes
    protected void build() {
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
        pnlBuildTurnLanes.add(jTextField, BorderLayout.SOUTH);

        add(pnlBuildTurnLanes, BorderLayout.SOUTH);

        //road change event
    }

    public void setDefaultLanes() {
        lanes(listBRoads.get(0));
    }

    public void lanes(BRoad road) {
    }

    public static class RoadChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(TurnSelectionUnidirectional.ROADCHANGED)) {
                BRoad bRoad = (BRoad) evt.getNewValue();
                jTextField.setText(bRoad.getTagturns());
            }
        }

    }

}
