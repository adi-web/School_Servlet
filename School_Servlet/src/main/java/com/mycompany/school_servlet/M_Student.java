/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.school_servlet;

import com.google.gson.Gson;
import com.mycompany.school_servlet.newpackage.pojo.Classi;
import com.mycompany.school_servlet.newpackage.pojo.Student;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author admirportatile
 */
@WebServlet(name = "M_Student", urlPatterns = {"/M_Student"})
public class M_Student extends HttpServlet {

    PrintWriter out;
    PreparedStatement ps;
    Connection con;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

        try {
            out = response.getWriter();
            con = getConnection();

            ps = con.prepareStatement("SELECT student.* FROM student inner join student_class on idStudent=student.id where idClass=?");
            ps.setString(1, request.getParameter("idClass"));

            ResultSet rs = ps.executeQuery();

            List<Student> elencoAlunni = new ArrayList();
            while (rs.next()) {

                Student student = new Student();
                student.setId(rs.getString(1));
                student.setName(rs.getString(2));
                student.setSurname(rs.getString(3));
                student.setSidiCode(rs.getString(4));
                student.setTaxCode(rs.getString(5));
                elencoAlunni.add(student);

            }

            Map<String, List<Student>> user = new TreeMap<String, List<Student>>();//utilizzato per mette classInfo
            user.put("studentInfo", elencoAlunni);//utilizzare anche per mettere status

            String ret = new Gson().toJson(user);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(ret);
            out.flush();
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            //out.close();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();

        try {
            out = resp.getWriter();
            con = getConnection();

            ps = con.prepareStatement("DELETE FROM student_class WHERE id=? ");
            ps.setString(1, req.getParameter("idStudent"));

            ps.executeUpdate();

            ps = con.prepareStatement("DELETE FROM student WHERE id=?");
            ps.setString(1, req.getParameter("idStudent"));

            ps.executeUpdate();

            map.put("status", Boolean.TRUE);

            String ret = new Gson().toJson(map);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(ret);
            out.flush();

        } catch (SQLException ex) {
            map.put("status", Boolean.FALSE);
            System.out.println(ex);
        }

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) //ancora da completare
            throws ServletException, IOException {

        List<Student> elencoAlunni = new ArrayList();
        out = response.getWriter();
        con = getConnection();
        HashMap<String, String> map = new HashMap<String, String>();

        Gson gson = new Gson();
        BufferedReader br
                = new BufferedReader(new InputStreamReader(request.getInputStream()));

        String json = "";
        if (br != null) {

            json = br.readLine();
        }

        // System.out.println(json);
        try {

            Student userArray = gson.fromJson(json, Student.class);

            elencoAlunni.add(userArray);

            ps = con.prepareStatement("insert into student(name, surname, sidiCode, taxCode)  VALUES(?,?,?,?)");
            ps.setString(1, userArray.getName()); //inserisce sul primo punto interrogativo il valore

            ps.setString(2, userArray.getSurname());
            ps.setString(3, userArray.getSidiCode());
            ps.setString(4, userArray.getTaxCode());
            ps.executeUpdate();//aggiorna il data base

            String id = ultimoId();

            map.put("studentInfo", id);

            String ret = new Gson().toJson(map);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(ret);
            out.flush();

        } catch (SQLException ex) {

            System.out.println(ex);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Student> elencoAlunni = new ArrayList();
        out = resp.getWriter();
        con = getConnection();
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();

        Gson gson = new Gson();
        BufferedReader br
                = new BufferedReader(new InputStreamReader(req.getInputStream()));

        String json = "";
        if (br != null) {

            json = br.readLine();
        }

        // System.out.println(json);
        try {

            Student userArray = gson.fromJson(json, Student.class);

            elencoAlunni.add(userArray);

            ps = con.prepareStatement("UPDATE student SET name=?,surname=?,sidiCode=?,taxCode=? WHERE id=?");
            ps.setString(1, userArray.getName()); //inserisce sul primo punto interrogativo il valore

            ps.setString(2, userArray.getSurname());
            ps.setString(3, userArray.getSidiCode());
            ps.setString(4, userArray.getTaxCode());
            ps.setString(5, userArray.getId());

            ps.executeUpdate();//aggiorna il data base

            map.put("status", Boolean.TRUE);

            String ret = new Gson().toJson(map);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(ret);
            out.flush();

        } catch (SQLException ex) {
            map.put("status", Boolean.FALSE);
            System.out.println(ex);
        }

    }

    public String ultimoId() throws SQLException {
        ps = con.prepareStatement("SELECT max(id) as 'id' FROM student");

        ResultSet rs = ps.executeQuery();

        List<Student> elencoAlunni = new ArrayList();
        while (rs.next()) {

            return rs.getString(1);

        }
        return null;

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
