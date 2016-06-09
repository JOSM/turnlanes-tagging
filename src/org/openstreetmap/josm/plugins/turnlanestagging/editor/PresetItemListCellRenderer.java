package org.openstreetmap.josm.plugins.turnlanestagging.editor;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.openstreetmap.josm.gui.tagging.presets.TaggingPreset;

public class PresetItemListCellRenderer extends JLabel implements ListCellRenderer<TaggingPreset> {
    //private static final Logger logger = Logger.getLogger(PresetItemListCellRenderer.class.getName());

    public Component getListCellRendererComponent(JList<? extends TaggingPreset> list, TaggingPreset item,
            int index, boolean isSelected, boolean cellHasFocus) {

        if (item == null) {
            setText(tr("(none)"));
            setIcon(null);
        } else {
            if (isSelected) {
                setBackground(UIManager.getColor("Table.selectionBackground"));
                setForeground(UIManager.getColor("Table.selectionForeground"));
            } else {
                setBackground(UIManager.getColor("Table.background"));
                setForeground(UIManager.getColor("Table.foreground"));
            }
            setIcon(item.getIcon());
            setText(item.getName());
            setOpaque(true);
            setFont(UIManager.getFont("Table.font"));
        }
        return this;
    }
}
