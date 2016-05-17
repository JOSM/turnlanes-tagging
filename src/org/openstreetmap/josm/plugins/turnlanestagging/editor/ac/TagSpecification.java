// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor.ac;

import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.josm.plugins.turnlanestagging.editor.ac.AutoCompletionContext;

/**
 * A TagSpecifications specifies a tag. The specifications consists of the following
 * elements:
 * <ul>
 *   <li>the <strong>key</strong> the of the tag</li>
 *   <li>the <strong>type</strong> of the tag</li>
 *   <li>whether the tag is applicable to a node, a way or a relation</li>
 * </ul>
 * @author Gubaer
 *
 */
public class TagSpecification {

    /** the key of the tag */
    private String key;

    /** the type of the tag */
    private String type;

    /** the type of the tag */

    private boolean applicableToNode = true;
    private boolean applicableToWay = true;
    private boolean applicableToRelation = true;

    private ArrayList<LabelSpecification> lables = null;


    /**
     * constructor
     */
    public TagSpecification() {
        lables = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<TagSpecification ");
        builder.append("key=\"").append(key).append("\"").append(", ");
        builder.append("type=\"").append(type).append("\"").append(", ");
        builder.append("applicable-to-node=\"").append(applicableToNode).append("\"").append(", ");
        builder.append("applicable-to-way=\"").append(applicableToWay).append("\"").append(", ");
        builder.append("applicable-to-relation=\"").append(applicableToRelation).append("\"");
        builder.append(" />");
        return builder.toString();
    }

    /**
     * @return th e list of predefined labels for this tag; an empty list if no
     *   labels are defined
     */
    public List<LabelSpecification> getLables() {
        return lables;
    }

    /**
     * sets the list of lables for this tag specification
     * 
     * @param lables  the list of lables; must not be null
     * @exception IllegalArgumentException thrown, if lables is null
     */
    public void setLables(List<LabelSpecification> lables) throws IllegalArgumentException {
        if (lables == null)
            throw new IllegalArgumentException("argument 'lables' must not be null");
        this.lables.clear();
        for (LabelSpecification l : lables) {
            this.lables.add(l);
        }
    }

    /**
     * Adds a label to the list of label for this tag specification. The label
     * is only added if i
     * 
     * @param lable the lalbe to add; must not be null
     * @exception IllegalArgumentException thrown, if lable is null
     */
    public void addLable(LabelSpecification lable) throws IllegalArgumentException  {
        if (lable == null)
            throw new IllegalArgumentException("argument 'lable' must not be null");
        if (!this.lables.contains(lable)) {
            this.lables.add(lable);
        }
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
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
