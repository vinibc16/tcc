/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.EscopoVul;
import pucrs.br.entity.EscopoVulPK;

/**
 *
 * @author psysvica
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
    
    public List<EscopoVul> findAllfindByIdEscopo(Escopo escopo) {        
        Query query = em.createNativeQuery("SELECT id_empresa, id_escopo, id_vulnerabilidade, impacto, probabilidade FROM escopo_vul e WHERE id_escopo = "+escopo.getEscopoPK().getIdEscopo());
        List<Object[]> lista = query.getResultList();
        List<EscopoVul> ev = new ArrayList<>();
        for (Object[] row : lista) {
            EscopoVul esc = new EscopoVul(new EscopoVulPK((int)row[0], (int)row[1],(int)row[2]));
            if (row[3] != null) {
                esc.setImpacto((int) row[3]);
            }
            if (row[4] != null) {
                esc.setImpacto((int) row[4]);
            }
            ev.add(esc);
        }
        return ev;
    }
    
}
