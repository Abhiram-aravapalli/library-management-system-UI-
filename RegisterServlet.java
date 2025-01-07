 import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("Registerpage.html");
            dispatcher.forward(request, response);
            return;
        }

        // Database credentials (you need to configure these based on your setup)
        String dbURL = "jdbc:mysql://localhost:3306/your_database";
        String dbUser = "your_db_user";
        String dbPass = "your_db_password";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

            // SQL query to insert the new user into the database
            String sql = "INSERT INTO users (full_name, email, username, password) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, username);
            stmt.setString(4, password);

            int result = stmt.executeUpdate();

            if (result > 0) {
                // Registration successful, redirect to login page
                response.sendRedirect("Loginpage.html");
            } else {
                // Error occurred during registration
                request.setAttribute("errorMessage", "Registration failed. Please try again.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("Registerpage.html");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again later.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("Registerpage.html");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
