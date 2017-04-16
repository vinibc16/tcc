/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import java.util.ArrayList;
import java.util.List;
import pucrs.br.dao.UsuarioDAO;
import pucrs.br.entity.Usuario;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pucrs.br.entity.GrupoUsuario;

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

    public List<GrupoUsuario> getUsuarios() {
        return usuarioDAO.getGrupos();
    }

    public void criar() {
        System.out.println(usuario.getId());
        if (usuarioDAO.inserirUsuario(usuario)) {
            setMenssagem("Usuario Criado!");
        } else {
            setMenssagem("Usuario já existe!");
        }

    }
}
