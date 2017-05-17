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
import pucrs.br.entity.Usuario;

/**
 *
 * @author psysvica
 */
@Stateless
public class EmpresaFacade extends AbstractFacade<Empresa> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }
    
    public boolean existeEmpresa(int idEmpresa) {

        try {
            Empresa empresa = (Empresa) em
                    .createQuery(
                            "SELECT u from Empresa u where u.id_empresa = :idEmpresa")
                    .setParameter("idEmpresa", idEmpresa).getSingleResult();
            return empresa != null;
        } catch (NoResultException e) {
            return false;
        }
    }
    
}
