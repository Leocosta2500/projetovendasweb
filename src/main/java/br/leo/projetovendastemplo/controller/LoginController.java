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

    private String novaSenha;
    private String confirmarSenha;

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

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

    public String irParaAlterarSenha() {
        return "/admin/alterarSenha.xhtml?faces-redirect=true";
    }

    public String alterarSenhaLogin() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        UsuarioEntity usuarioLogado = (UsuarioEntity) session.getAttribute("pessoaLogada");

        if (usuarioLogado == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não autenticado.", null));
            return null;
        }

        // Verifica se as senhas coincidem
        if (novaSenha == null || confirmarSenha == null || !novaSenha.equals(confirmarSenha)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "As senhas não coincidem!", null));
            return null;
        }

        // Verifica se a nova senha é igual à senha atual
        if (novaSenha.equals(usuarioLogado.getDes_senha())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A nova senha não pode ser igual à senha atual.", null));
            return null;
        }

        try {
            // Atualiza a senha do usuário logado
            usuarioLogado.setDes_senha(novaSenha);
            ejbFacade.edit(usuarioLogado);

            // Usa Flash Scope para manter a mensagem na próxima página
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha alterada com sucesso!", null));

            // Limpa os campos de senha
            novaSenha = null;
            confirmarSenha = null;

            // Retorna para a página inicial após sucesso
            return "/admin/inicio.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao alterar a senha: " + e.getMessage(), null));
            return null;
        }
    }

}
