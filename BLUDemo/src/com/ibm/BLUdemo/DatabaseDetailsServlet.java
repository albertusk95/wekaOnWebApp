package com.ibm.BLUdemo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DatabaseDetailsServlet")
public class DatabaseDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DatabaseDetailsServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("username");
		String password = request.getParameter("password");
		String hostname = request.getParameter("hostname");
		String port = request.getParameter("port");
		String database = request.getParameter("database");
		String trainingDataTable = request.getParameter("tableName");
		String trainingTableClassifierIndex = request
				.getParameter("trainingTableClassifierIndex");
		if (trainingTableClassifierIndex.equalsIgnoreCase("other")) {
			trainingTableClassifierIndex = request.getParameter("other");
		}
		Connection con = null;
		String errorMsg = null;
		String jdbcURL = "jdbc:db2://" + hostname + ":" + port + "/" + database;
		System.out.println("User entered jdbcURL::" + jdbcURL);

		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			con = DriverManager.getConnection(jdbcURL, user, password);
		} catch (SQLException e) {
			System.out.println("Invalid database details!.....");
			e.printStackTrace();
			errorMsg = "Access denied! Please enter valid database connection details!";
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			errorMsg = "JDBC driver not found!";
		}

		if (con != null) {
			request.getSession().setAttribute("jdbcURL", jdbcURL);
			request.getSession().setAttribute("trainingDataTable",
					trainingDataTable);
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("password", password);
			request.getSession().setAttribute("trainingTableClassifierIndex",
					Integer.parseInt(trainingTableClassifierIndex));
			response.sendRedirect("uploadTestingSet.html");
		} else {
			request.setAttribute("dbConnErrorMsg", errorMsg);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("/trainingDatabase.jsp");
			dispatcher.forward(request, response);
		}

	}
}
