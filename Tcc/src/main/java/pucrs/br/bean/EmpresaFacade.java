package pucrs.br.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import pucrs.br.entity.Empresa;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@Stateless
public class EmpresaFacade extends AbstractFacade<Empresa> {

    @PersistenceContext(unitName = "pucrs_Tcc_war_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmpresaFacade() {
        super(Empresa.class);
    }
    
    // Verifica se existe uma empresa cadastrada com o ID de parâmetro
    public boolean existeEmpresa(int idEmpresa) {

        try {
            Empresa empresa = (Empresa) em
                    .createQuery(
                            "SELECT u from Empresa u where u.id_empresa = :idEmpresa")
                    .setParameter("idEmpresa", idEmpresa).getSingleResult();
            return empresa != null;
        } catch (NoResultException e) {
            return false;
        }
    }
    
}
