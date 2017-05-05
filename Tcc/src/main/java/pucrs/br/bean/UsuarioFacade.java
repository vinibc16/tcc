/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.GrupoUsuario;
import pucrs.br.entity.Usuario;

/**
 *
 * @author psysvica
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public List<GrupoUsuario> getGrupos() {
        try {
            Query query = em.createNativeQuery("SELECT id_grupo from grupo_usuario");
            List lista = query.getResultList();
                //lista = (ArrayList<GrupoUsuario>) em.createQuery("SELECT id_grupo from grupo_usuario").getResultList();

            return lista;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario getUsuario(String id, String senha) {

        try {
            Usuario usuario = (Usuario) em
                    .createQuery(
                            "SELECT u from Usuario u where u.id = :id and u.senha = :senha")
                    .setParameter("id", id)
                    .setParameter("senha", senha).getSingleResult();

            return usuario;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public boolean existeUsuario(String id) {

        try {
            Usuario usuario = (Usuario) em
                    .createQuery(
                            "SELECT u from Usuario u where u.id = :id")
                    .setParameter("id", id).getSingleResult();
            return usuario != null;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean inserirUsuario(Usuario usuario) {
        try {
            if (existeUsuario(usuario.getId())) {
                return false;
            }
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean deletarUsuario(Usuario usuario) {
        try {
            em.remove(usuario);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
