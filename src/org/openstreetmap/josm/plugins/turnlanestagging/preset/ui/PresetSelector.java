package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
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
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;

/**
 *
 * @author ruben
 */
public class PresetSelector extends JPanel {

    //Table
    private JPanel pnlBuldLines = null;
    private JScrollPane scrollPane = null;
    private PresetsTable presetsTable = null;

    private PresetsTableModel presetsTableModel = null;

    //Panel for create the number of lines
    JPanel pnlGraps = null;
    JComboBox<Integer> comboBox = null;
    JTextField jTF = new JTextField();

    // Data to fill  table
    List<BRoad> listBRoads = null;
    PresetsData presetsData = new PresetsData();

    BRoad valBRoad = new BRoad();

    public static final String jTF_CHANGED = "jTF Changed";
    TurnSelection tlo = null;

    //Constructor
    public PresetSelector() {
        build();
    }

    protected JScrollPane buildPresetGrid() {
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
                comboBox.setSelectedIndex(listBRoads.get(rowNum).getLines() - 1);
                lanes(listBRoads.get(rowNum));
            }
        }
    }

    public PresetsTableModel getModel() {
        return (PresetsTableModel) presetsTable.getModel();
    }

    //form the tag
    protected JPanel buildPanelGrap() {
        //fill Combo Box
        pnlBuldLines = new JPanel(new BorderLayout());
        JPanel jPContenComboBox = new JPanel(new GridLayout(1, 2));
        comboBox = new JComboBox<>();
        for (int j = 0; j < 10; j++) {
            comboBox.addItem(j + 1);
        }
        //default
        comboBox.setSelectedIndex(2);

        jPContenComboBox.add(new JLabel("Number of lanes"));
        jPContenComboBox.add(comboBox);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JComboBox comboBox = (JComboBox) event.getSource();
                int selected = (int) comboBox.getSelectedItem();
                //SET ROAD LINES
                lanes(presetsData.defaultData(selected));
            }
        });

        pnlGraps = new JPanel();
        pnlBuldLines.add(jPContenComboBox, BorderLayout.NORTH);
        pnlBuldLines.add(pnlGraps, BorderLayout.CENTER);
        pnlBuldLines.add(jTF, BorderLayout.SOUTH);
        return pnlBuldLines;
    }

    public void lanes(BRoad road) {
        //Clone objtects
        valBRoad.setName(new String(road.getName()));
        List<BLine> listbl = new ArrayList<>();
        for (int k = 0; k < road.getLines(); k++) {
            BLine bl = new BLine(new Integer(road.getListLines().get(k).getPosition()), new String(road.getListLines().get(k).getTurn()));
            listbl.add(bl);
        }

        valBRoad.setListLines(listbl);
        pnlGraps.removeAll();
        pnlGraps.setLayout(new GridLayout(1, valBRoad.getLines()));
        jTF.setText(valBRoad.getTagturns());
        final List<BLine> listBLines = valBRoad.getListLines();
        for (int i = 0; i < valBRoad.getLines(); i++) {
            BLine b = valBRoad.getListLines().get(i);
            tlo = new TurnSelection(b);
            tlo.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(tlo.jRBLeft_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                    } else if (evt.getPropertyName().equals(tlo.jRBRight_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                    } else if (evt.getPropertyName().equals(tlo.jCBThrough_CHANGED)) {
                        listBLines.add((BLine) evt.getNewValue());
                        valBRoad.setListLines(listBLines);
                    }
                    jTF.setText(valBRoad.getTagturns());
                }
            });
            pnlGraps.add(tlo);
            pnlGraps.revalidate();
            pnlGraps.repaint();
        }
    }

    protected void build() {
        setLayout(new BorderLayout());
        add(new JLabel("Select Preset Turn Lanes"), BorderLayout.NORTH);
        add(buildPresetGrid(), BorderLayout.CENTER);
        add(buildPanelGrap(), BorderLayout.SOUTH);
        //Event to ad in tag
        jTF.getDocument().addDocumentListener(new SetTagTurnListener());
        //Add Default the the common turn lane
        lanes(listBRoads.get(0));
    }

    private class SetTagTurnListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            firePropertyChange(jTF_CHANGED, null, valBRoad);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

    }

}
