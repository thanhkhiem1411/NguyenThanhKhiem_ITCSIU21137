<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Student Management</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #2c3e50, #3498db);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-container {
            background: #fff;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            width: 350px;
        }
        .login-header h1 {
            margin: 0;
        }
        .login-header p {
            margin: 5px 0 20px;
            color: #777;
        }
        .input-group { margin-bottom: 15px; }
        .input-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .input-group input {
            width: 100%;
            padding: 8px 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
            outline: none;
        }
        .input-group input:focus {
            border-color: #3498db;
        }
        .btn-login {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 5px;
            background: #3498db;
            color: #fff;
            font-weight: bold;
            cursor: pointer;
        }
        .btn-login:hover { background: #2980b9; }
        .alert {
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 15px;
            font-size: 0.9rem;
        }
        .alert-error { background: #ffe5e5; color: #c0392b; }
        .alert-success { background: #e5ffe9; color: #27ae60; }
        .demo-credentials {
            margin-top: 15px;
            font-size: 0.9rem;
            color: #555;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="login-header">
        <h1>Login</h1>
        <p>Student Management System</p>
    </div>

    <!-- Error message -->
    <c:if test="${not empty error}">
        <div class="alert alert-error">
            ❌ ${error}
        </div>
    </c:if>

    <!-- Success message (logout, ...) -->
    <c:if test="${not empty param.message}">
        <div class="alert alert-success">
            ✅ ${param.message}
        </div>
    </c:if>

    <form action="login" method="post">
        <div class="input-group">
            <label for="username">Username</label>
            <input id="username" name="username"
                   value="${username != null ? username : ''}" />
        </div>
        <div class="input-group">
            <label for="password">Password</label>
            <input id="password" name="password" type="password" />
        </div>

        <button type="submit" class="btn-login">Login</button>
    </form>

    <div class="demo-credentials">
        <h4>Demo Credentials:</h4>
        <p><strong>Admin:</strong> admin / password123</p>
        <p><strong>User:</strong> john / password123</p>
    </div>
</div>
</body>
</html>
