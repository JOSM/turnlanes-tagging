package org.openstreetmap.josm.plugins.turnlanestagging.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author ruben
 */
public class BLanes {

    String type;
    List<BLane> lanes= new ArrayList<>();

    public BLanes() {
    }

    public BLanes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BLane> getLanes() {
        return lanes;
    }

    public void setLanes(List<BLane> newLanes) {

        Map<Integer, BLane> map = new TreeMap<>();
        for (int i = 0; i < newLanes.size(); i++) {
            map.put(newLanes.get(i).getPosition(), newLanes.get(i));
        }
        List<BLane> listLs = new ArrayList<>(map.values());
        this.lanes = listLs;

    }

    public String getTagturns() {
        String tagturns = "";
        for (int i = 0; i < lanes.size(); i++) {
            if (i == 0) {
                tagturns = lanes.get(i).getTurn();
            } else {
                tagturns = tagturns + "|" + lanes.get(i).getTurn();
            }
        }
        return tagturns;
    }

}
