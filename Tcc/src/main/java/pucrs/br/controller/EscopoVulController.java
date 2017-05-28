package pucrs.br.controller;

import pucrs.br.entity.EscopoVul;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.EscopoVulFacade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import pucrs.br.entity.Escopo;
import pucrs.br.entity.EscopoPK;
import pucrs.br.entity.EscopoVulPK;
import pucrs.br.entity.Vulnerabilidade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

@Named("escopoVulController")
@SessionScoped
public class EscopoVulController implements Serializable {

    private EscopoVul current;
    private DataModel items = null;
    @EJB
    private pucrs.br.bean.EscopoVulFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private DataModel lista = null;
    private List<EscopoVul> listaApontamento, listaAceite;
    private Escopo escopoPag = null;
    
    public EscopoVulController() {
        listaApontamento = new ArrayList<>();
        System.out.println("EscopoVulController CRIADO!");
        //lista = new ArrayList<>();
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
    
    public PaginationHelper getPagination2() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findAllfindByIdEscopo(escopoPag));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "ListEscopoVul";
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
            current.getEscopoVulPK().setIdEmpresa(current.getEscopoVulPK().getIdEmpresa());
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
            // all items were removed - go back to list
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
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
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
            key.setIdEmpresa(Integer.parseInt(values[0]));
            key.setIdEscopo(Integer.parseInt(values[1]));
            key.setIdVulnerabilidade(Integer.parseInt(values[2]));
            return key;
        }

        String getStringKey(pucrs.br.entity.EscopoVulPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdEmpresa());
            sb.append(SEPARATOR);
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
    
    public String associaVul(Escopo escopo, List<Vulnerabilidade> vul) {
        List<EscopoVul> lista = ejbFacade.findAllfindByIdEscopo(escopo);
        for (int i=0; lista.size()>i; i++) {
            ejbFacade.remove(lista.get(i));
        }
        
        for( int i=0; i<vul.size();i++) {
            EscopoVul ev = new EscopoVul();
            long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
            Date resultdate = new Date(yourmilliseconds);
            ev.setDataLink(resultdate);
            
            EscopoVulPK evpk = new EscopoVulPK();
            evpk.setIdEmpresa(escopo.getEscopoPK().getIdEmpresa());
            evpk.setIdEscopo(escopo.getEscopoPK().getIdEscopo());
            evpk.setIdVulnerabilidade(vul.get(i).getIdVulnerabilidade());
            ev.setEscopoVulPK(evpk);
            
            System.out.println(ev.toString());
            getFacade().create(ev);
            System.out.println("Associa Vul ->"+vul.get(i));            
        }                
        return "ListEscopo";
    }
    
    public String editEscopoVul(Escopo escopo) {
        recreateModel();
        escopoPag = escopo;
        lista = getPagination2().createPageDataModel();
        System.out.println("Id Escopo ->"+escopo.getEscopoPK().getIdEscopo());
        listaApontamento = getFacade().findAllfindByIdEscopo(escopo);
        System.out.println(""+listaApontamento.size());
        return "ListEscopoVul2.jsf";
    }
    
    public String editAceite(Escopo escopo) {
        recreateModel();
        escopoPag = escopo;
        lista = getPagination2().createPageDataModel();
        System.out.println("Id Escopo ->"+escopo.getEscopoPK().getIdEscopo());
        listaApontamento = getFacade().findAllfindByIdEscopo(escopo);
        System.out.println(""+listaApontamento.size());
        return "DefinirAceite.jsf";
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
    
    public String update2() {
        try {            
            for (int i = 0; i < listaApontamento.size(); i++) {
                //current = new EscopoVul();
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
            return "ListEscopo.jsf";
        } catch (Exception e) {
            return null;
        }
    }
    
    public String update3() {
        try {            
            for (int i = 0; i < listaApontamento.size(); i++) {
                current = new EscopoVul();
                current.setEscopoVulPK(listaApontamento.get(i).getEscopoVulPK());
                current.setAceito(listaApontamento.get(i).getAceito());
                getFacade().edit(current);
            }
            FacesMessage msg = new FacesMessage("EscopoVul Edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "ListEscopo.jsf";
        } catch (Exception e) {
            return null;
        }
    }
    
    public String update4(List<EscopoVul> vul) {
        try {            
            System.out.println("Lista1 ->"+vul.size());
            System.out.println("Lista2 ->"+listaAceite.size());
            for (int i = 0; i < vul.size(); i++) {
                current = new EscopoVul();
                current.setEscopoVulPK(vul.get(i).getEscopoVulPK());
                current.setAceito(i);
                getFacade().edit(current);
            }
            FacesMessage msg = new FacesMessage("EscopoVul Edited");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "ListEscopo.jsf";
        } catch (Exception e) {
            return null;
        }
    }
    
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("EscopoVul Edited");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("EscopoVul Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
    }
    
    public List<EscopoVul> findAllfindByIdEscopo(Escopo escopo) {
        return ejbFacade.findAllfindByIdEscopo(escopo);
    }
    
    
}
