package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.util.ArrayList;
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
        listBLines.add(new BLane(1, "left"));
        listBLines.add(new BLane(2, "through"));
        listBLines.add(new BLane(3, "through"));
        BRoad bRoad = new BRoad();
        bRoad.setName("Unidirectional");
        bRoad.setListLines(listBLines);
        listBRoads.add(bRoad);

        //Road 2
        List<BLane> listBLines2 = new ArrayList<>();
        listBLines2.add(new BLane(1, "left"));
        listBLines2.add(new BLane(2, "right"));
        listBLines2.add(new BLane(3, "through"));
        BRoad bRoad2 = new BRoad();
        bRoad2.setName("Unidirectional");

        bRoad2.setListLines(listBLines2);
        listBRoads.add(bRoad2);

        //Bidirectional 1
        BLanes bLanesA = new BLanes("backward");
        List<BLane> lbA = new ArrayList<>();
        lbA.add(new BLane(1, "left"));
        lbA.add(new BLane(2, "right"));
        lbA.add(new BLane(3, "through"));
        bLanesA.setLanes(lbA);

        BLanes bLanesB = new BLanes("both_way");
        List<BLane> lbB = new ArrayList<>();
        lbB.add(new BLane(1, "left"));
        bLanesB.setLanes(lbB);

        BLanes bLanesC = new BLanes("backward");
        List<BLane> lbC = new ArrayList<>();
        lbC.add(new BLane(1, "left"));
        lbC.add(new BLane(2, "right"));
        lbC.add(new BLane(3, "through"));
        bLanesC.setLanes(lbC);

        BRoad bRoad3 = new BRoad();
        bRoad3.setName("Bidirectional");

        bRoad3.setLanesA(bLanesA);
        bRoad3.setLanesB(bLanesB);
        bRoad3.setLanesC(bLanesC);

     listBRoads.add(bRoad3);

        //Bidirectional
        return listBRoads;

    }

    public List<BRoad> dataPresetBidirectional() {
        //oneway=yes
        //lanes=3
        //turn:lanes=left||
        //placement=right_of:2
        List<BRoad> listBRoads = new ArrayList<>();
        //Road 1
        List<BLane> listBLines = new ArrayList<>();
        listBLines.add(new BLane(1, "left"));
        listBLines.add(new BLane(2, "through"));
        listBLines.add(new BLane(3, "through"));
        BRoad bRoad = new BRoad();
        bRoad.setName("Unidirectional");
        bRoad.setListLines(listBLines);
        listBRoads.add(bRoad);

        //Road 2
        List<BLane> listBLines2 = new ArrayList<>();
        listBLines2.add(new BLane(1, "left"));
        listBLines2.add(new BLane(2, "right"));
        listBLines2.add(new BLane(3, "through"));
        BRoad bRoad2 = new BRoad();
        bRoad2.setName("Unidirectional");
        bRoad2.setListLines(listBLines2);
        listBRoads.add(bRoad2);

        return listBRoads;

    }

    public BRoad defaultData(int lines) {
        BRoad bRoad = new BRoad();
        List<BLane> listBLines = new ArrayList<>();
        for (int m = 0; m < lines; m++) {
            BLane bLine = new BLane((m + 1), "");
            listBLines.add(bLine);
        }
        bRoad.setName("Road test");
        bRoad.setListLines(listBLines);
        return bRoad;
    }

    public BLanes defaultLanes(int lines) {
        BLanes bLanes = new BLanes();
        List<BLane> list = new ArrayList<>();
        for (int m = 0; m < lines; m++) {
            BLane bLine = new BLane((m + 1), "");
            list.add(bLine);
        }
        bLanes.setLanes(list);
        return bLanes;
    }

}
