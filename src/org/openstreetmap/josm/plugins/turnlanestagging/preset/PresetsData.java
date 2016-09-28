// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.preset;

import java.util.ArrayList;
import java.util.List;

import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLanes;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;

/**
 *
 * @author ruben
 */
public class PresetsData {

    public List<BRoad> dataPreset() {
        List<BRoad> listBRoads = new ArrayList<>();
        //Unidirectional
        //oneway=yes && lanes=3 && turn:lanes=left||
        String[] presetUnidirectional = {"left||"};
        for (int i = 0; i < presetUnidirectional.length; i++) {
            BRoad bRoadUnidireactinal = new BRoad();
            bRoadUnidireactinal.setName("Unidirectional");
            bRoadUnidireactinal.getLanesUnid().setStringLanes("unid", "left||");
            listBRoads.add(bRoadUnidireactinal);
        }

        //Bidirectional
        //turn:lanes:forward=left| && lanes:backward=1
        BRoad bRoadBi1 = new BRoad();
        bRoadBi1.setName("Bidirectional");
        BLanes bLanesB1A = new BLanes("forward");
        bLanesB1A.setStringLanes("forward", "left|");
        bRoadBi1.setLanesA(bLanesB1A);
        bRoadBi1.setLanesC(defaultLanes("backward", 1));
        listBRoads.add(bRoadBi1);

        //turn:lanes:backward=left| && lanes:forward=1
        BRoad bRoadBi2 = new BRoad();
        bRoadBi2.setName("Bidirectional");
        BLanes bLanesB2C = new BLanes("backward");
        bLanesB2C.setStringLanes("backward", "left|");
        bRoadBi2.setLanesC(bLanesB2C);
        bRoadBi2.setLanesA(defaultLanes("forward", 1));
        listBRoads.add(bRoadBi2);

        //turn:lanes:forward=left|| && lanes:backward=2
        BRoad bRoadBi3 = new BRoad();
        bRoadBi3.setName("Bidirectional");
        BLanes bLanesB3A = new BLanes("forward");
        bLanesB3A.setStringLanes("forward", "left||");
        bRoadBi3.setLanesA(bLanesB3A);
        bRoadBi3.setLanesC(defaultLanes("backward", 2));
        listBRoads.add(bRoadBi3);

        //turn:lanes:backward=left|| && lanes:forward=2
        BRoad bRoadBi4 = new BRoad();
        bRoadBi4.setName("Bidirectional");
        BLanes bLanesB4C = new BLanes("backward");
        bLanesB4C.setStringLanes("backward", "left||");
        bRoadBi4.setLanesC(bLanesB4C);
        bRoadBi4.setLanesA(defaultLanes("forward", 2));
        listBRoads.add(bRoadBi4);

        //        BLanes bl1 = new BLanes("unid");
        //
        //        List<BLane> listbl1 = new ArrayList<>();
        //        listbl1.add(new BLane("unid", 1, "left"));
        //        listbl1.add(new BLane("unid", 2, "through"));
        //        listbl1.add(new BLane("unid", 3, "through"));
        //        bl1.setLanes(listbl1);
        //        BRoad bRoad = new BRoad();
        //        bRoad.setName("Unidirectional");
        //        bRoad.setLanesUnid(bl1);
        //        listBRoads.add(bRoad);
        //
        //        //Road 2
        //        BLanes bl2 = new BLanes("unid");
        //        List<BLane> listbl2 = new ArrayList<>();
        //        listbl2.add(new BLane("unid", 1, "left"));
        //        listbl2.add(new BLane("unid", 2, "right"));
        //        listbl2.add(new BLane("unid", 3, "through"));
        //        bl2.setLanes(listbl2);
        //        BRoad bRoad2 = new BRoad();
        //        bRoad2.setName("Unidirectional");
        //        bRoad2.setLanesUnid(bl2);
        //        listBRoads.add(bRoad2);
        //
        //        //Bidirectional 1
        //        BLanes bLanesA = new BLanes("forward");
        //        List<BLane> lbA = new ArrayList<>();
        //        lbA.add(new BLane("forward", 1, "left;through"));
        //        lbA.add(new BLane("forward", 2, "right"));
        ////        lbA.add(new BLane("forward", 3, "through"));
        //        bLanesA.setLanes(lbA);
        //
        //        BLanes bLanesB = new BLanes("both_ways");
        //        List<BLane> lbB = new ArrayList<>();
        //        lbB.add(new BLane("both_ways", 1, "left;reverse"));
        //        bLanesB.setLanes(lbB);
        //
        //        BLanes bLanesC = new BLanes("backward");
        //        List<BLane> lbC = new ArrayList<>();
        //        lbC.add(new BLane("backward", 1, "left"));
        //        lbC.add(new BLane("backward", 2, "right"));
        ////        lbC.add(new BLane("backward", 3, "through"));
        //        bLanesC.setLanes(lbC);
        //
        //        BRoad bRoad3 = new BRoad();
        //        bRoad3.setName("Bidirectional");
        //        bRoad3.setLanesA(bLanesA);
        //        bRoad3.setLanesB(bLanesB);
        //        bRoad3.setLanesC(bLanesC);
        //        listBRoads.add(bRoad3);
        //
        //        //Bidirectional 2
        //        BLanes bLanes2A = new BLanes("forward");
        //        List<BLane> lb2A = new ArrayList<>();
        //        lb2A.add(new BLane("forward", 1, "left"));
        //        lb2A.add(new BLane("forward", 2, "right;through"));
        //        lb2A.add(new BLane("forward", 3, "through"));
        //        bLanes2A.setLanes(lb2A);
        //        //made a empty  clall
        //        BLanes bLanes2C = new BLanes("backward");
        //        List<BLane> lb2C = new ArrayList<>();
        //        lb2C.add(new BLane("backward", 1, "left"));
        //        lb2C.add(new BLane("backward", 2, "right"));
        //        bLanes2C.setLanes(lb2C);
        //        BRoad bRoad4 = new BRoad();
        //        bRoad4.setName("Bidirectional");
        //        bRoad4.setLanesA(bLanes2A);
        //        bRoad4.setLanesC(bLanes2C);
        //        listBRoads.add(bRoad4);
        //
        //        //Bidirectional 3
        //        BRoad bRoad5 = new BRoad();
        //        bRoad5.setName("Bidirectional");
        //        BLanes bLA5 = new BLanes("forward");
        //        bLA5.setStringLanes("forward", "left|through;left|through|right");
        //        bRoad5.setLanesA(bLA5);
        //        listBRoads.add(bRoad5);
        //
        //        //Bidirectional 4
        //        BRoad bRoad6 = new BRoad();
        //        bRoad6.setName("Bidirectional");
        //        BLanes bLA6 = new BLanes("backward");
        //        bLA6.setStringLanes("backward", "left;through|through");
        //        bRoad6.setLanesC(bLA6);
        //        listBRoads.add(bRoad6);
        //Bidirectional
        return listBRoads;

    }

