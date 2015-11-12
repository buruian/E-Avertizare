/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.model;

import java.util.Date;

/**
 *
 * @author valera_
 */
public class Client {

    private int id;
    private String nume;
    private String prenume;
    private int nrTelefon;
    private String email;
    private Date dateExpirare;
    private Firma firma;
    private boolean trimis;

    public Client() {
    }

    public Client(int id, String nume, String prenume, int nrTelefon, String email, Date dateExpirare, Firma firma, boolean trimis) {
        this.id = id;
        this.nume = nume;
        this.prenume = prenume;
        this.nrTelefon = nrTelefon;
        this.email = email;
        this.dateExpirare = dateExpirare;
        this.firma = firma;
        this.trimis = trimis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public int getNrTelefon() {
        return nrTelefon;
    }

    public void setNrTelefon(int nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateExpirare() {
        return dateExpirare;
    }

    public void setDateExpirare(Date dateExpirare) {
        this.dateExpirare = dateExpirare;
    }

    public Firma getFirma() {
        return firma;
    }

    public void setFirma(Firma firma) {
        this.firma = firma;
    }

    public boolean isTrimis() {
        return trimis;
    }

    public void setTrimis(boolean trimis) {
        this.trimis = trimis;
    }

    @Override
    public boolean equals(Object o){
    if(o == null)                return false;

    Client other = (Client) o;
    if(this.id != other.id)      return false;
    if(this.nrTelefon != other.nrTelefon)      return false;
    if(! this.nume.equals(other.nume)) return false;
    if(! this.prenume.equals(other.prenume))   return false;
    if(! this.email.equals(other.email))   return false;
    if(! this.dateExpirare.equals(other.dateExpirare))   return false;

    return true;
  }
    
//    @Override
//    public String toString() {
//        return "Clients{" + "id=" + id + ", nume=" + nume + ", prenume=" + prenume + ", nrTelefon=" + nrTelefon + ", email=" + email + ", dateExpirare=" + dateExpirare + ", firma=" + firma + ", trimis=" + trimis + '}';
//    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", nume=" + nume + ", prenume=" + prenume + ", nrTelefon=" + nrTelefon + ", email=" + email + ", dateExpirare=" + dateExpirare + ", trimis=" + trimis + '}';
    }
    
   
}
