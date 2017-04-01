/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.tcc.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author psysvica
 */
public class Connection {
    
    private static final String USUARIO = "tccpucrs_add1";
    private static final String SENHA = "banco001";
    private static final String URL = "jdbc:mysql://mysql.tccpucrs.kinghost.net:3306/tccpucrs";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public static String status = "Não conectou...";
    // Conectar ao banco
    public static java.sql.Connection abrir() throws Exception {
        Connection connection = null;          //atributo do tipo Connection
        try {
            String driverName = "com.mysql.jdbc.Driver";
            Class.forName(driverName);
            String serverName = "";
            String mydatabase ="";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
            String username = "";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
            //Testa sua conexão//  
            if (connection != null) {
                status = ("STATUS--->Conectado com sucesso!");
            } else {
                status = ("STATUS--->Não foi possivel realizar conexão");
            }
            return connection;
        } catch (ClassNotFoundException e) {  
            System.out.println("O driver do banco de dados nao foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Nao foi possivel conectar ao Banco de Dados.");
            return null;
        }
    }
    
}
