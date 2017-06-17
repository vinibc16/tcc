package pucrs.br.controller;

import java.io.IOException;
import pucrs.br.entity.Usuario;
import pucrs.br.controller.util.JsfUtil;
import pucrs.br.controller.util.PaginationHelper;
import pucrs.br.bean.UsuarioFacade;

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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.ws.rs.BeanParam;
import net.bootsfaces.utils.FacesMessages;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import pucrs.br.entity.Empresa;
import pucrs.br.entity.GrupoUsuario;

@Named("usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    private Usuario current;
    private Usuario logado;
    private Usuario newUser;
    private DataModel items = null;
    @EJB
    private pucrs.br.bean.UsuarioFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String menssagem = null;    

    public UsuarioController() {
    }

    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public Usuario getSelectedNew() {
        if (newUser == null) {
            newUser = new Usuario();
            selectedItemIndex = -1;
        }
        return newUser;
    }
    
    public Usuario getLogado() {
        if (logado == null) {
            logado = new Usuario();
            selectedItemIndex = -1;
        }
        return logado;
    }

    private UsuarioFacade getFacade() {
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
        recreateModel();
        FacesContext.getCurrentInstance().getExternalContext().redirect("ListUsuario.jsf");
    }

    public String prepareView() {
        current = (Usuario) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "ViewUsuario";
    }

    public String prepareCreate() {
        newUser = new Usuario();
        selectedItemIndex = -1;
        return "ListUsuario";
    }

    public String create() {
        try {
            if(getFacade().existeUsuario(newUser.getId())) {
                FacesMessage msg = new FacesMessage("Usuário ja existe");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                //newUser.setIdEmpresa(logado.getIdEmpresa());
                System.out.println("ID ->"+newUser.getId());
                System.out.println("Empresa ->"+newUser.getIdEmpresa());
                System.out.println("Grupo ->"+newUser.getIdGrupo());
                System.out.println("Nome ->"+newUser.getNome());
                System.out.println("Senha ->"+newUser.getSenha());
                getFacade().create(newUser);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
            }
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Usuario) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "EditUsuario";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
            return "ListUsuario";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Usuario) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "ListUsuario";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "ViewUsuario";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "ListUsuario";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioDeleted"));
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
        return "ListUsuario";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "ListUsuario";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Usuario getUsuario(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioController");
            return controller.getUsuario(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }
    }
    
    public String envia() {
        logado = ejbFacade.getUsuario(logado.getId(), logado.getSenha());
        if (logado == null) {
            logado = new Usuario();
            FacesMessages.error("Usuário ou senha inválidos.");
            return null;
        } else {
            return "home.jsf";
        }
    }
    
    public String login() {
        System.out.println(logado.getId());
        if (logado.getId() != null) {
            System.out.println("Aqui logado");
            return "usuarioLogado";
        }
        return "login";
    }
    
    public void logout() throws IOException {
        logado = null;
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.jsf");
    }
    
    public String inicio() {
        return "index";
    }
    
    public String admin() {
        return "admin";
    }

    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.menssagem = menssagem;
    }
    
    public void logado() throws IOException {
        setMenssagem(null);
        if (logado == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
        } else if (logado.getId() == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");    
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.jsf");
        }
    }
    
    public void redireciona() throws IOException {
        if(logado.getIdGrupo().getIdGrupo() == 1) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("admin.jsf");
        } else if(logado.getIdGrupo().getIdGrupo() == 2) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("gestor.jsf");
        } else if(logado.getIdGrupo().getIdGrupo() == 3) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("usuario.jsf");
        } 
    }
    
    public void onRowEdit(RowEditEvent event) {
        current = ((Usuario) event.getObject());
        update();
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Usuario Cancelled", ((Usuario) event.getObject()).getId());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
