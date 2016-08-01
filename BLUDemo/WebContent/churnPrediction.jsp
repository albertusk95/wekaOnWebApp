<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Mobile Customer Churn Analysis</title>
<meta charset="utf-8">
<link rel="stylesheet" href="bootstrap/css/bootstrap.css"
	type="text/css" />
<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
</head>

<body>
	<div class="container">
		<br />
		<h1>
			<img src="bootstrap/img/bludemo.jpg"><a href="index.html">Mobile Customer Churn Analysis</a>
			<a href="index.html" class="btn btn-info"><i class="icon-white icon-home center"></i> Home</a>
			
		</h1>
		<div class="row-fluid">
		<div class="span10">
		<h2>Churn Analysis Report</h2>
		</div>	
		<!-- <div class="span2">
		<a href="index.html" class="btn btn-info btn-large"><i class="icon-white icon-home center"></i> Home</a>
		</div>-->
		</div>
		<hr>
		<table class="table" id="results" cellspacing='15'>
			<thead>
				<tr style="font-size: 18px;">
					<th>Customer ID</th>
					<th>Churn Analysis</th>
				</tr>
			</thead>
			<tbody>
				<%
					ArrayList<String> outputList = (ArrayList<String>) session
							.getAttribute("outputList");
					for (int i = 0; i < outputList.size(); i++) {
				%>
				<tr>
					<td><%=i + 1%></td>
					<td><%=outputList.get(i)%></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
</body>
</html>
