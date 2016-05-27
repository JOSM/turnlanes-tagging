package org.openstreetmap.josm.plugins.turnlanestagging.bean;

/**
 *
 * @author ruben
 */
public class BLane {  
    private int position;
    private String turn;
    private String type; //backward, fordward, bothway

    public BLane() {
    }

    
    public BLane(int position, String turn) {
        this.position = position;
        this.turn = turn;
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
