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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import pucrs.br.entity.Usuario;

public class UsuarioDAO {

    private EntityManagerFactory factory = Persistence
            .createEntityManagerFactory("pucrs_Tcc_war_1.0PU");
    private EntityManager em = factory.createEntityManager();

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
