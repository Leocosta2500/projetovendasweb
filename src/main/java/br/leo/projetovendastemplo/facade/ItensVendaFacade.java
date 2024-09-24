/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.leo.projetovendastemplo.facade;

import br.leo.projetovendastemplo.entity.ItensVendaEntity;
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
public class ItensVendaFacade extends AbstractFacade<ItensVendaEntity> {

    @PersistenceContext(unitName = "ProjetovendastemploPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItensVendaFacade() {
        super(ItensVendaEntity.class);
    }

    private List<ItensVendaEntity> entityList;

    public List<ItensVendaEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM ItensVendaEntity p ORDER BY p.id_item_vda");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<ItensVendaEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    public List<ItensVendaEntity> findByNumCupom(Integer numCupom) {
        List<ItensVendaEntity> itensVendaList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT i FROM ItensVendaEntity i WHERE i.num_cupom.num_cupom = :numCupom");
            query.setParameter("numCupom", numCupom);
            itensVendaList = query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return itensVendaList;
    }

}
