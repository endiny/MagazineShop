<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Участники курса</title>
</head>
<body>
<div align=center>
<h3>Выберите студента для оценивания:</h3>
<table cellspacing="1" cellpadding="10" border="1">
<tr><th>Student</th><th>Grade</th><th>Comment</th></tr>
<c:forEach var="p" items="${pList}">
<tr><td> <a href="./Controller?command=GRADE_PARTICIPIANT&id=${p.id}">${p.student}</a></td><td>${p.grade}</td><td>${p.comment}</td></tr>
</c:forEach>
</table>	
</div>
</body>
</html>