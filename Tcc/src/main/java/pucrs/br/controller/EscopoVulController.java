package pucrs.br.controller;

import java.io.IOException;
import pucrs.br.entity.EscopoVul;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.EscopoVulFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.EscopoVulPK;
import pucrs.br.entity.Vulnerabilidade;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@Named("escopoVulController")
@SessionScoped
public class EscopoVulController implements Serializable {

    private EscopoVul current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private DataModel lista = null;
    private List<EscopoVul> listaApontamento, listaAceite;
    @EJB
    private pucrs.br.bean.EscopoVulFacade ejbFacade;
    @Inject
    private UsuarioController logado;
    @Inject
    private VulnerabilidadeController vul;
    
    public EscopoVulController() {
        listaApontamento = new ArrayList<>();
    }

    public List<EscopoVul> getListaAceite() {
        return listaAceite;
    }

    public void setListaAceite(List<EscopoVul> listaAceite) {
        this.listaAceite = listaAceite;
    }    

    public EscopoVul getSelected() {
        if (current == null) {
            current = new EscopoVul();
            current.setEscopoVulPK(new pucrs.br.entity.EscopoVulPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private EscopoVulFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(1000) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }
    
    public void prepareList() throws IOException {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("ListEscopoVul.jsf");
    }

    public String prepareView() {
        current = (EscopoVul) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ViewEscopoVul";
    }

    public String prepareCreate() {
        current = new EscopoVul();
        current.setEscopoVulPK(new pucrs.br.entity.EscopoVulPK());
        selectedItemIndex = -1;
        return "";
    }    

    public String create() {
        try {
            current.getEscopoVulPK().setIdEscopo(current.getEscopoVulPK().getIdEscopo());
            current.setIdEmpresa(logado.getLogado().getIdEmpresa());
            current.getEscopoVulPK().setIdVulnerabilidade(current.getVulnerabilidade().getIdVulnerabilidade());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EscopoVulCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (EscopoVul) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "EditEscopoVul";
    }

    public String update() {
        try {
            current.getEscopoVulPK().setIdVulnerabilidade(current.getVulnerabilidade().getIdVulnerabilidade());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EscopoVulUpdated"));
            return "";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (EscopoVul) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "ListEscopoVul";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "ViewEscopoVul";
        } else {
            recreateModel();
            return "ListEscopoVul";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EscopoVulDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            selectedItemIndex = count - 1;
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "ListEscopoVul";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "ListEscopoVul";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public EscopoVul getEscopoVul(pucrs.br.entity.EscopoVulPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = EscopoVul.class)
    public static class EscopoVulControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EscopoVulController controller = (EscopoVulController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "escopoVulController");
            return controller.getEscopoVul(getKey(value));
        }

        pucrs.br.entity.EscopoVulPK getKey(String value) {
            pucrs.br.entity.EscopoVulPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new pucrs.br.entity.EscopoVulPK();
            key.setIdEscopo(Integer.parseInt(values[1]));
            key.setIdVulnerabilidade(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(pucrs.br.entity.EscopoVulPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdEscopo());
            sb.append(SEPARATOR);
            sb.append(value.getIdVulnerabilidade());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EscopoVul) {
                EscopoVul o = (EscopoVul) object;
                return getStringKey(o.getEscopoVulPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EscopoVul.class.getName());
            }
        }

    }
    
    public DataModel getLista() {
        return lista;
    }

    public void setLista(List<EscopoVul> lista) {
        this.lista = (DataModel) lista;
    }

    public List<EscopoVul> getListaApontamento() {
        return listaApontamento;
    }

    public void setListaApontamento(List<EscopoVul> listaApontamento) {
        this.listaApontamento = listaApontamento;
    }
    
    // Método chamado no momento de salvar as associações de vulnerabilidades ao escopo
    public void associaVul(Escopo escopo, List<Vulnerabilidade> vul) throws IOException {
        List<EscopoVul> list = getFacade().findAllfindByIdEscopo(escopo);
        for (int i=0; list.size()>i; i++) {
            ejbFacade.remove(list.get(i));
        }
        
        for( int i=0; i<vul.size();i++) {
            EscopoVul ev = new EscopoVul();
            long yourmilliseconds = System.currentTimeMillis();
            Date resultdate = new Date(yourmilliseconds);
            ev.setDataLink(resultdate);
            
            EscopoVulPK evpk = new EscopoVulPK();
            evpk.setIdEscopo(escopo.getIdEscopo());
            evpk.setIdVulnerabilidade(vul.get(i).getIdVulnerabilidade());
            ev.setEscopoVulPK(evpk);
            ev.setIdEmpresa(logado.getLogado().getIdEmpresa());
            ev.setAcoes(vul.get(i).getAcoes());
            ev.setAmaeaca(vul.get(i).getAmeaca());
            ev.setConsequencia(vul.get(i).getConsequencia());
            
            getFacade().create(ev);
        }                
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/View.jsf");
    }
    
    // Método chamado antes de abrir a tela de Definir risco
    public void abrirDefinirRisco(Escopo escopo) throws IOException {
        listaApontamento = getFacade().findAllfindByIdEscopo(escopo);
        for (int i=0; i<listaApontamento.size(); i++) {
            listaApontamento.get(i).setVulnerabilidade(vul.getVulnerabilidade(listaApontamento.get(i).getEscopoVulPK().getIdVulnerabilidade()));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopoVul/DefinirRisco.jsf");
    }
    
    // Método chamado antes de abrir a tela de Definir aceite
    public void abrirDefinirAceite(Escopo escopo) throws IOException {
        listaApontamento = getFacade().findAllfindByIdEscopo(escopo);
        for (int i=0; i<listaApontamento.size(); i++) {
            listaApontamento.get(i).setVulnerabilidade(vul.getVulnerabilidade(listaApontamento.get(i).getEscopoVulPK().getIdVulnerabilidade()));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopoVul/DefinirAceite.jsf");
    }

    public void definirRisco() {
        try {            
            for (int i = 0; i < listaApontamento.size(); i++) {
                current = listaApontamento.get(i);
                current.setEscopoVulPK(listaApontamento.get(i).getEscopoVulPK());
                if (listaApontamento.get(i).getImpacto() != 0 && listaApontamento.get(i).getProbabilidade() != 0) {
                    current.setImpacto(listaApontamento.get(i).getImpacto());
                    current.setProbabilidade(listaApontamento.get(i).getProbabilidade());
                    current.setRisco(listaApontamento.get(i).getImpacto()*listaApontamento.get(i).getProbabilidade());
                }
                if (listaApontamento.get(i).getAceito() != null) {
                    current.setAceito(listaApontamento.get(i).getAceito());
                }
                getFacade().edit(current);
            }
            FacesMessage msg = new FacesMessage("EscopoVul Edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/View.jsf");
        } catch (Exception e) {            
        }
    }
    
    // Retorna lista de vulnerabilidade do escopo
    public List<EscopoVul> findAllfindByIdEscopo(Escopo escopo) {
        return ejbFacade.findAllfindByIdEscopo(escopo);
    }
    
    // Retorna a List de vulnerabilidade do relatório
    public List<EscopoVul> consultaRelatorio(Escopo escopo) {
        return ejbFacade.consultaRelatorio(escopo);
    }
    
    // Verifica se desabilita botão de relatório
    public boolean desabilitaRelatorio(Escopo escopo) {
        return ejbFacade.desabilitaRelatorio(escopo);
    }
   
}
