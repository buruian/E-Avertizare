/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.dao;

import com.iucosoft.eavertizare.model.Firma;
import java.util.List;

/**
 *
 * @author valera_
 */
public interface FirmaDaoIntf {
    void save(Firma firma);
    void update(Firma firma);
    void delete(int id);
    void deleteByName(String firmaName);
    
    int getMaxId(String tabelaName);
    Firma findById(int idFirma);
    Firma findByName(String numeFirma);
    List<Firma> findAll();
    void dropTableClients(String tableName);
}
