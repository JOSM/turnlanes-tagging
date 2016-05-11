package org.openstreetmap.josm.plugins.turnlanestagging.preset.ui;

/**
 *
 * @author ruben
 */
public class PresetTurnLane {
    
    private String id;
    private String name;
    private String tags;

    public PresetTurnLane(String id, String name, String tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    
}
