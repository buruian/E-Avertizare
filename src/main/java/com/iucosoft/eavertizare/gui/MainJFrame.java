/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.gui;

import com.iucosoft.eavertizare.dao.ClientsDaoIntf;
import com.iucosoft.eavertizare.dao.ConfiguratiiDaoIntf;
import com.iucosoft.eavertizare.dao.FirmaDaoIntf;
import com.iucosoft.eavertizare.gui.models.ClientiTableModel;
import com.iucosoft.eavertizare.gui.models.FirmeListModel;
import com.iucosoft.eavertizare.gui.models.MyImageRenderer;
import com.iucosoft.eavertizare.model.Client;
import com.iucosoft.eavertizare.model.Configuratii;
import com.iucosoft.eavertizare.model.Firma;
import com.iucosoft.eavertizare.util.Export;
import com.iucosoft.eavertizare.util.MyMailSender;
import com.iucosoft.eavertizare.util.MySmsSender;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author valera_
 */
public class MainJFrame extends javax.swing.JFrame {

    private Firma firma;
    private FirmaDaoIntf firmaDao;
    private Configuratii configuratii;
    private final MySmsSender smsSender;
    private final MyMailSender mailSender;
    private final ClientsDaoIntf clientsDao;
    private ClientiTableModel clientiTableModel;
    private final FirmeListModel firmeListModel;
    private ApplicationContext contexDao = null;
    private final ApplicationContext contextMail;
    private final ConfiguratiiDaoIntf configuratiiDao;

    public MainJFrame() {
        setTitle("E - Avertizare");
        contexDao = new ClassPathXmlApplicationContext("spring-dao.xml");
        contextMail = new ClassPathXmlApplicationContext("spring-mail.xml");

        firma = new Firma();
        smsSender = contextMail.getBean(MySmsSender.class);
        mailSender = contextMail.getBean(MyMailSender.class);
        firmeListModel = contexDao.getBean(FirmeListModel.class);
        clientiTableModel = contexDao.getBean(ClientiTableModel.class);
        firmaDao = contexDao.getBean("firmaDao", FirmaDaoIntf.class);
        clientsDao = contexDao.getBean("clientsDao", ClientsDaoIntf.class);
        configuratiiDao = contexDao.getBean("configuratiiDao", ConfiguratiiDaoIntf.class);

        initComponents();
        initModes();
        setLocationRelativeTo(null);
    }

