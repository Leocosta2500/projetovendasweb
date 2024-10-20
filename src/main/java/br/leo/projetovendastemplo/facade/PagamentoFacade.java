package br.leo.projetovendastemplo.facade;

import br.leo.projetovendastemplo.entity.PagamentoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.leo.projetovendastemplo.entity.VendaEntity;

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
        List<PagamentoEntity> entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery(
                    "SELECT p.num_pag, p.dta_hor_pag, p.vlr_pago, p.tipo_pag, p.saldo_devedor, p.status_pag, v.num_cupom, v.des_cliente "
                    + "FROM PagamentoEntity p JOIN p.num_cupom v "
                    + "ORDER BY p.dta_hor_pag DESC"
            );

            List<Object[]> results = query.getResultList();
            for (Object[] result : results) {
                PagamentoEntity pagamento = new PagamentoEntity();
                pagamento.setNum_pag((Integer) result[0]);
                pagamento.setDta_hor_pag((Date) result[1]);
                pagamento.setVlr_pago((BigDecimal) result[2]);
                pagamento.setTipo_pag((String) result[3]);
                pagamento.setSaldo_devedor((BigDecimal) result[4]);
                pagamento.setStatus_pag((String) result[5]);

                // Criação do VendaEntity para atribuir o num_cupom
                VendaEntity venda = new VendaEntity();
                venda.setNum_cupom((Integer) result[6]);
                venda.setDes_cliente((String) result[7]);
                pagamento.setNum_cupom(venda);

                pagamento.setDes_cliente(venda.getDes_cliente());

                entityList.add(pagamento);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar histórico de pagamentos: " + e);
        }
        return entityList;
    }

}
