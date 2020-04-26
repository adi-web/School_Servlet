/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.school_servlet;

import com.google.gson.Gson;
import com.mycompany.school_servlet.newpackage.pojo.Class_Student;
import com.mycompany.school_servlet.newpackage.pojo.Classi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author admirportatile
 */
@WebServlet(name = "M_Class_Student", urlPatterns = {"/M_Class_Student"})
public class M_Class_Student extends HttpServlet {

    PrintWriter out;
    PreparedStatement ps;
    Connection con;

    private Connection getConnection() {
        try {
            String dbDriver = "com.mysql.jdbc.Driver";
            String dbURL = "jdbc:mysql:// localhost:3306/";
            // Database name to access 
            String dbName = "fi_itis_meucci ";
            String dbUsername = "mucaj";
            String dbPassword = "mucaj12345678";

            Class.forName(dbDriver);
            Connection con = DriverManager.getConnection(dbURL + dbName + "?serverTimezone=UTC",
                    dbUsername,
                    dbPassword);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("errore");
            return null;
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Class_Student> elencoC_S = new ArrayList();
        out = response.getWriter();
        con = getConnection();
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();

        Gson gson = new Gson();
        BufferedReader br
                = new BufferedReader(new InputStreamReader(request.getInputStream()));

        String json = "";
        if (br != null) {

            json = br.readLine();
        }

        // System.out.println(json);
        try {

            Class_Student userArray = gson.fromJson(json, Class_Student.class);

            elencoC_S.add(userArray);

            ps = con.prepareStatement("insert into student_class(idStudent,idClass)  VALUES(?,?)");
            ps.setString(2, userArray.getIdClass()); //inserisce sul primo punto interrogativo il valore

            ps.setString(1, userArray.getIdStudent());
            ps.executeUpdate();//aggiorna il data base

            map.put("status", Boolean.TRUE);

            String ret = new Gson().toJson(map);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(ret);
            out.flush();

        } catch (SQLException ex) {
            map.put("status", Boolean.FALSE);
            System.out.println(ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
