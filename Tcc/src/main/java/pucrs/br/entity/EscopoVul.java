/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

import java.io.Serializable;
import java.util.Date;
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
    @NamedQuery(name = "EscopoVul.findByIdEmpresa", query = "SELECT e FROM EscopoVul e WHERE e.escopoVulPK.idEmpresa = :idEmpresa"),
    @NamedQuery(name = "EscopoVul.findByIdEscopo", query = "SELECT e FROM EscopoVul e WHERE e.escopoVulPK.idEscopo = :idEscopo"),
    @NamedQuery(name = "EscopoVul.findByIdVulnerabilidade", query = "SELECT e FROM EscopoVul e WHERE e.escopoVulPK.idVulnerabilidade = :idVulnerabilidade"),
    @NamedQuery(name = "EscopoVul.findByDataLink", query = "SELECT e FROM EscopoVul e WHERE e.dataLink = :dataLink"),
    @NamedQuery(name = "EscopoVul.findByImpacto", query = "SELECT e FROM EscopoVul e WHERE e.impacto = :impacto"),
    @NamedQuery(name = "EscopoVul.findByProbabilidade", query = "SELECT e FROM EscopoVul e WHERE e.probabilidade = :probabilidade"),
    @NamedQuery(name = "EscopoVul.findByAceito", query = "SELECT e FROM EscopoVul e WHERE e.aceito = :aceito"),
    @NamedQuery(name = "EscopoVul.findByRisco", query = "SELECT e FROM EscopoVul e WHERE e.risco = :risco")})
public class EscopoVul implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EscopoVulPK escopoVulPK;
    @Column(name = "data_link")
    @Temporal(TemporalType.DATE)
    private Date dataLink;
    @Column(name = "impacto")
    private double impacto;
    @Column(name = "probabilidade")
    private double probabilidade;
    @Column(name = "aceito")
    private Integer aceito;
    @Column(name = "risco")
    private double risco;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;
    @JoinColumn(name = "id_vulnerabilidade", referencedColumnName = "id_vulnerabilidade", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Vulnerabilidade vulnerabilidade;

    public EscopoVul() {
    }

    public EscopoVul(EscopoVulPK escopoVulPK) {
        this.escopoVulPK = escopoVulPK;
    }

    public EscopoVul(int idEmpresa, int idEscopo, int idVulnerabilidade) {
        this.escopoVulPK = new EscopoVulPK(idEmpresa, idEscopo, idVulnerabilidade);
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

    public double getImpacto() {
        return impacto;
    }

    public void setImpacto(double impacto) {
        this.impacto = impacto;
    }

    public double getProbabilidade() {
        return probabilidade;
    }

    public void setProbabilidade(double probabilidade) {
        this.probabilidade = probabilidade;
    }

    public Integer getAceito() {
        return aceito;
    }

    public void setAceito(Integer aceito) {
        this.aceito = aceito;
    }

    public double getRisco() {
        return risco;
    }

    public void setRisco(double risco) {
        this.risco = risco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
