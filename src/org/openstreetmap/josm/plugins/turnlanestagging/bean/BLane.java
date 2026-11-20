// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.bean;

import java.io.Serializable;

/**
 *
 * @author ruben
 */
public class BLane implements Serializable {

    private int position;
    private String turn;
    private String change = "";
    private String type; //backward, fordward, both_ways
    private boolean bike;

    public BLane() {
    }

    public BLane(String type, int position, String turn) {
        this.position = position;
        this.turn = turn;
        this.type = type; // for unidirectional " unid""
    }

    public BLane(String type, int position, String turn, String change) {
        this.position = position;
        this.turn = turn;
        this.change = change;
        this.type = type; // for unidirectional " unid""
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    /**
     * Whether this lane is a bicycle lane ({@code bicycle:lanes=designated}).
     *
     * @return {@code true} if the lane is a designated bicycle lane
     */
    public boolean isBike() {
        return bike;
    }

    public void setBike(boolean bike) {
        this.bike = bike;
    }

}
