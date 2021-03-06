package pucrs.br.controller;

import java.io.IOException;
import pucrs.br.entity.VulnerabilidadeAdmin;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.VulnerabilidadeAdminFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@Named("vulnerabilidadeAdminController")
@SessionScoped
public class VulnerabilidadeAdminController implements Serializable {

    private VulnerabilidadeAdmin current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @EJB
    private pucrs.br.bean.VulnerabilidadeAdminFacade ejbFacade;

    public VulnerabilidadeAdminController() {
    }

    public VulnerabilidadeAdmin getSelected() {
        if (current == null) {
            current = new VulnerabilidadeAdmin();
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(VulnerabilidadeAdmin current) {
        this.current = current;
    }
    
    private VulnerabilidadeAdminFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

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
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulAdmin/List.jsf");
    }

    public void prepareView() throws IOException {
        current = (VulnerabilidadeAdmin) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulAdmin/View.jsf");
    }

    public void prepareCreate() throws IOException {
        current = new VulnerabilidadeAdmin();
        selectedItemIndex = -1;
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulAdmin/Create.jsf");
    }

    public void create() {
        try {
            current.setDataCriacao(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Vulnerabilidade Criada");
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro na criação da Vulnerabilidade");
        }
    }

    public void prepareEdit() throws IOException {
        current = (VulnerabilidadeAdmin) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulAdmin/Edit.jsf");
    }

    public void update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Vulnerabilidade atualizada");
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro na atualização da Vulnerabilidade");
        }
    }

    public void destroy() throws IOException {
        current = (VulnerabilidadeAdmin) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        prepareList();
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "ViewVulAdmin";
        } else {
            recreateModel();
            return "ListVulAdmin";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage("Vulnerabilidade deletada.");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro em deletar a vulnerabilidade");
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
        return "ListVulAdmin";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "ListVulAdmin";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public VulnerabilidadeAdmin getVulnerabilidadeAdmin(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = VulnerabilidadeAdmin.class)
    public static class VulnerabilidadeAdminControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VulnerabilidadeAdminController controller = (VulnerabilidadeAdminController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vulnerabilidadeAdminController");
            return controller.getVulnerabilidadeAdmin(getKey(value));
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
            if (object instanceof VulnerabilidadeAdmin) {
                VulnerabilidadeAdmin o = (VulnerabilidadeAdmin) object;
                return getStringKey(o.getIdVulnerabilidade());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + VulnerabilidadeAdmin.class.getName());
            }
        }

    }

    // Recupera todas as vulnerabilidades do Admin
    public List<VulnerabilidadeAdmin> findAll() {
        return ejbFacade.findAll();
    }
}
