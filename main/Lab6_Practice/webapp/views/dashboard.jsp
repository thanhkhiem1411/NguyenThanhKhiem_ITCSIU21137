<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Student Management</title>
    <style>
        body { margin:0; font-family: Arial, sans-serif; background:#f4f6f8; }
        .navbar {
            background:#2c3e50; color:#fff; padding:10px 20px;
            display:flex; justify-content:space-between; align-items:center;
        }
        .navbar-right a {
            margin-left:15px; color:#ecf0f1; text-decoration:none;
        }
        .role-badge {
            padding:3px 8px; border-radius:12px; font-size:0.8rem;
            margin-left:5px;
        }
        .role-admin { background:#e74c3c; }
        .role-user { background:#3498db; }
        .container { padding:20px; }
        .card {
            background:#fff; padding:20px; border-radius:10px;
            box-shadow:0 2px 6px rgba(0,0,0,0.1); margin-bottom:20px;
        }
        .stats-grid {
            display:grid; grid-template-columns:repeat(auto-fit, minmax(220px,1fr));
            gap:20px;
        }
        .btn {
            display:inline-block; padding:10px 15px; border-radius:5px;
            text-decoration:none; color:#fff;
        }
        .btn-primary { background:#3498db; }
        .btn-success { background:#27ae60; }
    </style>
</head>
<body>
<div class="navbar">
    <h2>Student Management System</h2>
    <div class="navbar-right">
        <span>Welcome, ${sessionScope.fullName}</span>
        <span class="role-badge role-${sessionScope.role}">
            ${sessionScope.role}
        </span>
        <a href="logout">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h1>${welcomeMessage}</h1>
        <p>Here's what's happening with your students today.</p>
    </div>

    <div class="stats-grid">
        <div class="card">
            <h3>${totalStudents}</h3>
            <p>Total Students</p>
        </div>
    </div>

    <div class="card">
        <a href="student?action=list" class="btn btn-primary">View All Students</a>
        <c:if test="${sessionScope.role eq 'admin'}">
            <a href="student?action=new" class="btn btn-success">➕ Add New Student</a>
        </c:if>
    </div>
</div>
</body>
</html>
