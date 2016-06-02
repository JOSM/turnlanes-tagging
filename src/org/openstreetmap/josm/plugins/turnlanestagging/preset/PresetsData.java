package org.openstreetmap.josm.plugins.turnlanestagging.preset;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLane;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLanes;

/**
 *
 * @author ruben
 */
public class PresetsData {

    public List<BRoad> dataPreset() {
        List<BRoad> listBRoads = new ArrayList<>();

        //oneway=yes
        //lanes=3
        //turn:lanes=left||
        //placement=right_of:2
        //road 1
        BLanes bl1 = new BLanes("unid");

        List<BLane> listbl1 = new ArrayList<>();
        listbl1.add(new BLane("unid", 1, "left"));
        listbl1.add(new BLane("unid", 2, "through"));
        listbl1.add(new BLane("unid", 3, "through"));
        bl1.setLanes(listbl1);
        BRoad bRoad = new BRoad();
        bRoad.setName("Unidirectional");
        bRoad.setLanesUnid(bl1);
        listBRoads.add(bRoad);

        //Road 2
        BLanes bl2 = new BLanes("unid");
        List<BLane> listbl2 = new ArrayList<>();
        listbl2.add(new BLane("unid", 1, "left"));
        listbl2.add(new BLane("unid", 2, "right"));
        listbl2.add(new BLane("unid", 3, "through"));
        bl2.setLanes(listbl2);
        BRoad bRoad2 = new BRoad();
        bRoad2.setName("Unidirectional");
        bRoad2.setLanesUnid(bl2);
        listBRoads.add(bRoad2);

        //Bidirectional 1
        BLanes bLanesA = new BLanes("forward");
        List<BLane> lbA = new ArrayList<>();
        lbA.add(new BLane("forward", 1, "left;through"));
        lbA.add(new BLane("forward", 2, "right"));
        lbA.add(new BLane("forward", 3, "through"));
        bLanesA.setLanes(lbA);

        BLanes bLanesB = new BLanes("both_ways");
        List<BLane> lbB = new ArrayList<>();
        lbB.add(new BLane("both_ways", 1, "left"));
        bLanesB.setLanes(lbB);

        BLanes bLanesC = new BLanes("backward");
        List<BLane> lbC = new ArrayList<>();
        lbC.add(new BLane("backward", 1, "left"));
        lbC.add(new BLane("backward", 2, "right"));
        lbC.add(new BLane("backward", 3, "through"));
        bLanesC.setLanes(lbC);

        BRoad bRoad3 = new BRoad();
        bRoad3.setName("Bidirectional");
        bRoad3.setLanesA(bLanesA);
        bRoad3.setLanesB(bLanesB);
        bRoad3.setLanesC(bLanesC);
        listBRoads.add(bRoad3);

        //Bidirectional 2
        BLanes bLanes2A = new BLanes("forward");
        List<BLane> lb2A = new ArrayList<>();
        lb2A.add(new BLane("forward", 1, "left"));
        lb2A.add(new BLane("forward", 2, "right;through"));
        lb2A.add(new BLane("forward", 3, "through"));
        bLanes2A.setLanes(lb2A);
        //made a empty  clall
        BLanes bLanes2C = new BLanes("backward");
        List<BLane> lb2C = new ArrayList<>();
        lb2C.add(new BLane("backward", 1, "left"));
        lb2C.add(new BLane("backward", 2, "right"));
        bLanes2C.setLanes(lb2C);
        BRoad bRoad4 = new BRoad();
        bRoad4.setName("Bidirectional");
        bRoad4.setLanesA(bLanes2A);
        bRoad4.setLanesC(bLanes2C);
        listBRoads.add(bRoad4);

        //Bidirectional 3
        BRoad bRoad5 = new BRoad();
        bRoad5.setName("Bidirectional");
        BLanes bLA5 = new BLanes("forward");
        bLA5.setStringLanes("forward", "left|through;left|through|right");
        bRoad5.setLanesA(bLA5);
        listBRoads.add(bRoad5);

        //Bidirectional 4
        BRoad bRoad6 = new BRoad();
        bRoad6.setName("Bidirectional");
        BLanes bLA6 = new BLanes("backward");
        bLA6.setStringLanes("backward", "left;through|through");
        bRoad6.setLanesC(bLA6);
        listBRoads.add(bRoad6);

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
        BLanes bLanes = new BLanes();
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
}
