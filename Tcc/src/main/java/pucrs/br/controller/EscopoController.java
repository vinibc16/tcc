package pucrs.br.controller;

import java.io.FileNotFoundException;
import pucrs.br.entity.Escopo;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.EscopoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import pucrs.br.bean.EscopoVulFacade;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.EscopoVul;
import pucrs.br.entity.Vulnerabilidade;
import java.io.FileOutputStream;
import java.io.OutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("escopoController")
@SessionScoped
public class EscopoController implements Serializable {

    private Escopo current;
    private Escopo escopoNew;
    private DataModel items = null;
    @EJB
    private EscopoFacade ejbFacade;
    @Inject
    private EscopoVulController escopoVulContrl;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Vulnerabilidade> selectedVul = new ArrayList<Vulnerabilidade>();
    private EntityManagerFactory factory = Persistence
            .createEntityManagerFactory("pucrs_Tcc_war_1.0PU");
    private EntityManager em = factory.createEntityManager();
    @Inject
    private UsuarioController usuarioLogado;
    private List<EscopoVul> escopovul = new ArrayList<EscopoVul>();
    
    public EscopoController() {
    }

    public Escopo getSelected() {
        if (current == null) {
            current = new Escopo();
            current.setEscopoPK(new pucrs.br.entity.EscopoPK());
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public Escopo getSelectedNew() {
        if (escopoNew == null) {
            escopoNew = new Escopo();
            escopoNew.setEscopoPK(new pucrs.br.entity.EscopoPK());
            selectedItemIndex = -1;
        }
        return escopoNew;
    }

    private EscopoFacade getFacade() {
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
        return "ListEscopo";
    }

    public String prepareView() {
        current = (Escopo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //Popular lista de vul
        selectedVul.clear();
        
        escopovul = escopoVulContrl.findAllfindByIdEscopo(current);
        for(int i=0;i<escopovul.size();i++) {
            selectedVul.add(escopovul.get(i).getVulnerabilidade());
        }
        //selectedVul.addAll(current.getVulnerabilidadeCollection().toArray());
        return "ViewEscopo";
    }

    public String prepareCreate() {
        escopoNew = new Escopo();
        escopoNew.setEscopoPK(new pucrs.br.entity.EscopoPK());
        selectedItemIndex = -1;
        return "CreateEscopo";
    }

    public String create() {
        try {
            escopoNew.getEscopoPK().setIdEmpresa(usuarioLogado.getLogado().getIdEmpresa().getIdEmpresa());
            escopoNew.setDataCriacao(new Date());
            getFacade().create(escopoNew);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EscopoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Escopo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "EditEscopo";
    }

    public String update() {
        try {
            current.getEscopoPK().setIdEmpresa(current.getEscopoPK().getIdEmpresa());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EscopoUpdated"));
            return "ViewEscopo";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Escopo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "ListEscopo";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "ViewEscopo";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "ListEscopo";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("EscopoDeleted"));
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
        return "ListEscopo";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "ListEscopo";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Escopo getEscopo(pucrs.br.entity.EscopoPK id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Escopo.class)
    public static class EscopoControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EscopoController controller = (EscopoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "escopoController");
            return controller.getEscopo(getKey(value));
        }

        pucrs.br.entity.EscopoPK getKey(String value) {
            pucrs.br.entity.EscopoPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new pucrs.br.entity.EscopoPK();
            key.setIdEmpresa(Integer.parseInt(values[0]));
            key.setIdEscopo(Integer.parseInt(values[1]));
            return key;
        }

        String getStringKey(pucrs.br.entity.EscopoPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdEmpresa());
            sb.append(SEPARATOR);
            sb.append(value.getIdEscopo());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Escopo) {
                Escopo o = (Escopo) object;
                return getStringKey(o.getEscopoPK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Escopo.class.getName());
            }
        }

    }

    public List<Vulnerabilidade> getSelectedVul() {
        return selectedVul;
    }

    public void setSelectedVul(List<Vulnerabilidade> selectedVul) {
        this.selectedVul = selectedVul;
    }
    
    public void onRowEdit(RowEditEvent event) {
        current = ((Escopo) event.getObject());
        update();
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Escopo Cancelled", ((Escopo) event.getObject()).getNome());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            selectedItemIndex  = event.getRowIndex();
            current = (Escopo)items.getRowData();
            update();
        }
    }
    
    public boolean visibleRelatorio() {
        return usuarioLogado.getLogado().getIdGrupo().getIdGrupo() == 2;
    }

    public List<EscopoVul> getEscopovul() {
        return escopovul;
    }

    public void setEscopovul(List<EscopoVul> escopovul) {
        this.escopovul = escopovul;
    }
    
    public void gerarPdf() throws Exception {
        String DEST = "results/relatorio.pdf";
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(2);
        for(int aw = 0; aw < 8; aw++){
            table.addCell("Id Vulnerabilidades ");
            table.addCell("Descrição");
        }
        document.add(table);
        document.close();
        JsfUtil.addSuccessMessage("Arquivo exportado.");
    }
    
}
