package pucrs.br.controller;

import java.io.IOException;
import pucrs.br.entity.Empresa;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.EmpresaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
import pucrs.br.entity.Vulnerabilidade;
import pucrs.br.entity.VulnerabilidadeAdmin;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@Named("empresaController")
@SessionScoped
public class EmpresaController implements Serializable {

    private Empresa current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @EJB
    private EmpresaFacade ejbFacade;
    @Inject
    private VulnerabilidadeAdminController vulAdminController;
    @Inject
    private VulnerabilidadeController vulController;

    public EmpresaController() {
    }

    public Empresa getSelected() {
        if (current == null) {
            current = new Empresa();
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(Empresa current) {
        this.current = current;
    }
    
    private EmpresaFacade getFacade() {
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
        recreatePagination();
        recreateModel();        
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/empresa/List.jsf");
    }

    public void prepareView() throws IOException {
        current = (Empresa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/empresa/View.jsf");
    }

    public void prepareCreate() throws IOException {
        current = new Empresa();
        selectedItemIndex = -1;
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/empresa/Create.jsf");
    }

    public void create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Empresa criada.");
                createVulsEmp();
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao criar empresa.");
        }
    }

    public void prepareEdit() throws IOException {
        current = (Empresa) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/empresa/Edit.jsf");
    }

    public void update() {
        try {
                getFacade().edit(current);
                FacesMessage msg = new FacesMessage("Empresa atualizada");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao atualizar empresa.");
        }
    }

    public void destroy() throws IOException {
        current = (Empresa) getItems().getRowData();
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
            return "ViewEmpresa";
        } else {
            recreateModel();
            return "ListEmpresa";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage("Empresa deletada");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao deletar empresa");
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
        return "ListEmpresa";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "ListEmpresa";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Empresa getEmpresa(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Empresa.class)
    public static class EmpresaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmpresaController controller = (EmpresaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "empresaController");
            return controller.getEmpresa(getKey(value));
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
            if (object instanceof Empresa) {
                Empresa o = (Empresa) object;
                return getStringKey(o.getIdEmpresa());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Empresa.class.getName());
            }
        }

    }
    
    // Retorna todas as empresa
    public List<Empresa> findAll() {
        return ejbFacade.findAll();
    }
    
    // Criar automaticamente vulnerabilidade vinculadas a empresa no momento que salva uma empresa nova
    public void createVulsEmp() {
        List<VulnerabilidadeAdmin> lista = new ArrayList<VulnerabilidadeAdmin>();
        lista = vulAdminController.findAll();
        for(int i=0;i<lista.size();i++) {            
            Vulnerabilidade vul = new Vulnerabilidade(null,
                                                            lista.get(i).getNome(), 
                                                            lista.get(i).getDescricao(), 
                                                            lista.get(i).getNivel(), 
                                                            lista.get(i).getAcoes(), 
                                                            lista.get(i).getFonte(), 
                                                            lista.get(i).getDataCriacao());
            vul.setIdEmpresa(current);
            vul.setConsequencia(lista.get(i).getConsequencia());
            vulController.insert(vul);
        }
    }
}
