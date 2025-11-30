<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${student != null && student.id > 0}">Edit Student</c:when>
            <c:otherwise>Add New Student</c:otherwise>
        </c:choose>
    </title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background-color: #f7fafc;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 650px;
            margin: 40px auto;
            background: #ffffff;
            padding: 24px 32px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(15, 23, 42, 0.12);
        }
        h2 {
            margin-top: 0;
            margin-bottom: 20px;
            color: #1a202c;
            font-size: 24px;
        }
        .form-group {
            margin-bottom: 16px;
        }
        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
            color: #4a5568;
        }
        input[type="text"],
        input[type="email"],
        select {
            width: 100%;
            padding: 9px 11px;
            border: 1px solid #cbd5e0;
            border-radius: 6px;
            box-sizing: border-box;
            font-size: 14px;
        }
        input[type="file"] {
            margin-top: 6px;
            font-size: 14px;
        }
        input:focus, select:focus {
            outline: none;
            border-color: #3182ce;
            box-shadow: 0 0 0 1px rgba(49,130,206,0.4);
        }
        .actions {
            margin-top: 22px;
            display: flex;
            gap: 10px;
        }
        .btn-primary {
            background-color: #3182ce;
            border: none;
            color: white;
            padding: 9px 18px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-primary:hover {
            background-color: #2b6cb0;
        }
        .btn-secondary {
            background-color: #e2e8f0;
            color: #2d3748;
            padding: 9px 18px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 14px;
        }
        .btn-secondary:hover {
            background-color: #cbd5e0;
        }
        .error {
            color: #e53e3e;
            font-size: 13px;
            display: block;
            margin-top: 4px;
        }
        .photo-preview img {
            max-height: 90px;
            margin-top: 8px;
            border-radius: 6px;
            border: 1px solid #e2e8f0;
        }
        small {
            color: #718096;
            display: block;
            margin-top: 3px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>
        <c:choose>
            <c:when test="${student != null && student.id > 0}">Edit Student</c:when>
            <c:otherwise>Add New Student</c:otherwise>
        </c:choose>
    </h2>

    <form action="student" method="post" enctype="multipart/form-data">
        <!-- insert / update -->
        <input type="hidden" name="action"
               value="${student != null && student.id > 0 ? 'update' : 'insert'}"/>

        <!-- id + existing photo (edit) -->
        <c:if test="${student != null && student.id > 0}">
            <input type="hidden" name="id" value="${student.id}"/>
            <input type="hidden" name="existingPhoto" value="${student.photo}"/>
        </c:if>

        <!-- Student Code -->
        <div class="form-group">
            <label for="studentCode">Student Code:</label>
            <input type="text"
                   id="studentCode"
                   name="studentCode"
                   value="${student.studentCode}"
                   ${student != null && student.id > 0 ? "readonly" : "required"} />
            <small>Format: 2 letters + 3+ digits (e.g., SV001, IT123)</small>
            <c:if test="${not empty errorCode}">
                <span class="error">${errorCode}</span>
            </c:if>
        </div>

        <!-- Full Name -->
        <div class="form-group">
            <label for="fullName">Full Name:</label>
            <input type="text"
                   id="fullName"
                   name="fullName"
                   value="${student.fullName}"
                   required />
            <c:if test="${not empty errorName}">
                <span class="error">${errorName}</span>
            </c:if>
        </div>

        <!-- Email (optional) -->
        <div class="form-group">
            <label for="email">Email (optional):</label>
            <input type="text"
                   id="email"
                   name="email"
                   value="${student.email}" />
            <c:if test="${not empty errorEmail}">
                <span class="error">${errorEmail}</span>
            </c:if>
        </div>

        <!-- Major: TEXT INPUT (không còn select) -->
        <div class="form-group">
            <label for="major">Major:</label>
            <input type="text"
                   id="major"
                   name="major"
                   placeholder="e.g., Computer Science"
                   value="${student.major}"
                   required />
            <c:if test="${not empty errorMajor}">
                <span class="error">${errorMajor}</span>
            </c:if>
        </div>

        <!-- Photo (Bonus 2) -->
        <div class="form-group">
            <label for="photo">Photo (optional):</label>
            <input type="file" id="photo" name="photo" accept="image/*" />
            <small>Only image files are allowed.</small>

            <c:if test="${student != null && not empty student.photo}">
                <div class="photo-preview">
                    <span>Current photo:</span><br/>
                    <img src="${pageContext.request.contextPath}/uploads/${student.photo}" alt="Student Photo"/>
                </div>
            </c:if>
        </div>

        <div class="actions">
            <button type="submit" class="btn-primary">
                <c:choose>
                    <c:when test="${student != null && student.id > 0}">Update</c:when>
                    <c:otherwise>Save</c:otherwise>
                </c:choose>
            </button>
            <a href="student?action=list" class="btn-secondary">Cancel</a>
        </div>
    </form>
</div>
</body>
</html>

