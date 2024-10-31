package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.enumeration.TipoPagamentoEnum;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import java.io.Serializable;

@Named(value = "tipController")
@SessionScoped
public class TipController implements Serializable {

    public SelectItem[] getTiposPagamento() {
        SelectItem[] items = new SelectItem[TipoPagamentoEnum.values().length];
        int i = 0;
        for (TipoPagamentoEnum tipo : TipoPagamentoEnum.values()) {
            items[i++] = new SelectItem(tipo, tipo.getLabel());
        }
        return items;
    }
}
