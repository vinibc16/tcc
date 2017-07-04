package pucrs.br.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.EscopoVul;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
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
    
    // Pesquisa todas as vulnerabilidade de um escopo
    public List<EscopoVul> findAllfindByIdEscopo(Escopo escopo) {   
        em.clear();
        Query query = em.createQuery("SELECT u"
                                    + " FROM EscopoVul u"
                                    + " WHERE u.escopo = :escopo")
                        .setParameter("escopo", escopo);
        List<EscopoVul> lista = query.getResultList();
        em.flush();
        return lista;
    }
    
    // Recupera dados do escopo e vulnerabilidades para o relatório
    public List<EscopoVul> consultaRelatorio(Escopo escopo) {        
        Query query = em.createQuery("SELECT u"
                                    + " FROM EscopoVul u"
                                    + " WHERE u.escopo = :escopo"
                                    + " AND u.aceito = 0")
                        .setParameter("escopo", escopo);
        List<EscopoVul> lista = query.getResultList();
        em.flush();
        return lista;
    }
    
    // Verifica se habilita ou não o botão do relatório
    public boolean desabilitaRelatorio(Escopo escopo) {        
        Query query = em.createQuery("SELECT u"
                                    + " FROM EscopoVul u"
                                    + " WHERE u.escopo = :escopo"
                                    + " AND u.aceito = 0")
                        .setParameter("escopo", escopo);
        List<EscopoVul> lista = query.getResultList();
        em.flush();
        if ( lista == null) {
            return true;
        }
        return true;
    }
    
    
}
