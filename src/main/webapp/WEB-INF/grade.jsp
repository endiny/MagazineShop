<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Страница оценки</title>
</head>
<body>
<div align=center>
<h3>Оценить студента: ${stud.student.firstName} ${stud.student.lastName}</h3>
<form action="./Controller" method="POST">
	<input type="hidden" name="command" value="WRITE_COMMENT">
	<input type="hidden" name="id" value="${stud.id}">
	<b>Оценка:</b><input type ="text" name="grade" value="${stud.grade}">
	<b>Комментарий:</b><input type ="text" name="comment" value="${stud.comment}" > 
	<input type="submit" value="Записать"/>
	</form>
</div>	
</body>
</html>