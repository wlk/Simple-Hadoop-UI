package pl.wroc.pwr.zpi.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class Network extends AbstractModelBase {

	private static final long serialVersionUID = -1179650500247183509L;

	private int id;

	private String name;

	private String schema;

	private String networkFileLocation;

	private int isValid;

	private int isPublic;

	private int userId;

	private String delimiter;

	private Date removedAt;

	public Network() {
	}

	public Network(int id, String shortName, String schema,
			String networkFileLocation, int isValid, int isPublic, int userId,
			String delimiter) {
		super();
		this.id = id;
		this.name = shortName;
		this.schema = schema;
		this.networkFileLocation = networkFileLocation;
		this.isValid = isValid;
		this.isPublic = isPublic;
		this.setUserId(userId);
		this.setDelimiter(delimiter);
	}

	public Network(String shortName, String networkFileLocation, int isPublic,
			int userId, String delimiter) {
		super();
		this.name = shortName;
		this.schema = "null";
		this.networkFileLocation = networkFileLocation;
		this.isValid = -1;
		this.isPublic = isPublic;
		this.setUserId(userId);
		this.setDelimiter(delimiter);
		save();
	}

	@Override
	public void save() {
		connection = DBConnection.getConnection();
		String checkQuery = "SELECT * FROM networks WHERE id = "
				+ String.valueOf(id);
		System.err.println("checkQuery: " + checkQuery);
		Statement stm;
		ResultSet rs = null;
		try {
			connection = DBConnection.getConnection();
			stm = connection.createStatement();
			rs = stm.executeQuery(checkQuery);
			if (rs.first()) {
				String query = "UPDATE networks SET name = " + toStringOrNULL(name)
						+ ", networks.schema = " + toStringOrNULLAndEscape(schema)
						+ ", network_file_location = " + toStringOrNULL(networkFileLocation)
						+ ", is_valid = " + isValid
						+ ", is_public = " + isPublic
						+ ", user_id = " + userId
						+ ", delimiter = " + toStringOrNULLAndEscape(delimiter)
						+ ", removed_at = " + toStringOrNULL(removedAt) 
						+ " WHERE id = "
						+ getId();
				executeNonReturningQuery(query);
			} else {
				String query = "INSERT INTO networks (name, networks.schema, network_file_location, is_valid, is_public, user_id, delimiter, removed_at) VALUES ("
						+ toStringOrNULL(name)
						+ ","
						+ toStringOrNULLAndEscape(schema)
						+ ", "
						+ toStringOrNULL(networkFileLocation)
						+ ", "
						+ isValid
						+ ", "
						+ isPublic
						+ ", "
						+ userId
						+ ", " + toStringOrNULLAndEscape(delimiter) + ", " + toStringOrNULL(removedAt) + ")";
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

	public static List<Network> getNetworks() {
		String query = "SELECT * FROM networks WHERE removed_at IS NULL";
		return getNetworksByQuery(query);
	}
	
	/**
	 * wszystkie sieci danego usera
	 * @param userId
	 * @return
	 */
	public static List<Network> getNetworksByUserID(int userId) {
		String query = "SELECT * FROM networks WHERE removed_at IS NULL AND user_id = "
				+ userId;
		return getNetworksByQuery(query);
	}
	
	/**
	 * wszytskie sieci publiczne
	 * @return
	 */
	public static List<Network> getPublicNetworks(){
		String query = "SELECT * FROM networks WHERE removed_at IS NULL is_public = 1";
		return getNetworksByQuery(query);
	}
	
	/**
	 * wszystie sieci danego usera i wszystkie sieci publiczne
	 * @param userId
	 * @return
	 */
	public static List<Network> getPrivateAndPublicNetworksByUserID(int userId){
		String query = "SELECT * FROM networks WHERE removed_at IS NULL AND user_id = "
				+ userId + " OR is_public = 1";
		return getNetworksByQuery(query);
	}

	public static void deleteById(int id) {
		Network n = Network.getByID(id);
		Date d = new Date(System.currentTimeMillis());
		n.setRemovedAt(d);
		n.save();
	}

	private static List<Network> getNetworksByQuery(String query) {
		System.err.println("query: " + query);
		connection = DBConnection.getConnection();
		List<Network> list = new ArrayList<Network>();
		connection = DBConnection.getConnection();
		Statement stm;
		ResultSet rs = null;
		try {
			stm = connection.createStatement();
			rs = stm.executeQuery(query);
			while (rs.next()) {
				Network n = new Network();
				n.populateObject(rs);
				list.add(n);
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
		return list;
	}
	
	private static Network getNetworkByQuery(String query){
		List<Network> list = new ArrayList<Network>();
		list = getNetworksByQuery(query);
		return list.get(0);
	}

	public static Network getByID(int id) {
		String query = "SELECT * FROM networks WHERE id = "
				+ String.valueOf(id);
		return getNetworkByQuery(query);
	}
	
	public static List<Network> getNetworksForAlgorithm(int userId, int algorithmId) {
		Algorithm alg = new Algorithm();
		alg = Algorithm.getByID(algorithmId);
//		String query = "SELECT * FROM networks WHERE removed_at IS NULL AND ( user_id = "
//				+ userId+ " OR is_public = 1 ) AND networks.schema = "+  alg.getEscapedSchema() + " AND delimiter = " + alg.getEscapedDelimiter();
		String query = "SELECT * FROM networks WHERE removed_at IS NULL AND ( user_id = "
				+ userId+ " OR is_public = 1 ) AND networks.schema = "+  alg.getEscapedSchema();
		return getNetworksByQuery(query);
	}
	
	protected void populateObject(ResultSet rs) throws SQLException{
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
		setSchema(unescape(rs.getString("schema")));
		setNetworkFileLocation(rs.getString("network_file_location"));
		setValid(rs.getInt("is_valid") != 0);
		setIsPublic(rs.getInt("is_public") != 0);
		setUserId(rs.getInt("user_id"));
		setDelimiter(unescape(rs.getString("delimiter")));
		setRemovedAt(rs.getDate("removed_at"));
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

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getNetworkFileLocation() {
		return networkFileLocation;
	}

	public void setNetworkFileLocation(String networkFileLocation) {
		this.networkFileLocation = networkFileLocation;
	}

	public boolean isValid() {
		return (isValid != 0);
	}

	public void setValid(boolean isValid) {
		if (isValid) {
			this.isValid = 1;
		} else {
			this.isValid = 0;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String shortName) {
		this.name = shortName;
	}

	public boolean isPublic() {
		return isPublic != 0;
	}

	public void setIsPublic(boolean isPublic) {
		if (isPublic) {
			this.isPublic = 1;
		} else {
			this.isPublic = 0;
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public Date getRemovedAt() {
		return removedAt;
	}

	public void setRemovedAt(Date removedAt) {
		this.removedAt = removedAt;
	}
	
	@Override
	public String toString(){
		return getName();
	}

}