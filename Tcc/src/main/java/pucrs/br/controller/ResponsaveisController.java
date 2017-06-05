package pucrs.br.controller;

import java.io.IOException;
import pucrs.br.entity.Responsaveis;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.ResponsaveisFacade;

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
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Escopo;

@Named("responsaveisController")
@SessionScoped
public class ResponsaveisController implements Serializable {

    private Responsaveis current;
    private Responsaveis newResp;
    private DataModel items = null;
    @EJB
    private pucrs.br.bean.ResponsaveisFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @Inject
    private UsuarioController usuario;

    public ResponsaveisController() {
    }

    public Responsaveis getSelected() {
        if (current == null) {
            current = new Responsaveis();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public Responsaveis getSelectedNew() {
        if (newResp == null) {
            newResp = new Responsaveis();
            selectedItemIndex = -1;
        }
        return newResp;
    }

    private ResponsaveisFacade getFacade() {
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
                    //return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                    return new ListDataModel(getFacade().findAllRespByUser(usuario.getLogado().getIdEmpresa().getIdEmpresa()));
                }
            };
        }
        return pagination;
    }

    public void prepareList() throws IOException {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("ListResponsaveis.jsf");
    }

    public String prepareView() {
        current = (Responsaveis) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ViewResponsaveis";
    }

    public String prepareCreate() {
        newResp = new Responsaveis();
        selectedItemIndex = -1;
        return "CreateResponsaveis";
    }

    public String create() {
        try {
            newResp.setIdEmpresa(usuario.getLogado().getIdEmpresa());
            getFacade().create(newResp);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ResponsaveisCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Responsaveis) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "EditResponsaveis";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ResponsaveisUpdated"));
            return "ViewResponsaveis";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Responsaveis) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "ListResponsaveis";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "ViewResponsaveis";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "ListResponsaveis";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ResponsaveisDeleted"));
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
        return "ListResponsaveis";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "ListResponsaveis";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Responsaveis getResponsaveis(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Responsaveis.class)
    public static class ResponsaveisControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ResponsaveisController controller = (ResponsaveisController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "responsaveisController");
            return controller.getResponsaveis(getKey(value));
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
            if (object instanceof Responsaveis) {
                Responsaveis o = (Responsaveis) object;
                return getStringKey(o.getIdResponsavel());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Responsaveis.class.getName());
            }
        }

    }
    
    public void onRowEdit(RowEditEvent event) {
        current = ((Responsaveis) event.getObject());
        update();
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Responsaveis Cancelled", ((Responsaveis) event.getObject()).getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<Responsaveis> findAll() {
        return ejbFacade.findAll();
    }
}
