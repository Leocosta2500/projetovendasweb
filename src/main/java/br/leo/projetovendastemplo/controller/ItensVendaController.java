package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.ItensVendaEntity;
import br.leo.projetovendastemplo.entity.VendaEntity;
import br.leo.projetovendastemplo.facade.ItensVendaFacade;
import br.leo.projetovendastemplo.facade.VendaFacade;
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
import java.util.Map;

/**
 *
 * @author leona
 */
@Named(value = "itensVendaController")
@SessionScoped
public class ItensVendaController implements Serializable {

    @EJB
    private ItensVendaFacade ejbFacade;

    @EJB
    private VendaFacade vendaFacade;

    @Inject
    private VendaController vendaController;

    //  @EJB
    //  private br.leo.projetovendastemplo.facade.ItensVendaFacade ejbFacade;
    // Objeto que representa um item de venda
    private ItensVendaEntity itensVenda = new ItensVendaEntity();
    // Objeto que representa uma lista de itens de venda

    private List<ItensVendaEntity> itensVendaList = new ArrayList<>();

    private ItensVendaEntity selected;

    //   public List<ItensVendaEntity> getVendaList() {
//        return ejbFacade.buscarTodos();
//    }
    //       public void calcularValorTotalVendaAtual() {
//    if (itensVenda != null && itensVenda.getNum_cupom() != null) {
//        vendaController.calcularValorTotalVenda(itensVenda.getNum_cupom().getNum_cupom());
//    }
//}
    public List<ItensVendaEntity> findByNumCupom(Integer numCupom) {
        List<ItensVendaEntity> result = ejbFacade.findByNumCupom(numCupom);
        return result != null ? result : new ArrayList<>();
    }

    public int getTotalQuantidadeVendida() {
        int total = 0;
        if (itensVendaList != null) {
            for (ItensVendaEntity item : itensVendaList) {
                total += item.getQtd_venda();
            }
        }
        return total;
    }

