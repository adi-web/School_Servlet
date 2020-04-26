/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.school_servlet;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mycompany.school_servlet.newpackage.pojo.Classi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.NativeArray.map;

/**
 *
 * @author admirportatile
 */
@WebServlet(name = "Get_Class", urlPatterns = {"/Get_Class"})
public class Get_Class extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();

    }

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
        //funziona
        try {
            out = response.getWriter();
            con = getConnection();
            String a = request.getParameter("id");
            if (a == null) {
                ps = con.prepareStatement("select * from class");
            } else {
                ps = con.prepareStatement("select * from class where  id= " + a + "order by yaer,section");
            }

            ResultSet rs = ps.executeQuery();

            List<Classi> elencoClassi = new ArrayList();
            while (rs.next()) {
                String year = rs.getString(2);
                String section = rs.getString(3);
                String id = rs.getString(1);
                Classi cl = new Classi();
                cl.setId(id);
                cl.setYear(year);
                cl.setSection(section);
                elencoClassi.add(cl);

            }

            Map<String, List<Classi>> user = new TreeMap<String, List<Classi>>();//utilizzato per mette classInfo
            user.put("classInfo", elencoClassi);//utilizzare anche per mettere status

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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)//funziona
            throws ServletException, IOException {

        List<Classi> elencoClassi = new ArrayList();
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

            Classi userArray = gson.fromJson(json, Classi.class);

            elencoClassi.add(userArray);

            ps = con.prepareStatement("insert into class(year,section)  VALUES(?,?)");
            ps.setString(1, userArray.getYear()); //inserisce sul primo punto interrogativo il valore

            ps.setString(2, userArray.getSection());
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

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Classi> elencoClassi = new ArrayList();
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

            Classi userArray = gson.fromJson(json, Classi.class);

            elencoClassi.add(userArray);

            String se = (String) userArray.getSection();
            ps = con.prepareStatement("UPDATE class SET year=?, section=? where id=?");
            ps.setString(1, userArray.getYear()); //inserisce sul primo punto interrogativo il valore

            ps.setString(2, userArray.getSection());
            ps.setString(3, userArray.getId());

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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {//funziona

        out = resp.getWriter();
        con = getConnection();
        String a = req.getParameter("id");
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        try {

            ps = con.prepareStatement("delete from class where id=" + a);
            ps.executeUpdate();
            ps = con.prepareStatement("delete from student_class where idClass=" + a);
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
