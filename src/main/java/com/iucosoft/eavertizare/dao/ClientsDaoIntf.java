/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iucosoft.eavertizare.dao;

import com.iucosoft.eavertizare.model.Client;
import com.iucosoft.eavertizare.model.Firma;
import java.util.List;

/**
 *
 * @author valera_
 */
public interface ClientsDaoIntf {

    void save(Firma firma, Client client);

    void update(Firma firma, Client client);

    Client findClient(Firma fima, String nume);

    Client findClient(Firma fima, String nume, String prenume);

    void createTabeleClienti(Firma firma);

    void deleletAll(Firma firma);

    void deleletById(Firma firma, Client client);

    void saveLocal(Firma firma, List<Client> clientsList);

    List<Client> findAllClientsForFirmaRemote(Firma firma);

    List<Client> findAllClientsForFirmaLocal(Firma firma);
}
