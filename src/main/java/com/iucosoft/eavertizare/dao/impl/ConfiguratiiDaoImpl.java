/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.dao.impl;

import com.iucosoft.eavertizare.dao.ConfiguratiiDaoIntf;
import com.iucosoft.eavertizare.model.Configuratii;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 *
 * @author valera_
 */
public class ConfiguratiiDaoImpl extends JdbcDaoSupport implements ConfiguratiiDaoIntf {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void save(Configuratii config) {

        String query = "insert into configuratii_db "
                + "(id, driver, url_db, username, password, tabela_clienti)"
                + " values (?, ?, ?, ?, ?, ?)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Object[] args = new Object[]{null, config.getDriver(), config.getUrlDb(),
            config.getUsername(), config.getPassword(), config.getTabelaClienti()};

        jdbcTemplate.update(query, args);

    }

    @Override
    public void update(Configuratii config) {

        String query = "update configuratii_db set driver=?, url_db=?, username=?, "
                + "password=?, tabela_clienti=? where id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Object[] args = new Object[]{config.getDriver(), config.getUrlDb(), config.getUsername(),
            config.getPassword(), config.getTabelaClienti(), config.getId()};

        jdbcTemplate.update(query, args);

    }

    @Override
    public void delete(int idConfiguratie) {
        
        String query = "delete from configuratii_db where id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
         
        jdbcTemplate.update(query, idConfiguratie);
       
    }

    @Override
    public Configuratii findById(int idConfiguratie) {

        String query = "select * from configuratii_db where id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //using RowMapper anonymous class, we can create a separate RowMapper for reuse
        Configuratii configuratii = jdbcTemplate.queryForObject(query, new Object[]{idConfiguratie}, new RowMapper<Configuratii>() {

            @Override
            public Configuratii mapRow(ResultSet rs, int i) throws SQLException {

                Configuratii configuratie = new Configuratii();
                configuratie.setId(rs.getInt(1));
                configuratie.setDriver(rs.getString(2));
                configuratie.setUrlDb(rs.getString(3));
                configuratie.setUsername(rs.getString(4));
                configuratie.setPassword(rs.getString(5));
                configuratie.setTabelaClienti(rs.getString(6));
                return configuratie;
            }
        });

        return configuratii;
    }

}
