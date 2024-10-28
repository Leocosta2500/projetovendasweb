package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.entity.UsuarioEntity;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author leona
 */
@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private br.leo.projetovendastemplo.facade.UsuarioFacade ejbFacade;

    public LoginController() {
    }

    //objeto que representa uma pessoa
    private UsuarioEntity usuario;

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public void prepareAutenticarUsuario() {
        usuario = new UsuarioEntity();
    }

    /**
     * Método utilizado para inicializar métodos ao instanciar a classe...
     */
    @PostConstruct
    public void init() {
        prepareAutenticarUsuario();
    }

    public String validarLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        // Busca o usuário no banco de dados
        UsuarioEntity usuarioDB = ejbFacade.buscarPorEmail(usuario.getEmail_user(), usuario.getDes_senha());
        if (usuarioDB != null && usuarioDB.getNome_user() != null) {
            // Define o usuário autenticado no atributo 'usuario' do controller
            this.usuario = usuarioDB;

            // Armazena o usuário na sessão para uso posterior
            session.setAttribute("pessoaLogada", usuarioDB);

            // Redireciona para a página inicial após login bem-sucedido
            return "/admin/inicio.xhtml?faces-redirect=true";
        } else {
            // Exibe mensagem de erro caso o login falhe
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha no Login!", "Email ou senha incorreto!");
            context.addMessage(null, fm);
            return null;
        }
    }

    public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();

        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        session.invalidate();

        return "/login.xhtml?faces-redirect=true";

    }

    public String getNomeUsuarioLogado() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("pessoaLogada");
        if (usuarioLogado != null) {
            return usuarioLogado.getNome_user();
        }
        return null;
    }

    public void verificarAcesso(String tipoRequerido) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("pessoaLogada");

        if (usuarioLogado == null || !usuarioLogado.getTip_usuario().equals(tipoRequerido)) {
            // Exibe uma mensagem de acesso negado na própria página
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Acesso Negado", "Você não tem permissão para acessar esta página."));
            try {
                // Redireciona o usuário para a página de login ou uma página de acesso restrito
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/inicio.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
