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
import java.util.ArrayList;
import pucrs.br.dao.UsuarioDAO;
import pucrs.br.entity.Usuario;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import pucrs.br.entity.GrupoUsuario;

@ManagedBean(name = "LoginMB")
@SessionScoped
public class LoginManagedBean {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuario = new Usuario();
    private String menssagem = null;

    public String envia() {
        usuario = usuarioDAO.getUsuario(usuario.getId(), usuario.getSenha());
        if (usuario == null) {
            usuario = new Usuario();
            setMenssagem("Erro no Login!");
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não encontrado!",
                            "Erro no Login!"));
            return null;
        } else if(usuario.getIdGrupo().getIdGrupo() == 1) {
            System.out.println("Aqui 1"+usuario.getId());
            return "admin";
        } else if(usuario.getIdGrupo().getIdGrupo() == 2) {
            return "gestor";
        } else if(usuario.getIdGrupo().getIdGrupo() == 3) {
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
    
    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.menssagem = menssagem;
    }
    
    public String login() {
        System.out.println(usuario.getId());
        if (usuario.getId() != null) {
            System.out.println("Aqui logado");
            return "usuarioLogado";
        }
        return "login";
    }
    
    public String logout() {
        usuario = new Usuario();
        return "index";
    }
    
    public String inicio() {
        return "index";
    }
    
    public String admin() {
        return "admin";
    }
}
