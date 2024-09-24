
package br.leo.projetovendastemplo.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author leona
 */
@Entity
@Table(name = "usuario")
public class UsuarioEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cod_usuario")
    private Integer cod_usuario;
    

    @Basic(optional = false) //atributo não é opcional
    @NotNull //definido como obrigatório
    @Size(min = 1, max = 50) //quantidade min e max de caracteres
    @Column(name = "nome_user")
    private String nome_user;
    

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email_user")
    private String email_user;
    

    @Basic(optional = false) //atributo não é opcional
    @NotNull //definido como obrigatório
    @Size(min = 1, max = 20) //quantidade min e max de caracteres
    @Column(name = "tip_usuario")
    private String tip_usuario;
        
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "des_senha")
    private String des_senha;
        
        
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datahorareg")
    private Date datahorareg;
    
 //   @ManyToOne(optional = false)
 //   @JoinColumn(name = "fk_Usuario_cod_usuario", referencedColumnName = "cod_usuario")
 //   private VendaEntity codusuario;
    


    public int getCod_usuario() {
        return cod_usuario;
    }

    public void setCod_usuario(int cod_usuario) {
        this.cod_usuario = cod_usuario;
    }

    public String getNome_user() {
        return nome_user;
    }

    public void setNome_user(String nome_user) {
        this.nome_user = nome_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getTip_usuario() {
        return tip_usuario;
    }

    public void setTip_usuario(String tip_usuario) {
        this.tip_usuario = tip_usuario;
    }

    public Date getDatahorareg() {
        return datahorareg;
    }

    public void setDatahorareg(Date datahorareg) {
        this.datahorareg = datahorareg;
    }

    public String getDes_senha() {
        return des_senha;
    }

    public void setDes_senha(String des_senha) {
        this.des_senha = des_senha;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.cod_usuario);
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
        final UsuarioEntity other = (UsuarioEntity) obj;
        return Objects.equals(this.cod_usuario, other.cod_usuario);
    }

    @Override
    public String toString() {
        return  nome_user;
    }
    
    

   
}
