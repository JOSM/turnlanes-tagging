package org.openstreetmap.josm.plugins.turnlanestagging.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {

    /**
     * Test method for
     * {@link org.openstreetmap.josm.plugins.turnlanestagging.util.Util#setNoneOnEmpty(java.lang.String)}.
     */
    @Test
    public void testSetNoneOnEmpty() {
        assertEquals("left", Util.setNoneOnEmpty("left"));
        assertEquals("left|none", Util.setNoneOnEmpty("left|"));
        assertEquals("none|right", Util.setNoneOnEmpty("|right"));
        assertEquals("left|none|none", Util.setNoneOnEmpty("left||"));
        assertEquals("left|none|none|none", Util.setNoneOnEmpty("left|||"));
        assertEquals("left|none|right", Util.setNoneOnEmpty("left||right"));
        assertEquals("left|none|none|right", Util.setNoneOnEmpty("left|||right"));
        assertEquals("left|none|none|right", Util.setNoneOnEmpty("left|none||right"));
        assertEquals("left|none|none|right", Util.setNoneOnEmpty("left||none|right"));
        assertEquals("left|none|none|none|right", Util.setNoneOnEmpty("left||none||right"));
        assertEquals("left|none|through|none|right", Util.setNoneOnEmpty("left||through||right"));
        assertEquals("left;through|none|none|right", Util.setNoneOnEmpty("left;through|||right"));
        assertEquals("none|none|none|slight_right", Util.setNoneOnEmpty("none|none||slight_right"));
        assertEquals("none|none|none|slight_right", Util.setNoneOnEmpty("|||slight_right"));
    }

}
