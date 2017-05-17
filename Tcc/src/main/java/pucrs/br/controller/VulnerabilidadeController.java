package pucrs.br.controller;

import pucrs.br.entity.Vulnerabilidade;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.event.RowEditEvent;
import pucrs.br.bean.VulnerabilidadeFacade;
import pucrs.br.entity.Empresa;

@Named("vulnerabilidadeController")
@SessionScoped
public class VulnerabilidadeController implements Serializable {

    private Vulnerabilidade current;
    private Vulnerabilidade newVul;
    private DataModel items = null;
    @EJB
    private pucrs.br.bean.VulnerabilidadeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public VulnerabilidadeController() {
    }

    public Vulnerabilidade getSelected() {
        if (current == null) {
            current = new Vulnerabilidade();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public Vulnerabilidade getSelectedNew() {
        if (newVul == null) {
            newVul = new Vulnerabilidade();
            selectedItemIndex = -1;
        }
        return newVul;
    }

    private VulnerabilidadeFacade getFacade() {
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

    public String prepareList() {
        recreateModel();
        return "ListVul";
    }

    public String prepareView() {
        current = (Vulnerabilidade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ViewVul";
    }

    public String prepareCreate() {
        newVul = new Vulnerabilidade();
        selectedItemIndex = -1;
        return "CreateVul";
    }

    public String create() {
        try {
            getFacade().create(newVul);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VulnerabilidadeCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Vulnerabilidade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "EditVul";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VulnerabilidadeUpdated"));
            return "ViewVul";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Vulnerabilidade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "ListVul";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "ViewVul";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "ListVul";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VulnerabilidadeDeleted"));
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
        return "ListVul";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "ListVul";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Vulnerabilidade getVulnerabilidade(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Vulnerabilidade.class)
    public static class VulnerabilidadeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VulnerabilidadeController controller = (VulnerabilidadeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vulnerabilidadeController");
            return controller.getVulnerabilidade(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Vulnerabilidade) {
                Vulnerabilidade o = (Vulnerabilidade) object;
                return getStringKey(o.getIdVulnerabilidade());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Vulnerabilidade.class.getName());
            }
        }

    }
    
    public void onRowEdit(RowEditEvent event) {
        current = ((Vulnerabilidade) event.getObject());
        update();
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Vulnerabilidade Cancelled", ((Vulnerabilidade) event.getObject()).getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
