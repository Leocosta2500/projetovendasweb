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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "itensvenda")
public class ItensVendaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_item_vda")
    private Integer id_item_vda;

    @Basic(optional = false)
    @NotNull
    @Column(name = "qtd_venda")
    private Integer qtd_venda;

    @Basic(optional = false)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 7, fraction = 2)
    @Column(name = "vlr_venda")
    private BigDecimal vlr_venda;

    @Basic(optional = false)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 7, fraction = 2)
    @Column(name = "vlr_total")
    private BigDecimal vlr_total;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_usuario_vda")
    private Integer cod_usuario_vda;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dta_hor_vda")
    private Date dta_hor_vda;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "des_cliente_vda")
    private String des_cliente_vda;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "dta_lancamento")
    private Date dta_lancamento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_cliente_vda")
    private Integer cod_cliente_vda;

    @JoinColumn(name = "num_cupom", referencedColumnName = "num_cupom")
    @ManyToOne(optional = false)
    private VendaEntity num_cupom;

    @JoinColumn(name = "id_item", referencedColumnName = "id_item")
    @ManyToOne(optional = false)
    private ItemEntity id_item;

    public Integer getId_item_vda() {
        return id_item_vda;
    }

    public void setId_item_vda(Integer id_item_vda) {
        this.id_item_vda = id_item_vda;
    }

    public Integer getQtd_venda() {
        return qtd_venda;
    }

    public void setQtd_venda(Integer qtd_venda) {
        this.qtd_venda = qtd_venda;
    }

    public BigDecimal getVlr_venda() {
        return vlr_venda;
    }

    public void setVlr_venda(BigDecimal vlr_venda) {
        this.vlr_venda = vlr_venda;
    }

    public BigDecimal getVlr_total() {
        return vlr_total;
    }

    public void setVlr_total(BigDecimal vlr_total) {
        this.vlr_total = vlr_total;
    }

    public Integer getCod_usuario_vda() {
        return cod_usuario_vda;
    }

    public void setCod_usuario_vda(Integer cod_usuario_vda) {
        this.cod_usuario_vda = cod_usuario_vda;
    }

    public Date getDta_hor_vda() {
        return dta_hor_vda;
    }

    public void setDta_hor_vda(Date dta_hor_vda) {
        this.dta_hor_vda = dta_hor_vda;
    }

    public String getDes_cliente_vda() {
        return des_cliente_vda;
    }

    public void setDes_cliente_vda(String des_cliente_vda) {
        this.des_cliente_vda = des_cliente_vda;
    }

    public Date getDta_lancamento() {
        return dta_lancamento;
    }

    public void setDta_lancamento(Date dta_lancamento) {
        this.dta_lancamento = dta_lancamento;
    }

    public Integer getCod_cliente_vda() {
        return cod_cliente_vda;
    }

    public void setCod_cliente_vda(Integer cod_cliente_vda) {
        this.cod_cliente_vda = cod_cliente_vda;
    }

    public VendaEntity getNum_cupom() {
        return num_cupom;
    }

    public void setNum_cupom(VendaEntity num_cupom) {
        this.num_cupom = num_cupom;
    }

    public ItemEntity getId_item() {
        return id_item;
    }

    public void setId_item(ItemEntity id_item) {
        this.id_item = id_item;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id_item_vda);
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
        final ItensVendaEntity other = (ItensVendaEntity) obj;
        return Objects.equals(this.id_item_vda, other.id_item_vda);
    }

    @Override
    public String toString() {
        return des_cliente_vda;
    }

}
