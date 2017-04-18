/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pucrs.br.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author psysvica
 */
public class ConnectMysql {

    private final String user = "tccpucrs_add1";
    private final String pass = "banco001";
    private final String dbClass = "com.mysql.jdbc.Driver";
    private final String dbDriver = "jdbc:mysql://mysql.tccpucrs.kinghost.net:3307/tccpucrs";
    private Connection conn = null;

    // Conectar ao banco
    public boolean getConexao() {
        boolean done = false;
        //load driver
        try {
            Class.forName(dbClass).newInstance();
            System.out.println("Driver Loaded"); // THIS IS BEING RETURNED
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.err.println(ex);
        }
        // Connection
        try {
            conn = DriverManager.getConnection(dbDriver, user, pass);
            System.out.println("Connected"); // THIS IS NOT BEING RETURNED
            done = true;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex);
        }
        return done;
    }

}
