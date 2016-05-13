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
    
    

//    public BLine(String id, String name, String tags) {
//        this.id = id;
//        this.name = name;
//        this.turn = tags;
//    }
//
//    
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTags() {
//        return turn;
//    }
//
//    public void setTags(String tags) {
//        this.turn = tags;
//    }
    
}
