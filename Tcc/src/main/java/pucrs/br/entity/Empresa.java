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
@Table(name = "empresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByIdEmpresa", query = "SELECT e FROM Empresa e WHERE e.idEmpresa = :idEmpresa"),
    @NamedQuery(name = "Empresa.findByNome", query = "SELECT e FROM Empresa e WHERE e.nome = :nome"),
    @NamedQuery(name = "Empresa.findByRazaoSocial", query = "SELECT e FROM Empresa e WHERE e.razaoSocial = :razaoSocial"),
    @NamedQuery(name = "Empresa.findByFuncionarios", query = "SELECT e FROM Empresa e WHERE e.funcionarios = :funcionarios"),
    @NamedQuery(name = "Empresa.findByEndereco", query = "SELECT e FROM Empresa e WHERE e.endereco = :endereco"),
    @NamedQuery(name = "Empresa.findByMissao", query = "SELECT e FROM Empresa e WHERE e.missao = :missao"),
    @NamedQuery(name = "Empresa.findByVisao", query = "SELECT e FROM Empresa e WHERE e.visao = :visao"),
    @NamedQuery(name = "Empresa.findBySegmento", query = "SELECT e FROM Empresa e WHERE e.segmento = :segmento"),
    @NamedQuery(name = "Empresa.findByCnpj", query = "SELECT e FROM Empresa e WHERE e.cnpj = :cnpj"),
    @NamedQuery(name = "Empresa.findByTelefone", query = "SELECT e FROM Empresa e WHERE e.telefone = :telefone")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_empresa")
    private Integer idEmpresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "razao_social")
    private String razaoSocial;
    @Column(name = "funcionarios")
    private Integer funcionarios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "endereco")
    private String endereco;
    @Size(max = 2000)
    @Column(name = "missao")
    private String missao;
    @Size(max = 2000)
    @Column(name = "visao")
    private String visao;
    @Size(max = 200)
    @Column(name = "segmento")
    private String segmento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cnpj")
    private int cnpj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "telefone")
    private int telefone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa")
    private Collection<EscopoVul> escopoVulCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa")
    private Collection<Escopo> escopoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa")
    private Collection<Responsaveis> responsaveisCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa")
    private Collection<Vulnerabilidade> vulnerabilidadeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa")
    private Collection<Usuario> usuarioCollection;

    public Empresa() {
    }

    public Empresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Empresa(Integer idEmpresa, String nome, String razaoSocial, String endereco, int cnpj, int telefone) {
        this.idEmpresa = idEmpresa;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.endereco = endereco;
        this.cnpj = cnpj;
        this.telefone = telefone;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Integer getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(Integer funcionarios) {
        this.funcionarios = funcionarios;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getMissao() {
        return missao;
    }

    public void setMissao(String missao) {
        this.missao = missao;
    }

    public String getVisao() {
        return visao;
    }

    public void setVisao(String visao) {
        this.visao = visao;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public int getCnpj() {
        return cnpj;
    }

    public void setCnpj(int cnpj) {
        this.cnpj = cnpj;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    @XmlTransient
    public Collection<EscopoVul> getEscopoVulCollection() {
        return escopoVulCollection;
    }

    public void setEscopoVulCollection(Collection<EscopoVul> escopoVulCollection) {
        this.escopoVulCollection = escopoVulCollection;
    }

    @XmlTransient
    public Collection<Escopo> getEscopoCollection() {
        return escopoCollection;
    }

    public void setEscopoCollection(Collection<Escopo> escopoCollection) {
        this.escopoCollection = escopoCollection;
    }

    @XmlTransient
    public Collection<Responsaveis> getResponsaveisCollection() {
        return responsaveisCollection;
    }

    public void setResponsaveisCollection(Collection<Responsaveis> responsaveisCollection) {
        this.responsaveisCollection = responsaveisCollection;
    }

    @XmlTransient
    public Collection<Vulnerabilidade> getVulnerabilidadeCollection() {
        return vulnerabilidadeCollection;
    }

    public void setVulnerabilidadeCollection(Collection<Vulnerabilidade> vulnerabilidadeCollection) {
        this.vulnerabilidadeCollection = vulnerabilidadeCollection;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresa != null ? idEmpresa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.idEmpresa == null && other.idEmpresa != null) || (this.idEmpresa != null && !this.idEmpresa.equals(other.idEmpresa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+idEmpresa;
    }
    
}
