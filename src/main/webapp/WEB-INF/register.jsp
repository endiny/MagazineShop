<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Регистрация</title>
</head>
<body>
<div align=center>  

<h3>Вы регестрируетесь на курс: ${cour.name} ${cour.date}</h3> 
<h4> Пожалуйста введите свое имя и фамилию:</h4>
<form action="./Controller">
	<input type="hidden" name="command" value="REGISTER">
	<input type ="text" name="firstName" >
	<input type ="text" name="lastName" >
	<input type ="hidden" name="course" value="${cour.id}">
	<input type="submit" value="Зарегестрироваться"/>
	</form>
</div>	
</body>
</html>