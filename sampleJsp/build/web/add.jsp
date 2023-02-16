
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <%! String sn1,sn2;int n1,n2,n3,n4,n5,n6; %>
    <%
       sn1 = request.getParameter("n1").trim();
       sn2 = request.getParameter("n2").trim();
     
       n1 = Integer.parseInt(sn1);
       n2 = Integer.parseInt(sn2);
       n3= n1+n2;
       n4 = n1-n2;
       n5 = n1*n2;
       n6 = n1 / n2;
       
    %>    
    <body>
        
        <form action="#" method="post">
            a=
            <input type="text" name="n1"value="<%= n1 %>" />    
            <p>b=
                <input type="text" name="n2"value=" <%= n2 %>"/>   
            <p>a+b=
                <input type="text" name="n3" value=" <%= n3 %>"/>   
            <p> a-b=
                <input type="text" name="n4" value=" <%= n4 %>"/> 
                <p> a*b=
                <input type="text" name="n4" value=" <%= n5 %>"/> 
                <p> a:b=
                <input type="text" name="n4" value=" <%= n6 %>"/> 

            <p><input type="Submit" value="DO">
        </form> 
    </body>
</html>