    private void initModes() {
        jListFirma.setModel(firmeListModel);
        jListFirma.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jListFirma.addMouseListener(mouseListenerList);

        jTableClients.setModel(clientiTableModel);
        jTableClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //  jTableClients.addMouseListener(mouseListenerTable);
        jTableClients.getColumnModel().getColumn(0).setPreferredWidth(27);
        jTableClients.getColumnModel().getColumn(7).setPreferredWidth(1);

        jTableClients.getColumnModel().getColumn(7).setCellRenderer(new MyImageRenderer());
        jTableClients.setBackground(new Color(220, 220, 220));
        jListFirma.setBackground(new Color(220, 220, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        jTableClients.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        jTableClients.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        refreshFrame();

    }

    public void refreshFrame() {
        firmeListModel.refreshModel();
        clientiTableModel.refreshModel();
    }

    MouseListener mouseListenerList = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            int index = jListFirma.locationToIndex(mouseEvent.getPoint());
            switch (mouseEvent.getClickCount()) {
                case 1:
                    if (index > 0) {
                        Object o = jListFirma.getModel().getElementAt(index);
                        clientiTableModel.refreshModel(o.toString());
                    }
                    if (index == 0) {
                        //  initModes();
                        clientiTableModel.refreshModel();
                        firmeListModel.refreshModel();
                    }
                    break;

                case 2:
                    if (index > 0) {
                        Object o = jListFirma.getModel().getElementAt(index);
                        System.out.println("Double-clicked on: " + o.toString());
                        firma = firmaDao.findByName(o.toString());
                        showFirmaJDialog(firma);
                    }
                    if (index == 0) {
                        firmeJDialog(contexDao);
                    }
                    break;

                default:
                    System.out.println("Invalid index");
                    break;
            }
        }
    };

//    MouseListener mouseListenerTable = new MouseAdapter() {
//        @Override
//        public void mouseClicked(MouseEvent mouseEvent) {
//            if (mouseEvent.getClickCount() == 2) {
//                int row = jTableClients.getSelectedRow();
//                JOptionPane.showMessageDialog(new Frame(), "Nu exista firma cu asa nume = " + row,
//                        "Error", JOptionPane.WARNING_MESSAGE);
//            }
//        }
//    };
    public void showFirmaJDialog(final Firma firma) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FirmaJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FirmaJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FirmaJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FirmaJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FirmaJDialog dialog = new FirmaJDialog(MainJFrame.this, true, firma);
                dialog.setVisible(true);
            }
        });
    }

    public void autoRun() {

        System.out.println("Spring 4 + Quartz 2.2.1 ~");

        //  SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
//        Runnable myRunnable = new Runnable() {
//
//            @Override
//            public void run() {
        DateTime start = new DateTime();

        List<Firma> listFirma = firmaDao.findAll();
        List<Client> listClients;

        //*******************Sincronizare************
        sincronizare();
        //*****************
        for (Firma firmalocal : listFirma) {
            listClients = clientsDao.findAllClientsForFirmaLocal(firmalocal);
            for (Client client : listClients) {
                //   System.out.println("" + sdf.format(client.getDateExpirare())); 
                DateTime end = new DateTime(client.getDateExpirare());
                Days days = Days.daysBetween(start, end);

                if (days.getDays() == -2) {
                    //   System.out.println("" + sdf.format(client.getDateExpirare()));
                    System.out.println(" " + client.getDateExpirare());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                    String mesajClient = client.getFirma().getMesajPentruClienti()
                            .replaceFirst("nume", client.getNume())
                            .replaceFirst("prenume", client.getPrenume())
                            .replaceFirst("data", sdf.format(client.getDateExpirare()).toString())
                            .replaceFirst("compania", client.getFirma().getNumeFirma());
                    try {
                        // client.setTrimis(true);
                        smsSender.sendSms(client.getNrTelefon(), mesajClient);
                        mailSender.sendMail(client.getEmail(), "E-avetizare", mesajClient);
                    } catch (Exception ex) {
                        Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, "Verificați conexiunea la internet!\n"
                                + ex, "Error", JOptionPane.ERROR_MESSAGE);
                    }

//                            try {
//                                //   this.wait();
//                                Thread.sleep(5000);
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
//                            }
                }
                // clientsDao.update(client.getFirma(), client);
            }

        }

        this.refreshFrame();

