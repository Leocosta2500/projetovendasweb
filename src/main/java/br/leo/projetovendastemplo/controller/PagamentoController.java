package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.PagamentoEntity;
import br.leo.projetovendastemplo.entity.VendaEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author leona
 */
@Named(value = "pagamentoController")
@SessionScoped
public class PagamentoController implements Serializable {

    @EJB
    private br.leo.projetovendastemplo.facade.VendaFacade vendaFacade;

    @EJB
    private br.leo.projetovendastemplo.facade.PagamentoFacade ejbFacade;

    @Inject
    private ItensVendaController itensVendaController;

    private PagamentoEntity pagamento = new PagamentoEntity();
    private List<PagamentoEntity> pagamentoList = new ArrayList<>();
    private PagamentoEntity selected;

    public List<PagamentoEntity> pagamentoAll() {
        return ejbFacade.buscarTodos();
    }

    public List<PagamentoEntity> getPagamentoList() {
        return pagamentoList;
    }

    public void setPagamentoList(List<PagamentoEntity> pagamentoList) {
        this.pagamentoList = pagamentoList;
    }

    public PagamentoEntity getSelected() {
        return selected;
    }

    public void setSelected(PagamentoEntity selected) {
        this.selected = selected;
    }

    public PagamentoEntity getPagamento() {
        return pagamento;
    }

    public void setPagamento(PagamentoEntity pagamento) {
        this.pagamento = pagamento;
    }

    public PagamentoEntity getPagamento(java.lang.Integer num_pag) {
        return ejbFacade.find(num_pag);
    }

    @FacesConverter(forClass = PagamentoEntity.class)
    public static class PagamentoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PagamentoController controller
                    = (PagamentoController) facesContext.getApplication().getELResolver().
                            getValue(facesContext.getELContext(),
                                    null, "pagamentoController");
            return controller.getPagamento(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PagamentoEntity) {
                PagamentoEntity o = (PagamentoEntity) object;
                return getStringKey(o.getNum_pag());
            } else {
                return null;
            }
        }
    }

    public void onCupomSelect() {
        VendaEntity venda = null;
        if (pagamento.getNum_cupom() != null) {
            venda = vendaFacade.findByNumCupom(pagamento.getNum_cupom().getNum_cupom());
        } else if (selected != null && selected.getNum_cupom() != null) {
            venda = vendaFacade.findByNumCupom(selected.getNum_cupom().getNum_cupom());
        }

        if (venda != null) {
            if (pagamento.getNum_cupom() != null) {
                pagamento.setVlr_total_pag(venda.getVlr_total_vda());
                if (venda.getCod_usuario() != null) {
                    pagamento.setCod_usuario_pag(venda.getCod_usuario().getCod_usuario());
                }
            } else if (selected != null && selected.getNum_cupom() != null) {
                selected.setVlr_total_pag(venda.getVlr_total_vda());
                if (venda.getCod_usuario() != null) {
                    selected.setCod_usuario_pag(venda.getCod_usuario().getCod_usuario());
                }
            }
            itensVendaController.setItensVendaList(itensVendaController.findByNumCupom(pagamento.getNum_cupom().getNum_cupom()));

        }
    }

    public void calcularSaldoDevedor() {
        BigDecimal total = (pagamento != null && pagamento.getVlr_total_pag() != null) ? pagamento.getVlr_total_pag() : BigDecimal.ZERO;
        BigDecimal pago = (pagamento != null && pagamento.getVlr_pago() != null) ? pagamento.getVlr_pago() : BigDecimal.ZERO;
        BigDecimal saldoDevedor = total.subtract(pago);
        if (pagamento != null) {
            pagamento.setSaldo_devedor(saldoDevedor);
        }
        if (selected != null) {
            BigDecimal totalSelected = (selected.getVlr_total_pag() != null) ? selected.getVlr_total_pag() : BigDecimal.ZERO;
            BigDecimal pagoSelected = (selected.getVlr_pago() != null) ? selected.getVlr_pago() : BigDecimal.ZERO;
            BigDecimal saldoDevedorSelected = totalSelected.subtract(pagoSelected);
            selected.setSaldo_devedor(saldoDevedorSelected);
        }
    }

    private BigDecimal valorAdicional;

    public BigDecimal getValorAdicional() {
        return valorAdicional;
    }

    public void setValorAdicional(BigDecimal valorAdicional) {
        this.valorAdicional = valorAdicional;
    }

    public void adicionarPagamento() {
        if (valorAdicional != null && selected != null) {
            BigDecimal novoValorPago = selected.getVlr_pago().add(valorAdicional);
            selected.setVlr_pago(novoValorPago);
            calcularSaldoDevedor();
        }
    }

    public PagamentoEntity prepareAdicionar() {
        pagamento = new PagamentoEntity(); // Inicializa uma nova entidade de pagamento
        try {
            // Redireciona para a página pagamentos.xhtml
            FacesContext.getCurrentInstance().getExternalContext().redirect("pagamentos.xhtml");
        } catch (IOException e) {
            e.printStackTrace(); // Exibe qualquer erro de redirecionamento
        }
        return pagamento;
    }

    public void adicionarItem() {
        try {
            // Definir a data e hora do pagamento
            Date dta_hor_pag = new Timestamp(System.currentTimeMillis());
            pagamento.setDta_hor_pag(dta_hor_pag);

            // Persistir o pagamento no banco de dados
            ejbFacade.createReturn(pagamento);

            // Exibir mensagem de sucesso
            addSuccessMessage("Registro incluído com sucesso!");

            // Limpar o objeto pagamento para limpar os campos da tela
            pagamento = new PagamentoEntity();

            // Limpar a lista de itens do cupom
            itensVendaController.setItensVendaList(null);

            // Redirecionar para a página pagamentos.xhtml
            FacesContext.getCurrentInstance().getExternalContext().redirect("pagamento.xhtml");

        } catch (IOException e) {
            addErrorMessage("Erro ao redirecionar: " + e.getMessage());
        } catch (Exception e) {
            addErrorMessage("Erro ao incluir pagamento: " + e.getMessage());
        }
    }

    public void editarItem() {
        persist(PagamentoController.PersistAction.UPDATE,
                "Registro alterado com sucesso!");
    }

    public void deletarItem() {
        persist(PagamentoController.PersistAction.DELETE,
                "Registro excluído com sucesso!");
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    private void persist(PersistAction persistAction, String successMessage) {
        try {
            if (null != persistAction) {
                switch (persistAction) {
                    case CREATE:
                        ejbFacade.createReturn(pagamento);
                        break;
                    case UPDATE:
                        ejbFacade.edit(selected);
                        selected = null;
                        break;
                    case DELETE:
                        ejbFacade.remove(selected);
                        selected = null;
                        break;
                    default:
                        break;
                }
            }
            addSuccessMessage(successMessage);
        } catch (EJBException ex) {
            String msg = "";
            Throwable cause = ex.getCause();
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }
            if (msg.length() > 0) {
                addErrorMessage(msg);
            } else {
                addErrorMessage(ex.getLocalizedMessage());
            }
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage());
        }
    }

}
