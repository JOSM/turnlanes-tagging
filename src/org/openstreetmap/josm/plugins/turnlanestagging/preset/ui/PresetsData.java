package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

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
        //oneway=yes
        //lanes=3
        //turn:lanes=left||
        //placement=right_of:2
        List<BRoad> listBRoads = new ArrayList<>();
        //Road 1
        List<BLane> listBLines = new ArrayList<>();
        listBLines.add(new BLane("unid", 1, "left"));
        listBLines.add(new BLane("unid", 2, "through"));
        listBLines.add(new BLane("unid", 3, "through"));
        BRoad bRoad = new BRoad();
        bRoad.setName("Unidirectional");
        bRoad.setListLines(listBLines);
        listBRoads.add(bRoad);

        //Road 2
        List<BLane> listBLines2 = new ArrayList<>();
        listBLines2.add(new BLane("unid", 1, "left"));
        listBLines2.add(new BLane("unid", 2, "right"));
        listBLines2.add(new BLane("unid", 3, "through"));
        BRoad bRoad2 = new BRoad();
        bRoad2.setName("Unidirectional");

        bRoad2.setListLines(listBLines2);
        listBRoads.add(bRoad2);

        //Bidirectional 1
        BLanes bLanesA = new BLanes("backward");
        List<BLane> lbA = new ArrayList<>();
        lbA.add(new BLane("backward", 1, "left"));
        lbA.add(new BLane("backward", 2, "right"));
        lbA.add(new BLane("backward", 3, "through"));
        bLanesA.setLanes(lbA);

        BLanes bLanesB = new BLanes("both_way");
        List<BLane> lbB = new ArrayList<>();
        lbB.add(new BLane("both_way", 1, "left"));
        bLanesB.setLanes(lbB);

        BLanes bLanesC = new BLanes("forward");
        List<BLane> lbC = new ArrayList<>();
        lbC.add(new BLane("forward", 1, "left"));
        lbC.add(new BLane("forward", 2, "right"));
        lbC.add(new BLane("forward", 3, "through"));
        bLanesC.setLanes(lbC);

        BRoad bRoad3 = new BRoad();
        bRoad3.setName("Bidirectional");

        bRoad3.setLanesA(bLanesA);
        bRoad3.setLanesB(bLanesB);
        bRoad3.setLanesC(bLanesC);

        listBRoads.add(bRoad3);
        //Bidirectional 2
        BLanes bLanes2A = new BLanes("backward");
        List<BLane> lb2A = new ArrayList<>();
        lb2A.add(new BLane("backward", 1, "left"));
        lb2A.add(new BLane("backward", 2, "right;through"));
        lb2A.add(new BLane("backward", 3, "through"));
        bLanes2A.setLanes(lb2A);
        //made a empty  clall
   
        lb2A.add(new BLane("backward", 1, "left"));
        lb2A.add(new BLane("backward", 2, "right;through"));
        lb2A.add(new BLane("backward", 3, "through"));
        bLanes2A.setLanes(lb2A);

        BLanes bLanes2C = new BLanes("forward");
        List<BLane> lb2C = new ArrayList<>();
        lb2C.add(new BLane("forward", 1, "left"));
        lb2C.add(new BLane("forward", 2, "right"));
        bLanes2C.setLanes(lb2C);

        BRoad bRoad4 = new BRoad();
        bRoad4.setName("Bidirectional");

        bRoad4.setLanesA(bLanes2A);
        bRoad4.setLanesC(bLanes2C);

        listBRoads.add(bRoad4);

        //Bidirectional
        return listBRoads;

    }

    public BRoad defaultDataUnidirectional(int lines) {
        BRoad bRoad = new BRoad();
        List<BLane> listBLines = new ArrayList<>();
        for (int m = 0; m < lines; m++) {
            BLane bLine = new BLane("unid", (m + 1), "");
            listBLines.add(bLine);
        }
        bRoad.setName("Unidirectional");
        bRoad.setListLines(listBLines);
        return bRoad;
    }

    public BLanes defaultLanes(String type, int lines) {
        BLanes bLanes = new BLanes();
        List<BLane> list = new ArrayList<>();
        for (int m = 0; m < lines; m++) {
            BLane bLine = new BLane(type, (m + 1), "");
            list.add(bLine);
        }
        bLanes.setLanes(list);
        return bLanes;
    }

}
