/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.gui.models;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author valera_
 */
public class MyImageRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //  ImageIcon v = new ImageIcon(getClass().getResource("/images/trimis.png"));
        JLabel lbl = new JLabel();
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String s = table.getModel().getValueAt(row, 7).toString();
        boolean isTrue = Boolean.parseBoolean(s);
        if (isTrue) {
//               c.setForeground(new Color(165,42,42));
//                c.setBackground(new Color(240, 240, 240, 255));
//               c.setBackground(new Color(205,201,201));
//               
            //table.setValueAt(new ImageIcon(getClass().getResource("/images/trimis.png")), row, 7);
            //lbl.setText((String) value);
            lbl.setIcon(new ImageIcon(getClass().getResource("/images/trimis.png")));
           setHorizontalAlignment(CENTER);
        //    table.getColumnModel().getColumn(0).setCellRenderer( JLabel.CENTER );
            
            c.setBackground(new Color(220,220,220));
       

        } else {
            lbl.setIcon(new ImageIcon(getClass().getResource("/images/trimis-stop.png")));

        }
//            else{
//               c.setForeground(Color.black);
//              // c.setBackground(Color.LIGHT_GRAY);
//               c.setBackground(new Color(220,220,220));
//               
//                
//            }
//            if (isSelected) {
//                c.setBackground(new Color(30,144,255));
//        }
//            table.repaint();
//            return c;

        return lbl;

    }
}
