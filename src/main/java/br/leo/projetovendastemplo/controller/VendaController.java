package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.ItensVendaEntity;
import br.leo.projetovendastemplo.entity.UsuarioEntity;
import br.leo.projetovendastemplo.entity.VendaEntity;
import br.leo.projetovendastemplo.facade.ItensVendaFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.servlet.http.HttpSession;
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
@Named(value = "vendaController")
@SessionScoped

public class VendaController implements Serializable {

    @EJB
    private br.leo.projetovendastemplo.facade.ItensVendaFacade itensVendaFacade;

    @EJB
    private br.leo.projetovendastemplo.facade.VendaFacade ejbFacade;

    //objeto que representa uma pessoa
    private VendaEntity venda = new VendaEntity();
    //objeto que representa uma lista de pessoas
    private List<VendaEntity> vendaList = new ArrayList<>();

    private VendaEntity selected;

    private List<ItensVendaEntity> itensVendaList;

    public List<ItensVendaEntity> getItensVendaList() {
        return itensVendaList;
    }

    private boolean itensVendaTabDisabled = true;

    public boolean isItensVendaTabDisabled() {
        return itensVendaTabDisabled;
    }

    public void setItensVendaTabDisabled(boolean itensVendaTabDisabled) {
        this.itensVendaTabDisabled = itensVendaTabDisabled;
    }

    private boolean mostrarIncluirVenda = false;

    private int activeTabIndex = 0;  // Índice da aba inicial

    // Getter para activeTabIndex
    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    // Setter para activeTabIndex
    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    public boolean isMostrarIncluirVenda() {
        return mostrarIncluirVenda;
    }

    public void toggleIncluirVenda() {
        this.mostrarIncluirVenda = !this.mostrarIncluirVenda;
    }

    public String getTipoVendaDescricao(String tipoVenda) {
        switch (tipoVenda) {
            case "301":
                return "Vista";
            case "302":
                return "Parcelado";
            default:
                return "Desconhecido";
        }
    }

    public void updateClienteInfo() {
        if (venda.getCod_cliente() != null) {
            venda.setDes_cliente(venda.getCod_cliente().getNome());
            venda.setDes_uf_cli(venda.getCod_cliente().getUf());
        }
    }

    public void updateCodOperacao() {
        if (venda.getTip_venda() != null) {
            if (venda.getTip_venda() == 301) {
                venda.setCod_operacao(10);
            } else if (venda.getTip_venda() == 302) {
                venda.setCod_operacao(11);
            }
        }
    }

