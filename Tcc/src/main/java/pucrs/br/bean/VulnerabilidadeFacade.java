/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Vulnerabilidade;

/**
 *
 * @author psysvica
 */
@Stateless
public class VulnerabilidadeFacade extends AbstractFacade<Vulnerabilidade> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VulnerabilidadeFacade() {
        super(Vulnerabilidade.class);
    }
    
    public boolean existeVul(int idVul) {

        try {
            Vulnerabilidade vulnerabilidade = (Vulnerabilidade) em
                    .createQuery(
                            "SELECT u from Vulnerabilidade u where u.id_vulnerabilidade = :idVul")
                    .setParameter("idVul", idVul).getSingleResult();
            return vulnerabilidade != null;
        } catch (NoResultException e) {
            return false;
        }
    }
}
