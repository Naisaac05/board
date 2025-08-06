
<%--
  Created by IntelliJ IDEA.
  User: JAVA
  Date: 2025-07-25
  Time: 오후 3:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript">
        function idok(id){
            opener.document.join.userid.value=id;
            opener.document.join.reid.value=id;
            self.close();
        }
    </script>
</head>
<body>
<form action="idcheck" style="margin: 20px 20px; display: flex; justify-content: center;">
    아이디:<input type="text" name="userid" value="${userid}" />
    <input type="submit" value="중복체크">
</form>

<div style="display: flex; justify-content: center;">
    <c:choose>
        <c:when test="${result eq '1'}">
            <script type="text/javascript">
                opener.document.join.userid.value="";
                opener.document.join.reid.value="";
            </script>
            ${userid}는 이미 사용중 입니다
        </c:when>
        <c:otherwise>
            ${userid}는 사용 가능 합니다
            <input type="button" value="사용하겠습니다" onclick="idok('${userid}')"/>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
