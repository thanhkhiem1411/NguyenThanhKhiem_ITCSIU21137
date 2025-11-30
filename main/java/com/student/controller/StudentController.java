package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/student")
@MultipartConfig
public class StudentController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            case "search":
                searchStudents(request, response); // EX5
                break;
            case "sort":
                sortStudents(request, response);   // EX7
                break;
            case "filter":
                filterStudents(request, response); // EX7
                break;
            default:
                listStudents(request, response);   // list + pagination
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "insert":
                insertStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
            default:
                response.sendRedirect("student?action=list");
        }
    }

    // ========== LIST + PAGINATION (EX8) ==========

    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pageParam = request.getParameter("page");
        int currentPage = 1;
        try {
            if (pageParam != null) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        int recordsPerPage = 10;
        if (currentPage < 1) currentPage = 1;

        int offset = (currentPage - 1) * recordsPerPage;

        List<Student> students = studentDAO.getStudentsPaginated(offset, recordsPerPage);
        int totalRecords = studentDAO.getTotalStudents();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (currentPage > totalPages) currentPage = totalPages;

        request.setAttribute("students", students);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.setAttribute("keyword", request.getParameter("keyword"));
        request.setAttribute("selectedMajor", request.getParameter("major"));
        request.setAttribute("sortBy", request.getParameter("sortBy"));
        request.setAttribute("order", request.getParameter("order"));

        forwardToList(request, response);
    }

    private void forwardToList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // ========== FORM ==========

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        Student student = studentDAO.getStudentById(id);

        request.setAttribute("student", student);
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("views/student-form.jsp");
        dispatcher.forward(request, response);
    }

    // ========== INSERT / UPDATE (EX6 + BONUS 2) ==========

    private void insertStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student student = new Student();
        student.setStudentCode(studentCode);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setMajor(major);

        if (!validateStudent(student, request)) {
            request.setAttribute("student", student);
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String photoFileName = processUploadFile(request);
        student.setPhoto(photoFileName);

        if (studentDAO.addStudent(student)) {
            response.sendRedirect("student?action=list&message=Added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");
        String existingPhoto = request.getParameter("existingPhoto");

        Student student = new Student();
        student.setId(id);
        student.setStudentCode(studentCode);
        student.setFullName(fullName);
        student.setEmail(email);
        student.setMajor(major);
        student.setPhoto(existingPhoto);

        if (!validateStudent(student, request)) {
            request.setAttribute("student", student);
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String newPhoto = processUploadFile(request);
        if (newPhoto != null) {
            student.setPhoto(newPhoto);
        }

        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }

    // ========== EX5: SEARCH ==========

    private void searchStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String major = request.getParameter("major");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");

        List<Student> students;
        if (keyword == null || keyword.trim().isEmpty()) {
            students = studentDAO.getAllStudents();
        } else {
            students = studentDAO.searchStudents(keyword.trim());
        }

        request.setAttribute("students", students);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedMajor", major);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);

        forwardToList(request, response);
    }

    // ========== EX7: SORT & FILTER ==========

    private void sortStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String keyword = request.getParameter("keyword");
        String major = request.getParameter("major");

        List<Student> students;

        if ((keyword != null && !keyword.trim().isEmpty()) ||
            (major != null && !major.trim().isEmpty() && !"ALL".equalsIgnoreCase(major))) {
            students = studentDAO.getStudentsFiltered(keyword, major, sortBy, order);
        } else {
            students = studentDAO.getStudentsSorted(sortBy, order);
        }

        request.setAttribute("students", students);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedMajor", major);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);

        forwardToList(request, response);
    }

    private void filterStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String major = request.getParameter("major");
        String keyword = request.getParameter("keyword");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");

        List<Student> students =
                studentDAO.getStudentsFiltered(keyword, major, sortBy, order);

        request.setAttribute("students", students);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedMajor", major);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("order", order);

        forwardToList(request, response);
    }

    // ========== EX6: VALIDATION ==========

    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;

        // 1. Student Code
        String code = student.getStudentCode();
        if (code == null || code.trim().isEmpty()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else {
            String codePattern = "[A-Z]{2}[0-9]{3,}";
            if (!code.matches(codePattern)) {
                request.setAttribute("errorCode",
                        "Invalid format. Use 2 letters + 3+ digits (e.g., SV001, IT123)");
                isValid = false;
            }
        }

        // 2. Full Name
        String name = student.getFullName();
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        } else if (name.trim().length() < 2) {
            request.setAttribute("errorName", "Full name must be at least 2 characters");
            isValid = false;
        }

        // 3. Email (optional)
        String email = student.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailPattern)) {
                request.setAttribute("errorEmail", "Invalid email format");
                isValid = false;
            }
        }

        // 4. Major (required)
        String major = student.getMajor();
        if (major == null || major.trim().isEmpty()) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }

        return isValid;
    }

    // ========== BONUS 2: UPLOAD PHOTO ==========

    private String processUploadFile(HttpServletRequest request)
            throws IOException, ServletException {

        Part filePart = null;
        try {
            filePart = request.getPart("photo");
        } catch (IllegalStateException e) {
            return null;
        }

        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return null;
        }

        String originalFileName =
                Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        String uploadPath = request.getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String newFileName = System.currentTimeMillis() + "_" + originalFileName;
        filePart.write(uploadPath + File.separator + newFileName);

        return newFileName;
    }
}
