<%--
  Created by IntelliJ IDEA.
  User: JAVA
  Date: 2025-07-25
  Time: 오후 2:54
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
    <h2>Board List</h2>
    <div class="logininfo">
        <div class="user">
            ${loginUser.userid}(${loginUser.name})님이 로그인하셨습니다
            <input type="button" value="회원정보수정" onClick="location.href='updateMemberForm'" />
            <input type="button" value="LogOut" onClick="location.href='logout'" />
            <input type="button" value="회원탈퇴" onClick="deleteMember('${loginUser.userid}')"/>
        </div>
        <div class="writebutton">
            <input type="button" value="게시글 등록" onClick="location.href='insertBoardForm'"/>
        </div>
    </div>

    <div class="board">
        <div class="title_row">
            <div class="title_col">번호</div>
            <div class="title_col">제목</div>
            <div class="title_col">작성자</div>
            <div class="title_col">작성일</div>
            <div class="title_col">조회수</div>
        </div>
    </div>
    <c:forEach items="${boardList}" var="board">
        <div class="row">
            <div class="col">${board.num}</div>
            <div class="col">
                <a href="boardView?num=${board.num}" style="text-decoration:none">
                    ${board.title}
                </a>
                <c:if test="${board.replycnt>0}">
                    <span style="color: red; font-weight: bold">[${board.replycnt}]</span>
                </c:if>
                <c:if test="${not empty board.savefilename}">
                    <span style="color: blue; font-weight: bold; font-size: 90%">[img]</span>
                </c:if>

            </div>
            <div class="col">${board.userid}</div>
            <div class="col"><fmt:formatDate value="${board.writedate}"/></div>
            <div class="col">${board.readcount}</div>
        </div>
    </c:forEach>

    <div class="paging">
        <c:if test="${paging.prev}">
            <a href="main?page=${paging.beginPage-1}">◀</a>&nbsp;
        </c:if>
        <c:forEach begin="${paging.beginPage}" end="${paging.endPage}" var="index">
            <a href="main?page=${index}">&nbsp;${index}&nbsp;</a>
        </c:forEach>
        <c:if test="${paging.next}">
            <a href="main?page=${paging.endPage+1}">▶</a>&nbsp;
        </c:if>
    </div>



</div>



</body>
</html>
