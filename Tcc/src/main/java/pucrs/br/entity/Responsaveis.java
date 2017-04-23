/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

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
    @NamedQuery(name = "Responsaveis.findByNome", query = "SELECT r FROM Responsaveis r WHERE r.nome = :nome")})
public class Responsaveis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_responsavel")
    private Integer idResponsavel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nome")
    private String nome;

    public Responsaveis() {
    }

    public Responsaveis(Integer idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public Responsaveis(Integer idResponsavel, String nome) {
        this.idResponsavel = idResponsavel;
        this.nome = nome;
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
        return "pucrs.br.entity.Responsaveis[ idResponsavel=" + idResponsavel + " ]";
    }
    
}
