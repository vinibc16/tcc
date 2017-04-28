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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "vulnerabilidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vulnerabilidade.findAll", query = "SELECT v FROM Vulnerabilidade v"),
    @NamedQuery(name = "Vulnerabilidade.findByIdVulnerabilidade", query = "SELECT v FROM Vulnerabilidade v WHERE v.idVulnerabilidade = :idVulnerabilidade"),
    @NamedQuery(name = "Vulnerabilidade.findByNome", query = "SELECT v FROM Vulnerabilidade v WHERE v.nome = :nome"),
    @NamedQuery(name = "Vulnerabilidade.findByDescricao", query = "SELECT v FROM Vulnerabilidade v WHERE v.descricao = :descricao"),
    @NamedQuery(name = "Vulnerabilidade.findByNivel", query = "SELECT v FROM Vulnerabilidade v WHERE v.nivel = :nivel"),
    @NamedQuery(name = "Vulnerabilidade.findByAcoes", query = "SELECT v FROM Vulnerabilidade v WHERE v.acoes = :acoes"),
    @NamedQuery(name = "Vulnerabilidade.findByFonte", query = "SELECT v FROM Vulnerabilidade v WHERE v.fonte = :fonte"),
    @NamedQuery(name = "Vulnerabilidade.findByDataCriacao", query = "SELECT v FROM Vulnerabilidade v WHERE v.dataCriacao = :dataCriacao")})
public class Vulnerabilidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_vulnerabilidade")
    private Integer idVulnerabilidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nivel")
    private int nivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "acoes")
    private String acoes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "fonte")
    private String fonte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_criacao")
    @Temporal(TemporalType.DATE)
    private Date dataCriacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vulnerabilidade")
    private List<EscopoVul> escopoVulList;

    public Vulnerabilidade() {
    }

    public Vulnerabilidade(Integer idVulnerabilidade) {
        this.idVulnerabilidade = idVulnerabilidade;
    }

    public Vulnerabilidade(Integer idVulnerabilidade, String nome, String descricao, int nivel, String acoes, String fonte, Date dataCriacao) {
        this.idVulnerabilidade = idVulnerabilidade;
        this.nome = nome;
        this.descricao = descricao;
        this.nivel = nivel;
        this.acoes = acoes;
        this.fonte = fonte;
        this.dataCriacao = dataCriacao;
    }

    public Integer getIdVulnerabilidade() {
        return idVulnerabilidade;
    }

    public void setIdVulnerabilidade(Integer idVulnerabilidade) {
        this.idVulnerabilidade = idVulnerabilidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getAcoes() {
        return acoes;
    }

    public void setAcoes(String acoes) {
        this.acoes = acoes;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVulnerabilidade != null ? idVulnerabilidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vulnerabilidade)) {
            return false;
        }
        Vulnerabilidade other = (Vulnerabilidade) object;
        if ((this.idVulnerabilidade == null && other.idVulnerabilidade != null) || (this.idVulnerabilidade != null && !this.idVulnerabilidade.equals(other.idVulnerabilidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pucrs.br.entity.Vulnerabilidade[ idVulnerabilidade=" + idVulnerabilidade + " ]";
    }
    
}
