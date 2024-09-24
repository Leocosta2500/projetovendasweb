
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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
@Table(name = "pagamento")
public class PagamentoEntity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "num_pag")
    private Integer num_pag;
    
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dta_hor_pag")
    private Date dta_hor_pag;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tipo_pag")
    private String tipo_pag;
    
    
    @Basic(optional = false)
    @DecimalMin("0.00")
    @DecimalMax("99999.99")
    @Column(name = "saldo_devedor", precision = 7, scale = 2)
    private BigDecimal saldo_devedor;
    
    
    @Basic(optional = false)
    @DecimalMin("0.00")
    @DecimalMax("99999.99")
    @Column(name = "vlr_pago", precision = 7, scale = 2)
    private BigDecimal vlr_pago;

    @Basic(optional = false)
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("99999.99")
    @Column(name = "vlr_total_pag", precision = 7, scale = 2)
    private BigDecimal vlr_total_pag;


    @Basic(optional = false)
    @NotNull
    @Column(name = "cod_usuario_pag")
    private Integer cod_usuario_pag;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "status_pag")
    private String status_pag;

    
    @JoinColumn(name = "num_cupom", referencedColumnName = "num_cupom")
    @ManyToOne(optional = false)
    private VendaEntity num_cupom;
    
    
        public Integer getNum_pag() {
        return num_pag;
    }

    public void setNum_pag(Integer num_pag) {
        this.num_pag = num_pag;
    }

    public Date getDta_hor_pag() {
        return dta_hor_pag;
    }

    public void setDta_hor_pag(Date dta_hor_pag) {
        this.dta_hor_pag = dta_hor_pag;
    }

    public String getTipo_pag() {
        return tipo_pag;
    }

    public void setTipo_pag(String tipo_pag) {
        this.tipo_pag = tipo_pag;
    }

    public BigDecimal getVlr_total_pag() {
        return vlr_total_pag;
    }

    public void setVlr_total_pag(BigDecimal vlr_total_pag) {
        this.vlr_total_pag = vlr_total_pag;
    }

    public Integer getCod_usuario_pag() {
        return cod_usuario_pag;
    }

    public void setCod_usuario_pag(Integer cod_usuario_pag) {
        this.cod_usuario_pag = cod_usuario_pag;
    }

    public String getStatus_pag() {
        return status_pag;
    }

    public void setStatus_pag(String status_pag) {
        this.status_pag = status_pag;
    }

    public VendaEntity getNum_cupom() {
        return num_cupom;
    }

    public void setNum_cupom(VendaEntity num_cupom) {
        this.num_cupom = num_cupom;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.num_pag);
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
        final PagamentoEntity other = (PagamentoEntity) obj;
        return Objects.equals(this.num_pag, other.num_pag);
    }

    @Override
    public String toString() {
        return status_pag;
    }

    public BigDecimal getSaldo_devedor() {
        return saldo_devedor;
    }

    public void setSaldo_devedor(BigDecimal saldo_devedor) {
        this.saldo_devedor = saldo_devedor;
    }

    public BigDecimal getVlr_pago() {
        return vlr_pago;
    }

    public void setVlr_pago(BigDecimal vlr_pago) {
        this.vlr_pago = vlr_pago;
    }


    
    
        
  
}
