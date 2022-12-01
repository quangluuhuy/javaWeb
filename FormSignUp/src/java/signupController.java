
import DAL.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ADMIN
 */
public class signupController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.sendRedirect("SignUp.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String first = req.getParameter("first_name");
       String last = req.getParameter("last_name");
       String phone = req.getParameter("phone");
       String pass = req.getParameter("pass");
       String regexPass = "[0-9][A-Za-z]";
       String regexName = "[a-zA-Z0-9]+";
       String regexPhone = "[0-9]{10}";
       String messName = "";
       String messPass = "";
       String messPhone = "";
       if(!(first.matches(regexName)) || !(last.matches(regexName))){
           messName = "Name must be not contain special character";
           req.setAttribute("messName", messName);
       }
       if(!phone.matches(regexPhone)){
           messPhone = "Phone must be 10 digit";
           req.setAttribute("messPhone", messPhone);
       }
       if(pass.length() < 8){
           messPass = "Password must be 8 or more and contain character and number";
           req.setAttribute("messPass", messPass);
       }
       if(messName == "" && messPass == "" && messPhone == ""){
           Account acc = new Account(phone, pass);
           HttpSession session = req.getSession();
           session.setAttribute("acc", acc);
           resp.getWriter().print(acc);
       }else{
           req.getRequestDispatcher("SignUp.jsp").forward(req, resp);
       }
       
    }
    
    
    
}
