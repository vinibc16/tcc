package pucrs.br.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Grafico;
import pucrs.br.entity.Vulnerabilidade;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
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
    
    // Pesquisa vulnerabilidade por um dado ID
    public Vulnerabilidade findVulbyId(int idVul) {        
        return (Vulnerabilidade) em.createQuery("SELECT u FROM Vulnerabilidade u WHERE u.idVulnerabilidade = :idVulnerabilidade")
                .setParameter("idVulnerabilidade", idVul)
                .getSingleResult();
    }
    
    // Pesquisa todas vulnerabilidade dado uma Empresa de parâmetro
    public List<Vulnerabilidade> findAllVulByUser(Empresa emp) {        
        return em.createQuery("SELECT u FROM Vulnerabilidade u WHERE u.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", emp)
                .getResultList();
    }
    
    // Consulta de banco para gerar o gráfico do dashboard
    public List<Grafico> findResultGrafico(Empresa emp) {
        Query query = em.createNativeQuery("SELECT t.id_vulnerabilidade,"
                                         + " COUNT(t.id_escopo)"
                                         + " FROM escopo_vul t"
                                         + " WHERE id_empresa = "+emp.getIdEmpresa()
                                         + " GROUP by t.id_vulnerabilidade"
                                         + " ORDER BY count(t.id_escopo) DESC");
        List<Object[]> lista = query.getResultList();
        List<Grafico> grafico = new ArrayList<>();
        int count = 0;
        for (Object[] row : lista) {
            if (count == 5) {
                break;
            }
            grafico.add(new Grafico(findVulbyId((int) row[0]), (long) row[1]));
        }
        return grafico;
    }
}
