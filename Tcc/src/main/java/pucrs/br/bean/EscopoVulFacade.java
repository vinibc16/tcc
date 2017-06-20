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
    private VulnerabilidadeFacade vulfac;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EscopoVulFacade() {
        super(EscopoVul.class);
    }
    
    public List<EscopoVul> findAllfindByIdEscopo(Escopo escopo) {        
        Query query = em.createNativeQuery("SELECT id_empresa, id_escopo, id_vulnerabilidade, impacto, probabilidade, aceito FROM escopo_vul e WHERE id_escopo = "+escopo.getIdEscopo());
        List<Object[]> lista = query.getResultList();
        List<EscopoVul> ev = new ArrayList<>();
        for (Object[] row : lista) {
            EscopoVul esc = new EscopoVul(new EscopoVulPK((int)row[1],(int)row[2]));
            //esc.setVulnerabilidade(vulfac.findbyId((int)row[2]));
            if (row[3] != null) {
                esc.setImpacto((double) row[3]);
            }
            if (row[4] != null) {
                esc.setProbabilidade((double) row[4]);
            }
            if (row[3] != null && row[4] != null) {
                esc.setRisco((double)row[3] * (double)row[4]);
            }
            if (row[5] != null) {
                esc.setAceito((int)row[5]);
            }
            ev.add(esc);
        }
        return ev;
    }
    
    public List<EscopoVul> consultaRelatorio(Escopo escopo) {        
        Query query = em.createNativeQuery("SELECT id_empresa, id_escopo, id_vulnerabilidade, impacto, probabilidade, aceito FROM escopo_vul e WHERE id_escopo = "+escopo.getIdEscopo()+
                                           " and aceito = 0");
        List<Object[]> lista = query.getResultList();
        List<EscopoVul> ev = new ArrayList<>();
        for (Object[] row : lista) {
            EscopoVul esc = new EscopoVul(new EscopoVulPK((int)row[1],(int)row[2]));
            //esc.setVulnerabilidade(vulfac.findbyId((int)row[2]));
            if (row[3] != null) {
                esc.setImpacto((double) row[3]);
            }
            if (row[4] != null) {
                esc.setProbabilidade((double) row[4]);
            }
            if (row[3] != null && row[4] != null) {
                esc.setRisco(((double)row[3] * (double)row[4]));
            }
            if (row[5] != null) {
                esc.setAceito((int)row[5]);
            }
            ev.add(esc);
        }
        return ev;
    }
    
    
}
