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
import pucrs.br.entity.Escopo;
import pucrs.br.entity.EscopoPK;
import pucrs.br.entity.EscopoVul;
import pucrs.br.entity.EscopoVulPK;

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
    
    public List<Escopo> findAllEscByUser(int idEmpresa) {        
        return em.createQuery("SELECT u FROM Escopo u WHERE u.escopoPK.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", idEmpresa)
                .getResultList();
    }
}
