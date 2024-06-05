<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap" rel="stylesheet">
	<link type="text/css" rel="stylesheet" href="static/css/login.css">
    <title>MIT App Inventor</title>
</head>
<body>
  <!--div class="container"-->
  <div class="left">
          <div>
            <img src="/static/images/Logo_Slogan.png" alt="Logo and Slogan" class="logo-slogan">
        </div>
  <!--table style="width:100%; height:100%" border=1>
   <tr>
   <td>

	</td>
	</tr>
	</table-->

	</div>
	
	<div class="login-container half">
	<table style="width:100%; height:100%">
	<tr>
	<td>
	<center>
	<!--table border=1>
	<tr><td><h1>Coding Platform</h1></td></tr>
    <tr><td><c:if test="${not empty errorMessage}">
        <p style="color: red">${errorMessage}</p>
    </c:if></td></tr>
	</table-->
	</center>
	</td>
	</tr>

	<tr>
	<td>
		<center>
    <form action="AppAuth" method="post">

	<table>
	<tr><td><div class="login-form"><h1>Coding Platform</h1></td></tr>
	<tr><td><c:if test="${not empty errorMessage}">
        <p style="color: red">${errorMessage}</p>
    </c:if></td></tr>
	<tr><td><label for="email">Email:</label></td></tr>
<tr><td><div class="input-wrapper"><input type=text class="email" name=email id="email" value="" placeholder="Enter email here" style="font-size: 100%;height: 40px;width: 280px;"></div></td></tr>
<!--tr><td>Password</td><td><input type=password name=password value="" placeholder="Enter password" style="font-size: 100%;height: 40px;width: 250px;"></td></tr-->
</table>


<p></p>
    
<table >
<tr><td>&nbsp;</td><td>       
		<button type="submit">
          <img src="/static/images/Untitled.png" alt="Login" class="landing-image">
        </button>

</td></tr> 
</table>
  
	</form>
		</center>
	</td>
	</tr>
	<tr>
	<td>
	<center>
	<a href="termcon.jsp">Terms and Conditions</a>
	</center>
	</td>
	</tr>
	</table>  	
	</div>
</body>
</html>