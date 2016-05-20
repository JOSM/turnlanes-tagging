package org.openstreetmap.josm.plugins.turnlanestagging.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.openstreetmap.josm.plugins.turnlanestagging.Util;

/**
 *
 * @author ruben
 */
public class BRoad implements Cloneable {

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

    public void setListLines(List<BLine> newLines) {
        Map<Integer, BLine> map = new TreeMap<Integer, BLine>();
        for (int i = 0; i < newLines.size(); i++) {
            map.put(newLines.get(i).getPosition(), newLines.get(i));
        }
        List<BLine> listLs = new ArrayList<>(map.values());
        this.listLines = listLs;
    }

    public String getTagturns() {
        String tagturns = "";
        for (int i = 0; i < listLines.size(); i++) {
            if (i == 0) {
                tagturns = listLines.get(i).getTurn();
            } else {
                tagturns = tagturns + "|" + listLines.get(i).getTurn();
            }
        }
        return tagturns;
    }

    public int getNumLanes() {
        return listLines.size();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setLanes(String turnLanes) {
        String turns[] = turnLanes.split("\\|", -1);
        List<BLine> lst = new ArrayList<>();
        for (int i = 0; i < turns.length; i++) {
            BLine bLine = new BLine((i + 1), turns[i]);
            lst.add(bLine);
        }
        this.listLines = lst;
    }
}
