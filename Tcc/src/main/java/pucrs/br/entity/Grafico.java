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
    
    private Vulnerabilidade vul;
    private long total;

    public Grafico(Vulnerabilidade vul, long total) {
        this.vul = vul;
        this.total = total;
    }

    public Vulnerabilidade getVul() {
        return vul;
    }

    public void setVul(Vulnerabilidade vul) {
        this.vul = vul;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
    
    
}
