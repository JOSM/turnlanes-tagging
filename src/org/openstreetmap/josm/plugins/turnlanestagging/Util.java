/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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