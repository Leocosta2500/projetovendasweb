
package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.UsuarioEntity;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author leona
 */
@Named(value = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    
   @EJB
    private br.leo.projetovendastemplo.facade.UsuarioFacade ejbFacade;
   
    /**
     * Creates a new instance of UsuarioController
     */
    public UsuarioController() {
    }
    
   
    private UsuarioEntity usuario = new UsuarioEntity();
    private List<UsuarioEntity> usuarioList = new ArrayList<>(); 
    private UsuarioEntity selected;

    
    
    public List<UsuarioEntity> usuarioAll() {
        return ejbFacade.buscarTodos();
    }

    public List<UsuarioEntity> getUsuarioList() {
        return usuarioList;
    }
    
    
//    public List<UsuarioEntity> getUsuarioList() {
//        return ejbFacade.buscarTodos();
//    }

    public void setUsuarioList(List<UsuarioEntity> usuarioList) {
        this.usuarioList = usuarioList;
    }
    
    
    public UsuarioEntity getSelected() {
        return selected;
    }

    public void setSelected(UsuarioEntity selected) {
        this.selected = selected;
    }


    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }
    

    public UsuarioEntity getUsuario(java.lang.Integer cod_usuario) {
        return ejbFacade.find(cod_usuario);
    }
    
      @FacesConverter(forClass = UsuarioEntity.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller
                    = (UsuarioController) facesContext.getApplication().getELResolver().
                            getValue(facesContext.getELContext(),
                                    null, "usuarioController");
            return controller.getUsuario(getKey(value));
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
            if (object instanceof UsuarioEntity) {
                UsuarioEntity o = (UsuarioEntity) object;
                return getStringKey(o.getCod_usuario());
            } else {
                return null;
            }
        }
    }
   
    
    
    public UsuarioEntity prepareAdicionar() {
        usuario = new UsuarioEntity();
        return usuario;
    }

    
        public void adicionarUsuario() {
        //buscando a datahoraatual do sistema.
        Date datahoraAtual = new Timestamp(System.currentTimeMillis());
        usuario.setDatahorareg(datahoraAtual);
        persist(UsuarioController.PersistAction.CREATE, 
                "Registro incluído com sucesso!");
    }

    
        public void editarUsuario() {
        persist(UsuarioController.PersistAction.UPDATE, 
                "Registro alterado com sucesso!");
    }

    
    
    public void deletarUsuario() {
        persist(UsuarioController.PersistAction.DELETE, 
                "Registro excluído com sucesso!");
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
                        ejbFacade.createReturn(usuario);
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
