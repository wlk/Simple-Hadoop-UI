package zpi.hadoopModel;


import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class Queue extends AbstractModelBase implements Comparable<Queue>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3982292252207551860L;

	private int id;
	
	private int algorithmId;

	private int networkId;
	
	private String outputFileLocation;
	
	private Date startedAt;
	
	private Date endedAt;
	
	private String parameters;
	
	private int progress;
	
	private int userId;
	
	private String status;
	
	private String hadoopJobId;
	
	private Date removedAt;
	
	private Timestamp endedAtTimestamp;
	
	private Timestamp startedAtTimestamp;
	

	public Queue(){
	}
	
	public static List<Queue> getQueues() throws SQLException{
		String query = "SELECT * FROM queue WHERE removed_at_timestamp IS NULL ORDER BY id DESC";
		return getQueuesByQuery(query);
	}
	
	public static List<Queue> getQueuesByStartTimeDesc() {
		PriorityQueue<Queue> q = null;
		try{
			q = new PriorityQueue<Queue>(getQueues());
		}catch(Exception e){}
		return new ArrayList<Queue>(q);
	}
	
	private static List<Queue> getQueuesByQuery(String query){
		System.err.println("query: " + query);
		List<Queue> result = null;
		try{
		Connection con = DBConnection.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		result = new ArrayList<Queue>();
		while (rs.next()){
			Queue q = new Queue();
			q.populateObject(rs);
			result.add(q);
		}
		con.close();
		}catch(Exception e){}
		return result;
	}
	
	public static List<Queue> getQueuesByAlgorithmId(int algorithmId) {
		String query = "SELECT * FROM queue WHERE removed_at_timestamp IS NULL AND algorithm_id="+algorithmId;
		return getQueuesByQuery(query);
		
	}
	
	public static List<Queue> getQueuesToTask() {
		String query = "SELECT * FROM queue WHERE removed_at_timestamp IS NULL AND started_at_timestamp IS NULL ORDER BY id ASC";
		return getQueuesByQuery(query);
	}
	
	
	public static List<Queue> getQueuesForUser(int user_id) {
		String query = "SELECT * FROM queue WHERE removed_at_timestamp IS NULL AND user_id = " + user_id + "  ORDER BY id ASC";
		return getQueuesByQuery(query);
	}
	
	public Queue(int id, int algorithmId, int networkId,
			String outputFileLocation, Date startedAt, Date endedAt,
			String parameters, int progress, int userId) {
		super();
		this.id = id;
		this.algorithmId = algorithmId;
		this.networkId = networkId;
		this.outputFileLocation = outputFileLocation;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.parameters = parameters;
		this.progress = progress;
		this.userId = userId;
	}
	
	public Queue(int algorithmId, int networkId,
			String outputFileLocation, Date startedAt, Date endedAt,
			String parameters, int progress, int userId) {
		this(0, algorithmId, networkId, outputFileLocation, startedAt, endedAt, parameters, progress, userId);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAlgorithmId() {
		return algorithmId;
	}

	public void setAlgorithmId(int algorithmId) {
		this.algorithmId = algorithmId;
	}

	public int getNetworkId() {
		return networkId;
	}

	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	public String getOutputFileLocation() {
		return outputFileLocation;
	}

	public void setOutputFileLocation(String outputFileLocation) {
		this.outputFileLocation = outputFileLocation;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public void save() {
		String query = "";
		if (id == 0) { //nowy obiekt
			query = "INSERT INTO queue(algorithm_id, network_id, output_file_location, parameters, progress, user_id, status, hadoop_job_id, removed_at, started_at_timestamp, ended_at_timestamp) "
					+ "VALUES("
					+ getAlgorithmId()
					+ "," + getNetworkId()
					+ "," + toStringOrNULLAndEscape(getOutputFileLocation())
					+ "," + toStringOrNULLAndEscape(getParameters())
					+ "," + getProgress() 
					+ "," + getUserId()
					+ "," + toStringOrNULL(status)
					+ "," + toStringOrNULL(getHadoopJobId())
					+ "," + toStringOrNULL(removedAt)
					+ "," + toStringOrNULL(startedAtTimestamp)
					+ "," + toStringOrNULL(endedAtTimestamp)
					+ ")";
		} else {// aktualizacj obiektu w bazie
			query = "UPDATE queue SET network_id=" + getNetworkId()
					+ ", output_file_location=" + toStringOrNULLAndEscape(getOutputFileLocation())
					+ ", parameters=" + toStringOrNULLAndEscape(getParameters()) 
					+ ", progress=" + getProgress() 
					+ ", user_id=" + getUserId() 
					+ ", status=" + toStringOrNULL(getStatus())
					+ ", hadoop_job_id=" + toStringOrNULL(getHadoopJobId())
					+ ", removed_at=" + toStringOrNULL(removedAt)
					+ ", started_at=" + toStringOrNULL(getStartedAt())
					+ ", ended_at=" + toStringOrNULL(getEndedAt())
					+ ", started_at_timestamp=" + toStringOrNULL(getStartedAtTimestamp())
					+ ", ended_at_timestamp=" + toStringOrNULL(getEndedAtTimestamp())
					+ " where id="
					+ id;
		}
		try {
			System.err.println("query: " + query);
			Connection con = DBConnection.getConnection();
			Statement st = con.createStatement();
			st.executeUpdate(query);
			con.close();
		} catch (SQLException e) {
			System.out.println("cannot save queue");
			e.printStackTrace();
		}
		
	}
	
	public String getOutputURL(){
		return "http://10.10.1.29:50075/browseDirectory.jsp?dir="+getOutputFileLocation().replaceAll("/" , "%2F");
	}
	
	public Queue getTop() throws SQLException{
		PriorityQueue<Queue> q = new PriorityQueue<Queue>(0, new Comparator<Queue>() {	
			public int compare(Queue o1, Queue o2) {
				return new Integer(o1.getId()).compareTo(o2.getId());
			}
		});
		q.addAll(getQueues());
		Queue res;
		while((res= q.poll()).getStartedAt()!=null){}
		return res;
	}

	
	public int compareTo(Queue o) {
			if (startedAt!=null && o.getStartedAt()!=null){
				return startedAt.compareTo(o.getStartedAt());
			}else{
				new Integer(getId()).compareTo(o.getId());
			}
		
		return 0;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHadoopJobId() {
		return hadoopJobId;
	}

	public void setHadoopJobId(String hadoopJobId) {
		this.hadoopJobId = hadoopJobId;
	}

	public Date getRemovedAt() {
		return removedAt;
	}

	public void setRemovedAt(Date date) {
		this.removedAt = date;
	}

	@Override
	protected void populateObject(ResultSet rs) throws SQLException {
		setId(rs.getInt("id"));
		setAlgorithmId(rs.getInt("algorithm_id"));
		setEndedAt(rs.getDate("ended_at"));
		setHadoopJobId(rs.getString("hadoop_job_id"));
		setNetworkId(rs.getInt("network_id"));
		setOutputFileLocation(rs.getString("output_file_location"));
		setParameters(rs.getString("parameters"));
		setProgress(rs.getInt("progress"));
		setRemovedAt(rs.getDate("removed_at"));
		setStartedAt(rs.getDate("started_at"));
		setStatus(rs.getString("status"));
		setUserId(rs.getInt("user_id"));
		setStartedAtTimestamp(rs.getTimestamp("started_at_timestamp"));
		setEndedAtTimestamp(rs.getTimestamp("ended_at_timestamp"));
	}

	public Timestamp getEndedAtTimestamp() {
		return endedAtTimestamp;
	}

	public void setEndedAtTimestamp(Timestamp endedAtTimestamp) {
		this.endedAtTimestamp = endedAtTimestamp;
	}

	public Timestamp getStartedAtTimestamp() {
		return startedAtTimestamp;
	}

	public void setStartedAtTimestamp(Timestamp startedAtTimestamp) {
		this.startedAtTimestamp = startedAtTimestamp;
	}
	
	public void kill(){
		System.out.println("kill dla zadania uruchomione");
		if (getStartedAtTimestamp()==null){
			setStartedAtTimestamp(new Timestamp(System.currentTimeMillis()));
			System.out.println("zadanie nie bylo uruchomione, uzupelnienie daty startu");
		}
		setEndedAtTimestamp(new Timestamp(System.currentTimeMillis()));
		setStatus("-1");		
		System.out.println("data konca i status na -1");
		save();
	}
	
	
	
	
}