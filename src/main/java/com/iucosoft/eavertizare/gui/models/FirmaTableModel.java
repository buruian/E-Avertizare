/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.gui.models;

import com.iucosoft.eavertizare.dao.FirmaDaoIntf;
import com.iucosoft.eavertizare.model.Client;
import com.iucosoft.eavertizare.model.Firma;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author valera_
 */
public class FirmaTableModel extends DefaultTableModel {

    @Autowired
    FirmaDaoIntf firmaDao;
    private final String[] columns = {"Nr", "Nume", "Adresa", "Descriere", "Mesaj clienti"};

    public FirmaTableModel() {
        for (String numeColoana : columns) {
            super.addColumn(numeColoana);
        }
    }

    public void refreshModel() {
        clearModel();
        List<Firma> listaFirme = firmaDao.findAll();
        List<Client> listaClienti;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        int nr = 1;

        //   rowAllClients();
        for (Firma firma : listaFirme) {

            Vector rowData = new Vector();
            rowData.addElement(nr++);
            rowData.addElement(firma.getNumeFirma());
            rowData.addElement(firma.getAdresaFirma());
            rowData.addElement(firma.getDescriereFirma());
            rowData.addElement(firma.getMesajPentruClienti());
            super.addRow(rowData);
        }
    }

    private void clearModel() {
        for (int i = super.getRowCount() - 1; i >= 0; i--) {
            super.removeRow(i);
        }
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

}
