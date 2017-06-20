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
import com.itextpdf.text.*;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static com.sun.org.apache.xerces.internal.impl.XMLEntityManager.DEFAULT_BUFFER_SIZE;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pucrs.br.entity.Grafico;

@Named("escopoController")
@SessionScoped
public class EscopoController implements Serializable {

    private Escopo current;
    private Escopo escopoNew;
    private DataModel items = null;
    @EJB
    private EscopoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Vulnerabilidade> selectedVul = new ArrayList<Vulnerabilidade>();
    private EntityManagerFactory factory = Persistence
            .createEntityManagerFactory("pucrs_Tcc_war_1.0PU");
    private EntityManager em = factory.createEntityManager();
    @Inject
    private UsuarioController usuarioLogado;
    private List<EscopoVul> escopovul = new ArrayList<EscopoVul>();
    @Inject
    private EscopoVulController escopoVulContrl;
    @Inject
    private VulnerabilidadeController vulctrl;

    public EscopoController() {
    }

    public Escopo getSelected() {
        if (current == null) {
            current = new Escopo();
            selectedItemIndex = -1;
        }
        return current;
    }

    public Escopo getSelectedNew() {
        if (escopoNew == null) {
            escopoNew = new Escopo();
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
                    return new ListDataModel(getFacade().findAllEscByUser(usuarioLogado.getLogado().getIdEmpresa().getIdEmpresa()));
                    //return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public void prepareList() throws IOException {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("ListEscopo.jsf");
    }

    public String prepareView() {
        current = (Escopo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        //Popular lista de vul
        selectedVul.clear();

        escopovul = escopoVulContrl.findAllfindByIdEscopo(current);
        for (int i = 0; i < escopovul.size(); i++) {
            selectedVul.add(escopovul.get(i).getVulnerabilidade());
        }
        //selectedVul.addAll(current.getVulnerabilidadeCollection().toArray());
        return "ViewEscopo";
    }

    public String prepareCreate() {
        escopoNew = new Escopo();
        selectedItemIndex = -1;
        return "CreateEscopo";
    }

    public String create() {
        try {
            escopoNew.setIdEmpresa(usuarioLogado.getLogado().getIdEmpresa());
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
            //current.setIdEmpresa(current.getIdEmpresa());
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

    public Escopo getEscopo(int id) {
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
            if (object instanceof Escopo) {
                Escopo o = (Escopo) object;
                return getStringKey(o.getIdEscopo());
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

        if (newValue != null && !newValue.equals(oldValue)) {
            selectedItemIndex = event.getRowIndex();
            current = (Escopo) items.getRowData();
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

    public void gerarPdf(Escopo esc) throws Exception {
        long lDateTime = new Date().getTime();
        String DEST = "results/relatorio" + lDateTime + ".pdf";
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        createPdf(DEST, esc);
    }

    public void createPdf(String dest, Escopo esc) throws IOException, DocumentException {
        Document document = new Document();
        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(2);
        List<EscopoVul> escvul = escopoVulContrl.consultaRelatorio(esc);
        int idvul;
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Vulnerabilidade", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Risco", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        for (int aw = 0; aw < escvul.size(); aw++) {
            idvul = escvul.get(aw).getEscopoVulPK().getIdVulnerabilidade();
            table.addCell(vulctrl.getVulNome(idvul));
            table.addCell(recuperaRisco(escvul.get(aw).getRisco()));
        }
        document.add(table);
        document.close();
        JsfUtil.addSuccessMessage("Arquivo exportado.");
    }
    
    public String recuperaRisco(double risco) {
        if (round(risco,2) == 0.05 || 
            round(risco,2) == 0.04 ||
            round(risco,2) == 0.03 ||
            round(risco,2) == 0.02 ||
            round(risco,2) == 0.01 ) {
            return "Baixo";
        } else if (round(risco,2) == 0.09 ||
                   round(risco,2) == 0.07 ||
                   round(risco,2) == 0.14 ||
                   round(risco,2) == 0.10 ||
                   round(risco,2) == 0.06 ||
                   round(risco,2) == 0.12 ||
                   round(risco,2) == 0.08 ) {
            return "Moderado";
        } else if (round(risco,2) == 0.18 ||
                   round(risco,2) == 0.36 ||
                   round(risco,2) == 0.72 ||
                   round(risco,2) == 0.28 ||
                   round(risco,2) == 0.56 ||
                   round(risco,2) == 0.20 ||
                   round(risco,2) == 0.40 ||
                   round(risco,2) == 0.24 ){
            return "Alto";
        } else {
            return "Risco não encontrado ->"+round(risco,2);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    public List<Escopo> findAll() {
        return ejbFacade.findAll();
    }
    
    public List<Grafico> findResultGrafico() {
        return ejbFacade.findResultGrafico(usuarioLogado.getLogado().getIdEmpresa().getIdEmpresa());
    }
}
