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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.EscopoVul;
import pucrs.br.entity.EscopoVulPK;
import pucrs.br.entity.Grafico;
import pucrs.br.entity.Usuario;
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
    
    public Vulnerabilidade findbyId(int idVul) {        
        Query query = em.createNativeQuery("SELECT * FROM escopo_vul e WHERE id_vulnerabilidade = "+idVul);
        return (Vulnerabilidade) query.getSingleResult();
    }
    
    public Vulnerabilidade findVulbyId(int idVul) {        
        return (Vulnerabilidade) em.createQuery("SELECT u FROM Vulnerabilidade u WHERE u.idVulnerabilidade = :idVulnerabilidade")
                .setParameter("idVulnerabilidade", idVul)
                .getSingleResult();
    }
    
    public String getVulNome(int idVul) {
        Query query = em.createNativeQuery("SELECT nome FROM vulnerabilidade WHERE id_vulnerabilidade = "+idVul);
        return (String) query.getSingleResult();
    }
    
    public List<Vulnerabilidade> findAllVulByUser(Empresa emp) {        
        return em.createQuery("SELECT u FROM Vulnerabilidade u WHERE u.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", emp)
                .getResultList();
    }
    
    public List<Grafico> findResultGrafico(Empresa emp) {
        Query query = em.createNativeQuery("select t.id_vulnerabilidade, "
                                         + "count(t.id_escopo) "
                                         + "from escopo_vul t "
                                         + "WHERE id_empresa = "+emp.getIdEmpresa()
                                         + " GROUP by t.id_vulnerabilidade");
        List<Object[]> lista = query.getResultList();
        List<Grafico> grafico = new ArrayList<>();
        for (Object[] row : lista) {
            grafico.add(new Grafico(findVulbyId((int) row[0]), (long) row[1]));
        }
        return grafico;
    }
}
