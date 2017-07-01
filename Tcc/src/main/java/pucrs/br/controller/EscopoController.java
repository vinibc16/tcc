package pucrs.br.controller;

import pucrs.br.entity.Escopo;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.EscopoFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pucrs.br.entity.EscopoVul;
import pucrs.br.entity.Vulnerabilidade;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.*;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import pucrs.br.entity.Grafico;
import pucrs.br.entity.Relatorio;

@Named("escopoController")
@SessionScoped
public class EscopoController implements Serializable {

    private Escopo current;
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
    private UploadedFile file;

    public EscopoController() {
    }

    public Escopo getSelected() {
        if (current == null) {
            current = new Escopo();
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(Escopo current) {
        this.current = current;
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
                    return new ListDataModel(getFacade().findAllEscByUser(usuarioLogado.getLogado().getIdEmpresa()));
                    //return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public void prepareList() throws IOException {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/List.jsf");
    }
    
    public void associar() throws IOException {
        selectedVul.clear();

        escopovul = escopoVulContrl.findAllfindByIdEscopo(current);
        for (int i = 0; i < escopovul.size(); i++) {
            selectedVul.add(escopovul.get(i).getVulnerabilidade());
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopoVul/Associar.jsf");
    }

    public void prepareView() throws IOException {
        current = (Escopo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/View.jsf");
    }

    public void prepareCreate() throws IOException {
        current = new Escopo();
        selectedItemIndex = -1;
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/Create.jsf");
    }

    public void create() {
        try {
            current.setIdEmpresa(usuarioLogado.getLogado().getIdEmpresa());
            current.setDataCriacao(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Escopo criado");
            prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao criar escopo");
        }
    }

    public void prepareEdit() throws IOException {
        current = (Escopo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/Edit.jsf");
    }

    public void update() {
        try {
            getFacade().edit(current);
            prepareList();
            JsfUtil.addSuccessMessage("Escopo atualizado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao atualizar o escopo");
        }
    }

    public void destroy() throws IOException {
        current = (Escopo) getItems().getRowData();
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
            JsfUtil.addSuccessMessage("Escopo deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao deletar escopo");
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
        Paragraph p = new Paragraph();
        PdfPTable table;
        List<EscopoVul> escvul = escopoVulContrl.consultaRelatorio(esc);
        int idvul;
        PdfPCell cell;
        Font boldFont16 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Font boldFont14 = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.BOLD);
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        // Cabeçalho
        // Empresa
        document.add(new Paragraph("Empresa",boldFont16));
        p.add(new Phrase("Nome: ",boldFont14));
        p.add(new Phrase(esc.getIdEmpresa().getNome()));
        document.add(p);
        p.clear();
        p.add(new Phrase("Missão: ",boldFont14));
        p.add(new Phrase(esc.getIdEmpresa().getMissao()));
        document.add(p);
        p.clear();
        p.add(new Phrase("Segmento: ",boldFont14));
        p.add(new Phrase(esc.getIdEmpresa().getSegmento()));
        document.add(p);
        p.clear();
        
        // Escopo
        document.add(new Paragraph("Escopo",boldFont16));
        p.add(new Phrase("Nome: ",boldFont14));
        p.add(new Phrase(esc.getNome()));
        document.add(p);
        p.clear();
        p.add(new Phrase("Responsável: ",boldFont14));
        p.add(new Phrase(esc.getIdResponsavel().getNome()));
        document.add(p);
        p.clear();
        p.add(new Phrase("Descrição: ",boldFont14));
        p.add(new Phrase(esc.getDescricao()));
        document.add(p);
        p.clear();
        p.add(new Phrase("Ativos: ",boldFont14));
        p.add(new Phrase(esc.getAtivosEnvolvidos()));
        document.add(p);
        p.clear();
        document.add(new Paragraph(" "));
        
        // Tabela 1
        document.add(new Paragraph("Área de Ataque",boldFont16));
        table = new PdfPTable(2);
        
        cell = new PdfPCell(new Phrase("Ameaça", boldFont16));
        document.add(new Paragraph(" "));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Consequência", boldFont16));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        for (int aw = 0; aw < escvul.size(); aw++) {
            table.addCell(escvul.get(aw).getAmaeaca());
            table.addCell(escvul.get(aw).getConsequencia());            
        }
        document.add(table);
        
        // Tabela 2
        document.add(new Paragraph("Vulnerabilidade - Ativos de Informação",boldFont16));
        table = new PdfPTable(3);
        
        cell = new PdfPCell(new Phrase("Vulnerabilidade", boldFont16));
        document.add(new Paragraph(" "));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Fonte", boldFont16));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Risco", boldFont16));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        for (int aw = 0; aw < escvul.size(); aw++) {
            idvul = escvul.get(aw).getEscopoVulPK().getIdVulnerabilidade();
            table.addCell(escvul.get(aw).getVulnerabilidade().getNome());
            table.addCell(escvul.get(aw).getVulnerabilidade().getFonte());
            cell = new PdfPCell(new Phrase(""+recuperaRisco(escvul.get(aw).getRisco())));
            if (recuperaRisco(escvul.get(aw).getRisco()).equals("Alto")) {
                cell.setBackgroundColor(BaseColor.RED);
            } else if (recuperaRisco(escvul.get(aw).getRisco()).equals("Moderado")) {
                cell.setBackgroundColor(BaseColor.YELLOW);
            } else if (recuperaRisco(escvul.get(aw).getRisco()).equals("Baixo")) {
                cell.setBackgroundColor(BaseColor.GREEN);
            }
            table.addCell(cell);
            //table.addCell(recuperaRisco(escvul.get(aw).getRisco()));
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
    
    public void visualizarRelatorio() {
        try {
            System.out.println("entrou no visualizar relatorio");
            //---------- gera o relatorio ----------
            List<Relatorio> listaRelatorio = new ArrayList<Relatorio>();
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Map<String, Object> params = new HashMap<String, Object>(); 
            //InputStream stream = this.getClass().getResourceAsStream("/relatorio/Relatorio.jasper");
            //JasperReport report = (JasperReport) JRLoader.loadObject(stream);
            ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
            JasperPrint print = JasperFillManager.fillReport(scontext.getRealPath("/WEB-INF/Relatorio.jasper"), params, new JRBeanCollectionDataSource(listaRelatorio));
            JasperExportManager.exportReportToPdfStream(print, baos);
            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            response.setHeader("Content-disposition", "inline; filename=relatorio.pdf");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            context.responseComplete();
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public void importa(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
