package br.leo.projetovendastemplo.enumeration;

public enum TipoPagamentoEnum {
    PIX("Pix"),
    CARTAO_DEBITO("Cartão Débito"),
    CARTAO_CREDITO("Cartão de Crédito");

    private final String label;

    TipoPagamentoEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
