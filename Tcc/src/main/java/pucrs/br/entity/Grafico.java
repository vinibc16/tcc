/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.entity;

/**
 *
 * @author psysvica
 */
public class Grafico {
    
    private Escopo escopo;
    private double total;

    public Grafico(Escopo escopo, double total) {
        this.escopo = escopo;
        this.total = total;
    }

    public Escopo getEscopo() {
        return escopo;
    }

    public void setEscopo(Escopo escopo) {
        this.escopo = escopo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
}
