/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.gui.models;

import com.iucosoft.eavertizare.dao.FirmaDaoIntf;
import com.iucosoft.eavertizare.model.Firma;
import java.awt.Frame;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author valera_
 */
public final class FirmeListModel extends DefaultListModel {

    @Autowired
    FirmaDaoIntf firmaDao;

    public void refreshModel() {
        super.clear();
        List<Firma> listaFirme = firmaDao.findAll();
        super.addElement("All firms");
        for (Firma firma : listaFirme) {
            super.addElement(firma.getNumeFirma());
        }
    }

    public void refreshModel(Frame frame, String numeFirma) {
        super.clear();
        Firma firma = firmaDao.findByName(numeFirma);
        if (firma != null) {
            super.addElement("All firms");
            super.addElement(firma.getNumeFirma());
        } else {
            refreshModel();
            JOptionPane.showMessageDialog(frame, "Nu exista firma cu asa nume = " + numeFirma,
                    "Error", JOptionPane.WARNING_MESSAGE);
        }

    }

}
