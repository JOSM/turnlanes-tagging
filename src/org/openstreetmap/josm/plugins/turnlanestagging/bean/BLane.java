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
    private String type; //backward, fordward, both_ways

    public BLane() {
    }

    public BLane(String type, int position, String turn) {
        this.position = position;
        this.turn = turn;
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

}
