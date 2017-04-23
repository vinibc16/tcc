/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.EscopoVul;

/**
 *
 * @author psysvica
 */
@Stateless
public class EscopoVulFacade extends AbstractFacade<EscopoVul> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EscopoVulFacade() {
        super(EscopoVul.class);
    }
    
    public List<EscopoVul> findAllfindByIdEscopo(Escopo escopo) {
        
        Query query = em.createNativeQuery("SELECT * FROM escopo_vul WHERE id_escopo = :idEscopo");
        query.setParameter("idEscopo", escopo.getEscopoPK().getIdEscopo());
        List ev = query.getResultList();
        return ev;
    }
    
}
