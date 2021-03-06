<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String errorMsg = (String) request
			.getAttribute("dbConnErrorMsg");
%>
<!DOCTYPE html>
<html>
<head>
<title>Mobile Customer Churn Analysis</title>
<meta charset="utf-8">
<link rel="stylesheet" href="bootstrap/css/bootstrap.css"
	type="text/css" />
<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
<script src="bootstrap/js/parsley.js"></script>
</head>
<style>
label.radio{display: inline;
margin-top: 20px;
padding-left: 5px;
padding-right: 20px;
position: relative;
top: 4px;}

</style>

<body>

	<div class="container">
		<br />
		<h1>
			<img src="bootstrap/img/bludemo.jpg"><a href="index.html">Mobile Customer Churn Analysis</a>
			<a href="index.html" class="btn btn-info"><i class="icon-white icon-home center"></i> Home</a>
			
		</h1>
		
		<hr>
		
		<%	if (errorMsg != null) { %>
		<div class="container">

			<div class="alert alert-error" id="authError">
				<button type="button" class="close" data-dismiss="alert">x</button>
				<strong><%= errorMsg %></strong>
			</div>
			<% } %>
			<div class="row-fluid">
				<div class="span12">
				
					<form class="form-horizontal" name="trainingdataform" method="POST"
						action="./DatabaseDetailsServlet" parsley-validate>
						<div class="area">
							<div class="heading">
								<h4 class="form-heading">Training Set Database details</h4>
								<h6>The details provided will be used to generate the model</h6>
							</div>
							<div class="control-group">
								<label class="control-label">Hostname</label>
								<div class="controls">
									<input type="text" parsley-trigger="change" name="hostname"
										placeholder="bluemix.ace.net" required>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Port</label>
								<div class="controls">
									<input type="number" name="port" placeholder="50000"
										parsley-trigger="change" required>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Database</label>
								<div class="controls">
									<input type="text" name="database" parsley-trigger="change"
										required />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Username</label>
								<div class="controls">
									<input type="text" name="username" required>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Password</label>
								<div class="controls">
									<input type="password" name="password" required>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Training Set TableName</label>
								<div class="controls">
									<input type="text" name="tableName" parsley-trigger="change"
										required />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">Class Index</label>
								<div class="controls">								
								<input type="radio" name="trainingTableClassifierIndex" checked="checked" value="0" /><label class="radio">First</label>
										
								<input type="radio" name="trainingTableClassifierIndex" value="-1" /><label class="radio">Last</label>
										
								<input type="radio" name="trainingTableClassifierIndex" value="other" /><label class="radio">Specify Index</label> 
			
								<input style="display: none;" type="number" name=other id="other" />
								</div>
							</div>
							<div class="control-group">
								<div class="controls">
									<button type="submit" class="btn btn-success">Proceed
										to upload Testing Set</button>
								</div>
							</div>
						</div>
					</form>
					
										
				</div>
			</div>

		</div>

	</div>
	
	<script>
	$("input[type='radio']").change(function(){
		   
		if($(this).val()=="other")
		{
		    $("#other").show();
		}
		else
		{
		       $("#other").hide(); 
		}
		    
		});

	</script>

</body>
</html>
