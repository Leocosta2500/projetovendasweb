package br.leo.projetovendastemplo.facade;

import br.leo.projetovendastemplo.entity.UsuarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leona
 */
@Stateless //utilizados para outras chamadas de qualquer cliente. 
public class UsuarioFacade extends AbstractFacade<UsuarioEntity> {

    @PersistenceContext(unitName = "ProjetovendastemploPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(UsuarioEntity.class);
    }

    private List<UsuarioEntity> entityList;

    public List<UsuarioEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            //utilizando JPQL para construir a query 
            Query query = getEntityManager().createQuery("SELECT p FROM UsuarioEntity p order by p.nome_user");
            //verifica se existe algum resultado para não gerar excessão
            if (!query.getResultList().isEmpty()) {
                entityList = (List<UsuarioEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    /**
     * Buscar uma pessoa por email
     *
     * @param email
     * @param senha
     * @return
     */
    public UsuarioEntity buscarPorEmail(String email_user, String des_senha) {
        UsuarioEntity usuario = new UsuarioEntity();
        try {
            //utilizando JPQL para construir a query 
            Query query = getEntityManager()
                    .createQuery("SELECT p FROM UsuarioEntity p WHERE p.email_user = :email_user AND p.des_senha = :des_senha");
            query.setParameter("email_user", email_user);
            query.setParameter("des_senha", des_senha);

            //verifica se existe algum resultado para não gerar excessão
            if (!query.getResultList().isEmpty()) {
                usuario = (UsuarioEntity) query.getSingleResult();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return usuario;
    }

    public UsuarioEntity buscarPorEmail(String email_user) {
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT p FROM UsuarioEntity p WHERE p.email_user = :email_user");
            query.setParameter("email_user", email_user);

            if (!query.getResultList().isEmpty()) {
                return (UsuarioEntity) query.getSingleResult();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return null;
    }

    public boolean emailExiste(String email) {
        Query query = em.createQuery("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.email_user = :email");
        query.setParameter("email", email);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    public boolean emailExiste(String email, Integer codUsuario) {
        Query query = em.createQuery(
                "SELECT COUNT(u) FROM UsuarioEntity u WHERE u.email_user = :email AND u.cod_usuario <> :codUsuario"
        );
        query.setParameter("email", email);
        query.setParameter("codUsuario", codUsuario);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

}