    public BigDecimal getTotalValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItensVendaEntity item : itensVendaList) {
            total = total.add(item.getVlr_total());
        }
        return total;
    }

    public void removerItem(ItensVendaEntity item) {
        if (item != null) {
            try {
                // Remover o item do banco de dados
                ejbFacade.remove(item);

                // Remover o item da lista local (para atualizar a UI)
                itensVendaList.remove(item);

                // Mensagem de sucesso para feedback ao usuário
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Item removido com sucesso!", null));
            } catch (Exception e) {
                // Mensagem de erro caso haja falha ao remover
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover item: " + e.getMessage(), null));
            }
        } else {
            // Mensagem de erro caso o item seja nulo
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao remover o item.", null));
        }
    }

    public List<ItensVendaEntity> itensvendaAll() {
        return ejbFacade.buscarTodos();
    }

    public List<ItensVendaEntity> getItensVendaList() {
        return itensVendaList;
    }

    public void setItensVendaList(List<ItensVendaEntity> itensVendaList) {
        this.itensVendaList = itensVendaList != null ? itensVendaList : new ArrayList<>();
    }

    public ItensVendaEntity getSelected() {
        return selected;
    }

    public void setSelected(ItensVendaEntity selected) {
        this.selected = selected;
    }

    public ItensVendaEntity getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(ItensVendaEntity itensVenda) {
        this.itensVenda = itensVenda;
    }

    public void updateValorItem() {
        if (itensVenda.getId_item() != null) {
            itensVenda.setVlr_venda(itensVenda.getId_item().getVlr_item());
        }
    }

    public void onCupomSelect() {
        if (itensVenda != null && itensVenda.getNum_cupom() != null) {
            VendaEntity venda = vendaFacade.findByNumCupom(itensVenda.getNum_cupom().getNum_cupom());
            if (venda != null) {
                itensVenda.setDes_cliente_vda(venda.getDes_cliente());
                if (venda.getCod_cliente() != null) {
                    itensVenda.setCod_cliente_vda(venda.getCod_cliente().getCod_cliente());
                }
                if (venda.getCod_usuario() != null) {
                    itensVenda.setCod_usuario_vda(venda.getCod_usuario().getCod_usuario());
                }
            }
        }
    }

    public void onCupomSelectS() {
        if (selected != null && selected.getNum_cupom() != null) {
            VendaEntity venda = vendaFacade.findByNumCupom(selected.getNum_cupom().getNum_cupom());
            if (venda != null) {
                selected.setDes_cliente_vda(venda.getDes_cliente());
                if (venda.getCod_cliente() != null) {
                    selected.setCod_cliente_vda(venda.getCod_cliente().getCod_cliente());
                }
                if (venda.getCod_usuario() != null) {
                    selected.setCod_usuario_vda(venda.getCod_usuario().getCod_usuario());
                }
            }
        }
    }

    public void calcularValorTotal() {
        if (itensVenda.getQtd_venda() != null && itensVenda.getVlr_venda() != null) {
            BigDecimal quantidade = new BigDecimal(itensVenda.getQtd_venda());
            BigDecimal valorVenda = itensVenda.getVlr_venda();
            BigDecimal valorTotal = quantidade.multiply(valorVenda);
            itensVenda.setVlr_total(valorTotal);
        }
    }

    public void calcularValorTotalS() {
        if (selected != null && selected.getQtd_venda() != null && selected.getVlr_venda() != null) {
            BigDecimal quantidade = new BigDecimal(selected.getQtd_venda());
            BigDecimal valorVenda = selected.getVlr_venda();
            BigDecimal valorTotal = quantidade.multiply(valorVenda);
            selected.setVlr_total(valorTotal);
        }
    }

    public void updateValorItemS() {
        if (selected != null && selected.getId_item() != null) {
            selected.setVlr_venda(selected.getId_item().getVlr_item());
            calcularValorTotal(); // Atualiza também o valor total ao mudar o item
        }
    }

    public void prepararEdicao(ItensVendaEntity item) {
        if (item != null) {
            this.selected = item;
            if (this.selected.getNum_cupom() == null) {
                this.selected.setNum_cupom(new VendaEntity());
            }
            System.out.println("Selected item set for editing: " + this.selected);
        } else {
            addErrorMessage("Nenhum item selecionado para edição.");
            System.out.println("Nenhum item selecionado para edição.");
        }
    }

    //adicionado esse para puxar num_cupom automatico
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("num_cupom")) {
            String numCupomParam = params.get("num_cupom");
            if (numCupomParam != null && !numCupomParam.isEmpty()) {
                try {
                    Integer numCupom = Integer.parseInt(numCupomParam);
                    VendaEntity venda = vendaFacade.findByNumCupom(numCupom);
                    itensVenda.setNum_cupom(venda);
                    onCupomSelect();  // Adicione esta linha para atualizar os campos automaticamente
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Set the latest cupom automatically if num_cupom is not passed in URL
            Integer lastNumCupom = vendaFacade.findLastNumCupom();
            if (lastNumCupom != null) {
                VendaEntity venda = vendaFacade.findByNumCupom(lastNumCupom);
                itensVenda.setNum_cupom(venda);
                onCupomSelect();  // Adicione esta linha para atualizar os campos automaticamente
            }
        }
    }

    public void adicionarItem() {
        Date dta_hor_vda = new Timestamp(System.currentTimeMillis());
        itensVenda.setDta_hor_vda(dta_hor_vda);
        itensVenda.setDta_lancamento(new Date()); // Definindo a data de lançamento como a data atual

        // Salva o item no banco de dados
        ejbFacade.createReturn(itensVenda);

        // Adiciona o item à lista local
        itensVendaList.add(itensVenda);

        // Reseta o objeto para permitir a adição de novos itens
        VendaEntity vendaAtual = itensVenda.getNum_cupom();
        itensVenda = new ItensVendaEntity();
        itensVenda.setDta_lancamento(new Date());
        itensVenda.setNum_cupom(vendaAtual);

        if (vendaAtual != null) {
            itensVenda.setDes_cliente_vda(vendaAtual.getDes_cliente());
            if (vendaAtual.getCod_cliente() != null) {
                itensVenda.setCod_cliente_vda(vendaAtual.getCod_cliente().getCod_cliente());
            }
            if (vendaAtual.getCod_usuario() != null) {
                itensVenda.setCod_usuario_vda(vendaAtual.getCod_usuario().getCod_usuario());
            }
        }
        // itensVenda = new ItensVendaEntity();
    }

    public ItensVendaEntity prepareAdicionar() {
        itensVenda = new ItensVendaEntity();
        itensVenda.setDta_lancamento(new Date()); // Definindo a data de lançamento como a data atual
        Integer lastNumCupom = vendaFacade.findLastNumCupom();
        if (lastNumCupom != null) {
            VendaEntity venda = vendaFacade.findByNumCupom(lastNumCupom);
            itensVenda.setNum_cupom(venda);
            onCupomSelect();  // Adicione esta linha para atualizar os campos automaticamente
        }
        return itensVenda;
    }

    public void adicionarItensVenda() {
        Date dta_hor_vda = new Timestamp(System.currentTimeMillis());
        itensVenda.setDta_hor_vda(dta_hor_vda);

        // Tratar valores nulos apenas se o item NÃO for selecionado
        if (itensVenda.getId_item() == null) {
            if (itensVenda.getQtd_venda() == null) {
                itensVenda.setQtd_venda(1); // Define quantidade padrão como 1
            }
            if (itensVenda.getVlr_venda() == null) {
                itensVenda.setVlr_venda(BigDecimal.ZERO); // Define valor da venda padrão como 0
            }
            calcularValorTotal(); // Calcula o valor total
        }

        // Calcular valor total da venda após adicionar o item
        vendaController.calcularValorTotalVenda(itensVenda.getNum_cupom().getNum_cupom());

        calcularQuantidadeTotalItens(itensVenda.getNum_cupom().getNum_cupom());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Venda concluída com sucesso!", null));

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("venda.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //     vendaController.calcularValorTotalVendaAtual();
        itensVendaList.clear();
        itensVenda = new ItensVendaEntity();

    }

    public void editarItensVenda() {
        if (selected != null) {
            if (selected.getNum_cupom() == null) {
                selected.setNum_cupom(new VendaEntity());
            }
            System.out.println("Editing item: " + selected);
            persist(PersistAction.UPDATE, "Registro alterado com sucesso!");

            // Calcular valor total da venda após editar o item
            vendaController.calcularValorTotalVenda(selected.getNum_cupom().getNum_cupom());
        } else {
            addErrorMessage("Nenhum item selecionado para edição.");
            System.out.println("Nenhum item selecionado para edição.");
        }
    }

    // Método para deletar um item de venda
    public void deletarItensVenda() {
        if (selected != null) {
            Integer numCupom = selected.getNum_cupom().getNum_cupom();
            BigDecimal valorItemExcluido = selected.getVlr_total();
            int qtdItensExcluidos = selected.getQtd_venda();

            persist(PersistAction.DELETE, "Registro excluído com sucesso!");

            // Atualiza o valor total da venda após deletar o item
            vendaController.diminuirValorTotalVenda(numCupom, valorItemExcluido);

            // Atualiza a quantidade total de itens da venda após deletar o item
            vendaController.diminuirQuantidadeTotalItens(numCupom, qtdItensExcluidos);
        } else {
            addErrorMessage("Nenhum item selecionado para exclusão.");
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
                        ejbFacade.createReturn(itensVenda);
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

    public ItensVendaEntity getItensVenda(java.lang.Integer id_item_vda) {
        return ejbFacade.find(id_item_vda);
    }

    @FacesConverter(forClass = ItensVendaEntity.class)
    public static class ItensVendaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItensVendaController controller
                    = (ItensVendaController) facesContext.getApplication().getELResolver().
                            getValue(facesContext.getELContext(),
                                    null, "itensvendaController");
            return controller.getItensVenda(getKey(value));
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
            if (object instanceof ItensVendaEntity) {
                ItensVendaEntity o = (ItensVendaEntity) object;
                return getStringKey(o.getId_item_vda());
            } else {
                return null;
            }
        }
    }

    public void calcularQuantidadeTotalItens(Integer numCupom) {
        int quantidadeTotal = 0;
        List<ItensVendaEntity> itensVendaList = ejbFacade.findByNumCupom(numCupom);

        for (ItensVendaEntity item : itensVendaList) {
            quantidadeTotal += item.getQtd_venda();
        }

        VendaEntity venda = vendaFacade.findByNumCupom(numCupom);
        venda.setQtd_itens(quantidadeTotal);
        vendaFacade.edit(venda);
    }

}
