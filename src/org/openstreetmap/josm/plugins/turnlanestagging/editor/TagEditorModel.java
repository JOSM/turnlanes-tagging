// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor;

import static org.openstreetmap.josm.tools.I18n.trn;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.command.Command;
import org.openstreetmap.josm.command.SequenceCommand;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.gui.tagging.TagModel;
import org.openstreetmap.josm.gui.tagging.presets.TaggingPreset;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.ac.KeyValuePair;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.preset.AdvancedTag;

@SuppressWarnings("serial")
public class TagEditorModel extends org.openstreetmap.josm.gui.tagging.TagEditorModel {
    //static private final Logger logger = Logger.getLogger(TagEditorModel.class.getName());

    private DefaultComboBoxModel<TaggingPreset> appliedPresets = null;

    /**
     * constructor
     */
    public TagEditorModel(DefaultListSelectionModel rowSelectionModel, DefaultListSelectionModel colSelectionModel) {
        super(rowSelectionModel, colSelectionModel);
        appliedPresets = new DefaultComboBoxModel<>();
    }

    /**
     * applies the tags defined for a preset item to the tag model.
     *
     * Mandatory tags are added to the list of currently edited tags.
     * Optional tags are not added.
     * The model remembers the currently applied presets.
     *
     * @param item  the preset item. Must not be null.
     * @exception IllegalArgumentException thrown, if item is null
     *
     */
    public void applyPreset(TaggingPreset item) {
        if (item == null)
            throw new IllegalArgumentException("argument 'item' must not be null");
        // check whether item is already applied
        //
        for (int i = 0; i < appliedPresets.getSize(); i++) {
            if (appliedPresets.getElementAt(i).equals(item))
                // abort - preset already applied
                return;
        }

        // apply the tags proposed by the preset
        //
        for (AdvancedTag tag : AdvancedTag.forTaggingPreset(item)) {
            if (!tag.isOptional()) {
                if (!includesTag(tag.getKey())) {
                    TagModel tagModel = new TagModel(tag.getKey(), tag.getValue());
                    prepend(tagModel);
                } else {
                    TagModel tagModel = get(tag.getKey());
                    // only overwrite an existing value if the preset
                    // proposes a value. I.e. don't overwrite
                    // existing values for tag 'name' with an empty string
                    if (tag.getValue() != null) {
                        tagModel.setValue(tag.getValue());
                    }
                }
            }
        }

        // remember the preset and make it the current preset
        //
        appliedPresets.addElement(item);
        appliedPresets.setSelectedItem(item);
        fireTableDataChanged();
    }


    /**
     * applies a tag given by a {@see KeyValuePair} to the model
     *
     * @param pair the key value pair
     */
    public void applyKeyValuePair(KeyValuePair pair) {
        TagModel tagModel = get(pair.getKey());
        if (tagModel == null) {
            tagModel = new TagModel(pair.getKey(), pair.getValue());
            prepend(tagModel);
        } else {
            tagModel.setValue(pair.getValue());
        }
        fireTableDataChanged();
    }

    public DefaultComboBoxModel<TaggingPreset> getAppliedPresetsModel() {
        return appliedPresets;
    }

    public void removeAppliedPreset(TaggingPreset item) {
        if (item == null)
            return;
        for (AdvancedTag tag: AdvancedTag.forTaggingPreset(item)) {
            if (tag.getValue() != null) {
                // preset tag with explicit key and explicit value. Remove tag model
                // from the current model if both the key and the value match
                //
                TagModel tagModel = get(tag.getKey());
                if (tagModel != null && tag.getValue().equals(tagModel.getValue())) {
                    tags.remove(tagModel);
                    setDirty(true);
                }
            } else {
                // preset tag with required key. No explicit value given. Remove tag
                // model with the respective key
                //
                TagModel tagModel = get(tag.getKey());
                if (tagModel != null) {
                    tags.remove(tagModel);
                    setDirty(true);
                }
            }
        }
        appliedPresets.removeElement(item);
        fireTableDataChanged();
    }

    public void clearAppliedPresets() {
        appliedPresets.removeAllElements();
        fireTableDataChanged();
    }

    public void highlightCurrentPreset() {
        fireTableDataChanged();
    }

    /**
     * updates the tags of the primitives in the current selection with the
     * values in the current tag model
     *
     */
    public void updateJOSMSelection() {
        ArrayList<Command> commands = new ArrayList<>();
        Collection<OsmPrimitive> selection = Main.getLayerManager().getEditDataSet().getSelected();
        if (selection == null)
            return;
        for (TagModel tag : tags) {
            Command command = createUpdateTagCommand(selection, tag);
            if (command != null) {
                commands.add(command);
            }
        }
        Command deleteCommand = createDeleteTagsCommand(selection);
        if (deleteCommand != null) {
            commands.add(deleteCommand);
        }

        SequenceCommand command = new SequenceCommand(
                trn("Updating properties of up to {0} object", "Updating properties of up to {0} objects", selection.size(), selection.size()),
                commands
        );

        // executes the commands and adds them to the undo/redo chains
        Main.main.undoRedo.add(command);
    }

    /**
     * initializes the model with the tags in the current JOSM selection
     */
    public void initFromJOSMSelection() {
        Collection<OsmPrimitive> selection = Main.getLayerManager().getEditDataSet().getSelected();
        clear();
        for (OsmPrimitive element : selection) {
            for (String key : element.keySet()) {
                String value = element.get(key);
                add(key, value);
            }
        }
        sort();
        setDirty(false);
    }
}
