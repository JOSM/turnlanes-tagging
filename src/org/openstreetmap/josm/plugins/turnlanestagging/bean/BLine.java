package org.openstreetmap.josm.plugins.turnlanestagging.bean;

/**
 *
 * @author ruben
 */
public class BLine {  
    private int position;
    private String turn;

    public BLine() {
    }

    
    public BLine(int position, String turn) {
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
}
