package pucrs.br.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import pucrs.br.entity.Vulnerabilidade;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import pucrs.br.bean.VulnerabilidadeFacade;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.Grafico;

@Named("vulnerabilidadeController")
@SessionScoped
public class VulnerabilidadeController implements Serializable {

    private Vulnerabilidade current;
    private DataModel items = null;
    @EJB
    private pucrs.br.bean.VulnerabilidadeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @Inject
    private UsuarioController usuario;
    private Part arquivo;

    public VulnerabilidadeController() {
    }

    public Vulnerabilidade getSelected() {
        if (current == null) {
            current = new Vulnerabilidade();
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(Vulnerabilidade current) {
        this.current = current;
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
                    return new ListDataModel(getFacade().findAllVulByUser(usuario.getLogado().getIdEmpresa()));
                    //return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public void prepareList() throws IOException {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulnerabilidade/List.jsf");
    }

    public void prepareView() throws IOException {
        current = (Vulnerabilidade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulnerabilidade/View.jsf");
    }

    public void prepareCreate() throws IOException {
        current = new Vulnerabilidade();
        selectedItemIndex = -1;
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulnerabilidade/Create.jsf");
    }

    public void create() {
        try {
            current.setIdEmpresa(usuario.getLogado().getIdEmpresa());
            current.setDataCriacao(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Vulnerabilidade criada");
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao criar vulnerabilidade");
        }
    }

    public void prepareEdit() throws IOException {
        current = (Vulnerabilidade) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/vulnerabilidade/Edit.jsf");
    }

    public void update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Vulnerabilidade atualizada");
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao atualizar vulnerabilidade");
        }
    }

    public void destroy() throws IOException {
        current = (Vulnerabilidade) getItems().getRowData();
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
            JsfUtil.addSuccessMessage("Vulnerabilidade deletada");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao deletar vulnerabilidade");
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

    public void insert(Vulnerabilidade vul) {
        try {
            getFacade().create(vul);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro interno");
        }
    }

    public String getVulNome(int idVul) {
        return ejbFacade.getVulNome(idVul);
    }

    public List<Grafico> findResultGrafico(Empresa emp) {
        return getFacade().findResultGrafico(emp);
    }

    public void importa(FileUploadEvent event) throws IOException {
        String str = "";
        StringBuffer buf = new StringBuffer();
        UploadedFile uploadedFile = event.getFile();
        InputStream is = uploadedFile.getInputstream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            if (is != null) {
                while ((str = reader.readLine()) != null) {
                    Pattern p = Pattern.compile("[a-zA-Zà-úÀ-Ú]+");
                    Matcher m = p.matcher(str);
                    System.out.println("Aqui -> " + str);
                    String[] vulNova = str.split(";");
                    current = new Vulnerabilidade();
                    selectedItemIndex = -1;
                    current.setNome(vulNova[0]);
                    current.setDescricao(vulNova[1]);
                    current.setNivel(Integer.parseInt(vulNova[2]));
                    current.setAcoes(vulNova[3]);
                    current.setFonte(vulNova[4]);
                    current.setConsequencia(vulNova[5]);
                    create();
                    FacesMessage message = new FacesMessage("O arquivo ", uploadedFile.getFileName() + " foi carregado com susesso.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, "Erro ao importar o arquivo.");
            //FacesMessage message = new FacesMessage("Erro ao importar o arquivo.");
            //FacesContext.getCurrentInstance().addMessage(null, message);
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "","Erro ao importar o arquivo.");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }

    public Part getArquivo() {
        return arquivo;
    }

    public void setArquivo(Part arquivo) {
        this.arquivo = arquivo;
    }

}
