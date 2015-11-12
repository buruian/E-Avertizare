/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.dao.impl;

import com.iucosoft.eavertizare.dao.ClientsDaoIntf;
import com.iucosoft.eavertizare.dao.FirmaDaoIntf;
import com.iucosoft.eavertizare.model.Client;
import com.iucosoft.eavertizare.model.Firma;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author valera_
 */
public class ClientsDaoImpl extends JdbcDaoSupport implements ClientsDaoIntf {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private FirmaDaoIntf firmaDao;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void save(Firma firma, Client client) {
        String query = "INSERT INTO " + firma.getTabelaClientiLocal()
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Object[] args = new Object[]{client.getId(), client.getNume(), client.getPrenume(),
            client.getNrTelefon(), client.getEmail(),
            new java.sql.Timestamp(client.getDateExpirare().getTime()),
            firma.getId(), 0};

        jdbcTemplate.update(query, args);
    }

    @Override
    public void update(Firma firma, Client client) {
        String query = "update " + firma.getTabelaClientiLocal() + " set"
                + " nume=?, prenume=?, nr_telefon=?, email=?, "
                + "data_expirare=?, trimis=? where id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[]{client.getNume(), client.getPrenume(),
            client.getNrTelefon(), client.getEmail(),
            new java.sql.Timestamp(client.getDateExpirare().getTime()),
            client.isTrimis(), client.getId()};

        jdbcTemplate.update(query, args);
    }

    @Override
    public void saveLocal(Firma firma, List<Client> clientsList) {

        String query = "INSERT INTO " + firma.getTabelaClientiLocal()
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(query);
            // Set auto-commit to false
            con.setAutoCommit(false);

            for (Client client : clientsList) {
                ps.setInt(1, client.getId());
                ps.setString(2, client.getNume());
                ps.setString(3, client.getPrenume());
                ps.setInt(4, client.getNrTelefon());
                ps.setString(5, client.getEmail());
                ps.setTimestamp(6, (new java.sql.Timestamp(client.getDateExpirare().getTime())));
                ps.setInt(7, firma.getId());
                ps.setInt(8, 0);

                ps.addBatch();
            }

            // Create an int[] to hold returned values
            int[] count = ps.executeBatch();
            //Explicitly commit statements to apply changes
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ClientsDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createTabeleClienti(Firma firma) {

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();) {

            String queryLocal = "CREATE TABLE " + firma.getTabelaClientiLocal() + " "
                    + "(id INT NOT NULL, "
                    + " nume VARCHAR(45), "
                    + " prenume VARCHAR(45), "
                    + " nr_telefon INT, "
                    + " email VARCHAR(45), "
                    + " data_expirare DATE, "
                    + " id_firma INT, "
                    + " trimis TINYINT(1), "
                    + " PRIMARY KEY ( id ))";

            stmt.executeUpdate(queryLocal);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Client> findAllClientsForFirmaRemote(Firma firma) {

        DriverManagerDataSource dataSourceClient = new DriverManagerDataSource();
        dataSourceClient.setDriverClassName(firma.getConfiguratii().getDriver());
        dataSourceClient.setUrl(firma.getConfiguratii().getUrlDb());
        dataSourceClient.setUsername(firma.getConfiguratii().getUsername());
        dataSourceClient.setPassword(firma.getConfiguratii().getPassword());

        String query = "select * from " + firma.getConfiguratii().getTabelaClienti();
        List<Client> clientsList = new ArrayList<>();

        try (Connection con = dataSourceClient.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt(1));
                client.setNume(rs.getString(2));
                client.setPrenume(rs.getString(3));
                client.setNrTelefon(rs.getInt(4));
                client.setEmail(rs.getString(5));
                client.setDateExpirare(rs.getTimestamp(6));
                clientsList.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsList;
    }

    @Override
    public List<Client> findAllClientsForFirmaLocal(Firma firma) {

        String query = "select * from " + firma.getTabelaClientiLocal();
        List<Client> clientsList = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                Firma firmaBD = firmaDao.findById(rs.getInt(7));
                Client client = new Client();
                client.setId(rs.getInt(1));
                client.setNume(rs.getString(2));
                client.setPrenume(rs.getString(3));
                client.setNrTelefon(rs.getInt(4));
                client.setEmail(rs.getString(5));
                client.setDateExpirare(rs.getTimestamp(6));
                client.setFirma(firmaBD);
                if (rs.getInt(8) == 0) {
                    client.setTrimis(false);
                } else {
                    client.setTrimis(true);
                }
                clientsList.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsList;
    }

    @Override
    public void deleletAll(Firma firma) {
        String query = "delete from " + firma.getTabelaClientiLocal() + " where id>0";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query);
    }

    @Override
    public void deleletById(Firma firma, Client client) {

        String query = "delete from " + firma.getTabelaClientiLocal() + " where id=" + client.getId();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(query);
    }

    @Override
    public Client findClient(Firma firma, String nume) {
        String query = "select * from " + firma.getTabelaClientiLocal()
                + " where nume='" + nume + "'";
        Client client = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {

            if (rs.next()) {
                client = new Client(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getDate(6),
                        firma,
                        rs.getBoolean(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Client findClient(Firma firma, String nume, String prenume) {
        String query = "select * from " + firma.getTabelaClientiLocal()
                + " where nume='" + nume + "' and prenume='" + prenume + "'";
        Client client = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {

            if (rs.next()) {
                client = new Client(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getDate(6),
                        firma,
                        rs.getBoolean(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }
}
