package br.leo.projetovendastemplo.facade;

import br.leo.projetovendastemplo.entity.ItemEntity;
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
public class ItemFacede extends AbstractFacade<ItemEntity> {

    @PersistenceContext(unitName = "ProjetovendastemploPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItemFacede() {
        super(ItemEntity.class);
    }

    private List<ItemEntity> entityList;

    public List<ItemEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT i FROM ItemEntity i ORDER BY i.desc_item");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<ItemEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}
