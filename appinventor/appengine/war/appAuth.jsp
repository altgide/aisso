<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<link type="text/css" rel="stylesheet" href="static/css/login.css">
    <title>MIT App Inventor</title>
</head>
<body>
  <div class="container">
  <table>
   <tr>
   <td>
    <div class="image-container">
      <img src="/static/images/IMG-LOGIN_L.jpg" alt="Coding Platform">  
	</div>
	</td>
	<td>
	<div class="login-container">
		<center>
		<table>
		<tr><td>			
	</td></tr>
	<tr><td><font face="Arial Black" size=5>Coding Platform</font></td></tr>
    <tr><td><c:if test="${not empty errorMessage}">
        <p style="color: red">${errorMessage}</p>
    </c:if></td></tr>
	</table>
	</center>
    <form action="AppAuth" method="post">
	<center><table>
<tr><td>&nbsp;</td><td><input type=text name=email value="" placeholder="Enter email" style="font-size: 100%;height: 40px;width: 250px;"></td></tr>
<!--tr><td>Password</td><td><input type=password name=password value="" placeholder="Enter password" style="font-size: 100%;height: 40px;width: 250px;"></td></tr-->
</table></center>
<p></p>
<center>
<table>
<tr><td>&nbsp;</td><td>        
		<button type="submit">
          <img src="/static/images/IMG-LOGIN_BTNF.jpg" alt="Login" class="landing-image">
        </button>
</td></tr> 
</table>
</center>   
	</form>
	<center>
	<a href="termcon.jsp">Terms and Conditions</a>
	</center>  	
	</div>
	</td>
	</tr>
   </table>
  </div>
</body>
</html>