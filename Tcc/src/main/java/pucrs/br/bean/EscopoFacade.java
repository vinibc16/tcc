package pucrs.br.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Escopo;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
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
    
    // Pesquisa todos os escopos de uma dada empresa
    public List<Escopo> findAllEscByUser(Empresa emp) {        
        return em.createQuery("SELECT u FROM Escopo u WHERE u.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", emp)
                .getResultList();
    }
    
    // Pesquisa um escopo dado um ID
    public Escopo findbyId(int idEscopo) {        
        Escopo esc = (Escopo) em.createQuery("SELECT u FROM Escopo u WHERE u.idEscopo = :idEscopo")
                .setParameter("idEscopo", idEscopo)
                .getSingleResult();
        return esc;
    }
}
