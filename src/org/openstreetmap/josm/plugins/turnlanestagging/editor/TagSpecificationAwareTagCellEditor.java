// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openstreetmap.josm.gui.tagging.TagCellEditor;
import org.openstreetmap.josm.gui.tagging.TagModel;
import org.openstreetmap.josm.gui.tagging.ac.AutoCompletionItemPriority;
import org.openstreetmap.josm.gui.tagging.ac.AutoCompletionListItem;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.ac.AutoCompletionContext;
import org.openstreetmap.josm.plugins.turnlanestagging.editor.ac.TagSpecifications;

public class TagSpecificationAwareTagCellEditor extends TagCellEditor {
    private static final Logger logger = Logger.getLogger(TagCellEditor.class.getName());

    public TagSpecificationAwareTagCellEditor() {
		super(0);
	}

	/**
     * initializes  the auto completion list when the table cell editor starts
     * to edit the key of a tag. In this case the auto completion list is
     * initialized with the set of standard key values and the set of current key
     * values from the current JOSM data set. Keys already present in the
     * current tag model are removed from the auto completion list.
     * 
     * @param model  the tag editor model
     * @param currentTag  the current tag
     */
    protected void initAutoCompletionListForKeys(TagEditorModel model, TagModel currentTag) {       
        if (getAutoCompletionList() == null) {
            logger.warning("autoCompletionList is null. Make sure an instance of AutoCompletionList is injected into TableCellEditor.");
            return;
        }

        autoCompletionList.clear();

        // add the list of standard keys
        //
        try {
            //autoCompletionList.add(TagSpecifications.getInstance().getKeysForAutoCompletion(context));
        } catch(Exception e) {
            logger.log(Level.WARNING, "failed to initialize auto completion list with standard keys.", e);
        }

        // add the list of keys in the current data set
        //
        autocomplete.populateWithKeys(autoCompletionList);
        AutoCompletionContext context = new AutoCompletionContext();
        try {
            context.initFromJOSMSelection();
            autoCompletionList.add(TagSpecifications.getInstance().getKeysForAutoCompletion(context));
        } catch(Exception e) {
            System.out.println("Warning: failed to initialize auto completion list with tag specification keys. Exception was: " + e.toString());
            e.printStackTrace();
        }

        // remove the keys already present in the current tag model
        //
        for (String key : model.getKeys()) {
            if (! key.equals(currentTag.getName())) {
                autoCompletionList.remove(key);
            }
        }
        autoCompletionList.fireTableDataChanged();
    }

    /**
     * initializes the auto completion list when the cell editor starts to edit
     * a tag value. In this case the auto completion list is initialized with the
     * set of standard values for a given key and the set of values present in the
     * current data set for the given key.
     * 
     * @param forKey the key
     */
    protected void initAutoCompletionListForValues(String forKey) {

        if (autoCompletionList == null) {
            logger.warning("autoCompletionList is null. Make sure an instance of AutoCompletionList is injected into TableCellEditor.");
            return;
        }
        autoCompletionList.clear();
        autocomplete.populateWithTagValues(autoCompletionList, forKey);
        
        AutoCompletionContext context = new AutoCompletionContext();
        try {
            context.initFromJOSMSelection();
            autoCompletionList.add(TagSpecifications.getInstance().getLabelsForAutoCompletion(forKey, context));
        } catch(Exception e) {
            System.out.println("Warning: failed to initialize auto completion list with tag specification values. Exception was: " + e.toString());
            e.printStackTrace();
        }

        //  add the list of possible values for a key from the current selection
        //
        if (currentTag.getValueCount() > 1) {
            for (String value : currentTag.getValues()) {
                //logger.info("adding ac item " + value + " with priority IN_SELECTION");;
                autoCompletionList.add(
                        new AutoCompletionListItem(value, AutoCompletionItemPriority.IS_IN_SELECTION)
                );
            }
        }
    }
}
