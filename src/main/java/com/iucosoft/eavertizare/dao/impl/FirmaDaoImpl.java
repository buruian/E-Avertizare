/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.dao.impl;

import com.iucosoft.eavertizare.dao.ConfiguratiiDaoIntf;
import com.iucosoft.eavertizare.dao.FirmaDaoIntf;
import com.iucosoft.eavertizare.model.Configuratii;
import com.iucosoft.eavertizare.model.Firma;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 *
 * @author valera_
 */
public class FirmaDaoImpl extends JdbcDaoSupport implements FirmaDaoIntf {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }
    @Autowired
    private ConfiguratiiDaoIntf configuratiiDao;

    @Override
    public void save(Firma firma) {
        String query = "INSERT INTO firme "
                + "(id, nume_firma, adresa, descriere, tabela_clienti, mesaj_clienti, id_configuratie)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Object[] args = new Object[]{null, firma.getNumeFirma(), firma.getAdresaFirma(),
            firma.getDescriereFirma(), firma.getNumeFirma() + "_clienti",
            firma.getMesajPentruClienti(), firma.getConfiguratii().getId()};

        jdbcTemplate.update(query, args);
    }

    @Override
    public void update(Firma firma) {

        String query = "update firme set nume_firma=?, adresa=?, descriere=?, "
                + "tabela_clienti=?, mesaj_clienti=?, id_configuratie=? where id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[]{firma.getNumeFirma(), firma.getAdresaFirma(),
            firma.getDescriereFirma(), firma.getNumeFirma() + "_clienti",
            firma.getMesajPentruClienti(), firma.getConfiguratii().getId(), firma.getId()};

        jdbcTemplate.update(query, args);
    }

    @Override
    public void delete(int id) {
        String query = "delete from firme where id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query, id);
    }

    @Override
    public void deleteByName(String firmaName) {
        String query = "delete from firme where nume_firma=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query, firmaName);
    }

    @Override
    public Firma findByName(String numeFirma) {

        String query = "select * from firme where nume_firma='" + numeFirma + "'";
        Firma firma = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {

            if (rs.next()) {

                Configuratii configuratie = configuratiiDao.findById(rs.getInt("id_configuratie"));

                firma = new Firma(rs.getInt("id"),
                        rs.getString("nume_firma"),
                        rs.getString("adresa"),
                        rs.getString("descriere"),
                        rs.getString("tabela_clienti"),
                        rs.getString("mesaj_clienti"),
                        configuratie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firma;
    }

    @Override
    public List<Firma> findAll() {
        String query = "select * from firme";
        List<Firma> firmaList = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                Configuratii configuratie = configuratiiDao.findById(rs.getInt(7));
                Firma firma = new Firma();
                firma.setId(rs.getInt(1));
                firma.setNumeFirma(rs.getString(2));
                firma.setAdresaFirma(rs.getString(3));
                firma.setDescriereFirma(rs.getString(4));
                firma.setTabelaClientiLocal(rs.getString(5));
                firma.setMesajPentruClienti(rs.getString(6));
                firma.setConfiguratii(configuratie);
                firmaList.add(firma);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), e, "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        return firmaList;
    }

    @Override
    public Firma findById(int idFirma) {

        String query = "select * from firme where id=" + idFirma;
        List<Firma> firmaList = new ArrayList<>();
        Firma firma = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {

            if (rs.next()) {

                Configuratii configuratie = configuratiiDao.findById(rs.getInt("id_configuratie"));

                firma = new Firma(rs.getInt("id"),
                        rs.getString("nume_firma"),
                        rs.getString("adresa"),
                        rs.getString("descriere"),
                        rs.getString("tabela_clienti"),
                        rs.getString("mesaj_clienti"),
                        configuratie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firma;
    }

    @Override
    public int getMaxId(String tabelaName) {
        int nr = 0;
        String query = "SELECT MAX(id) FROM " + tabelaName;

        try (Connection con = dataSource.getConnection();
                Statement stat = con.createStatement();
                ResultSet rs = stat.executeQuery(query);) {

            if (rs.next()) {
                nr = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nr;
    }

    @Override
    public void dropTableClients(String tableName) {

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();) {

            String queryLocal = "DROP TABLE " + tableName;

            stmt.executeUpdate(queryLocal);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
