/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.model;

/**
 *
 * @author valera_
 */
public class Configuratii {

    private int id;
    private String driver;
    private String urlDb;
    private String username;
    private String password;
    private String tabelaClienti;

    public Configuratii() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrlDb() {
        return urlDb;
    }

    public void setUrlDb(String urlDb) {
        this.urlDb = urlDb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTabelaClienti() {
        return tabelaClienti;
    }

    public void setTabelaClienti(String tabelaClienti) {
        this.tabelaClienti = tabelaClienti;
    }

    @Override
    public String toString() {
        return "Configuratii{" + "id=" + id + ", driver=" + driver + ", urlDb=" + urlDb + ", username=" + username + ", password=" + password + ", tabelaClienti=" + tabelaClienti + '}';
    }

}
