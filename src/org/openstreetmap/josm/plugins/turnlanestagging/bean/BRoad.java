// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.bean;

import java.io.Serializable;

/**
 *
 * @author ruben
 */
public class BRoad implements Serializable {

    String name;
    private BLanes lanesUnid = new BLanes();
    private BLanes lanesA = new BLanes();
    private BLanes lanesB = new BLanes();
    private BLanes lanesC = new BLanes();
    private boolean none;

    public BRoad() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumLanesoo() {
        return lanesUnid.getLanes().size();
    }

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

    public boolean isNone() {
        return none;
    }

    public void setNone(boolean none) {
        this.none = none;
    }

    public String turns() {
        return lanesA.getLanes().size() + "-" + lanesA.getTagturns() + "-" +
               lanesB.getLanes().size() + "-" + lanesB.getTagturns() + "-" +
               lanesC.getLanes().size() + "-" + lanesC.getTagturns();
    }
}
