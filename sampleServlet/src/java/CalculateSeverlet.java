/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author admin
 */
public class CalculateSeverlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String cal(double num1, double num2, String o) {
        String rs = "";
        switch (o) {
            case "+":
                rs = num1 + "+" + num2 + "=" + (num1 + num2);
                break;
                case "-":
                rs = num1 + "-" + num2 + "=" + (num1 - num2);
                break;
                case "x":
                rs = num1 + "x" + num2 + "=" + (num1* num2);
                break;
                case ":":
                    if(num2==0){
                        rs="khong chia duoc cho 0";
                    }else{
                rs = num1 + "+" + num2 + "=" + (num1 + num2);
                break;
                    }

        }
        return rs;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CalculateSeverlet</title>");
            out.println("</head>");
            out.println("<body>");
            String n1 = request.getParameter("num1");
            String n2 = request.getParameter("num2");
            String o = request.getParameter("op");
            double num1, num2;
            try {
                num1 = Double.parseDouble(n1);
                num2 = Double.parseDouble(n2);

                String rs = cal(num1, num2, o);
                out.println("<h1> " + rs + "</h1>");
            } catch (NumberFormatException e) {
                response.sendRedirect("index.html");
            }
            
            out.println("</html>");
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
        processRequest(request, response);
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
