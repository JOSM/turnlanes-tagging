// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor.preset;

import static org.openstreetmap.josm.tools.I18n.tr;
import static org.openstreetmap.josm.tools.I18n.trc;

import java.util.ArrayList;
import java.util.Collection;

import org.openstreetmap.josm.data.osm.Tag;
import org.openstreetmap.josm.gui.tagging.presets.TaggingPreset;
import org.openstreetmap.josm.gui.tagging.presets.TaggingPresetItem;
import org.openstreetmap.josm.gui.tagging.presets.items.KeyedItem;
import org.openstreetmap.josm.gui.tagging.presets.items.Optional;

public class AdvancedTag extends Tag {
    private String displayName;
    private boolean optional = false;

    public AdvancedTag() {
    }

    public AdvancedTag(String key, String value) {
        super(key, value);
    }

    public AdvancedTag(String key) {
        super(key);
    }

    public AdvancedTag(Tag tag) {
        super(tag);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
    
    public static Collection<AdvancedTag> forTaggingPreset(TaggingPreset preset) {
        Collection<AdvancedTag> result = new ArrayList<>();
        boolean isOptional = false;
        for (TaggingPresetItem item : preset.data) {
            if (item instanceof KeyedItem) {
                KeyedItem ki = (KeyedItem)item;
                for (String value : ki.getValues()) {
                    AdvancedTag tag = new AdvancedTag(ki.key, value);
                    tag.setOptional(isOptional);
                    if (ki.text != null) {
                        if (ki.text_context != null) {
                            tag.setDisplayName(trc(ki.text_context, ki.text));
                        } else {
                            tag.setDisplayName(tr(ki.text));
                        }
                    }
                    result.add(tag);
                }
            } else if (item instanceof Optional) {
                isOptional = true;
            }
            // TODO optional stuff is not perfect: all items found after an optional item will be considered as optional
            // even if they are outside the <optional> element.
            // We can't easily access this information. The plugin previously reimplemented a lot of JOSM code to determine that.
            // This duplicated code has been stripped off. It's not really important as optional items are almost always
            // located at the end of an item.
        }
        return result;
    }
}
