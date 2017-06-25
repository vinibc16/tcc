/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author psysvica
 */
@Entity
@Table(name = "escopo_vul")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EscopoVul.findAll", query = "SELECT e FROM EscopoVul e"),
    @NamedQuery(name = "EscopoVul.findByIdEscopo", query = "SELECT e FROM EscopoVul e WHERE e.escopoVulPK.idEscopo = :idEscopo"),
    @NamedQuery(name = "EscopoVul.findByIdVulnerabilidade", query = "SELECT e FROM EscopoVul e WHERE e.escopoVulPK.idVulnerabilidade = :idVulnerabilidade"),
    @NamedQuery(name = "EscopoVul.findByDataLink", query = "SELECT e FROM EscopoVul e WHERE e.dataLink = :dataLink"),
    @NamedQuery(name = "EscopoVul.findByImpacto", query = "SELECT e FROM EscopoVul e WHERE e.impacto = :impacto"),
    @NamedQuery(name = "EscopoVul.findByProbabilidade", query = "SELECT e FROM EscopoVul e WHERE e.probabilidade = :probabilidade"),
    @NamedQuery(name = "EscopoVul.findByAceito", query = "SELECT e FROM EscopoVul e WHERE e.aceito = :aceito"),
    @NamedQuery(name = "EscopoVul.findByRisco", query = "SELECT e FROM EscopoVul e WHERE e.risco = :risco"),
    @NamedQuery(name = "EscopoVul.findByAcao", query = "SELECT e FROM EscopoVul e WHERE e.acao = :acao")})
public class EscopoVul implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscopoVulPK escopoVulPK;
    @Column(name = "data_link")
    @Temporal(TemporalType.DATE)
    private Date dataLink;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "impacto")
    private Double impacto;
    @Column(name = "probabilidade")
    private Double probabilidade;
    @Column(name = "aceito")
    private Integer aceito;
    @Column(name = "risco")
    private Double risco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "acao")
    private String acao;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne
    private Empresa idEmpresa;
    @JoinColumn(name = "id_escopo", referencedColumnName = "id_escopo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Escopo escopo;
    @JoinColumn(name = "id_vulnerabilidade", referencedColumnName = "id_vulnerabilidade", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Vulnerabilidade vulnerabilidade;

    public EscopoVul() {
    }

    public EscopoVul(EscopoVulPK escopoVulPK) {
        this.escopoVulPK = escopoVulPK;
    }

    public EscopoVul(EscopoVulPK escopoVulPK, String acao) {
        this.escopoVulPK = escopoVulPK;
        this.acao = acao;
    }

    public EscopoVul(int idEscopo, int idVulnerabilidade) {
        this.escopoVulPK = new EscopoVulPK(idEscopo, idVulnerabilidade);
    }

    public EscopoVulPK getEscopoVulPK() {
        return escopoVulPK;
    }

    public void setEscopoVulPK(EscopoVulPK escopoVulPK) {
        this.escopoVulPK = escopoVulPK;
    }

    public Date getDataLink() {
        return dataLink;
    }

    public void setDataLink(Date dataLink) {
        this.dataLink = dataLink;
    }

    public Double getImpacto() {
        return impacto;
    }

    public void setImpacto(Double impacto) {
        this.impacto = impacto;
    }

    public Double getProbabilidade() {
        return probabilidade;
    }

    public void setProbabilidade(Double probabilidade) {
        this.probabilidade = probabilidade;
    }

    public Integer getAceito() {
        return aceito;
    }

    public void setAceito(Integer aceito) {
        this.aceito = aceito;
    }

    public Double getRisco() {
        return risco;
    }

    public void setRisco(Double risco) {
        this.risco = risco;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Escopo getEscopo() {
        return escopo;
    }

    public void setEscopo(Escopo escopo) {
        this.escopo = escopo;
    }

    public Vulnerabilidade getVulnerabilidade() {
        return vulnerabilidade;
    }

    public void setVulnerabilidade(Vulnerabilidade vulnerabilidade) {
        this.vulnerabilidade = vulnerabilidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escopoVulPK != null ? escopoVulPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscopoVul)) {
            return false;
        }
        EscopoVul other = (EscopoVul) object;
        if ((this.escopoVulPK == null && other.escopoVulPK != null) || (this.escopoVulPK != null && !this.escopoVulPK.equals(other.escopoVulPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pucrs.br.entity.EscopoVul[ escopoVulPK=" + escopoVulPK + " ]";
    }
    
}
