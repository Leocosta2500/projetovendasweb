package br.leo.projetovendastemplo.facade;

import br.leo.projetovendastemplo.entity.VendaEntity;
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
public class VendaFacade extends AbstractFacade<VendaEntity> {

    @PersistenceContext(unitName = "ProjetovendastemploPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VendaFacade() {
        super(VendaEntity.class);
    }

    private List<VendaEntity> entityList;

    public List<VendaEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            //utilizando JPQL para construir a query 
            Query query = getEntityManager().
                    createQuery("SELECT p FROM VendaEntity p ORDER BY p.des_cliente");
            //verifica se existe algum resultado para não gerar excessão
            entityList = (List<VendaEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    public VendaEntity findByNumCupom(Integer numCupom) {
        try {
            return em.find(VendaEntity.class, numCupom);
        } catch (Exception e) {
            System.out.println("Erro ao buscar venda por número do cupom: " + e);
            return null;
        }
    }

    public void edit(VendaEntity entity) {
        getEntityManager().merge(entity);
    }

    //adicionado esse para puxar num_cupom automatico
    public Integer findLastNumCupom() {
        try {
            Query query = getEntityManager().createQuery("SELECT MAX(v.num_cupom) FROM VendaEntity v");
            return (Integer) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erro ao buscar o último número de cupom: " + e);
            return null;
        }
    }

}
