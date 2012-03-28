package zpi.db;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import zpi.model.MyAlgorithm;
import zpi.model.MyNetwork;
import zpi.model.MyQueue;
import zpi.model.MyUser;

public class DbManager {
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private static DbManager instance = new DbManager();
	
	private DbManager(){
	}
	
	public static DbManager getInstance(){
		return instance;
	}
	
	public List<MyAlgorithm> getAlgorithms(){
		List<MyAlgorithm> algorithmsList = new LinkedList<MyAlgorithm>();
//		
//		algorithmsList.add(new MyAlgorithm(1, "schema", "location", "Algorytm 1", "Opis algorytmu 1", false));
//		algorithmsList.add(new MyAlgorithm(2, "schema", "location", "Algorytm 2", "Opis algorytmu 2", false));
//		algorithmsList.add(new MyAlgorithm(3, "schema", "location", "Algorytm 3", "Opis algorytmu 3", false));
//		
		return algorithmsList;
	}
	
	public List<MyNetwork> getNetworks(){
		List<MyNetwork> networksList = new LinkedList();
//		networksList.add(new MyNetwork(1, "schema 1", "location1", true));
//		networksList.add(new MyNetwork(2, "schema 2", "location2", true));
//		networksList.add(new MyNetwork(3, "schema 3", "location3", true));
		
		return networksList;
	}
	
	public List<MyQueue> getQueues(){
		List<MyQueue> queues = new LinkedList<MyQueue>();
		Date date = new Date(0);
		queues.add(new MyQueue(1, 1, 1, "Location1", date, date, "par1",  1, 1 ));
		queues.add(new MyQueue(2, 2, 2, "Location2", date, date, "par2",  2, 2 ));
		
		return queues;
		
	}
	
	public List<MyUser> getUsers(){
		List<MyUser> users = new LinkedList<MyUser>();
//		users.add(new MyUser(1, "", ""));
//		users.add(new MyUser(2, "marek", "marek"));
		return users;
	}
	
	public void deleteAlgorithm(int id){
		//TODO: usuwanie
	}
	
	public void deleteNetwork(int id){
		//TODO: usuwanie
	}
	
	
	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://10.10.1.29?"
							+ "user=root&password=root");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from ZPI.NETWORKS");
			writeResultSet(resultSet);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert into  FEEDBACK.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
			// "myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
			// Parameters start with 1
			preparedStatement.setString(1, "Test");
			preparedStatement.setString(2, "TestEmail");
			preparedStatement.setString(3, "TestWebpage");
			preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
			preparedStatement.setString(5, "TestSummary");
			preparedStatement.setString(6, "TestComment");
			preparedStatement.executeUpdate();

			preparedStatement = connect
					.prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from FEEDBACK.COMMENTS");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

			// Remove again the insert comment
			preparedStatement = connect
			.prepareStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
			preparedStatement.setString(1, "Test");
			preparedStatement.executeUpdate();
			
			resultSet = statement
			.executeQuery("select * from FEEDBACK.COMMENTS");
			writeMetaData(resultSet);
			
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// 	Now get some metadata from the database
		// Result set get the result of the SQL query
		
		System.out.println("The columns in the table are: ");
		
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summery = resultSet.getString("summery");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summery: " + summery);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}	

}
