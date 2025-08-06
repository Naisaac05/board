<%--
  Created by IntelliJ IDEA.
  User: JAVA
  Date: 2025-07-28
  Time: 오후 4:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="/css/board.css">
    <script src="/script/board.js"></script>
</head>
<body>

<div id="main_container">
    <h2>게시글 등록</h2>
    <div class="board">
        <form class="insertBoard" method="post" name="insertBoard" action="insertBoard">
            <div class="field">
                <label>작성자</label><input type="text" name="userid" value="${loginUser.userid}" readonly/>
            </div>
            <div class="field">
                <label>비밀번호</label><input type="password" name="pass" />
            </div>
            <div class="field">
                <label>이메일</label><input type="text" name="email" value="${loginUser.email}" />
            </div>
            <div class="field">
                <label>제목</label><input type="text" name="title" value="${dto.title}" />
            </div>
            <div class="field">
                <label>내용</label><textarea name="content" rows="10" cols="100">${dto.content}"</textarea>
            </div>

            <div class="field">
                <label>이미지</label><input type="button" value="이미지 선택" onclick="selectImg()"/>
            </div>
            <div class="field">
                <label>이미지 미리보기</label>
                <c:choose>
                    <c:when test="${empty dto.savefilename}">
                        <div style="flex:4">
                            <img src="" id="previewimg" width="150" style="display: none"/>
                        </div>
                        <input type="hidden" name="image" />
                        <input type="hidden" name="savefilename" />
                    </c:when>
                    <c:otherwise>
                        <div style="flex:4">
                            <img src="/images/${dto.savefilename}" id="previewimg" width="150" />
                        </div>
                        <input type="hidden" name="image" value="${dto.image}"/>
                        <input type="hidden" name="savefilename" value="${dto.savefilename}"/>
                    </c:otherwise>
                </c:choose>

            </div>
            <div class="field">
                <input type="submit" value="작성완료"/>
                <input type="button" value="돌아가기" onclick="location.href='main'"/>
            </div>
            <div class="field">
                ${msg}
            </div>
        </form>
    </div>
</div>

</body>
</html>
