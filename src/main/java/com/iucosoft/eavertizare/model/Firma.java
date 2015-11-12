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
public class Firma {

    private int id;
    private String numeFirma;
    private String adresaFirma;
    private String descriereFirma;
    private String tabelaClientiLocal;
    private String mesajPentruClienti;
    private Configuratii configuratii;

    public Firma() {
    }

    public Firma(int id, String numeFirma, String adresaFirma, String descriereFirma, String tabelaClienti, String mesajPentruClienti, Configuratii configuratii) {
        this.id = id;
        this.numeFirma = numeFirma;
        this.adresaFirma = adresaFirma;
        this.descriereFirma = descriereFirma;
        this.tabelaClientiLocal = tabelaClienti;
        this.mesajPentruClienti = mesajPentruClienti;
        this.configuratii = configuratii;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeFirma() {
        return numeFirma;
    }

    public void setNumeFirma(String numeFirma) {
        this.numeFirma = numeFirma;
    }

    public String getAdresaFirma() {
        return adresaFirma;
    }

    public void setAdresaFirma(String adresaFirma) {
        this.adresaFirma = adresaFirma;
    }

    public String getDescriereFirma() {
        return descriereFirma;
    }

    public void setDescriereFirma(String descriereFirma) {
        this.descriereFirma = descriereFirma;
    }

    public String getTabelaClientiLocal() {
        return tabelaClientiLocal;
    }

    public void setTabelaClientiLocal(String tabelaClientiLocal) {
        this.tabelaClientiLocal = tabelaClientiLocal;
    }

    public Configuratii getConfiguratii() {
        return configuratii;
    }

    public void setConfiguratii(Configuratii configuratii) {
        this.configuratii = configuratii;
    }

    public String getMesajPentruClienti() {
        return mesajPentruClienti;
    }

    public void setMesajPentruClienti(String mesajPentruClienti) {
        this.mesajPentruClienti = mesajPentruClienti;
    }

    @Override
    public String toString() {
        return "Firma{" + "id=" + id + ", numeFirma=" + numeFirma + ", adresaFirma=" + adresaFirma + ", descriereFirma=" + descriereFirma + ", mesajPentruClienti=" + mesajPentruClienti + ", tabelaClienti=" + tabelaClientiLocal + ", configuratii=" + configuratii + '}';
    }

}