    public BRoad defaultRoadUnidirectional(int lanes) {
        BRoad bRoad = new BRoad();
        BLanes bLanes = new BLanes("unid");
        List<BLane> listLanes = new ArrayList<>();
        for (int m = 0; m < lanes; m++) {
            BLane bLine = new BLane("unid", (m + 1), "");
            listLanes.add(bLine);
        }
        bLanes.setLanes(listLanes);
        bRoad.setName("Unidirectional");
        bRoad.setLanesUnid(bLanes);
        return bRoad;
    }

    public BLanes defaultLanes(String type, int lanes) {
        BLanes bLanes = new BLanes(type);
        List<BLane> list = new ArrayList<>();
        for (int m = 0; m < lanes; m++) {
            BLane bLine = new BLane(type, (m + 1), "");
            list.add(bLine);
        }
        bLanes.setLanes(list);
        return bLanes;
    }

    public BRoad defaultRoadBidirectional(String typeA, int lanesA, String typeC, int lanesC) {
        BLanes bLanes2A = new BLanes(typeA);
        List<BLane> lb2A = new ArrayList<>();
        for (int i = 0; i < lanesA; i++) {
            lb2A.add(new BLane(typeA, (i + 1), ""));
        }
        bLanes2A.setLanes(lb2A);
        BLanes bLanes2C = new BLanes(typeC);
        List<BLane> lb2C = new ArrayList<>();
        for (int j = 0; j < lanesC; j++) {
            lb2C.add(new BLane(typeC, (j + 1), ""));
        }
        bLanes2C.setLanes(lb2C);
        BRoad bRoad = new BRoad();
        bRoad.setName("Bidirectional");
        bRoad.setLanesA(bLanes2A);
        bRoad.setLanesC(bLanes2C);
        return bRoad;
    }

    public BLanes addLanes(BLanes bLanes, String type, int lanes) {
        //remove  merge_to_left and merge_to_right fro midle lanes
        for (int j = 1; j < bLanes.getLanes().size(); j++) {
            String[] dirs = bLanes.getLanes().get(j).getTurn().split("\\;", -1);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < dirs.length; i++) {
                if (!dirs[i].equals("merge_to_right") && !dirs[i].equals("merge_to_left")) {
                    list.add(dirs[i]);
                }
            }
            String t = list.toString().replace("[", "").replace("]", "").replace(", ", ";");
            bLanes.getLanes().get(j).setTurn(t);
        }

        for (int m = 0; m < lanes; m++) {
            BLane bLine = new BLane(type, bLanes.getLanes().size() + 1, "");
            bLanes.getLanes().add(bLine);
        }
        return bLanes;
    }

    public BLanes removeLanes(BLanes bLanes, int lanes) {
        int numLanes = bLanes.getLanes().size();
        for (int i = 1; i <= lanes; i++) {
            bLanes.getLanes().remove(numLanes - i);
        }
        return bLanes;
    }
}
