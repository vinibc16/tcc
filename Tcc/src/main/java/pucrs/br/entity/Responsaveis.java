/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author psysvica
 */
@Entity
@Table(name = "responsaveis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Responsaveis.findAll", query = "SELECT r FROM Responsaveis r"),
    @NamedQuery(name = "Responsaveis.findByIdResponsavel", query = "SELECT r FROM Responsaveis r WHERE r.idResponsavel = :idResponsavel"),
    @NamedQuery(name = "Responsaveis.findByNome", query = "SELECT r FROM Responsaveis r WHERE r.nome = :nome"),
    @NamedQuery(name = "Responsaveis.findByEMail", query = "SELECT r FROM Responsaveis r WHERE r.eMail = :eMail")})
public class Responsaveis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_responsavel")
    private Integer idResponsavel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "e_mail")
    private String eMail;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idResponsavel")
    private Collection<Escopo> escopoCollection;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;

    public Responsaveis() {
    }

    public Responsaveis(Integer idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public Responsaveis(Integer idResponsavel, String nome, String eMail) {
        this.idResponsavel = idResponsavel;
        this.nome = nome;
        this.eMail = eMail;
    }

    public Integer getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(Integer idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    @XmlTransient
    public Collection<Escopo> getEscopoCollection() {
        return escopoCollection;
    }

    public void setEscopoCollection(Collection<Escopo> escopoCollection) {
        this.escopoCollection = escopoCollection;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idResponsavel != null ? idResponsavel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Responsaveis)) {
            return false;
        }
        Responsaveis other = (Responsaveis) object;
        if ((this.idResponsavel == null && other.idResponsavel != null) || (this.idResponsavel != null && !this.idResponsavel.equals(other.idResponsavel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+idResponsavel;
    }
    
}
