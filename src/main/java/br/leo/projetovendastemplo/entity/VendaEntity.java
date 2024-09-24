package br.leo.projetovendastemplo.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "venda")

public class VendaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "num_cupom")
    private Integer num_cupom;

    @Basic(optional = false)
    @Column(name = "vlr_total_vda", precision = 7, scale = 2)
    private BigDecimal vlr_total_vda;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dta_hor_mov")
    private Date dta_hor_mov;

    @Basic(optional = false)
    @NotNull
    @Column(name = "tip_venda")
    private Integer tip_venda;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_operacao")
    private Integer cod_operacao;

    @Basic(optional = false)
    @Column(name = "qtd_itens")
    private Integer qtd_itens;

    @Size(min = 1, max = 50)
    @Column(name = "des_uf_cli")
    private String des_uf_cli;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "des_cliente")
    private String des_cliente;

    @JoinColumn(name = "cod_usuario", referencedColumnName = "cod_usuario")
    @ManyToOne(optional = false)
    private UsuarioEntity cod_usuario;

    @JoinColumn(name = "cod_cliente", referencedColumnName = "cod_cliente")
    @ManyToOne(optional = false)
    private ClienteEntity cod_cliente;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.num_cupom);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VendaEntity other = (VendaEntity) obj;
        return Objects.equals(this.num_cupom, other.num_cupom);
    }

    public Integer getNum_cupom() {
        return num_cupom;
    }

    public void setNum_cupom(Integer num_cupom) {
        this.num_cupom = num_cupom;
    }

    public BigDecimal getVlr_total_vda() {
        return vlr_total_vda;
    }

    public void setVlr_total_vda(BigDecimal vlr_total_vda) {
        this.vlr_total_vda = vlr_total_vda;
    }

    public Date getDta_hor_mov() {
        return dta_hor_mov;
    }

    public void setDta_hor_mov(Date dta_hor_mov) {
        this.dta_hor_mov = dta_hor_mov;
    }

    public Integer getTip_venda() {
        return tip_venda;
    }

    public void setTip_venda(Integer tip_venda) {
        this.tip_venda = tip_venda;
    }

    public Integer getCod_operacao() {
        return cod_operacao;
    }

    public void setCod_operacao(Integer cod_operacao) {
        this.cod_operacao = cod_operacao;
    }

    public Integer getQtd_itens() {
        return qtd_itens;
    }

    public void setQtd_itens(Integer qtd_itens) {
        this.qtd_itens = qtd_itens;
    }

    public String getDes_uf_cli() {
        return des_uf_cli;
    }

    public void setDes_uf_cli(String des_uf_cli) {
        this.des_uf_cli = des_uf_cli;
    }

    public String getDes_cliente() {
        return des_cliente;
    }

    public void setDes_cliente(String des_cliente) {
        this.des_cliente = des_cliente;
    }

    public UsuarioEntity getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(UsuarioEntity cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public ClienteEntity getCod_cliente() {
        return cod_cliente;
    }

    public void setCod_cliente(ClienteEntity cod_cliente) {
        this.cod_cliente = cod_cliente;
    }

    @Override
    public String toString() {
        return des_cliente;
    }

}
