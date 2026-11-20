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

    public String getTagchanges() {
        String tagchanges = "";
        for (int i = 0; i < lanes.size(); i++) {
            if (i == 0) {
                tagchanges = lanes.get(i).getChange();
            } else {
                tagchanges = tagchanges + "|" + lanes.get(i).getChange();
            }
        }
        return tagchanges;
    }

    public void setStringLanes(String type, String turnLanes) {
        applyLaneValues(type, turnLanes.split("\\|", -1), null);
    }

    public void setStringChanges(String type, String changeLanes) {
        applyLaneValues(type, null, changeLanes.split("\\|", -1));
    }

    /**
     * Builds/updates the lane list by position, preserving the turn value while
     * setting changes (or vice versa). This keeps turn and change information in
     * sync when a way carries both {@code turn:lanes} and {@code change:lanes}
     * tags, regardless of the order they are processed in.
     */
    private void applyLaneValues(String type, String[] turns, String[] changes) {
        Map<Integer, BLane> existing = new TreeMap<>();
        for (BLane lane : lanes) {
            existing.put(lane.getPosition(), lane);
        }

        int count = lanes.size();
        if (turns != null) {
            count = Math.max(count, turns.length);
        }
        if (changes != null) {
            count = Math.max(count, changes.length);
        }

        List<BLane> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int position = i + 1;
            BLane previous = existing.get(position);
            String turn = turns != null ? normalizeTurn(turns[i]) : (previous != null ? previous.getTurn() : "");
            String change = changes != null ? changes[i] : (previous != null ? previous.getChange() : "");
            result.add(new BLane(type, position, turn, change));
        }
        this.lanes = result;
    }

    private String normalizeTurn(String turn) {
        if (turn.equals("none")) {
            return "";
        }
        if (turn.indexOf(";") > 0) {
            return sortTurns(turn);
        }
        return turn;
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
