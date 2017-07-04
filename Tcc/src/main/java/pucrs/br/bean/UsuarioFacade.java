package pucrs.br.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pucrs.br.entity.GrupoUsuario;
import pucrs.br.entity.Usuario;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
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
    
    // Recupera os grupos
    public List<GrupoUsuario> getGrupos() {
        try {
            Query query = em.createNativeQuery("SELECT id_grupo from grupo_usuario");
            List lista = query.getResultList();
            return lista;
        } catch (NoResultException e) {
            return null;
        }
    }

    // Recupera dados de um uusário a partir de um id e senha
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
    
    // Verifica se existe um usuário a partir de um ID
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
}
