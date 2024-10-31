package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.enumeration.TipoPagamentoEnum;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "tipController")
@SessionScoped
public class TipController implements Serializable {

    @Inject
    private LoginController loginController; // Injeta o LoginController para acessar o usuário logado

    public SelectItem[] getTiposPagamento() {
        List<SelectItem> items = new ArrayList<>();

        for (TipoPagamentoEnum tipo : TipoPagamentoEnum.values()) {
            // Se o tipo for "OUTROS" e o usuário não for administrador, ignore
            if (tipo == TipoPagamentoEnum.OUTROS && !isAdmin()) {
                continue;
            }
            items.add(new SelectItem(tipo, tipo.getLabel()));
        }

        return items.toArray(new SelectItem[0]);
    }

    // Verifica se o usuário atual é administrador
    private boolean isAdmin() {
        return loginController.getUsuario() != null
                && "Administrador".equals(loginController.getUsuario().getTip_usuario());
    }
}
