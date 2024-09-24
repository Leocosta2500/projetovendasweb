package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.ItemEntity;
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
@Named(value = "itemController")
@SessionScoped
public class ItemController implements Serializable {

    @EJB
    private br.leo.projetovendastemplo.facade.ItemFacede ejbFacade;

    private ItemEntity item = new ItemEntity();
    private List<ItemEntity> itemList = new ArrayList<>();
    private ItemEntity selected;

    public List<ItemEntity> itemAll() {
        return ejbFacade.buscarTodos();
    }

    public List<ItemEntity> geItemList() {
        return itemList;
    }

    public void setItemList(List<ItemEntity> itemList) {
        this.itemList = itemList;
    }

    public ItemEntity getSelected() {
        return selected;
    }

    public void setSelected(ItemEntity selected) {
        this.selected = selected;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    //   public List<ItemEntity> getItemList() {
//        return itemList;
//    }
    public ItemEntity getItem(java.lang.Integer id_item) {
        return ejbFacade.find(id_item);
    }

    @FacesConverter(forClass = ItemEntity.class)
    public static class ItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemController controller
                    = (ItemController) facesContext.getApplication().getELResolver().
                            getValue(facesContext.getELContext(),
                                    null, "itemController");
            return controller.getItem(getKey(value));
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
            if (object instanceof ItemEntity) {
                ItemEntity o = (ItemEntity) object;
                return getStringKey(o.getId_item());
            } else {
                return null;
            }
        }
    }

    public ItemEntity prepareAdicionar() {
        item = new ItemEntity();
        return item;
    }

    public void adicionarItem() {
        //buscando a datahoraatual do sistema.
        persist(ItemController.PersistAction.CREATE,
                "Registro incluído com sucesso!");
    }

    public void editarItem() {
        persist(ItemController.PersistAction.UPDATE,
                "Registro alterado com sucesso!");
    }

    public void deletarItem() {
        persist(ItemController.PersistAction.DELETE,
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
                        ejbFacade.createReturn(item);
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
