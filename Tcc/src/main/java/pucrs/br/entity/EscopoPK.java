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
public class EscopoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_empresa")
    private int idEmpresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_escopo")
    private int idEscopo;

    public EscopoPK() {
    }

    public EscopoPK(int idEmpresa, int idEscopo) {
        this.idEmpresa = idEmpresa;
        this.idEscopo = idEscopo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEmpresa;
        hash += (int) idEscopo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EscopoPK)) {
            return false;
        }
        EscopoPK other = (EscopoPK) object;
        if (this.idEmpresa != other.idEmpresa) {
            return false;
        }
        if (this.idEscopo != other.idEscopo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pucrs.br.entity.EscopoPK[ idEmpresa=" + idEmpresa + ", idEscopo=" + idEscopo + " ]";
    }
    
}
