
package pucrs.br.entity;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
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
