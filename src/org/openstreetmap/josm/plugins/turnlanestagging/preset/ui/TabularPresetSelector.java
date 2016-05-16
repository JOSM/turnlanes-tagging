package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import com.sun.java.swing.plaf.gtk.GTKColorType;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import org.openstreetmap.josm.plugins.turnlanestagging.Util;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import static org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.TurnSelection.jCBThrough_CHANGED;
import static org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.TurnSelection.jRBLeft_CHANGED;
import static org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.TurnSelection.jRBRight_CHANGED;

/**
 *
 * @author ruben
 */
public class TabularPresetSelector extends JPanel {

    //Table
    private JPanel jPanelBuldidTags = null;
    private JScrollPane scrollPane = null;
    private PresetsTable presetsTable = null;
    PresetsTableModel presetsTableModel;

    //Panel for create the tags
    JComboBox<Integer> comboBox;
    JPanel panelGraps;
    JTextField jTF = new JTextField();

    // Data to fill  table
    PresetsData presetsData = new PresetsData();
    List<BRoad> listBRoads = new LinkedList<BRoad>(presetsData.data());

    BRoad valBRoad = new BRoad();

    //Array of lines of turnlaes selections
    List<TurnSelection> listTurnSelection = new LinkedList<TurnSelection>();

    //Constructor
    public TabularPresetSelector() {
        build();
    }

    protected JScrollPane buildPresetGrid() {
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
                lines(listBRoads.get(rowNum));
            }
        }
    }

    protected PresetsTableModel getModel() {
        return (PresetsTableModel) presetsTable.getModel();
    }

    //form the tag
    protected JPanel buildPanelGrap() {
        //fill Combo Box
        jPanelBuldidTags = new JPanel(new BorderLayout());
        JPanel jPContenComboBox = new JPanel(new GridLayout(1, 2));
        comboBox = new JComboBox<>();
        for (int j = 0; j < 10; j++) {
            comboBox.addItem(j + 1);
        }
        comboBox.setSelectedIndex(2);
        jPContenComboBox.add(new JLabel("Number of Lines"));
        jPContenComboBox.add(comboBox);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JComboBox comboBox = (JComboBox) event.getSource();
                int selected = (int) comboBox.getSelectedItem();
                //SET ROAD LINES
                lines(presetsData.defaultData(selected));
            }
        });

        //Build default Lines : 3 lines
        panelGraps = new JPanel(new GridLayout(1, 3));
        //SET ROAD LINES
        lines(presetsData.defaultData(3));
        jPanelBuldidTags.add(jPContenComboBox, BorderLayout.NORTH);
        jPanelBuldidTags.add(panelGraps, BorderLayout.CENTER);
        jPanelBuldidTags.add(jTF, BorderLayout.SOUTH);
        return jPanelBuldidTags;
    }

    protected void lines(BRoad road) {
        //inicializar
        valBRoad = road;
        panelGraps.removeAll();
        panelGraps.setLayout(new GridLayout(1, valBRoad.getLines()));
        jTF.setText(valBRoad.getTagturns());
        final List<BLine> listBLines = valBRoad.getListLines();
        for (int i = 0; i < valBRoad.getLines(); i++) {
            BLine b = valBRoad.getListLines().get(i);
            final TurnSelection tlo = new TurnSelection(b);
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
            listTurnSelection.add(tlo);
            panelGraps.add(tlo);
            panelGraps.revalidate();
            panelGraps.repaint();
        }
    }

    protected void build() {
        setLayout(new BorderLayout());
        add(new JLabel("Select Preset Turn Lanes"), BorderLayout.NORTH);
        add(buildPresetGrid(), BorderLayout.CENTER);
        add(buildPanelGrap(), BorderLayout.SOUTH);
    }

}
