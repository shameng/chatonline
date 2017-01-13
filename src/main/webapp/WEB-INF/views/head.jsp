<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
欢迎你，<font class="userName"><shiro:principal property="name"></shiro:principal></font>&nbsp;&nbsp;
[<a href="logout">注销</a>]
