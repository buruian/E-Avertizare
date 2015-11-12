/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.dao;

import com.iucosoft.eavertizare.model.Configuratii;

/**
 *
 * @author valera_
 */
public interface ConfiguratiiDaoIntf {
    void save(Configuratii config);
    void update(Configuratii config);
    void delete(int idConfig);
    
    Configuratii findById(int idConfig);
}