    public void setUsuarioLogado() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("pessoaLogada");
        if (usuarioLogado != null) {
            venda.setCod_usuario(usuarioLogado);
        }
    }

    public void updateClienteInfoS() {
        if (selected.getCod_cliente() != null) {
            selected.setDes_cliente(selected.getCod_cliente().getNome());
            selected.setDes_uf_cli(selected.getCod_cliente().getUf());
        }
    }

    public void updateCodOperacaoS() {
        if (selected.getTip_venda() != null) {
            if (selected.getTip_venda() == 301) {
                selected.setCod_operacao(10);
            } else if (selected.getTip_venda() == 302) {
                selected.setCod_operacao(11);
            }
        }
    }

    //Calcula o valor total da venda com base nos itens associados ao número do cupom (numCupom).

    public void calcularValorTotalVenda(Integer numCupom) {
        BigDecimal valorTotalVenda = BigDecimal.ZERO;
        List<ItensVendaEntity> itensVendaList = itensVendaFacade.findByNumCupom(numCupom);  // Use a instância injetada

        for (ItensVendaEntity item : itensVendaList) {
            valorTotalVenda = valorTotalVenda.add(item.getVlr_total());
        }

        VendaEntity venda = ejbFacade.find(numCupom);
        venda.setVlr_total_vda(valorTotalVenda);
        ejbFacade.edit(venda);
    }

    public List<VendaEntity> vendaAll() {
        return ejbFacade.buscarTodos();
    }

    public List<VendaEntity> getVendaList() {
        return vendaList;
    }

    public void setVendaList(List<VendaEntity> vendaList) {
        this.vendaList = vendaList;
    }

    public VendaEntity getSelected() {
        return selected;
    }

    public void setSelected(VendaEntity selected) {
        this.selected = selected;
    }

    public VendaEntity getVenda() {
        return venda;
    }

    public void setVenda(VendaEntity venda) {
        this.venda = venda;
    }

    public VendaEntity prepareAdicionar() {
        venda = new VendaEntity();
        setUsuarioLogado(); // Definir o usuário logado
        this.activeTabIndex = 0;
        this.itensVendaTabDisabled = true;
        try {
            // Redirecionando para a página vendasitens.xhtml
            FacesContext.getCurrentInstance().getExternalContext().redirect("vendasitens.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return venda;
    }

    public void prepareEditar(VendaEntity venda) {
        this.selected = venda;
    }

    
    // Valida os campos obrigatórios da venda, como cliente, UF e tipo de venda
    public void adicionarVenda() {

        if (venda.getCod_cliente() == null || venda.getDes_cliente() == null || venda.getDes_cliente().isEmpty()
                || venda.getDes_uf_cli() == null || venda.getDes_uf_cli().isEmpty()
                || venda.getTip_venda() == null) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Todos os campos obrigatórios devem ser preenchidos.", null));
            return;
        }
        Date dta_hor_mov = new Timestamp(System.currentTimeMillis());
        venda.setDta_hor_mov(dta_hor_mov);
        persist(VendaController.PersistAction.CREATE, "Registro incluído com sucesso!");

        //     try {
        //         Integer lastNumCupom = ejbFacade.findLastNumCupom();
        //          FacesContext.getCurrentInstance().getExternalContext().redirect("itensvenda.xhtml?num_cupom=" + lastNumCupom);
        //      } catch (IOException e) {
        //          e.printStackTrace();
        //      }
        setItensVendaTabDisabled(false);
        this.itensVendaTabDisabled = false;
        activeTabIndex = 1;
        vendaList.add(venda);
    }

    
    //Carrega os itens associados à venda selecionada (selected) usando o número do cupom.
    public void prepareItensVenda() {
        if (selected != null && selected.getNum_cupom() != null) {
            itensVendaList = itensVendaFacade.findByNumCupom(selected.getNum_cupom());
        } else {
            itensVendaList = new ArrayList<>();
        }
    }

    //Atualiza os dados da venda selecionada no banco de dados.

    public void editarVenda() {
        persist(VendaController.PersistAction.UPDATE,
                "Registro alterado com sucesso!");
    }

    
   // Verifica se a venda possui pagamentos associados

    public void deletarVenda() {
        if (selected != null) {
            Integer numCupom = selected.getNum_cupom();
            try {
                // Verificar se há pagamentos associados ao número do cupom
                if (ejbFacade.hasPagamentoRegistrado(numCupom)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Cupom contém registro de pagamento. Exclusão não permitida.", null));
                    return; // Impedir a execução da lógica de exclusão
                }

                // Se não houver pagamentos registrados, prosseguir com a exclusão
                List<ItensVendaEntity> itensVendaList = itensVendaFacade.findByNumCupom(numCupom);
                if (itensVendaList != null && !itensVendaList.isEmpty()) {
                    for (ItensVendaEntity item : itensVendaList) {
                        itensVendaFacade.remove(item); // Remover o item do banco de dados
                    }
                }

                // Excluir a venda
                ejbFacade.remove(selected);

                // Mensagem de sucesso
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Venda excluída com sucesso!"));

                // Limpar a seleção e atualizar a lista de vendas
                selected = null;
                vendaList = null;

            } catch (Exception e) {
                // Tratar erros durante a exclusão
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Erro ao excluir a venda: " + e.getMessage(), null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Nenhuma venda selecionada para exclusão.", null));
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    /**
     * Método que recebe a requisição para persistencia e executa a mesma.
     *
     * @param persistAction
     * @param successMessage
     */
    private void persist(PersistAction persistAction, String successMessage) {
        try {
            if (null != persistAction) {
                switch (persistAction) {
                    case CREATE:
                        ejbFacade.createReturn(venda);
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

    public VendaEntity getVenda(java.lang.Integer num_cupom) {
        return ejbFacade.find(num_cupom);
    }

    @FacesConverter(forClass = VendaEntity.class)
    public static class VendaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VendaController controller
                    = (VendaController) facesContext.getApplication().getELResolver().
                            getValue(facesContext.getELContext(),
                                    null, "vendaController");
            return controller.getVenda(getKey(value));
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
        public String getAsString(FacesContext facesContext,
                UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof VendaEntity) {
                VendaEntity o = (VendaEntity) object;
                return getStringKey(o.getNum_cupom());
            } else {
                return null;
            }
        }
    }

    
    //Subtrai o valor de um item excluído do total da venda.

    public void diminuirValorTotalVenda(Integer numCupom, BigDecimal valorItemExcluido) {
        VendaEntity venda = ejbFacade.find(numCupom);
        if (venda != null && venda.getVlr_total_vda() != null) {
            BigDecimal valorTotalAtual = venda.getVlr_total_vda();
            BigDecimal novoValorTotal = valorTotalAtual.subtract(valorItemExcluido);
            venda.setVlr_total_vda(novoValorTotal);
            ejbFacade.edit(venda);
        }
    }

    //Subtrai a quantidade de itens excluídos do total de itens da venda.

    public void diminuirQuantidadeTotalItens(Integer numCupom, int qtdItensExcluidos) {
        VendaEntity venda = ejbFacade.find(numCupom);
        if (venda != null && venda.getQtd_itens() != null) {
            int quantidadeTotalAtual = venda.getQtd_itens();
            int novaQuantidadeTotal = quantidadeTotalAtual - qtdItensExcluidos;
            venda.setQtd_itens(novaQuantidadeTotal);
            ejbFacade.edit(venda);
        }
    }

}
