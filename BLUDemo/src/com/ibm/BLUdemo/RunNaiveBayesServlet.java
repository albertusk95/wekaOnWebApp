package com.ibm.BLUdemo;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

@WebServlet("/RunNaiveBayesServlet")
public class RunNaiveBayesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String user;
	String password;
	String jdbcURL;
	String trainingDataTable = "churntrainingset";
	Integer trainingTableClassifierIndex = 0;
	String propsFilePath = System.getenv("HOME") + "/../wekafiles/props/DatabaseUtils.props";
	
	public RunNaiveBayesServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		
		
		// Check to see whether the connection details are in the session
		// If not, get the connection details from VCAP
		if (request.getSession().getAttribute("jdbcURL") != null) {
			jdbcURL = (String) request.getSession().getAttribute("jdbcURL");
			trainingDataTable = (String) request.getSession().getAttribute("trainingDataTable");
			user = (String) request.getSession().getAttribute("user");
			password = (String) request.getSession().getAttribute("password");
			trainingTableClassifierIndex = (Integer)request.getSession().getAttribute("trainingTableClassifierIndex");
		} else {
			VCAPServices vcapServices = new VCAPServices();
			user = vcapServices.getUser();
			password = vcapServices.getPassword();
			jdbcURL = vcapServices.getUrl();
		}
		
		// Override the weka.jar DatabaseUtils.props file by creating our own
		Properties dbprop = new Properties();
		System.out.println("Starting properties file creation....");
		OutputStream output = new FileOutputStream(propsFilePath);
		dbprop.setProperty("jdbcDriver","com.ibm.db2.jcc.DB2Driver");
		dbprop.store(output, null);
		System.out.println("Done properties file creation....");
		
		String prediction = null;
		ArrayList<String> outputList = new ArrayList<String>();
		try {
			
			// Get the arff file
		    String arff_file_name = (String) request.getAttribute("arff_file_name");
		    
		    // Create a new instance query
			InstanceQuery query = new InstanceQuery();
			System.out.println("Connecting to ::"+jdbcURL);
			query.setDatabaseURL(jdbcURL);
			query.setUsername(user);
			query.setPassword(password);
			
			// Create the training query and the train instance
			String trainingQuery = "Select * from "+user+".\""+ trainingDataTable+"\"";
			System.out.println(trainingQuery);
			Instances train;
			
			// Retrieve the query from the DataWarehouse
			query.setQuery(trainingQuery);
			train = query.retrieveInstances();
			
			// set the class Index
			if (trainingTableClassifierIndex != -1) {
				train.setClassIndex(trainingTableClassifierIndex);
			} else {
				train.setClassIndex(train.numAttributes() - 1);
			}			
			
			// Creating the NaiveBayes model
			NaiveBayes nb = new NaiveBayes();
			nb.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.crossValidateModel(nb, train, 2, new Random(1));
			System.out.println(eval
					.toSummaryString("Results\n========\n", true));
			System.out.println(eval.fMeasure(1) + " " + eval.precision(1) + " "
					+ eval.recall(1));	

			// Reading the Testing Set
			BufferedReader breader = new BufferedReader(new FileReader(arff_file_name));
			Instances test = new Instances(breader);
			
			
		    // Use the same classifier index as the training table
			test.setClassIndex(trainingTableClassifierIndex); 
		
			
			breader.close();
			System.out.println("Number of Test instances::" + test.numInstances());
			
			// Performing NaiveBayesian Classification
			for (int i = 0; i < test.numInstances(); i++) {
				double pred = nb.classifyInstance(test.instance(i));
				System.out.println("Pred= " + pred);
				if (pred >= 0.5) {
					System.out.println("The customer will churn");
					prediction = "Yes";
				} else {
					System.out
							.println("The customer will not not churn");
					prediction = "No";
				}				
				outputList.add(prediction);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.getSession().setAttribute("outputList", outputList);
		response.sendRedirect("churnPrediction.jsp");
	}

}
