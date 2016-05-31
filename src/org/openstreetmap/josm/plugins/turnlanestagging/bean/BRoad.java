package org.openstreetmap.josm.plugins.turnlanestagging.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.openstreetmap.josm.plugins.turnlanestagging.util.Util;

/**
 *
 * @author ruben
 */
public class BRoad implements Serializable {

    String name;
//    List<BLane> lanes;
    private BLanes lanesUnid = new BLanes();
    private BLanes lanesA = new BLanes();
    private BLanes lanesB = new BLanes();
    private BLanes lanesC = new BLanes();

    public BRoad() {
    }

//    public BRoad(String name, List<BLane> listLines) {
//        this.name = name;
//        this.lanes = listLines;
//    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<BLane> getListLines() {
//        return lanes;
//    }
//    public void setListLines(List<BLane> newLines) {
//
//        Map<Integer, BLane> map = new TreeMap<>();
//        for (int i = 0; i < newLines.size(); i++) {
//            map.put(newLines.get(i).getPosition(), newLines.get(i));
//        }
//        List<BLane> listLs = new ArrayList<>(map.values());
//        this.lanes = listLs;
//    }
//
//    public String getTagturns() {
//        String tagturns = "";
//        for (int i = 0; i < lanes.size(); i++) {
//            if (i == 0) {
//                tagturns = lanes.get(i).getTurn();
//            } else {
//                tagturns = tagturns + "|" + lanes.get(i).getTurn();
//            }
//        }
//        return tagturns;
//    }
    public int getNumLanes() {
        return lanesUnid.getLanes().size();
    }

//    public void setStringLanes(String turnLanes) {
//        String turns[] = turnLanes.split("\\|", -1);
//        List<BLane> lst = new ArrayList<>();
//        for (int i = 0; i < turns.length; i++) {
//            //remove none tags
//            if (turns[i].equals("none")) {
//                turns[i] = "";
//            }
//            BLane bLine = new BLane("unid", (i + 1), turns[i]);
//            lst.add(bLine);
//        }
//        this.lanes = lst;
//    }
    public int getNumLanesBidirectional() {
        int numdirc = 0;
        if (lanesA != null) {
            numdirc = numdirc + lanesA.getLanes().size();
        }
        if (lanesB != null) {
            numdirc = numdirc + lanesB.getLanes().size();
        }
        if (lanesC != null) {
            numdirc = numdirc + lanesC.getLanes().size();
        }
        return numdirc;
    }

    public BLanes getLanesA() {
        return lanesA;
    }

    public void setLanesA(BLanes lanesA) {
        this.lanesA = lanesA;
    }

    public BLanes getLanesB() {
        return lanesB;
    }

    public void setLanesB(BLanes lanesB) {
        this.lanesB = lanesB;
    }

    public BLanes getLanesC() {
        return lanesC;
    }

    public void setLanesC(BLanes lanesC) {
        this.lanesC = lanesC;
    }

    //for unidirectional
    public BLanes getLanesUnid() {
        return lanesUnid;
    }

    public void setLanesUnid(BLanes Lanes) {
        this.lanesUnid = Lanes;
    }

}
