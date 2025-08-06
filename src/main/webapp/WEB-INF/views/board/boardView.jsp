<%--
  Created by IntelliJ IDEA.
  User: JAVA
  Date: 2025-07-28
  Time: 오후 12:36
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
  <h2>게시글 상세 보기</h2>
  <div class="board">
    <div class="field">
      <div class="label">작성자</div><div class="text">${board.userid}</div>
    </div>
    <div class="field">
      <div class="label">이메일</div><div class="text">${board.email}</div>
    </div>
    <div class="field">
      <div class="label">조회수</div><div class="text">${board.readcount}</div>
    </div>
    <div class="field">
      <div class="label">작성일</div><div class="text">
      <fmt:formatDate value="${board.writedate}"/>
      </div>
    </div>
    <div class="field">
      <div class="label">제목</div><div class="text">${board.title}</div>
    </div>
    <div class="field">
      <div class="label">내용</div><div class="text"><pre>${board.content}</pre></div>
    </div>

    <div class="field">
      <div class="label">이미지</div>
      <div class="text">
        <c:choose>
          <c:when test="${empty board.savefilename}">
            <img src="/images/noname.jpg" width="250"/><br>
            이미지가 없습니다
          </c:when>
          <c:otherwise>
            <img src="/images/${board.savefilename}" width="350"/><br />
            ${board.image}
          </c:otherwise>
        </c:choose>
      </div>
    </div>
    <div class="login-button">
      <input type="button" value="수정" onClick="location.href='updateBoardForm?num=${board.num}'"/>
      <input type="button" value="삭제" onClick="deleteBoard('${board.num}')"/>
      <input type="button" value="패스워드 수정" onClick="updatePass('${board.num}')"/>
      <input type="button" value="목록으로" onClick="location.href='main'"/>
    </div>
  </div>

  <div class="reply">
    <div class="reply_row">
      <div class="reply_col reply_title">작성자</div>
      <div class="reply_col reply_title">작성일시</div>
      <div class="reply_col reply_title" style="text-align:center">댓글</div>
      <div class="reply_col reply_title">작성/삭제</div>
    </div>

    <form action="addReply" method="post" name="addRep">
      <input type="hidden" name="boardnum" value="${board.num}" />
      <input type="hidden" name="userid" value="${loginUser.userid}" />
      <div class="reply_row">
        <div class="reply_col">${loginUser.userid}</div>
        <div class="reply_col">
          <c:set var="now" value="<%=new java.util.Date()%>"/>
          <fmt:formatDate value="${now}" pattern="MM/dd hh:mm"/>
        </div>
        <div class="reply_col"><input type="text" name="content" size="60"></div>
        <div class="reply_col">
          <input type="submit" value="댓글작성" onclick="return replyCheck();">
        </div>
      </div>
    </form>
    <c:choose>
      <c:when test="${replyList.size()==0}">
        <div class="reply_col">댓글이 하나도 없습니다. 댓글을 남겨서 작성자를 응원해주세요</div>
      </c:when>
      <c:otherwise>
        <c:forEach items="${replyList}" var="reply">
          <div class="reply_row">
            <div class="reply_col">${reply.userid}</div>
            <div class="reply_col"><fmt:formatDate value="${reply.writedate}" pattern="MM/dd hh:mm"/></div>
            <div class="reply_col">${reply.content}</div>
            <div class="reply_col">
                <c:if test="${reply.userid==loginUser.userid}">
                  <input type="button" value="삭제" onClick="deleteReply('${reply.replynum}','${board.num}')" />
                </c:if>
            </div>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </div>
</div>
</body>
</html>
