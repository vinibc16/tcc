/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.controller;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
 
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;
import pucrs.br.entity.Grafico;
/**
 *
 * @author psysvica
 */
@ManagedBean
@Named("graficocontroller")
public class Graficocontroller implements Serializable {
    
    private BarChartModel bar;
    @Inject
    private EscopoController escopoCtrl;
    
    @PostConstruct
    public void init() {
        createBar();
    }
    
    private void createBar() {
        bar = initBarModel();
         
        bar.setTitle("Risco médio por Escopos");
        //bar.setLegendPosition("ne");
         
        Axis xAxis = bar.getAxis(AxisType.X);
        xAxis.setLabel("Escopo");
         
        Axis yAxis = bar.getAxis(AxisType.Y);
        yAxis.setLabel("Risco");
        yAxis.setMin(0);
    }
    
    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        ChartSeries escopos = new ChartSeries();
        escopos.setLabel("Risco");
        List<Grafico> lista = escopoCtrl.findResultGrafico();
        for(int i=0;i<lista.size();i++) {
            escopos.set(lista.get(i).getEscopo().getNome(), lista.get(i).getTotal());
        }
        model.addSeries(escopos);
         
        return model;
    }

    public BarChartModel getBar() {
        return bar;
    }

    public void setBar(BarChartModel bar) {
        this.bar = bar;
    }
    
    
}
