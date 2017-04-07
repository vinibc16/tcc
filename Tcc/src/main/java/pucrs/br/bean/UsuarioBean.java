/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import pucrs.br.dao.UsuarioDAO;
import pucrs.br.entity.Usuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
/**
 *
 * @author psysvica
 */
@ManagedBean(name = "UsuarioMB")
@SessionScoped
public class UsuarioBean {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuario = new Usuario();
    private String menssagem = null;

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
    
   
    public void criar() {
        usuarioDAO.inserirUsuario(usuario);
    }
}
