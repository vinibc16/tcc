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
import pucrs.br.entity.Grafico;

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
        return em.createQuery("SELECT u FROM Escopo u WHERE u.idEmpresa = :idEmpresa")
                .setParameter("idEmpresa", idEmpresa)
                .getResultList();
    }
    
    public Escopo findbyId(int idEscopo) {        
        Escopo esc = (Escopo) em.createQuery("SELECT u FROM Escopo u WHERE u.idEscopo = :idEscopo")
                .setParameter("idEscopo", idEscopo)
                .getSingleResult();
        return esc;
    }
    
    public List<Grafico> findResultGrafico(int idEmpresa) {
        Query query = em.createNativeQuery("select id_escopo, "
                                        + "round(sum(risco) / (select count(1) from escopo_vul t where t.id_escopo = p.id_escopo and t.id_empresa = p.id_empresa),2) "
                                        + "from escopo_vul p "
                                        + "where id_empresa = "+idEmpresa
                                        + " group by id_escopo");
        List<Object[]> lista = query.getResultList();
        List<Grafico> grafico = new ArrayList<>();
        for (Object[] row : lista) {
            grafico.add(new Grafico(findbyId((int)row[0]), (double)row[1]));
        }
        return grafico;
    }
}
