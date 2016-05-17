// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor.ac;

import org.openstreetmap.josm.plugins.turnlanestagging.editor.ac.AutoCompletionContext;

public class LabelSpecification {

    /** the key of the tag */
    private String value;
        
    
    private boolean applicableToNode = true;
    private boolean applicableToWay = true;
    private boolean applicableToRelation = true;
    
    /**
     * constructor 
     */
    public LabelSpecification() {
    }

    public boolean isApplicable(AutoCompletionContext context) {
        boolean ret = false;
        if (context.isSelectionEmpty()) {
            ret = true;
        } else {
            ret = ret || (applicableToNode && context.isSelectionIncludesNodes());
            ret = ret || (applicableToWay && context.isSelectionIncludesWays());
            ret = ret || (applicableToRelation && context.isSelectionIncludesRelations());
        }
        return ret;
    }

    /* --------------------------------------------------------------------------- */
    /* setters/getters                                                             */
    /* --------------------------------------------------------------------------- */

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isApplicableToNode() {
        return applicableToNode;
    }

    public void setApplicableToNode(boolean applicableToNode) {
        this.applicableToNode = applicableToNode;
    }

    public boolean isApplicableToWay() {
        return applicableToWay;
    }

    public void setApplicableToWay(boolean applicableToWay) {
        this.applicableToWay = applicableToWay;
    }

    public boolean isApplicableToRelation() {
        return applicableToRelation;
    }

    public void setApplicableToRelation(boolean applicableToRelation) {
        this.applicableToRelation = applicableToRelation;
    }
}
