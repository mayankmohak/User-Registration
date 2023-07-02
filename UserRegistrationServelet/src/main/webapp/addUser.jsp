<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add User</title>
</head>
<body>
	<form method="post" action="<%=request.getContextPath() %>/ServletAdd">
		<label> Enter Username </label> 
		<input type="text" name="name">
		<label>Enter email </label> 
		<input type="email" name="email"> 
		<label>Enter password </label> 
		<input type="text" name="pass">
		<button type="submit">Add User</button>
	</form>
</body>
</html>