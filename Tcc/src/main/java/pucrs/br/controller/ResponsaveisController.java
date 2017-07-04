package pucrs.br.controller;

import java.io.IOException;
import pucrs.br.entity.Responsaveis;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.ResponsaveisFacade;
import java.io.Serializable;
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
import javax.inject.Inject;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@Named("responsaveisController")
@SessionScoped
public class ResponsaveisController implements Serializable {

    private Responsaveis current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @EJB
    private pucrs.br.bean.ResponsaveisFacade ejbFacade;    
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

    public void setSelected(Responsaveis current) {
        this.current = current;
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
                    return new ListDataModel(getFacade().findAllRespByUser(usuario.getLogado().getIdEmpresa()));
                }
            };
        }
        return pagination;
    }

    public void prepareList() throws IOException {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/responsaveis/List.jsf");
    }

    public void prepareView() throws IOException {
        current = (Responsaveis) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/responsaveis/View.jsf");
    }

    public void prepareCreate() throws IOException {
        current = new Responsaveis();
        selectedItemIndex = -1;
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/responsaveis/Create.jsf");
    }

    public void create() {
        try {
            current.setIdEmpresa(usuario.getLogado().getIdEmpresa());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Responsável criado");
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao criar responsável");
        }
    }

    public void prepareEdit() throws IOException {
        current = (Responsaveis) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/responsaveis/Edit.jsf");
    }

    public void update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Responsável atualizado");
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao atualizar responsável");
        }
    }

    public void destroy() throws IOException {
        current = (Responsaveis) getItems().getRowData();
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
            return "ViewResponsaveis";
        } else {
            recreateModel();
            return "ListResponsaveis";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage("Responsável deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao deletar responsável");
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
    
    // Recupera lista de responsáveis
    public List<Responsaveis> findAll() {
        return ejbFacade.findAllRespByUser(usuario.getLogado().getIdEmpresa());
    }
}
