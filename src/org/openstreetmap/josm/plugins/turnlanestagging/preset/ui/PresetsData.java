/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BLine;
import org.openstreetmap.josm.plugins.turnlanestagging.bean.BRoad;

/**
 *
 * @author ruben
 */
public class PresetsData {

    public List<BRoad> data() {
        List<BRoad> listBRoads = new LinkedList<>();
        for (int k = 0; k < 10; k++) {
            BRoad bRoad = new BRoad();
            List<BLine> listBLines = new LinkedList<>();
            for (int m = 0; m < (k+1); m++) {
                BLine bLine = new BLine((m + 1), "left");
                listBLines.add(bLine);
            }
            bRoad.setName("Road " + (k + 1));
            bRoad.setListLines(listBLines);
            listBRoads.add(bRoad);
        }
        return listBRoads;

    }

    public BRoad defaultData(int lines) {
        BRoad bRoad = new BRoad();
        List<BLine> listBLines = new LinkedList<>();
        for (int m = 0; m < lines; m++) {
            BLine bLine = new BLine((m + 1), "left");
            listBLines.add(bLine);
        }
        bRoad.setName("Road test");
        bRoad.setListLines(listBLines);
        return bRoad;
    }
}
