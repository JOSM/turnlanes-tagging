// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor.ac;

import org.openstreetmap.josm.Main;

public class AutoCompletionContext {

    private boolean selectionIncludesNodes = false;
    private boolean selectionIncludesWays = false;
    private boolean selectionIncludesRelations = false;
    private boolean selectionEmpty = false;

    public AutoCompletionContext(){
    }

    public void initFromJOSMSelection() {
        selectionIncludesNodes = ! Main.getLayerManager().getEditDataSet().getSelectedNodes().isEmpty();
        selectionIncludesWays = !Main.getLayerManager().getEditDataSet().getSelectedWays().isEmpty();
        selectionIncludesRelations = !Main.getLayerManager().getEditDataSet().getSelectedRelations().isEmpty();
        selectionEmpty = (Main.getLayerManager().getEditDataSet().getSelected().size() == 0);
    }

    public boolean isSelectionEmpty() {
        return selectionEmpty;
    }

    public boolean isSelectionIncludesNodes() {
        return selectionIncludesNodes;
    }

    public void setSelectionIncludesNodes(boolean selectionIncludesNodes) {
        this.selectionIncludesNodes = selectionIncludesNodes;
    }

    public boolean isSelectionIncludesWays() {
        return selectionIncludesWays;
    }

    public void setSelectionIncludesWays(boolean selectionIncludesWays) {
        this.selectionIncludesWays = selectionIncludesWays;
    }

    public boolean isSelectionIncludesRelations() {
        return selectionIncludesRelations;
    }

    public void setSelectionIncludesRelations(boolean selectionIncludesRelations) {
        this.selectionIncludesRelations = selectionIncludesRelations;
    }
}
