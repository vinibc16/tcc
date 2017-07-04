package pucrs.br.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Responsaveis;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
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
    
    // Recupera os responsáveis de uma dada empresa
    public List<Responsaveis> findAllRespByUser(Empresa emp) {        
        return em.createQuery("SELECT u FROM Responsaveis u WHERE u.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", emp)
                .getResultList();
    }
}
