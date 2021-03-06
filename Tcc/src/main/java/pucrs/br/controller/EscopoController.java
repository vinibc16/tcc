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
import pucrs.br.entity.EscopoVul;
import pucrs.br.entity.Vulnerabilidade;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.*;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.util.Streams;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * @Henrique Knorre 
 * @Vinicius Canteiro
 */
@Named("escopoController")
@SessionScoped
public class EscopoController extends HttpServlet implements Serializable {

    private Escopo current;
    private DataModel items = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Vulnerabilidade> selectedVul = new ArrayList<Vulnerabilidade>();
    private List<EscopoVul> escopovul = new ArrayList<EscopoVul>();
    private UploadedFile file;
    private byte[] exportContent;
    private StreamedContent fileDownload;
    @EJB
    private EscopoFacade ejbFacade;
    @Inject
    private UsuarioController usuarioLogado;
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
                }
            };
        }
        return pagination;
    }

    public void prepareList() throws IOException {
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/List.jsf");
    }
    
    public void prepareView() throws IOException {
        current = (Escopo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopo/View.jsf");
    }
    
    public void prepareViewFromEscopo(Escopo escopo) throws IOException {
        current = escopo;
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

    public void destroyEscopo() throws IOException {
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
    
    public List<EscopoVul> getEscopovul() {
        return escopovul;
    }

    public void setEscopovul(List<EscopoVul> escopovul) {
        this.escopovul = escopovul;
    }
    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public StreamedContent getFileDownload() {
        return fileDownload;
    }

    public void setFileDownload(StreamedContent fileDownload) {
        this.fileDownload = fileDownload;
    }

    // Retorna se habilita ou não o botão do relatório
    public boolean visibleRelatorio() {
        return usuarioLogado.getLogado().getIdGrupo().getIdGrupo() == 2;
    }

    // Mostra o PDF no browser
    public void showPdf(Escopo esc) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=Relatorio.pdf");
        response.getOutputStream().write(createPdf(esc).toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        context.responseComplete();
        
    }
    
    // Monta o PDF
    public ByteArrayOutputStream createPdf(Escopo esc) throws IOException, DocumentException {
        Document document = new Document();
        Paragraph p = new Paragraph();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfPTable table;
        List<EscopoVul> escvul = escopoVulContrl.consultaRelatorio(esc);
        int idvul;
        PdfPCell cell;
        Font boldFont16 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Font boldFont14 = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.BOLD);
        PdfWriter.getInstance(document, byteArrayOutputStream);
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
        document.add(new Paragraph(" "));
        
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
        document.add(new Paragraph("Área de Ataque",boldFont14));
        table = new PdfPTable(2);
        
        cell = new PdfPCell(new Phrase("Ameaça", boldFont14));
        document.add(new Paragraph(" "));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Consequência", boldFont14));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        for (int aw = 0; aw < escvul.size(); aw++) {
            table.addCell(escvul.get(aw).getAmaeaca());
            table.addCell(escvul.get(aw).getConsequencia());            
        }
        document.add(table);
        document.add(new Paragraph(" "));
        
        // Tabela 2
        document.add(new Paragraph("Vulnerabilidade - Ativos de Informação",boldFont16));
        table = new PdfPTable(4);
        document.add(new Paragraph(" "));
        table.setTotalWidth(400f);
        table.setLockedWidth(true);
        float[] widths = new float[] { 200f, 100f, 50f, 50f };
        table.setWidths(widths);
        cell = new PdfPCell(new Phrase("Vulnerabilidade", boldFont14));        
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Fonte", boldFont14));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Risco", boldFont14));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Ação", boldFont14));
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
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            table.addCell(escvul.get(aw).getAcao());
        }
        document.add(table);
        document.add(new Paragraph(" "));
        
        // Açoes e controles
        document.add(new Paragraph("Ações e Controles recomendados",boldFont16));
        for (int aw = 0; aw < escvul.size(); aw++) {
            document.add(new Paragraph("       - "+escvul.get(aw).getAcoes()));
        }
        
        document.close();
        return byteArrayOutputStream;
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
    
    // Método auxiliar para arredondar
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    // Metodo para recuperar todos escopos
    public List<Escopo> findAll() {
        return ejbFacade.findAll();
    }
    
    // Importar arquivos
    public void importa(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        byte[] arquivo = IOUtils.toByteArray(uploadedFile.getInputstream());
        
        current.setFile(arquivo);
        current.setNomeArquivo(uploadedFile.getFileName());
        getFacade().edit(current);
        FacesMessage message = new FacesMessage("O arquivo ", uploadedFile.getFileName() + " foi carregado com susesso.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    // Exportar arquivos
    public void export() {
        exportContent = current.getFile();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("text/plain");
        ec.setResponseContentLength(exportContent.length);
        String attachmentName = "attachment; filename="+current.getNomeArquivo();
        ec.setResponseHeader("Content-Disposition", attachmentName);
        try {
            OutputStream output = ec.getResponseOutputStream();
            Streams.copy(new ByteArrayInputStream(exportContent), output, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        fc.responseComplete();
        InputStream myInputStream = new ByteArrayInputStream(current.getFile()); 
        fileDownload = new DefaultStreamedContent(myInputStream);
        FacesMessage message = new FacesMessage("O arquivo ", current.getNomeArquivo() + " foi exportado com susesso.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    // Método executa antes de abrir a tela de associar
    public void associar() throws IOException {
        selectedVul.clear();
        escopovul = escopoVulContrl.findAllfindByIdEscopo(current);
        for (int i=0; i<escopovul.size(); i++) {
            escopovul.get(i).setVulnerabilidade(vulctrl.getVulnerabilidade(escopovul.get(i).getEscopoVulPK().getIdVulnerabilidade()));
        }
        for (int i = 0; i < escopovul.size(); i++) {
            selectedVul.add(escopovul.get(i).getVulnerabilidade());
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("/Tcc/escopoVul/Associar.jsf");
    }
}
