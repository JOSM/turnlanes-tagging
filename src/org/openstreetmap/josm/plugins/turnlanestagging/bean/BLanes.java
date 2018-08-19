// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author ruben
 */
public class BLanes implements Serializable {

    String type;
    List<BLane> lanes = new ArrayList<>();

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
        changeDirectionLanes(type);

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

    public void setStringLanes(String type, String turnLanes) {
        String[] turns = turnLanes.split("\\|", -1);
        List<BLane> lst = new ArrayList<>();
        for (int i = 0; i < turns.length; i++) {
            //remove none tags
            if (turns[i].equals("none")) {
                turns[i] = "";
            } else if (turns[i].indexOf(";") > 0) {
                turns[i] = sortTurns(turns[i]);
            }
            BLane bLine = new BLane(type, (i + 1), turns[i]);
            lst.add(bLine);
        }
        this.lanes = lst;
    }

    private String sortTurns(String roadTurns) {
        //https://wiki.openstreetmap.org/wiki/Key:turn
        List<String> turnsList = Arrays.asList(
                "reverse", "sharp_left", "left", "slight_left", "merge_to_right", "through",
                "reversible", "merge_to_left", "slight_right", "right", "sharp_right");
        List<String> roadTurnsList = Arrays.asList(roadTurns.split(";"));
        List<String> newRoadTurns = new ArrayList<>();
        for (String e : turnsList) {
            if (roadTurnsList.indexOf(e) > -1) {
                newRoadTurns.add(e);
            }
        }
        return newRoadTurns.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ";");
    }

    public void changeDirectionLanes(String type) {
        for (int k = 0; k < lanes.size(); k++) {
            lanes.get(k).setType(type);
        }
    }
}
