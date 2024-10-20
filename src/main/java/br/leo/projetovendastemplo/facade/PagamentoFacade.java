package br.leo.projetovendastemplo.facade;

import br.leo.projetovendastemplo.entity.PagamentoEntity;
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
public class PagamentoFacade extends AbstractFacade<PagamentoEntity> {

    @PersistenceContext(unitName = "ProjetovendastemploPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PagamentoFacade() {
        super(PagamentoEntity.class);
    }

    private List<PagamentoEntity> entityList;

    public List<PagamentoEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM PagamentoEntity p ORDER BY p.num_pag");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<PagamentoEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    public List<PagamentoEntity> buscarHistoricoPorPagamento() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM PagamentoEntity p ORDER BY p.num_pag");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<PagamentoEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

}
