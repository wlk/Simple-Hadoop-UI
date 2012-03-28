package pl.wroc.pwr.zpi.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User extends AbstractModelBase {

	private static final long serialVersionUID = -6997937835915108743L;
	private int id;
	private String name;
	private String password;
	private int isAdmin;
	private Date removedAt;

	public User() {
		super();
	}

	/**
	 * zapisuje zmieniony obiekt w bazie
	 */
	public void save() {
		String checkQuery = "SELECT * FROM users WHERE id = " + this.id;
		Statement stm;
		ResultSet rs = null;
		try {
			connection = DBConnection.getConnection();
			stm = connection.createStatement();
			rs = stm.executeQuery(checkQuery);
			// istnieje już ten obiekt - update
			if (rs.first()) {
				String query = "UPDATE users SET name = " + toStringOrNULL(name)
						+ ", password = " + toStringOrNULL(password)
						+ ", is_admin = " + isAdmin 
						+ ", removed_at = " + toStringOrNULL(removedAt) 
						+ " WHERE id = " + getId();
				executeNonReturningQuery(query);
			}
			// jeszcze go nie ma - zapis nowego
			else {
				String query = "INSERT INTO users (name, password, removed_at, is_admin) VALUES ("
						+ toStringOrNULL(name) + ", "
						+ toStringOrNULL(password) + ", "
						+ toStringOrNULL(removedAt) + ", "
						+ isAdmin + ")";
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
	
	public static void deleteById(int id) {
		User u = User.getByID(id);
		Date d = new Date(System.currentTimeMillis());
		u.setRemovedAt(d);
		u.save();
	}
	
	private static List<User> getUsersByQuery(String query){
		System.err.println("query: " + query);
		connection = DBConnection.getConnection();
		Statement stm;
		ResultSet rs = null;
		ArrayList<User> a = new ArrayList<User>();
		try {
			stm = connection.createStatement();
			rs = stm.executeQuery(query);
			while (rs.next()) {
				User u = new User();
				u.populateObject(rs);
				a.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}

	public static List<User> getUsers(){
		String query = "SELECT * FROM users WHERE removed_at IS NULL";
		return getUsersByQuery(query);
	}

	public static List<User> getOtherUsers(int userId) {
		String query = "SELECT * FROM users WHERE removed_at IS NULL AND user_id <>" + userId;
		return getUsersByQuery(query);
	}

	public static void removeUserData(int id) {
		User.deleteById(id);
		// TODO usuwa uzytkownika, jego sieci niepubliczne, algorytmy, nie
		// wyrzuca zadan z kolejki
	}
	
	protected void populateObject(ResultSet rs) throws SQLException{
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
		setPassword(rs.getString("password"));
		setAdmin(rs.getInt("is_admin"));
		setRemovedAt(rs.getDate("removed_at"));
	}
	
	private static User getUserByQuery(String query){
		List<User> list = new ArrayList<User>();
		list = getUsersByQuery(query);
		if (list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	/**
	 * pobiera obiekt użytkownika z bazy
	 * 
	 * @param id
	 *            - id użytkownika
	 * @return obiekt użytkownika z bazy o podanym id
	 */
	public static User getByID(int id) {
		String query = "SELECT * FROM users WHERE removed_at IS NULL AND id = "
				+ String.valueOf(id);
		return getUserByQuery(query);
	}

	public static User getByName(String name) {
		String query = "SELECT * FROM users WHERE name = '"
				+ name + "'";
		return getUserByQuery(query);
	}

	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin != 0;
	}

	public void setAdmin(boolean isAdmin) {
		if (isAdmin)
			this.isAdmin = 1;
		else
			this.isAdmin = 0;
	}

	public void setAdmin(int i) {
		isAdmin = i;
	}

	public static boolean isAuthenticated(String user, String pass) {
		boolean result = false;
		connection = DBConnection.getConnection();
		String query = "SELECT * FROM users WHERE removed_at IS NULL AND name='"
				+ user + "' AND password = '" + pass + "'";
		System.err.println("query: " + query);
		Statement stm;
		ResultSet rs = null;
		try {
			stm = connection.createStatement();
			rs = stm.executeQuery(query);
			if (rs.next()) {
				result = true;
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
		return result;
	}

	public Date getRemovedAt() {
		return removedAt;
	}

	public void setRemovedAt(Date removedAt) {
		this.removedAt = removedAt;
	}

}
