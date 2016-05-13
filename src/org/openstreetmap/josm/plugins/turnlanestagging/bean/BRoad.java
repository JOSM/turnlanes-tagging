package org.openstreetmap.josm.plugins.turnlanestagging.bean;

import java.util.List;

/**
 *
 * @author ruben
 */
public class BRoad {

    String name;
    List<BLine> listLines;

    public BRoad() {
    }

    public BRoad(String name, List<BLine> listLines) {
        this.name = name;
        this.listLines = listLines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BLine> getListLines() {
        return listLines;
    }

    public void setListLines(List<BLine> listLines) {
        this.listLines = listLines;
    }

    public String getTagturns() {
        int position = listLines.get(0).getPosition();
        String tagturns = "";
        for (int i = 0; i < listLines.size(); i++) {
            tagturns = tagturns + "|" + listLines.get(i).getTurn();
        }
        return tagturns;
    }

    public int getLines() {
        return listLines.size();
    }
}
