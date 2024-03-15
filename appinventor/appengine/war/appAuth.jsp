<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MIT App Inventor</title>
</head>
<body>
	<center>
	<table>
	<tr><td>
	<center>
	<img class="img-scale"
                src="/static/images/digixedu.jpg" alt="Digixedu" width="150" height="150">
	</center>			
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
<tr><td>&nbsp;</td><td><input type=Submit value="Login" style="background: #33B9FF;
color: white;
border-style: outset;
border-color: #33B9FF;
font-size: 200%; height: 40px; width: 250px; left: 150; top: 150;">
</td></tr> 
</table>
</center>   
	</form>
</body>
</html>