/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.dao;

/**
 *
 * @author psysvica
 */
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.GrupoUsuario;

public class EmpresaDAO {

    private EntityManagerFactory factory = Persistence
            .createEntityManagerFactory("pucrs_Tcc_war_1.0PU");
    private EntityManager em = factory.createEntityManager();

    public boolean inserirEmpresa(Empresa empresa) {
        try {
            em.getTransaction().begin();
            em.persist(empresa);
            em.getTransaction().commit();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
    
    public List<Empresa> getEmpresas() {
        try {
            Query query = em.createNativeQuery("SELECT id_empresa from empresa");
            List lista = query.getResultList();
                //lista = (ArrayList<GrupoUsuario>) em.createQuery("SELECT id_grupo from grupo_usuario").getResultList();

            return lista;
        } catch (NoResultException e) {
            return null;
        }
    }

}
