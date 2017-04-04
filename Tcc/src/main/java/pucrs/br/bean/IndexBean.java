/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author psysvica
 */
@ManagedBean(name = "IndexMB")
@ViewScoped
public class IndexBean {
    
    public void conectar() {
        EntityManagerFactory factory = Persistence
            .createEntityManagerFactory("pucrs_Tcc_war_1.0PU");
        EntityManager em = factory.createEntityManager();
    }
}
