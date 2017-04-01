/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.tcc.principal;
import pucrs.tcc.controller.ConnectMysql;
/**
 *
 * @author psysvica
 */
public class main {
    
    public static void main(String[] args) {
        ConnectMysql conn = new ConnectMysql();
        conn.getConexao();
    }
    
}
