package zpi.hadoopModel;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Algorithm extends AbstractModelBase implements INetAlg {

	private static final long serialVersionUID = 350860265664692203L;

	private int id;

	private int isPublic;

	private int userId;

	private String schema;

	private String parameters;

	private String jarFileLocation;

	private String shortName;

	private String longName;

	private Date removedAt;
	
	private String delimiter;

	public Algorithm(int id, int isPublic, int userId, String schema,
			String parameters, String jarFileLocation, String shortName,
			String longName, String delimiter) {
		super();
		this.id = id;
		this.isPublic = isPublic;
		this.userId = userId;
		this.schema = schema;
		this.parameters = parameters;
		this.jarFileLocation = jarFileLocation;
		this.shortName = shortName;
		this.longName = longName;
		this.delimiter = delimiter;
	}

	public Algorithm() {
	}

	@Override
	public void save() {
		String checkQuery = "SELECT * FROM algorithms WHERE id = " + this.id;
		System.err.println("checkQuery: " + checkQuery);
		Statement stm;
		ResultSet rs = null;
		try {
			connection = DBConnection.getConnection();
			stm = connection.createStatement();
			rs = stm.executeQuery(checkQuery);
			// istnieje już ten obiekt - update
			if (rs.first()) {
				String query = "UPDATE algorithms SET short_name = "
						+ toStringOrNULL(shortName) + ", long_name = " + toStringOrNULL(longName)
						+ ", algorithms.schema = " + toStringOrNULLAndEscape(schema)
						+ ", parameters = " + toStringOrNULLAndEscape(parameters)
						+ ", user_id = " + userId
						+ ", jar_file_location = " + toStringOrNULL(jarFileLocation)
						+ ", is_public = " + isPublic
						+ ", removed_at = " + toStringOrNULL(removedAt)
						+ ", delimiter = " + toStringOrNULLAndEscape(delimiter)
						+ " WHERE id = " + getId();
				executeNonReturningQuery(query);
			}
			// jeszcze go nie ma - zapis nowego
			else {
				String query = "INSERT INTO algorithms "
						+ "(short_name, long_name, algorithms.schema, parameters, user_id, jar_file_location, is_public, removed_at, delimiter) "
						+ "VALUES (" + toStringOrNULL(shortName) + ", " + toStringOrNULL(longName)
						+ ", " + toStringOrNULLAndEscape(schema)+ ", " + toStringOrNULLAndEscape(parameters)
						+ ", " + userId + ", "
						+ toStringOrNULL(jarFileLocation) + ", "
						+ isPublic
						+ "," + toStringOrNULL(removedAt)
						+ "," + toStringOrNULLAndEscape(delimiter)
						+ ")";
				executeNonReturningQuery(query);
			}
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
	
	private static Algorithm getAlgorithmByQuery(String query){
		List<Algorithm> list = new ArrayList<Algorithm>();
		list = getAlgorithmsByQuery(query);
		return list.get(0);
	}
	
	public static Algorithm getByID(int id) {
		String query = "SELECT * FROM algorithms WHERE id = "
				+ String.valueOf(id);
		return getAlgorithmByQuery(query);
	}
	
	public static List<Algorithm> getPrivateAndPublicAlgorithmsByUserID(int userId){
		String query = "SELECT * FROM algorithms WHERE removed_at IS NULL AND user_id = "
				+ userId + " OR is_public = 1";
		return getAlgorithmsByQuery(query);
	}
	

	public static void deleteById(int id) {
		Algorithm a = Algorithm.getByID(id);
		Date d = new Date(System.currentTimeMillis());
		a.setRemovedAt(d);
		a.save();
	}
	
	private static List<Algorithm> getAlgorithmsByQuery(String query){
		List<Algorithm> list = new ArrayList<Algorithm>();
		connection = DBConnection.getConnection();
		Statement stm;
		ResultSet rs = null;
		try {
			stm = connection.createStatement();
			rs = stm.executeQuery(query);
			list = addAlgorithmsToList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static List<Algorithm> getAlgorithms() {
		String query = "SELECT * FROM algorithms WHERE removed_at IS NULL";
		return getAlgorithmsByQuery(query);
	}

	public static List<Algorithm> getPublicAlgorithms() {
		String query = "SELECT * FROM algorithms WHERE removed_at IS NULL AND is_public = 1";
		return getAlgorithmsByQuery(query);
	}

	public static List<Algorithm> getUserAlgorithms(int givenUserId) {
		String query = "SELECT * FROM algorithms WHERE removed_at IS NULL AND user_id = " + givenUserId;
		return getAlgorithmsByQuery(query);
	}

	private static List<Algorithm> addAlgorithmsToList(ResultSet rs)
			throws SQLException {
		List<Algorithm> list = new ArrayList<Algorithm>();
		while (rs.next()) {
			Algorithm a = new Algorithm();
			a.populateObject(rs);
			list.add(a);
		}
		return list;
	}
	
	protected void populateObject(ResultSet rs) throws SQLException{
		setId(rs.getInt("id"));
		setShortName(rs.getString("short_name"));
		setJarFileLocation(rs.getString("jar_file_location"));
		setIsPublic((rs.getInt("is_public") != 0));
		setSchema(unescape(rs.getString("schema")));
		setParameters(unescape(rs.getString("parameters")));
		setUserId(rs.getInt("user_id"));
		setLongName(rs.getString("long_name"));
		setRemovedAt(rs.getDate("removed_at"));
		setDelimiter(rs.getString("delimiter"));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSchema() {
		return schema;
	}
	
	public String getEscapedSchema(){
		return toStringOrNULLAndEscape(schema);
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getJarFileLocation() {
		return jarFileLocation;
	}

	public void setJarFileLocation(String jarFileLocation) {
		this.jarFileLocation = jarFileLocation;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public int getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		if (isPublic)
			this.isPublic = 1;
		else
			this.isPublic = 0;
	}

	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public boolean isUserId() {
		return userId != 0;
	}

	public void setUserId(int userId) {
		this.userId = userId;

	}

	public int getUserId() {
		return userId;
	}
	
	public String getDelimiter(){
		return delimiter;
	}
	
	public String getEscapedDelimiter(){
		return toStringOrNULLAndEscape(delimiter);
	}
	
	public void setDelimiter(String delimiter){
		this.delimiter = delimiter;
	}

	@Override
	public String toString() {
		return getShortName();
	}

	public Date getRemovedAt() {
		return removedAt;
	}

	public void setRemovedAt(Date removedAt) {
		this.removedAt = removedAt;
	}

}
