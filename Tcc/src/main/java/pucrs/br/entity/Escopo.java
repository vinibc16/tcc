/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author psysvica
 */
@Entity
@Table(name = "escopo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Escopo.findAll", query = "SELECT e FROM Escopo e"),
    @NamedQuery(name = "Escopo.findByIdEmpresa", query = "SELECT e FROM Escopo e WHERE e.escopoPK.idEmpresa = :idEmpresa"),
    @NamedQuery(name = "Escopo.findByIdEscopo", query = "SELECT e FROM Escopo e WHERE e.escopoPK.idEscopo = :idEscopo"),
    @NamedQuery(name = "Escopo.findByNome", query = "SELECT e FROM Escopo e WHERE e.nome = :nome"),
    @NamedQuery(name = "Escopo.findByIdResponsavel", query = "SELECT e FROM Escopo e WHERE e.idResponsavel = :idResponsavel"),
    @NamedQuery(name = "Escopo.findByDescricao", query = "SELECT e FROM Escopo e WHERE e.descricao = :descricao"),
    @NamedQuery(name = "Escopo.findByDataCriacao", query = "SELECT e FROM Escopo e WHERE e.dataCriacao = :dataCriacao")})
public class Escopo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscopoPK escopoPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_responsavel")
    private int idResponsavel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_criacao")
    @Temporal(TemporalType.DATE)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escopo")
    private List<EscopoVul> escopoVulList;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;

    public Escopo() {
    }

    public Escopo(EscopoPK escopoPK) {
        this.escopoPK = escopoPK;
    }

    public Escopo(EscopoPK escopoPK, String nome, int idResponsavel, String descricao, Date dataCriacao) {
        this.escopoPK = escopoPK;
        this.nome = nome;
        this.idResponsavel = idResponsavel;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
    }

    public Escopo(int idEmpresa, int idEscopo) {
        this.escopoPK = new EscopoPK(idEmpresa, idEscopo);
    }

    public EscopoPK getEscopoPK() {
        return escopoPK;
    }

    public void setEscopoPK(EscopoPK escopoPK) {
        this.escopoPK = escopoPK;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(int idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @XmlTransient
    public List<EscopoVul> getEscopoVulList() {
        return escopoVulList;
    }

    public void setEscopoVulList(List<EscopoVul> escopoVulList) {
        this.escopoVulList = escopoVulList;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escopoPK != null ? escopoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Escopo)) {
            return false;
        }
        Escopo other = (Escopo) object;
        if ((this.escopoPK == null && other.escopoPK != null) || (this.escopoPK != null && !this.escopoPK.equals(other.escopoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pucrs.br.entity.Escopo[ escopoPK=" + escopoPK + " ]";
    }
    
}
