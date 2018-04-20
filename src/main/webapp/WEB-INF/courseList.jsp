<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Выбор курса</title>
</head>
<body>
<div align=center>
<h3>Выберите курс для регистрации:</h3>

<table cellspacing="1" cellpadding="10" border="1">
<tr><th>Name</th><th>Date</th><th>Lecturer</th></tr>
<c:forEach var="course" items="${result}">
<tr><td> <a href="./Controller?command=CHOOSE&id=${course.id}">${course.name}</a></td><td>${course.date}</td><td>${course.lecturer}</td></tr>
</c:forEach>
</table>
</div>
</body>
</html>