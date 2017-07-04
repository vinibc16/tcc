package pucrs.br.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pucrs.br.entity.VulnerabilidadeAdmin;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@Stateless
public class VulnerabilidadeAdminFacade extends AbstractFacade<VulnerabilidadeAdmin> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VulnerabilidadeAdminFacade() {
        super(VulnerabilidadeAdmin.class);
    }
    
}
