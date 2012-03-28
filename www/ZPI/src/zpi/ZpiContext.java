package zpi;

import java.io.Serializable;
import java.util.List;

import zpi.hadoopModel.Algorithm;
import zpi.hadoopModel.User;

/**
 * Kontekst aplikacji.
 *
 */
@SuppressWarnings("serial")
public class ZpiContext implements Serializable{

	private User user;

	public ZpiContext() {
	}
	
	public ZpiContext(User user) {
		super();
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<Algorithm> getUserAlgorithms(){
		List<Algorithm> userAlgorithms = Algorithm.getUserAlgorithms(user.getId());
		return userAlgorithms;
	}
	
	public List<Algorithm> getPublicAlgorithms(){
		
		List<Algorithm> algorithms = Algorithm.getPublicAlgorithms();
		
		return algorithms;
	}
	
	
}
