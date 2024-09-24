/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package br.leo.projetovendastemplo.enumeration;

/**
 *
 * @author leona
 */
public enum UFEnum {

    RS("RS"),
    SC("SC"),
    PR("PR");
    
    private final String value;
    
    private UFEnum (String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
}
