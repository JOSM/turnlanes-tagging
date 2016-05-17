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
        selectionIncludesNodes = ! Main.main.getCurrentDataSet().getSelectedNodes().isEmpty();
        selectionIncludesWays = !Main.main.getCurrentDataSet().getSelectedWays().isEmpty();
        selectionIncludesRelations = !Main.main.getCurrentDataSet().getSelectedRelations().isEmpty();
        selectionEmpty = (Main.main.getCurrentDataSet().getSelected().size() == 0);
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
