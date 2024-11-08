package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.ClienteEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leona
 */
@Named(value = "clienteController")
@SessionScoped
public class ClienteController implements Serializable {

    @EJB
    private br.leo.projetovendastemplo.facade.ClienteFacede ejbFacade;

    private ClienteEntity cliente = new ClienteEntity();
    private List<ClienteEntity> clienteList = new ArrayList<>();
    private ClienteEntity selected;

    public List<ClienteEntity> clienteAll() {
        return ejbFacade.buscarTodos();
    }

    public List<ClienteEntity> getClienteList() {
        return clienteList;
    }

//    public List<ClienteEntity> getClienteList() {
//        return ejbFacade.buscarTodos();
//       }
    public void setClienteList(List<ClienteEntity> clienteList) {
        this.clienteList = clienteList;
    }

    public ClienteEntity getSelected() {
        return selected;
    }

    public void setSelected(ClienteEntity selected) {
        this.selected = selected;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;

    }

    public ClienteEntity getCliente(java.lang.Integer cod_cliente) {
        return ejbFacade.find(cod_cliente);
    }

    @FacesConverter(forClass = ClienteEntity.class)
    public static class ClienteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClienteController controller
                    = (ClienteController) facesContext.getApplication().getELResolver().
                            getValue(facesContext.getELContext(),
                                    null, "clienteController");
            return controller.getCliente(getKey(value));
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
            if (object instanceof ClienteEntity) {
                ClienteEntity o = (ClienteEntity) object;
                return getStringKey(o.getCod_cliente());
            } else {
                return null;
            }
        }
    }

    public ClienteEntity prepareAdicionar() {
        cliente = new ClienteEntity();
        return cliente;
    }

    public void adicionarCliente() {
        //buscando a datahoraatual do sistema.
        persist(ClienteController.PersistAction.CREATE,
                "Registro incluído com sucesso!");
    }

    public void editarCliente() {
        persist(ClienteController.PersistAction.UPDATE,
                "Registro alterado com sucesso!");
    }

    public void deletarCliente() {
        try {
            if (selected == null) {
                addErrorMessage("Nenhum item selecionado para exclusão.");
                return;
            }

            // Lógica de exclusão
            ejbFacade.remove(selected);
            selected = null; // Limpar a seleção após a exclusão
            addSuccessMessage("Registro excluído com sucesso!");

        } catch (EJBException ex) {
            // Aqui você pode interceptar o erro específico e exibir a mensagem personalizada
            addErrorMessage("Não é permitido excluir o cliente..");
        } catch (Exception ex) {
            addErrorMessage("Nenhum cliente selecionado para exclusão. " + ex.getMessage());
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
                        ejbFacade.createReturn(cliente);
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
