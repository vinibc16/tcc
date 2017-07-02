/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.Grafico;

/**
 *
 * @author psysvica
 */
@Stateless
public class EscopoFacade extends AbstractFacade<Escopo> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EscopoFacade() {
        super(Escopo.class);
    }
    
    public List<Escopo> findAllEscByUser(Empresa emp) {        
        return em.createQuery("SELECT u FROM Escopo u WHERE u.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", emp)
                .getResultList();
    }
    
    public Escopo findbyId(int idEscopo) {        
        Escopo esc = (Escopo) em.createQuery("SELECT u FROM Escopo u WHERE u.idEscopo = :idEscopo")
                .setParameter("idEscopo", idEscopo)
                .getSingleResult();
        return esc;
    }
}
