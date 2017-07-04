/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
    @NamedQuery(name = "Escopo.findByIdEscopo", query = "SELECT e FROM Escopo e WHERE e.idEscopo = :idEscopo"),
    @NamedQuery(name = "Escopo.findByNome", query = "SELECT e FROM Escopo e WHERE e.nome = :nome"),
    @NamedQuery(name = "Escopo.findByDescricao", query = "SELECT e FROM Escopo e WHERE e.descricao = :descricao"),
    @NamedQuery(name = "Escopo.findByDataCriacao", query = "SELECT e FROM Escopo e WHERE e.dataCriacao = :dataCriacao"),
    @NamedQuery(name = "Escopo.findByAtivosEnvolvidos", query = "SELECT e FROM Escopo e WHERE e.ativosEnvolvidos = :ativosEnvolvidos"),
    @NamedQuery(name = "Escopo.findByControlesExistentes", query = "SELECT e FROM Escopo e WHERE e.controlesExistentes = :controlesExistentes"),
    @NamedQuery(name = "Escopo.findByRestricoesImpostas", query = "SELECT e FROM Escopo e WHERE e.restricoesImpostas = :restricoesImpostas")})
public class Escopo implements Serializable {

    @Lob
    @Column(name = "file")
    private byte[] file;
    @Size(max = 200)
    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_escopo")
    private Integer idEscopo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nome")
    private String nome;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "ativos_envolvidos")
    private String ativosEnvolvidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "controles_existentes")
    private String controlesExistentes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "restricoes_impostas")
    private String restricoesImpostas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escopo")
    private Collection<EscopoVul> escopoVulCollection;
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;
    @JoinColumn(name = "id_responsavel", referencedColumnName = "id_responsavel")
    @ManyToOne(optional = false)
    private Responsaveis idResponsavel;

    public Escopo() {
    }

    public Escopo(Integer idEscopo) {
        this.idEscopo = idEscopo;
    }

    public Escopo(Integer idEscopo, String nome, String descricao, Date dataCriacao, String ativosEnvolvidos, String controlesExistentes, String restricoesImpostas) {
        this.idEscopo = idEscopo;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.ativosEnvolvidos = ativosEnvolvidos;
        this.controlesExistentes = controlesExistentes;
        this.restricoesImpostas = restricoesImpostas;
    }

    public Integer getIdEscopo() {
        return idEscopo;
    }

    public void setIdEscopo(Integer idEscopo) {
        this.idEscopo = idEscopo;
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

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getAtivosEnvolvidos() {
        return ativosEnvolvidos;
    }

    public void setAtivosEnvolvidos(String ativosEnvolvidos) {
        this.ativosEnvolvidos = ativosEnvolvidos;
    }

    public String getControlesExistentes() {
        return controlesExistentes;
    }

    public void setControlesExistentes(String controlesExistentes) {
        this.controlesExistentes = controlesExistentes;
    }

    public String getRestricoesImpostas() {
        return restricoesImpostas;
    }

    public void setRestricoesImpostas(String restricoesImpostas) {
        this.restricoesImpostas = restricoesImpostas;
    }

    @XmlTransient
    public Collection<EscopoVul> getEscopoVulCollection() {
        return escopoVulCollection;
    }

    public void setEscopoVulCollection(Collection<EscopoVul> escopoVulCollection) {
        this.escopoVulCollection = escopoVulCollection;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Responsaveis getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(Responsaveis idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEscopo != null ? idEscopo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Escopo)) {
            return false;
        }
        Escopo other = (Escopo) object;
        if ((this.idEscopo == null && other.idEscopo != null) || (this.idEscopo != null && !this.idEscopo.equals(other.idEscopo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pucrs.br.entity.Escopo[ idEscopo=" + idEscopo + " ]";
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    
}
