/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.gui.models;

import com.iucosoft.eavertizare.dao.ClientsDaoIntf;
import com.iucosoft.eavertizare.dao.FirmaDaoIntf;
import com.iucosoft.eavertizare.model.Client;
import com.iucosoft.eavertizare.model.Firma;
import java.awt.Color;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author valera_
 */
public final class ClientiTableModel extends DefaultTableModel {

    @Autowired
    ClientsDaoIntf clientsDao;
    @Autowired
    FirmaDaoIntf firmaDao;
    private final String[] columns = {"Nr", "Nume", "Prenume", "Telefon", "Email", "Expirare", "Firma", "✔"};
 
    public ClientiTableModel() {
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
            listaClienti = clientsDao.findAllClientsForFirmaLocal(firma);
            for (Client client : listaClienti) {

                Vector rowData = new Vector();
                rowData.addElement(nr++);
                rowData.addElement(client.getNume());
                rowData.addElement(client.getPrenume());
                rowData.addElement(client.getNrTelefon());
                rowData.addElement(client.getEmail());
                rowData.addElement(sdf.format(client.getDateExpirare()));
                rowData.addElement(client.getFirma().getNumeFirma());
              //  rowData.addElement(new Boolean(true));
                rowData.addElement(client.isTrimis());
                super.addRow(rowData);
            }
        }
    }

    public void refreshModel(String numeFirma) {
        clearModel();
        Firma firma = firmaDao.findByName(numeFirma);
        List<Client> listaClienti = clientsDao.findAllClientsForFirmaLocal(firma);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        int nr = 1;

        //   rowAllClients();
        for (Client client : listaClienti) {

            Vector rowData = new Vector();
            rowData.addElement(nr++);
            rowData.addElement(client.getNume());
            rowData.addElement(client.getPrenume());
            rowData.addElement(client.getNrTelefon());
            rowData.addElement(client.getEmail());
            rowData.addElement(sdf.format(client.getDateExpirare()));
            rowData.addElement(client.getFirma().getNumeFirma());
            rowData.addElement(client.isTrimis());
            super.addRow(rowData);
        }
    }

    private void clearModel() {
        for (int i = super.getRowCount() - 1; i >= 0; i--) {
            super.removeRow(i);
        }
    }

    private void setColums() {
        for (String numeColoana : columns) {
            super.addColumn(numeColoana);
        }
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

//    @Override
//    public Class<?> getColumnClass(int columnIndex) {
//
//        if (columnIndex == 7) {
//            return Boolean.class;
//        } else {
//            return String.class;
//        }
//    }
    private void rowAllClients() {
        Vector rowAll = new Vector();
        rowAll.addElement(null);
        rowAll.addElement(null);
        rowAll.addElement(null);
        rowAll.addElement(null);
        rowAll.addElement("All clients");
        rowAll.addElement(null);
        rowAll.addElement(null);
        rowAll.addElement(null);

        super.addRow(rowAll);
    }

    public void findClient(Frame frame, String nume) {
        clearModel();
        Client client;
        List<Client> listaClienti = null;
        List<Firma> listaFirme = firmaDao.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        int nr = 1;
        Vector rowData = null;
        for (Firma firma : listaFirme) {
            client = clientsDao.findClient(firma, nume);
            if (client != null) {
                rowData = new Vector();
                rowData.addElement(nr++);
                rowData.addElement(client.getNume());
                rowData.addElement(client.getPrenume());
                rowData.addElement(client.getNrTelefon());
                rowData.addElement(client.getEmail());
                rowData.addElement(sdf.format(client.getDateExpirare()));
                rowData.addElement(client.getFirma().getNumeFirma());
                rowData.addElement(client.isTrimis());

                super.addRow(rowData);
            }
        }
        if (rowData == null) {
            refreshModel();
            JOptionPane.showMessageDialog(frame, "Nu exista client cu asa nume = " + nume,
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void findClient(Frame frame, String nume, String prenume) {
        clearModel();
        Client client;
        List<Client> listaClienti = null;
        List<Firma> listaFirme = firmaDao.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        int nr = 1;
        Vector rowData = null;
        for (Firma firma : listaFirme) {
            client = clientsDao.findClient(firma, nume, prenume);
            if (client != null) {
                rowData = new Vector();
                rowData.addElement(nr++);
                rowData.addElement(client.getNume());
                rowData.addElement(client.getPrenume());
                rowData.addElement(client.getNrTelefon());
                rowData.addElement(client.getEmail());
                rowData.addElement(sdf.format(client.getDateExpirare()));
                rowData.addElement(client.getFirma().getNumeFirma());
                rowData.addElement(client.isTrimis());
                super.addRow(rowData);
            }
        }
        if (rowData == null) {
            refreshModel();
            JOptionPane.showMessageDialog(frame, "Nu exista client cu asa nume = " + nume
                    + " si prenume = " + prenume,
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
     List<Color> rowColours = Arrays.asList(
        Color.RED,
        Color.GREEN,
        Color.CYAN
    );
  public void setRowColour(int row, Color c) {
        rowColours.set(row, c);
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
        return rowColours.get(row);
    }
    
}
