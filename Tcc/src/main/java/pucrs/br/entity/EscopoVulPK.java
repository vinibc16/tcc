/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author psysvica
 */
@Embeddable
public class EscopoVulPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_empresa")
    private int idEmpresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_escopo")
    private int idEscopo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_vulnerabilidade")
    private int idVulnerabilidade;

    public EscopoVulPK() {
    }

    public EscopoVulPK(int idEmpresa, int idEscopo, int idVulnerabilidade) {
        this.idEmpresa = idEmpresa;
        this.idEscopo = idEscopo;
        this.idVulnerabilidade = idVulnerabilidade;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdEscopo() {
        return idEscopo;
    }

    public void setIdEscopo(int idEscopo) {
        this.idEscopo = idEscopo;
    }

    public int getIdVulnerabilidade() {
        return idVulnerabilidade;
    }

    public void setIdVulnerabilidade(int idVulnerabilidade) {
        this.idVulnerabilidade = idVulnerabilidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEmpresa;
        hash += (int) idEscopo;
        hash += (int) idVulnerabilidade;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscopoVulPK)) {
            return false;
        }
        EscopoVulPK other = (EscopoVulPK) object;
        if (this.idEmpresa != other.idEmpresa) {
            return false;
        }
        if (this.idEscopo != other.idEscopo) {
            return false;
        }
        if (this.idVulnerabilidade != other.idVulnerabilidade) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pucrs.br.entity.EscopoVulPK[ idEmpresa=" + idEmpresa + ", idEscopo=" + idEscopo + ", idVulnerabilidade=" + idVulnerabilidade + " ]";
    }
    
}
