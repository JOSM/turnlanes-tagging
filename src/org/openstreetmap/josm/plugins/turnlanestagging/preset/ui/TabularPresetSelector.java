package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

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
import java.util.LinkedList;
import java.util.List;
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
import static org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.TurnSelection.jRBLeft_CHANGED;

/**
 *
 * @author ruben
 */
public class TabularPresetSelector extends JPanel {

    JSplitPane jSplitPane = null;
    JPanel jPanelBuldidTags = null;

    private JScrollPane scrollPane = null;
    private PresetsTable presetsTable = null;
    PresetsTableModel presetsTableModel;
    ArrayList<PresetTurnLane> listpresetturnlanes = new ArrayList<>();
    //panel for create the tags
    JComboBox<Integer> comboBox;
    JPanel panelGraps;
    JTextField jTF = new JTextField();

    //Array of lines of turnlaes selections
    List<TurnSelection> listLines = new LinkedList<TurnSelection>();

    public TabularPresetSelector() {
        build();
    }

    protected JScrollPane buildPresetGrid() {
        //add data        
        PresetTurnLane presetTurnLane = new PresetTurnLane("1", "turn:lanes 1", "left|through|right");
        PresetTurnLane presetTurnLane2 = new PresetTurnLane("2", "turn:lanes 2", "left|left|||right");
        listpresetturnlanes.add(presetTurnLane);
        listpresetturnlanes.add(presetTurnLane2);
        presetsTableModel = new PresetsTableModel(listpresetturnlanes);
        presetsTable = new PresetsTable(presetsTableModel);
        scrollPane = new JScrollPane(presetsTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setSize(new Dimension(50, 150));
        scrollPane.addComponentListener(
                new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        Dimension d = scrollPane.getViewport().getExtentSize();
                        presetsTable.adjustColumnWidth(d.width);
//                        presetsTable.adjustColumnWidth(d.height - 200);
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
                lines(4);
            }
        }
    }

    protected PresetsTableModel getModel() {
        return (PresetsTableModel) presetsTable.getModel();
    }

    //form the tag
    protected JPanel buildPanelGrap() {
        jPanelBuldidTags = new JPanel(new BorderLayout());
        JPanel jpScroll = new JPanel(new GridLayout(1, 2));
        //fill Combo Box
        comboBox = new JComboBox<>();
        for (int j = 0; j < 10; j++) {
            comboBox.addItem(j + 1);
        }
        //
        comboBox.setSelectedIndex(2);
        jpScroll.add(new JLabel("Number of Lines"));
        jpScroll.add(comboBox);
        //default Lines
        panelGraps = new JPanel(new GridLayout(1, 3));
        lines(3);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JComboBox comboBox = (JComboBox) event.getSource();
                int selected = (int) comboBox.getSelectedItem();
                System.err.println(selected);
                lines(selected);
            }
        });

        jPanelBuldidTags.add(jpScroll, BorderLayout.NORTH);
        jPanelBuldidTags.add(panelGraps, BorderLayout.CENTER);
        jPanelBuldidTags.add(jTF, BorderLayout.SOUTH);
        return jPanelBuldidTags;
    }

    String innerValue = "";

    protected void lines(int lines) {
        comboBox.setSelectedIndex(lines - 1);
        panelGraps.removeAll();
        panelGraps.setLayout(new GridLayout(1, lines));
        for (int i = 0; i < lines; i++) {
            final TurnSelection tlo = new TurnSelection("Line " + (i + 1));
            tlo.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(tlo.jRBLeft_CHANGED)) {
                        innerValue = evt.getNewValue().toString();
                       jTF.setText(innerValue);
                    }
                }
            });
            listLines.add(tlo);
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
