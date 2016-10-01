// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellEditor;

import org.openstreetmap.josm.gui.tagging.TagTable;
import org.openstreetmap.josm.plugins.tageditor.editor.TagEditorModel;

public class TagEditor extends JPanel {

    private TagEditorModel tagEditorModel;
    private TagTable tblTagEditor;

    protected JPanel buildButtonsPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));

        // add action
        JButton btn;
        pnl.add(btn = new JButton(tblTagEditor.getAddAction()));
        btn.setMargin(new Insets(0, 0, 0, 0));
        tblTagEditor.addComponentNotStoppingCellEditing(btn);

        // delete action
        pnl.add(btn = new JButton(tblTagEditor.getDeleteAction()));
        btn.setMargin(new Insets(0, 0, 0, 0));
        tblTagEditor.addComponentNotStoppingCellEditing(btn);
        return pnl;
    }

    public void addComponentNotStoppingCellEditing(Component c) {
        tblTagEditor.addComponentNotStoppingCellEditing(c);
    }

    protected JPanel buildTagEditorPanel() {
        JPanel pnl = new JPanel(new GridBagLayout());
        DefaultListSelectionModel rowSelectionModel = new DefaultListSelectionModel();
        DefaultListSelectionModel colSelectionModel = new DefaultListSelectionModel();

        tagEditorModel = new TagEditorModel(rowSelectionModel, colSelectionModel);
        // build the scrollable table for editing tag names and tag values
        tblTagEditor = new TagTable(tagEditorModel, 0);
        TagTableCellRenderer renderer = new TagTableCellRenderer();
        tblTagEditor.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tblTagEditor.getColumnModel().getColumn(1).setCellRenderer(renderer);

        final JScrollPane scrollPane = new JScrollPane(tblTagEditor);
        JPanel pnlTagTable = new JPanel(new BorderLayout());
        pnlTagTable.add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints gc = new GridBagConstraints();

        //buttons panel
        gc.fill = GridBagConstraints.VERTICAL;
        gc.weightx = 0.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.NORTHWEST;
        pnl.add(buildButtonsPanel(), gc);

        // the panel with the editor table
        gc.gridx = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.CENTER;
        pnl.add(pnlTagTable, gc);

        return pnl;
    }

    protected void build() {
        setLayout(new BorderLayout());
        add(buildTagEditorPanel(), BorderLayout.CENTER);
    }

    public TagEditor() {
        build();
    }

    public TagEditorModel getTagEditorModel() {
        return tagEditorModel;
    }

    public void clearSelection() {
        tblTagEditor.getSelectionModel().clearSelection();
    }

    public void stopEditing() {
        TableCellEditor editor = tblTagEditor.getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
    }

    public void requestFocusInTopLeftCell() {
        tblTagEditor.requestFocusInCell(0, 0);
    }

    public TagEditorModel getModel() {
        return tagEditorModel;
    }
}
