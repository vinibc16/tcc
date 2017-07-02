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
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.Responsaveis;

/**
 *
 * @author psysvica
 */
@Stateless
public class ResponsaveisFacade extends AbstractFacade<Responsaveis> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResponsaveisFacade() {
        super(Responsaveis.class);
    }
    
    public List<Responsaveis> findAllRespByUser(Empresa emp) {        
        return em.createQuery("SELECT u FROM Responsaveis u WHERE u.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", emp)
                .getResultList();
    }
}
