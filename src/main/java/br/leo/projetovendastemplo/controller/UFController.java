/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.leo.projetovendastemplo.controller;

import br.leo.projetovendastemplo.enumeration.UFEnum;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author leona
 */

@Named(value = "ufController")
@SessionScoped

public class UFController implements Serializable{
    
     public SelectItem[] getUFs() {

        SelectItem[] items = new SelectItem[UFEnum.values().length];
        int i = 0;
        for (UFEnum t : UFEnum.values()) {
            items[i++] = new SelectItem(t, t.getValue());
        }
        return items;
    }
    
}
