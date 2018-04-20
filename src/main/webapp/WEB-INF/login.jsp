<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Вход</title>
</head>
<body>
<div align=center>
<h3>Вход для преподователей:</h3>
<h3>${message}</h3>
<form action="./Controller" method="POST">
	<input type="hidden" name="command" value="GET_COURSE_LIST_BY_LECTOR">
	<b>ID</b><input type ="text" name="id" >
	<b>Пароль</b><input type ="password" name="pass" > 
	<input type="submit" value="Войти"/>
	</form>
</div>
</body>
</html>