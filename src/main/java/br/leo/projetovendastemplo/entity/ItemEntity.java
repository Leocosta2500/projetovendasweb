package br.leo.projetovendastemplo.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "item")
public class ItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_item")
    private Integer id_item;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "desc_item")
    private String desc_item;

    @Basic(optional = false)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 7, fraction = 2)
    @Column(name = "vlr_item")
    private BigDecimal vlr_item;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "id_status")
    private String id_status;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_item")
    private Integer cod_item;

    @Size(max = 50)
    @Column(name = "cod_barras")
    private String cod_barras;
    
    

    public Integer getId_item() {
        return id_item;
    }

    public void setId_item(Integer id_item) {
        this.id_item = id_item;
    }

    public String getDesc_item() {
        return desc_item;
    }

    public void setDesc_item(String desc_item) {
        this.desc_item = desc_item;
    }

    public BigDecimal getVlr_item() {
        return vlr_item;
    }

    public void setVlr_item(BigDecimal vlr_item) {
        this.vlr_item = vlr_item;
    }

    public String getId_status() {
        return id_status;
    }

    public void setId_status(String id_status) {
        this.id_status = id_status;
    }

    public Integer getCod_item() {
        return cod_item;
    }

    public void setCod_item(Integer cod_item) {
        this.cod_item = cod_item;
    }

    public String getCod_barras() {
        return cod_barras;
    }

    public void setCod_barras(String cod_barras) {
        this.cod_barras = cod_barras;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id_item);
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
        final ItemEntity other = (ItemEntity) obj;
        return Objects.equals(this.id_item, other.id_item);
    }

    @Override
    public String toString() {
        return desc_item;
    }
    
    
    
    

}
