// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import org.openstreetmap.josm.gui.tagging.TagModel;
import org.openstreetmap.josm.gui.tagging.presets.TaggingPreset;
import org.openstreetmap.josm.plugins.tageditor.editor.TagEditorModel;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.preset.AdvancedTag;

/**
 * This is the table cell renderer for cells for the table of tags in the tag
 * editor dialog.
 */
public class TagTableCellRenderer extends JLabel implements TableCellRenderer {

    //private static Logger logger = Logger.getLogger(TagTableCellRenderer.class.getName());
    public static final Color BG_COLOR_HIGHLIGHTED = new Color(255, 255, 204);

    private Font fontStandard = null;
    private Font fontItalic = null;

    public TagTableCellRenderer() {
        fontStandard = getFont();
        fontItalic = fontStandard.deriveFont(Font.ITALIC);
        setOpaque(true);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    /**
     * renders the name of a tag in the second column of the table
     *
     * @param tag the tag
     */
    protected void renderTagName(TagModel tag) {
        setText(tag.getName());
    }

    /**
     * renders the value of a a tag in the third column of the table
     *
     * @param tag the tag
     */
    protected void renderTagValue(TagModel tag) {
        if (tag.getValueCount() == 0) {
            setText("");
        } else if (tag.getValueCount() == 1) {
            setText(tag.getValues().get(0));
        } else if (tag.getValueCount() > 1) {
            setText(tr("multiple"));
            setFont(fontItalic);
        }
    }

    /**
     * resets the renderer
     */
    protected void resetRenderer() {
        setText("");
        setIcon(null);
        setFont(fontStandard);
    }

    protected TagEditorModel getModel(JTable table) {
        return (TagEditorModel) table.getModel();
    }

    protected boolean belongsToSelectedPreset(TagModel tagModel, TagEditorModel model) {

        // current tag is empty or consists of whitespace only => can't belong to
        // a selected preset
        //
        if (tagModel.getName().trim().equals("") && tagModel.getValue().equals("")) {
            return false;
        }

        // no current preset selected?
        //
        TaggingPreset item = (TaggingPreset) model.getAppliedPresetsModel().getSelectedItem();
        if (item == null) {
            return false;
        }

        for (AdvancedTag tag : AdvancedTag.forTaggingPreset(item)) {
            if (tag.getValue() == null) {
                if (tagModel.getName().equals(tag.getKey())) {
                    return true;
                }
            } else {
                if (tagModel.getName().equals(tag.getKey())
                        && tagModel.getValue().equals(tag.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * renders the background color. The default color is white. It is set to {
     *
     * @param tagModel the tag model
     * @param model the tag editor model
     *
     * @see TableCellRenderer#BG_COLOR_HIGHLIGHTED} if this cell displays the
     * tag which is suggested by the currently selected preset.
     */
    protected void renderColor(TagModel tagModel, TagEditorModel model, boolean isSelected, boolean isvalTag) {
        if (isvalTag) {
            setBackground(new Color(247, 246, 225));
        } else {
            if (isSelected) {
                setBackground(UIManager.getColor("Table.selectionBackground"));
                setForeground(UIManager.getColor("Table.selectionForeground"));
            } else {
                setBackground(UIManager.getColor("Table.background"));
                setForeground(UIManager.getColor("Table.foreground"));
            }
        }
        if (belongsToSelectedPreset(tagModel, model)) {
            setBackground(BG_COLOR_HIGHLIGHTED);
        }
    }

    /**
     * replies the cell renderer component for a specific cell
     *
     * @param table the table
     * @param value the value to be rendered
     * @param isSelected true, if the value is selected
     * @param hasFocus true, if the cell has focus
     * @param rowIndex the row index
     * @param vColIndex the column index
     *
     * @return the renderer component
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {

        resetRenderer();
        TagModel tagModel = (TagModel) value;

        switch (vColIndex) {
            case 0:
                renderTagName(tagModel);
                break;
            case 1:
                renderTagValue(tagModel);
                break;
        }

        String[] val_tags = {"lanes:backward",
            "turn:lanes:backward",
            "lanes:both_ways",
            "turn:lanes:both_ways",
            "lanes:forward",
            "turn:lanes:forward",
            "lanes",
            "turn:lanes"};

        if (Arrays.asList(val_tags).indexOf(tagModel.getName()) > -1) {
            renderColor(tagModel, (TagEditorModel) table.getModel(), isSelected, true);
            if (hasFocus && isSelected) {
                if (table.getSelectedColumnCount() == 1 && table.getSelectedRowCount() == 1) {
                    if (table.getEditorComponent() != null) {
                        table.getEditorComponent().requestFocusInWindow();
                    }
                }
            }
        } else {
            renderColor(tagModel, (TagEditorModel) table.getModel(), isSelected, false);
            if (hasFocus && isSelected) {
                if (table.getSelectedColumnCount() == 1 && table.getSelectedRowCount() == 1) {
                    if (table.getEditorComponent() != null) {
                        table.getEditorComponent().requestFocusInWindow();
                    }
                }
            }
        }

        return this;
    }
}
