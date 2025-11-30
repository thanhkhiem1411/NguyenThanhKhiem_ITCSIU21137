package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");

        int totalStudents = studentDAO.getTotalStudents(); // thêm hàm này trong StudentDAO

        request.setAttribute("welcomeMessage", "Welcome back, " + user.getFullName() + "!");
        request.setAttribute("totalStudents", totalStudents);

        request.getRequestDispatcher("/views/dashboard.jsp")
               .forward(request, response);
    }
}
