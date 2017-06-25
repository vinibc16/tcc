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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "vulnerabilidade_admin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VulnerabilidadeAdmin.findAll", query = "SELECT v FROM VulnerabilidadeAdmin v"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByIdVulnerabilidade", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.idVulnerabilidade = :idVulnerabilidade"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByNome", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.nome = :nome"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByDescricao", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.descricao = :descricao"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByNivel", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.nivel = :nivel"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByAcoes", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.acoes = :acoes"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByFonte", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.fonte = :fonte"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByDataCriacao", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByConsequencia", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.consequencia = :consequencia"),
    @NamedQuery(name = "VulnerabilidadeAdmin.findByAmeaca", query = "SELECT v FROM VulnerabilidadeAdmin v WHERE v.ameaca = :ameaca")})
public class VulnerabilidadeAdmin implements Serializable {

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
    @Size(min = 1, max = 2000)
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
    @Size(max = 2000)
    @Column(name = "consequencia")
    private String consequencia;
    @Size(max = 2000)
    @Column(name = "ameaca")
    private String ameaca;

    public VulnerabilidadeAdmin() {
    }

    public VulnerabilidadeAdmin(Integer idVulnerabilidade) {
        this.idVulnerabilidade = idVulnerabilidade;
    }

    public VulnerabilidadeAdmin(Integer idVulnerabilidade, String nome, String descricao, int nivel, String acoes, String fonte, Date dataCriacao) {
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

    public String getConsequencia() {
        return consequencia;
    }

    public void setConsequencia(String consequencia) {
        this.consequencia = consequencia;
    }

    public String getAmeaca() {
        return ameaca;
    }

    public void setAmeaca(String ameaca) {
        this.ameaca = ameaca;
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
        if (!(object instanceof VulnerabilidadeAdmin)) {
            return false;
        }
        VulnerabilidadeAdmin other = (VulnerabilidadeAdmin) object;
        if ((this.idVulnerabilidade == null && other.idVulnerabilidade != null) || (this.idVulnerabilidade != null && !this.idVulnerabilidade.equals(other.idVulnerabilidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pucrs.br.entity.VulnerabilidadeAdmin[ idVulnerabilidade=" + idVulnerabilidade + " ]";
    }
    
}
