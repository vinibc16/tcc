package pucrs.br.controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.PieChartModel;
import pucrs.br.entity.Grafico;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@ManagedBean
@Named("graficocontroller")
public class Graficocontroller implements Serializable {
    
    private PieChartModel pieModel1;
    @Inject
    private VulnerabilidadeController vulCtrl;
    @Inject 
    private UsuarioController user;
    
    @PostConstruct
    public void init() {
        createPieModel();
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public void setPieModel1(PieChartModel pieModel1) {
        this.pieModel1 = pieModel1;
    }
    
    private void createPieModel() {
        pieModel1 = new PieChartModel();
        List<Grafico> lista = new ArrayList<Grafico>();
        lista = vulCtrl.findResultGrafico(user.getLogado().getIdEmpresa());
        for(int i=0;i<lista.size();i++) {
            pieModel1.set(lista.get(i).getVul().getNome(), lista.get(i).getTotal());
            
            pieModel1.setTitle("Vulnerabilidades por Escopo");
            pieModel1.setLegendPosition("s");
        }
    }
        
}
