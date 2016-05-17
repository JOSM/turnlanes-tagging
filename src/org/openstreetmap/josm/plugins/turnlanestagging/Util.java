package org.openstreetmap.josm.plugins.turnlanestagging;

import javax.swing.JOptionPane;


/**
 *
 * @author ruben
 */
public class Util {
    public static void alert(Object object) {
        JOptionPane.showMessageDialog(null, object);
    }

    public static void print(Object object) {
        System.err.println(object);
    }
}