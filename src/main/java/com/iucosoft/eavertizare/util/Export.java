/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.iucosoft.eavertizare.gui.MainJFrame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author valera_
 */
public class Export {

    public static void toPdf(JFrame frame, JTable jTableClients, String firma) {

        Document document = new Document(PageSize.A4.rotate());
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {

                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());
                PdfWriter.getInstance(document, new FileOutputStream(fileToSave.getAbsolutePath() + ".pdf"));

            }

            document.open();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            if (firma.equals("All firms")) {

                addTitle(document, "Raport pentru toate firmele cu toti clienti \n din "
                        + dateFormat.format(date));
            } else {

                addTitle(document, "Raport pentru " + firma + " \n din "
                        + dateFormat.format(date));
            }

            addEmptyLine(document, new Paragraph(), 2);

            PdfPTable table = new PdfPTable(jTableClients.getColumnCount());
            table.setTotalWidth(jTableClients.getColumnCount() + 780);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);

            for (int i = 0; i < table.getNumberOfColumns(); i++) {

                if (jTableClients.getColumnName(i).equals("✔")) {
                    table.addCell("Trimis");
                } else {
                    table.addCell(jTableClients.getColumnName(i));
                }
            }
            Object value;
            for (int i = 0; i < jTableClients.getRowCount(); i++) {
                for (int j = 0; j < table.getNumberOfColumns(); j++) {
                    value = jTableClients.getValueAt(i, j);
                    table.addCell(value.toString());
                }
            }
            document.add(table);
            document.close();

        } catch (DocumentException ex) {
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void addTitle(Document document, String header) throws DocumentException {
        addEmptyLine(document, new Paragraph(), 1);
        Paragraph preface = new Paragraph();
        preface.add(header);
        preface.setAlignment(Element.ALIGN_CENTER);
        document.add(preface);
    }

    private static void addEmptyLine(Document document, Paragraph paragraph, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
        document.add(paragraph);
    }
}
