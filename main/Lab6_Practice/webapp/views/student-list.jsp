<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Student Management (MVC)</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background-color: #ffffff; /* full trắng */
            margin: 0;
            padding: 0;
        }
        .container {
            width: 100%;
            max-width: 100%;
            margin: 0;
            padding: 18px 24px 24px;
            box-sizing: border-box;
            background: #ffffff;
        }

        /* Title row */
        .title-row {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 10px;
        }
        .title-icon {
            font-size: 22px;
        }
        h2 {
            margin: 0;
            font-size: 22px;
            color: #1a202c;
            font-weight: 700;
        }

        /* Buttons & search row */
        .actions-search-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 8px;
        }
        .actions-left {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        /* Filter row */
        .filter-row {
            margin-bottom: 10px;
        }
        .filter-row form {
            display: flex;
            align-items: center;
            gap: 6px;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 4px;
            text-decoration: none;
            font-size: 13px;
        }
        .btn-primary {
            background-color: #3182ce;
            color: white;
        }
        .btn-primary:hover { background-color: #2b6cb0; }

        .btn-secondary {
            background-color: #e2e8f0;
            color: #2d3748;
        }
        .btn-secondary:hover { background-color: #cbd5e0; }

        .btn-export {
            background-color: #38a169;
            color: white;
        }
        .btn-export:hover { background-color: #2f855a; }

        .btn-danger {
            background-color: #e53e3e;
            color: white;
        }
        .btn-danger:hover { background-color: #c53030; }

        .btn-small {
            padding: 3px 8px;
            font-size: 12px;
        }

        .message {
            margin-bottom: 8px;
            padding: 8px 10px;
            border-radius: 4px;
            font-size: 14px;
        }
        .message.success {
            background-color: #c6f6d5;
            color: #22543d;
        }
        .message.error {
            background-color: #fed7d7;
            color: #742a2a;
        }

        label {
            font-size: 14px;
            color: #4a5568;
        }
        input[type="text"], select {
            padding: 6px 8px;
            font-size: 13px;
            border-radius: 4px;
            border: 1px solid #cbd5e0;
        }

        /* Table style: full grid + header bold */
        table {
            width: 100%;
            border-collapse: collapse;
            border: 1px solid #e2e8f0;   /* viền ngoài */
            margin-top: 6px;
        }
        th, td {
            padding: 8px 10px;
            border: 1px solid #e2e8f0;   /* viền cho từng ô */
            text-align: left;
            font-size: 14px;
        }
        th {
            background-color: #f7fafc;
            font-weight: 700;            /* header in đậm */
            color: #2d3748;
        }
        th a {
            text-decoration: none;
            color: inherit;
        }
        th a:hover {
            text-decoration: underline;
        }
        tr:nth-child(even) {
            background-color: #f9fafb;
        }
        .empty-row {
            text-align: center;
            color: #718096;
        }
        img.photo {
            max-height: 48px;
            border-radius: 4px;
        }

        .pagination {
            margin-top: 12px;
            text-align: center;
        }
        .pagination a {
            padding: 5px 9px;
            margin: 0 2px;
            border: 1px solid #cbd5e0;
            border-radius: 4px;
            text-decoration: none;
            color: #2d3748;
            font-size: 13px;
        }
        .pagination strong {
            padding: 5px 9px;
            margin: 0 2px;
            border-radius: 4px;
            background-color: #3182ce;
            color: white;
            font-size: 13px;
        }
        .page-info {
            margin-top: 4px;
            font-size: 13px;
            color: #4a5568;
        }
    </style>
</head>
<body>
<div class="container">

    <!-- Row 1: icon + title -->
    <div class="title-row">
        <span class="title-icon">📚</span>
        <h2>Student Management (MVC)</h2>
    </div>

    <!-- Messages -->
    <c:if test="${not empty param.message}">
        <div class="message success">${param.message}</div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="message error">${param.error}</div>
    </c:if>

    <!-- Row 2: buttons left, search right -->
    <div class="actions-search-row">
        <div class="actions-left">
            <a href="student?action=new" class="btn btn-primary">＋ Add New Student</a>
            <a href="student?action=list" class="btn btn-secondary">Refresh</a>
            <a href="export" class="btn btn-export">⬇ Export to Excel</a>
        </div>

        <div class="search-box">
            <form action="student" method="get">
                <input type="hidden" name="action" value="search"/>
                <input type="hidden" name="major" value="${selectedMajor}"/>
                <input type="hidden" name="sortBy" value="${sortBy}"/>
                <input type="hidden" name="order" value="${order}"/>

                <input type="text"
                       name="keyword"
                       placeholder="Search by code, name, email"
                       value="${keyword}"/>
                <button type="submit" class="btn btn-primary btn-small">Search</button>

                <c:if test="${not empty keyword}">
                    <a href="student?action=list" class="btn btn-small">Clear</a>
                </c:if>
            </form>
        </div>
    </div>

    <!-- Row 3: filter by major -->
    <div class="filter-row">
        <form action="student" method="get">
            <input type="hidden" name="action" value="filter"/>
            <input type="hidden" name="keyword" value="${keyword}"/>
            <input type="hidden" name="sortBy" value="${sortBy}"/>
            <input type="hidden" name="order" value="${order}"/>

            <label>Filter by Major:</label>
            <select name="major">
                <option value="ALL"
                    ${selectedMajor == 'ALL' || empty selectedMajor ? 'selected' : ''}>
                    All Majors
                </option>
                <option value="Computer Science"
                    ${selectedMajor == 'Computer Science' ? 'selected' : ''}>
                    Computer Science
                </option>
                <option value="Information Technology"
                    ${selectedMajor == 'Information Technology' ? 'selected' : ''}>
                    Information Technology
                </option>
                <option value="Software Engineering"
                    ${selectedMajor == 'Software Engineering' ? 'selected' : ''}>
                    Software Engineering
                </option>
                <option value="Data Science"
                    ${selectedMajor == 'Data Science' ? 'selected' : ''}>
                    Data Science
                </option>
                <option value="Business Administration"
                    ${selectedMajor == 'Business Administration' ? 'selected' : ''}>
                    Business Administration
                </option>
            </select>
            <button type="submit" class="btn btn-small">Apply Filter</button>

            <c:if test="${not empty selectedMajor && selectedMajor != 'ALL'}">
                <a href="student?action=list" class="btn btn-small">Clear Filter</a>
            </c:if>
        </form>
    </div>

    <!-- Search info -->
    <c:if test="${not empty keyword}">
        <p>Search results for: <strong>${keyword}</strong></p>
    </c:if>

    <!-- Table -->
    <table>
        <thead>
        <tr>
            <th>
                <a href="student?action=sort&sortBy=id&order=${order == 'asc' ? 'desc' : 'asc'}&keyword=${keyword}&major=${selectedMajor}">
                    ID
                    <c:if test="${sortBy == 'id'}">
                        ${order == 'asc' ? '▲' : '▼'}
                    </c:if>
                </a>
            </th>
            <th>
                <a href="student?action=sort&sortBy=student_code&order=${order == 'asc' ? 'desc' : 'asc'}&keyword=${keyword}&major=${selectedMajor}">
                    Code
                    <c:if test="${sortBy == 'student_code'}">
                        ${order == 'asc' ? '▲' : '▼'}
                    </c:if>
                </a>
            </th>
            <th>
                <a href="student?action=sort&sortBy=full_name&order=${order == 'asc' ? 'desc' : 'asc'}&keyword=${keyword}&major=${selectedMajor}">
                    Name
                    <c:if test="${sortBy == 'full_name'}">
                        ${order == 'asc' ? '▲' : '▼'}
                    </c:if>
                </a>
            </th>
            <th>
                <a href="student?action=sort&sortBy=email&order=${order == 'asc' ? 'desc' : 'asc'}&keyword=${keyword}&major=${selectedMajor}">
                    Email
                    <c:if test="${sortBy == 'email'}">
                        ${order == 'asc' ? '▲' : '▼'}
                    </c:if>
                </a>
            </th>
            <th>
                <a href="student?action=sort&sortBy=major&order=${order == 'asc' ? 'desc' : 'asc'}&keyword=${keyword}&major=${selectedMajor}">
                    Major
                    <c:if test="${sortBy == 'major'}">
                        ${order == 'asc' ? '▲' : '▼'}
                    </c:if>
                </a>
            </th>
            <th>Photo</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty students}">
            <tr>
                <td colspan="7" class="empty-row">No students found</td>
            </tr>
        </c:if>

        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.id}</td>
                <td>${student.studentCode}</td>
                <td>${student.fullName}</td>
                <td>${student.email}</td>
                <td>${student.major}</td>
                <td>
                    <c:if test="${not empty student.photo}">
                        <img class="photo"
                             src="${pageContext.request.contextPath}/uploads/${student.photo}"
                             alt="Photo"/>
                    </c:if>
                </td>
                <td>
                    <a href="student?action=edit&id=${student.id}"
                       class="btn btn-small">Edit</a>
                    <a href="student?action=delete&id=${student.id}"
                       class="btn btn-small btn-danger"
                       onclick="return confirm('Are you sure you want to delete this student?');">
                        Delete
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Pagination -->
    <c:if test="${not empty totalPages && totalPages > 1}">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="student?action=list&page=${currentPage - 1}">« Previous</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="student?action=list&page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="student?action=list&page=${currentPage + 1}">Next »</a>
            </c:if>
        </div>
        <p class="page-info">
            Showing page ${currentPage} of ${totalPages}
        </p>
    </c:if>
</div>
</body>
</html>



