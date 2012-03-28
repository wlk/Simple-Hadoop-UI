package zpi.model;

import java.io.Serializable;
import java.sql.Date;

public class MyQueue implements Serializable {

	private int id;
	
	private int algorithmId;

	private int networkId;
	
	private String outputFileLocation;
	
	private Date startedAt;
	
	private Date endedAt;
	
	private String parameters;
	
	private int progress;
	
	private int userId;

	public MyQueue(int id, int algorithmId, int networkId,
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
	
	
	
}
