package pl.wroc.pwr.zpi.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractModelBase implements Serializable{
	private static final long serialVersionUID = -7182760397372411079L;
	protected static Connection connection;
	
	/**
	 * gdy obiekt już istnieje to go aktualizuje, gdy go nie ma to zapisuje nowy
	 */
	public abstract void save();
	
	protected abstract void populateObject(ResultSet rs) throws SQLException;
	
	public static AbstractModelBase getByID(int id){
		throw new NoSuchMethodError("zaimplementuj mnie w klasie pochodnej");
	}
	
	protected static void executeNonReturningQuery(String query){
		System.err.println("query: " + query);
		connection = DBConnection.getConnection();
		Statement stm;
		try {
			stm = connection.createStatement();
			stm.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String escape(String s){
		if (s == null){
			return null;
		}
		s = s.replace(";", "\\;");
		s = s.replace("'", "\\'");
		s = s.replace("\"", "\\\"");
		return s;
	}
	
	protected static String unescape(String s){
		if (s == null){
			return null;
		}
		s = s.replace("\\;", ";");
		s = s.replace("\\'", "\\'");
		s = s.replace("\\\"", "\\\"");
		return s;
	}

	protected String toStringOrNULL(Object o){
		if(o == null){
			return "NULL";
		}
		else{
			return "'" + o.toString() + "'";
		}
	}
	
	protected String toStringOrNULLAndEscape(Object o){
		if(o == null){
			return "NULL";
		}
		else{
			return "'" + escape(o.toString()) + "'";
		}
	}

}