//            }
//        };
//
//        Thread thread = new Thread(myRunnable);
//        thread.start();
    }

    public void sincronizare() {
        Runnable myRunnable = new Runnable() {

            @Override
            public void run() {
                System.out.println("Running sincronizarea..........");
                List<Firma> listFirma = firmaDao.findAll();
                List<Client> listaClientsRemote;
                List<Client> listaClientsLocal;

                for (Firma firma : listFirma) {
                    listaClientsRemote = clientsDao.findAllClientsForFirmaRemote(firma);
                    listaClientsLocal = clientsDao.findAllClientsForFirmaLocal(firma);
                    int delete = 0;
                    int save = 0;
                    int contor = 0;

                    for (int i = 0; i < listaClientsRemote.size(); i++) {
                        for (int j = 0; j < listaClientsLocal.size(); j++) {
                            contor++;
                            if (listaClientsRemote.get(i).getId() == listaClientsLocal.get(j).getId()) {
                                if (!listaClientsRemote.get(i).equals(listaClientsLocal.get(j))) {
                                    //update client local
                                    if (listaClientsRemote.get(i).getDateExpirare().equals(listaClientsLocal.get(i).getDateExpirare())) {
                                        System.out.println("date este egala");
                                        listaClientsRemote.get(i).setTrimis(true);
                                    }
                                    System.out.println("NU CONTINE update: " + listaClientsRemote.get(i)
                                            + "firma = " + listaClientsLocal.get(j).getFirma().getNumeFirma());
                                    clientsDao.update(firma, listaClientsRemote.get(i));

                                }
                            }
                        }
                    }
                }
                for (Firma firma : listFirma) {
                    listaClientsRemote = clientsDao.findAllClientsForFirmaRemote(firma);
                    listaClientsLocal = clientsDao.findAllClientsForFirmaLocal(firma);
                    for (int i = 0; i < listaClientsLocal.size(); i++) {
                        if (!listaClientsRemote.contains(listaClientsLocal.get(i))) {
                            //delete client local
                            System.out.println("NU CONTINE delete: " + listaClientsLocal.get(i)
                                    + "firma = " + listaClientsLocal.get(i).getFirma().getNumeFirma());
                            clientsDao.deleletById(firma, listaClientsLocal.get(i));

                        }
                    }

                    for (int i = 0; i < listaClientsRemote.size(); i++) {

                        if (!listaClientsLocal.contains(listaClientsRemote.get(i))) {

                            //save client local
                            System.out.println("NU CONTINE save: " + listaClientsRemote.get(i)
                                    + "firma = " + listaClientsLocal.get(i).getFirma().getNumeFirma());
                            clientsDao.save(firma, listaClientsRemote.get(i));
                        }
                    }
                }

                refreshFrame();
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldCautaFirma = new javax.swing.JTextField();
        jButtonCautaFirma = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListFirma = new javax.swing.JList();
        jButtonAddFirma = new javax.swing.JButton();
        jButtonDeleteFirma = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTextFieldCautaNumeClient = new javax.swing.JTextField();
        jTextFieldCautaPrenumeClient = new javax.swing.JTextField();
        jButtonCautaClientii = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableClients = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonSmsAndEmail = new javax.swing.JButton();
        jButtonEmail = new javax.swing.JButton();
        jButtonSms = new javax.swing.JButton();
        jButtonExportPdf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Firme"));

        jTextFieldCautaFirma.setToolTipText("Nume firma");

        jButtonCautaFirma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-icon 16px.png"))); // NOI18N
        jButtonCautaFirma.setToolTipText("Search firma");
        jButtonCautaFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCautaFirmaActionPerformed(evt);
            }
        });

        jListFirma.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jListFirma);

        jButtonAddFirma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/network_add (1).png"))); // NOI18N
        jButtonAddFirma.setToolTipText("Add firma");
        jButtonAddFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddFirmaActionPerformed(evt);
            }
        });

        jButtonDeleteFirma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/network_delete (1).png"))); // NOI18N
        jButtonDeleteFirma.setToolTipText("Delete firma");
        jButtonDeleteFirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteFirmaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextFieldCautaFirma, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCautaFirma))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonAddFirma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonDeleteFirma, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonCautaFirma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldCautaFirma))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonAddFirma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDeleteFirma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Clientii"));

        jButtonCautaClientii.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Zoom-icon 16px.png"))); // NOI18N
        jButtonCautaClientii.setText("search");
        jButtonCautaClientii.setToolTipText("Search clients");
        jButtonCautaClientii.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCautaClientiiActionPerformed(evt);
            }
        });

        jTableClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTableClients);

        jLabel1.setText("Nume:");

        jLabel2.setText("Prenume:");

        jButtonSmsAndEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/SMS-Message-icon.png"))); // NOI18N
        jButtonSmsAndEmail.setToolTipText("Send sms and e-mail");
        jButtonSmsAndEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSmsAndEmailActionPerformed(evt);
            }
        });

        jButtonEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/email.png"))); // NOI18N
        jButtonEmail.setToolTipText("Send e-mail");
        jButtonEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEmailActionPerformed(evt);
            }
        });

        jButtonSms.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sms.png"))); // NOI18N
        jButtonSms.setToolTipText("Send sms");
        jButtonSms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSmsActionPerformed(evt);
            }
        });

        jButtonExportPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf.png"))); // NOI18N
        jButtonExportPdf.setToolTipText("Export to pdf");
        jButtonExportPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addComponent(jButtonSms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButtonEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButtonSmsAndEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButtonExportPdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(193, 193, 193))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCautaNumeClient, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldCautaPrenumeClient, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCautaClientii, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCautaNumeClient, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonCautaClientii)
                        .addComponent(jLabel2)
                        .addComponent(jLabel1)
                        .addComponent(jTextFieldCautaPrenumeClient, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButtonEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSmsAndEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonExportPdf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCautaFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCautaFirmaActionPerformed
        if (!jTextFieldCautaFirma.getText().equals("")) {
            firmeListModel.refreshModel(this, jTextFieldCautaFirma.getText());
            clientiTableModel.refreshModel(jTextFieldCautaFirma.getText());
            jTextFieldCautaFirma.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Introduceti numele firme!", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_jButtonCautaFirmaActionPerformed

    private void jButtonAddFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddFirmaActionPerformed
        showFirmaJDialog(new Firma());
    }//GEN-LAST:event_jButtonAddFirmaActionPerformed

    private void jButtonDeleteFirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteFirmaActionPerformed
        int index = jListFirma.getSelectedIndex();
        if (index != -1) {
            Object numeFirma = jListFirma.getModel().getElementAt(index);
            firma = firmaDao.findByName((String) numeFirma);

            int rez = JOptionPane.showConfirmDialog(this, "Esti sigur vrei sa sterg firma ?",
                    "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            switch (rez) {
                case JOptionPane.YES_OPTION:

                    int idConfiguratie = firma.getConfiguratii().getId();
                    firmaDao.dropTableClients(firma.getTabelaClientiLocal());
                    System.out.println("conf id = " + idConfiguratie);
                    firmaDao.delete(firma.getId());
                    configuratiiDao.delete(idConfiguratie);
                    JOptionPane.showMessageDialog(this, "Firma stersa cu success", "Succes",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    break;
            }
            refreshFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Selectati firma!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButtonDeleteFirmaActionPerformed

    private void jButtonCautaClientiiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCautaClientiiActionPerformed
        String nume = jTextFieldCautaNumeClient.getText();
        String prenume = jTextFieldCautaPrenumeClient.getText();
        if (nume.equals("") && prenume.equals("")) {
            JOptionPane.showMessageDialog(this, "Introduceti numele si/sau prenumele!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (!nume.equals("") && prenume.equals("")) {
                clientiTableModel.findClient(this, nume);
                jTextFieldCautaNumeClient.setText("");
                System.out.println("aici");
            }
            if (!nume.equals("") && !prenume.equals("")) {
                clientiTableModel.findClient(this, nume, prenume);
                jTextFieldCautaNumeClient.setText("");
                jTextFieldCautaPrenumeClient.setText("");
            }
        }

    }//GEN-LAST:event_jButtonCautaClientiiActionPerformed

    private void jButtonSmsAndEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSmsAndEmailActionPerformed
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running send all");
                sendAvertizare("SMS AND E-MAIL");
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();
    }//GEN-LAST:event_jButtonSmsAndEmailActionPerformed

    private void jButtonSmsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSmsActionPerformed
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running send sms");
                sendAvertizare("SMS");
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();

    }//GEN-LAST:event_jButtonSmsActionPerformed

    private void jButtonEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEmailActionPerformed
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running send email");
                sendAvertizare("E-MAIL");
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();

    }//GEN-LAST:event_jButtonEmailActionPerformed

    private void jButtonExportPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportPdfActionPerformed
        Vector element = new Vector();
        for (int i = 0; i < firmeListModel.getSize(); i++) {
            element.add(firmeListModel.getElementAt(i));
        }
        final JComboBox<String> combo = new JComboBox<>(element);
        String[] options = {"OK", "Cancel"};
        String title = "Selctati firma!";
        int selection = JOptionPane.showOptionDialog(null, combo, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, element.get(0));
        try {
            if (options[selection].equals("OK")) {
                Object firmaSelectata = combo.getSelectedItem();
                if (firmaSelectata.equals("All firms")) {
                    clientiTableModel.refreshModel();
                } else {
                    clientiTableModel.refreshModel((String) firmaSelectata);
                }
                Export.toPdf(this, jTableClients, (String) firmaSelectata);
                clientiTableModel.refreshModel();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButtonExportPdfActionPerformed

    private void sendAvertizare(String tipAvertizare) {
        int selectedRowIndex = jTableClients.getSelectedRow();
        Client client = null;
        int contor = 0;
        String mesaj = "";
        Icon icon = new ImageIcon(getClass().getResource("/images/help_and_support.png"));
        if (selectedRowIndex != -1) {

            int rez = JOptionPane.showConfirmDialog(this, "Doriti sa modificati mesajul ?",
                    "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
            switch (rez) {
                case JOptionPane.YES_OPTION:
                    client = cientForIndex(selectedRowIndex);

                    sendAvertizareJFrame(client.getFirma(), client, tipAvertizare);
                    break;

                case JOptionPane.NO_OPTION:
                    client = cientForIndex(selectedRowIndex);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                    String mesajClient = client.getFirma().getMesajPentruClienti()
                            .replaceFirst("nume", client.getNume())
                            .replaceFirst("prenume", client.getPrenume())
                            .replaceFirst("data", sdf.format(client.getDateExpirare()).toString())
                            .replaceFirst("compania", client.getFirma().getNumeFirma());
                    switch (tipAvertizare) {
                        case "SMS AND E-MAIL":
                            mesaj = "Sms si e-mail";

                             {
                                try {

                                    client.setTrimis(true);
                                    clientsDao.update(client.getFirma(), client);
                                    smsSender.sendSms(client.getNrTelefon(), mesajClient);
                                    mailSender.sendMail(client.getEmail(), "E-avetizare", mesajClient);
                                } catch (Exception ex) {
                                    //Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                    JOptionPane.showMessageDialog(new JFrame(), "Verificați conexiunea la internet!\n"
                                            + ex, "Error", JOptionPane.ERROR_MESSAGE);
                                    contor = 1;
                                }

                            }

                            clientiTableModel.refreshModel();
                            break;
                        case "SMS":
                            mesaj = "Sms";

                            try {
                                smsSender.sendSms(client.getNrTelefon(), mesajClient);
                            } catch (Exception ex) {
                                //Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(new JFrame(), "Verificați conexiunea la internet!\n"
                                        + ex, "Error", JOptionPane.ERROR_MESSAGE);
                                contor = 1;
                            }

                            break;
                        case "E-MAIL":
                            mesaj = "E-mail";
                            try {
                                mailSender.sendMail(client.getEmail(), "E-avetizare", mesajClient);
                            } catch (Exception ex) {
                                //Logger.getLogger(MainJFrame.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(new JFrame(), "Verificați conexiunea la internet!\n"
                                        + ex, "Error", JOptionPane.ERROR_MESSAGE);
                                contor = 1;
                            }

                            break;
                    }
                    if (contor != 1) {
                        JOptionPane.showMessageDialog(MainJFrame.this, mesaj + " transmis cu succes!",
                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    }

                    break;

                case JOptionPane.CLOSED_OPTION:
                    break;

            }
        } else {
            JOptionPane.showMessageDialog(this, "Selectati clientul!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Client cientForIndex(int index) {;
        int selectedRowIndex = jTableClients.getSelectedRow();
        String nume = (String) jTableClients.getValueAt(selectedRowIndex, 1);
        String prenume = (String) jTableClients.getValueAt(selectedRowIndex, 2);
        String firmaName = (String) jTableClients.getValueAt(selectedRowIndex, 6);
        firma = firmaDao.findByName(firmaName);
        Client client = clientsDao.findClient(firma, nume, prenume);
        return client;
    }

    private void sendAvertizareJFrame(final Firma firma, final Client client, final String varianta) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SendAvertizareManualJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SendAvertizareManualJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SendAvertizareManualJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SendAvertizareManualJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SendAvertizareManualJDialog dialog = new SendAvertizareManualJDialog(MainJFrame.this, true, clientsDao, firma, client, varianta);
                dialog.setVisible(true);
            }
        });
    }

    private void cautaClientJDialog() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CautaClientJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CautaClientJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CautaClientJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CautaClientJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CautaClientJDialog dialog = new CautaClientJDialog(MainJFrame.this, true);
                dialog.setVisible(true);
            }
        });
    }

    private void firmeJDialog(final ApplicationContext contexDao) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FirmeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FirmeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FirmeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FirmeJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FirmeJDialog dialog = new FirmeJDialog(MainJFrame.this, true, contexDao);
                dialog.setVisible(true);
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws InterruptedException {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                MainJFrame frame = new MainJFrame();
                new ClassPathXmlApplicationContext("spring-quartz.xml");
                ProgressBarJDialog progressBar = new ProgressBarJDialog(new JFrame(), true);
                while (!ProgressBarJDialog.getFlag()) {
                    progressBar.setVisible(true);
                }
                frame.setVisible(true);
            }
        });

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddFirma;
    private javax.swing.JButton jButtonCautaClientii;
    private javax.swing.JButton jButtonCautaFirma;
    private javax.swing.JButton jButtonDeleteFirma;
    private javax.swing.JButton jButtonEmail;
    private javax.swing.JButton jButtonExportPdf;
    private javax.swing.JButton jButtonSms;
    private javax.swing.JButton jButtonSmsAndEmail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jListFirma;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableClients;
    private javax.swing.JTextField jTextFieldCautaFirma;
    private javax.swing.JTextField jTextFieldCautaNumeClient;
    private javax.swing.JTextField jTextFieldCautaPrenumeClient;
    // End of variables declaration//GEN-END:variables

}
