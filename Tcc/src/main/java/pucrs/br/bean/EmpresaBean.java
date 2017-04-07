/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pucrs.br.dao.EmpresaDAO;
import pucrs.br.entity.Empresa;
/**
 *
 * @author psysvica
 */
@ManagedBean(name = "EmpresaMB")
@SessionScoped
public class EmpresaBean {
    
    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private Empresa empresa = new Empresa();
    private String menssagem = null;

    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.menssagem = menssagem;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
   
    public void criar() {
        System.out.println(empresa.getNome());
        if (empresaDAO.inserirEmpresa(empresa)) {
            setMenssagem("Empresa criada!");
        } else {
            setMenssagem("Empresa já existe!");
        }
    }
}
