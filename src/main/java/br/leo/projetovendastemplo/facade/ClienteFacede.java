
package br.leo.projetovendastemplo.facade;

import br.leo.projetovendastemplo.entity.ClienteEntity;
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
@Stateless
public class ClienteFacede extends AbstractFacade<ClienteEntity>{
    
    @PersistenceContext(unitName = "ProjetovendastemploPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacede() {
        super(ClienteEntity.class);
    }

    private List<ClienteEntity> entityList;

    public List<ClienteEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM ClienteEntity p order by p.nome");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<ClienteEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    

}
