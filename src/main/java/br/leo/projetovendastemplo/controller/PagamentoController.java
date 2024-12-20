package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.PagamentoEntity;
import br.leo.projetovendastemplo.entity.VendaEntity;
import jakarta.annotation.PostConstruct;
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
import org.primefaces.PrimeFaces;

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

    private List<PagamentoEntity> detalhesPagamentoList = new ArrayList<>();

    private List<PagamentoEntity> historicoPagamentoList = new ArrayList<>();

    public List<PagamentoEntity> getHistoricoPagamentoList() {
        return historicoPagamentoList;
    }

    public void setHistoricoPagamentoList(List<PagamentoEntity> historicoPagamentoList) {
        this.historicoPagamentoList = historicoPagamentoList;
    }
    
    
    
    //Carrega o histórico de pagamentos e exibe uma mensagem se nenhum for encontrado.

    public void prepareHistorico() {
        try {
            // Carrega todos os registros de pagamentos usando o método buscarHistoricoPorPagamento
            historicoPagamentoList = ejbFacade.buscarHistoricoPorPagamento();

            if (historicoPagamentoList == null || historicoPagamentoList.isEmpty()) {
                addErrorMessage("Nenhum histórico de pagamentos encontrado.");
            }
        } catch (Exception e) {
            addErrorMessage("Erro ao carregar o histórico de pagamentos: " + e.getMessage());
        }
    }

    public List<PagamentoEntity> getDetalhesPagamentoList() {
        return detalhesPagamentoList;
    }

    public void setDetalhesPagamentoList(List<PagamentoEntity> detalhesPagamentoList) {
        this.detalhesPagamentoList = detalhesPagamentoList;
    }

    
    //Adiciona os detalhes do pagamento selecionado à lista de exibição.

    public void prepareDetalhesPagamento() {
        if (selected != null) {
            detalhesPagamentoList = new ArrayList<>();
            detalhesPagamentoList.add(selected);
        } else {
            detalhesPagamentoList = new ArrayList<>();
        }
    }

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

    public void prepararEdicao() {
        if (selected != null) {
            try {
                // Oculta o campo de valor adicional ao preparar a edição
                this.valorAdicional = null; // ou BigDecimal.ZERO se for melhor para seu caso

                // Define o tipo de pagamento como null para exibir "Selecione um tipo de pagamento..."
                selected.setTipo_pag(null);

                // Redireciona para a página de edição com os dados do pagamento selecionado
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("pagamento", selected);
                FacesContext.getCurrentInstance().getExternalContext().redirect("pagamentoseditar.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
                addErrorMessage("Erro ao redirecionar para a página de edição: " + e.getMessage());
            }
        } else {
            addErrorMessage("Nenhum pagamento selecionado para edição.");
        }
    }

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext.getExternalContext().getFlash().get("pagamento") != null) {
            selected = (PagamentoEntity) facesContext.getExternalContext().getFlash().get("pagamento");
        }
    }

    // Exibe o campo para adicionar um valor extra ao pagamento.

    public void mostrarCampoAdicional() {
        this.valorAdicional = BigDecimal.ZERO; // Define um valor inicial para exibir o campo
    }

    //Valida e recalcula o saldo devedor ao incluir um valor adicional:

    public void calcularSaldoDevedorComAdicional() {

        if (valorAdicional != null && valorAdicional.compareTo(BigDecimal.ZERO) <= 0) {
            // Exibe mensagem de erro
            addErrorMessage("O valor adicional deve ser maior que zero.");

            // Fecha o campo de valor adicional (define como nulo ou zero)
            valorAdicional = null;

            // Atualiza a interface para mostrar a mensagem e ocultar o valor adicional
            PrimeFaces.current().ajax().update("frmEditPagamento:growl", "adicionalValorPanel");

            // Recarrega a página após um breve intervalo
            PrimeFaces.current().executeScript("setTimeout(function(){ location.reload(); }, 3000);");
            return;
        }
        if (selected != null) {
            BigDecimal total = selected.getVlr_total_pag() != null ? selected.getVlr_total_pag() : BigDecimal.ZERO;
            BigDecimal pago = selected.getVlr_pago() != null ? selected.getVlr_pago() : BigDecimal.ZERO;
            BigDecimal adicional = valorAdicional != null ? valorAdicional : BigDecimal.ZERO;
            BigDecimal saldoDevedor = total.subtract(pago);

            // Verifica se o valor adicional é maior que o saldo devedor
            if (adicional.compareTo(saldoDevedor) > 0) {
                addErrorMessage("Atenção: O valor adicional não pode ser maior que o saldo devedor.");
                // Define o valor adicional como nulo ou vazio para o usuário corrigir
                valorAdicional = null;
                // Atualiza somente a mensagem, sem alterar Valor Pago e Saldo Devedor
                PrimeFaces.current().ajax().update("frmEditPagamento:growl", "frmEditPagamento:iptValorAdicional");

                // Adiciona um script para recarregar a página após a mensagem
                PrimeFaces.current().executeScript("setTimeout(function(){ location.reload(); }, 3000);"); // 3 segundos de delay

            } else {
                // Atualiza o Saldo Devedor e Valor Pago se o valor adicional for válido
                saldoDevedor = saldoDevedor.subtract(adicional);
                selected.setSaldo_devedor(saldoDevedor);
                selected.setVlr_pago(pago.add(adicional));
                PrimeFaces.current().ajax().update("frmEditPagamento:iptSaldoDevedor", "frmEditPagamento:iptVlrPago");
            }
        }
    }

    //Adiciona o valor extra ao valor pago do pagamento selecionado.

    public void somarValorAdicional() {
        if (selected != null && selected.getVlr_pago() != null && valorAdicional != null) {
            // Soma o valor adicional ao valor pago
            BigDecimal novoValorPago = selected.getVlr_pago().add(valorAdicional);
            selected.setVlr_pago(novoValorPago);
        }
    }

    //Verifica se o cupom já tem pagamento associado.

    public void onCupomSelect() {
        VendaEntity venda = null;
        if (pagamento.getNum_cupom() != null) {
            venda = vendaFacade.findByNumCupom(pagamento.getNum_cupom().getNum_cupom());
        }

        // Verificar se já existe um pagamento para o cupom selecionado
        if (venda != null && ejbFacade.existsPaymentForCupom(venda.getNum_cupom())) {
            addErrorMessage("Este cupom já possui um pagamento registrado.");

            // Limpar o campo do cupom para evitar um novo pagamento
            pagamento.setNum_cupom(null);

            // Atualizar a interface com a mensagem de erro
            PrimeFaces.current().ajax().update("frmPagamento:growl", "frmPagamento:num_cupom");
            return;
        }

        // Segue o restante da lógica de atribuição do valor total e itens do cupom
        if (venda != null) {
            pagamento.setVlr_total_pag(venda.getVlr_total_vda());
            if (venda.getCod_usuario() != null) {
                pagamento.setCod_usuario_pag(venda.getCod_usuario().getCod_usuario());
            }
            itensVendaController.setItensVendaList(itensVendaController.findByNumCupom(pagamento.getNum_cupom().getNum_cupom()));
        }
    }

    //Calcula o saldo devedor com base no total e no valor já pago.

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

    
    // Inicializa um novo pagamento e redireciona para a página pagamentos.xhtml
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

    
    //Valida e adiciona um pagamento ao banco de dados:

    public void adicionarItem() {

        if ("Pago".equals(pagamento.getStatus_pag())) {
            BigDecimal valorTotal = pagamento.getVlr_total_pag() != null ? pagamento.getVlr_total_pag() : BigDecimal.ZERO;
            BigDecimal valorPago = pagamento.getVlr_pago() != null ? pagamento.getVlr_pago() : BigDecimal.ZERO;

            // Validação para garantir que o valor pago seja igual ao valor total quando o status for "Pago"
            if (valorPago.compareTo(valorTotal) != 0) {
                addErrorMessage("Para status 'Pago', o valor pago deve ser igual ao valor total.");
                return;
            }
        }
        try {
            // Definir a data e hora do pagamento
            Date dta_hor_pag = new Timestamp(System.currentTimeMillis());
            pagamento.setDta_hor_pag(dta_hor_pag);

            // Persistir o pagamento no banco de dados
            ejbFacade.createReturn(pagamento);

            // Exibir mensagem de sucesso
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
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

    
    //Salva as alterações no pagamento selecionado e Redireciona para a página principal com uma mensagem de sucesso.
    public void editarItem() {
        try {
            // Atualiza o item no banco de dados
            ejbFacade.edit(selected);

            // Mantém as mensagens no contexto do Flash para serem exibidas após o redirecionamento
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().getFlash().setKeepMessages(true);

            // Exibe mensagem de sucesso
            addSuccessMessage("Registro alterado com sucesso!");

            // Redireciona para a página pagamentos.xhtml
            facesContext.getExternalContext().redirect("pagamento.xhtml");

        } catch (IOException e) {
            addErrorMessage("Erro ao redirecionar: " + e.getMessage());
        } catch (Exception e) {
            addErrorMessage("Erro ao atualizar o pagamento: " + e.getMessage());
        }
    }

    //Remove o pagamento selecionado do banco de dados.

    public void deletarItem() {
        persist(PagamentoController.PersistAction.DELETE,
                "Registro excluído com sucesso!");
    }

    
    // Ajusta automaticamente o valor pago para ser igual ao total se o status for "Pago".

    public void validarValorPago() {
        if ("Pago".equals(pagamento.getStatus_pag())) {
            BigDecimal valorTotal = pagamento.getVlr_total_pag() != null ? pagamento.getVlr_total_pag() : BigDecimal.ZERO;
            BigDecimal valorPago = pagamento.getVlr_pago() != null ? pagamento.getVlr_pago() : BigDecimal.ZERO;

            // Verifica se o valor pago é igual ao valor total
            if (valorPago.compareTo(valorTotal) != 0) {
                pagamento.setVlr_pago(valorTotal);
            }
        }
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
