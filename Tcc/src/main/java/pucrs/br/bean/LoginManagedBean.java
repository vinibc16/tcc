/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

/**
 *
 * @author psysvica
 */
import pucrs.br.dao.UsuarioDAO;
import javax.faces.bean.ViewScoped;
import pucrs.br.entity.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "LoginMB")
@ViewScoped
public class LoginManagedBean {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuario = new Usuario();

    public String envia() {
        usuario = usuarioDAO.getUsuario(usuario.getId(), usuario.getSenha());
        if (usuario == null) {
            usuario = new Usuario();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
                            "Erro no Login!"));
            return null;
        } else if(usuario.getIdGrupo() == 1) {
            System.out.println("Aqui 1");
            return "admin";
        } else if(usuario.getIdGrupo() == 2) {
            return "gestor";
        } else if(usuario.getIdGrupo() == 3) {
            return "usuario";
        } else {
            System.out.println("Aqui 4");
            return null;
        }

    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String login() {
        return "login.jsf";
    }
}
