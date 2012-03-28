package pl.wroc.pwr.zpi.standAlone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.JobTracker;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TaskTracker;
import org.apache.hadoop.util.Progress;
import org.apache.hadoop.util.RunJar;

import pl.wroc.pwr.zpi.model.Algorithm;
import pl.wroc.pwr.zpi.model.Network;
import pl.wroc.pwr.zpi.model.Queue;

/*
 * class to queueing tasks from database
 * this class make thread 
 * to check queue table in database
 * and run it on hadoop if hadoop has any task to job
 */
public class TaskQueue extends Thread {

	class ThreadJob extends Thread {
		String[] job;
		Queue queue;

		ThreadJob(String[] job, Queue toDo) {
			this.job = job;
			queue = toDo;
			System.out.println("job i queue zapisane w konstruktorze");
		}

		public void run() {
			Process child = null;
			int returnCode = 0;
			boolean hasError = false;

			try {
				
				 StringBuilder sb = new StringBuilder();
	               for (int i = 0; i < job.length; ++i) {
	                       sb.append(job[i]);
	                       sb.append(" ");
	               }
	               System.out.println(sb);
				child = Runtime.getRuntime().exec("hadoop jar " + sb);
				synchronized(queue){
					queue.setStatus("0");
					queue.save();
					child.waitFor();
					//queue.setEndedAt(new Date(System.currentTimeMillis()));
					queue.setEndedAtTimestamp(new Timestamp(System.currentTimeMillis()));
					//queue.setEndedAt(new Timestamp(System.currentTimeMillis()));
					queue.setStatus("1");
					queue.save();
				}
				
				String line;
				BufferedReader input = new BufferedReader(
						new InputStreamReader(child.getInputStream()));
				BufferedReader error = new BufferedReader(
						new InputStreamReader(child.getErrorStream()));
				/*
				 * nie bedziemy pobierac JobID
				
				synchronized(queue){
					while (queue.getHadoopJobId()==null || queue.getHadoopJobId() == "") {
						System.out.println("czekanie na nowa linie tekstu");
						while((line = input.readLine())==null);
						System.out.println("w petli czekania na JobId");
						line = input.readLine();
						System.out.println(line);
						if (line.contains("Running job:")) {
							String[] s = line.split(" ");
							System.out.println("Jest JobID: "+s[s.length - 1]);
							queue.setHadoopJobId(s[s.length - 1]);
							queue.save();
						}
					}
				}
				
				System.out.println("petla czekania na JobId skonczona");
				*/
				while((line = input.readLine())!=null){
					System.out.println(line);
				}
				input.close();
			

				while ((line = error.readLine()) != null) {
					if (line.contains("ERROR") || line.contains("Exception")){
						hasError = true;
					}
					System.out.println(line);
					break;
				}
				error.close();
					
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				returnCode = child.exitValue();
				if (//returnCode != 0 ||
						hasError) {
					//System.exit(returnCode);
					synchronized(queue){
						queue.setStatus("-1");
						queue.save();
					}
				}
			}
		}
	}

	private final int sleepTime = 15000;
	// TODO: parametry do MySql za pomocą frameworka webowego
	// private final String url = "jdbc:mysql://10.10.1.29/zpi";
	// private final String user = "root";
	// private final String pass = "root";
	// TO EDIT ABOVE

	private int progress;
	private Connection connection;
	private List<Queue> queueList;

	public TaskQueue() {
		queueList = Queue.getQueuesToTask();
	}

	public void  run() {
		while (true) { // change to press key
			queueList = Queue.getQueuesToTask();
			if (queueList.isEmpty()) {
				System.out.println("nie ma zadañ do wykonania");
				try {
					Thread.sleep(sleepTime);
					System.out.println("sleep on " + sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					Queue toDo = queueList.remove(0);
					System.out.println("algorytm pobrany z kolejki");
					String jarFile = Algorithm.getByID(toDo.getAlgorithmId())
							.getJarFileLocation();
					String networkFile = Network.getByID(toDo.getNetworkId())
							.getNetworkFileLocation();
					if (toDo.getParameters()==null){ //na szybko
						toDo.setParameters("");
					}
					String[] args = toDo.getParameters().split(" ");
					String[] job = new String[args.length + 4];
					// System.arraycopy(jarFile, 0, job, 0, 1);
					job[0] = jarFile;
					job[1] = networkFile;
					job[2] = "/jobs/results/"+toDo.getUserId()+"/"+toDo.getAlgorithmId()+"/"+System.nanoTime();
					job[3] = String.valueOf(toDo.getId())+"-";
					// System.arraycopy(networkFile, 0, job, 1, 2);
					System.arraycopy(args, 0, job, 4, args.length);
					/*
					System.out.println("job do puszczenia: ");
					for (String val : Arrays.asList(job)) 
						System.out.print(val+" ");
					System.out.println();
					*/
					toDo.setOutputFileLocation(job[2]);
					//toDo.setStartedAt(new Date(System.currentTimeMillis()));
					toDo.setStartedAtTimestamp(new Timestamp(System.currentTimeMillis()));
					toDo.save();
					System.out.println("uruchamiany watek ThreadJob");
					Thread actual = new ThreadJob(job,toDo);
					actual.start();
					new KillerMonitor(actual, toDo);
					
					//RunJar.main(job);
					//progress = new Progress();
					//System.out
					//		.println("starting progress at " + progress.get());
					/*
					 * nie bedziemy pobierac JobID
						while(toDo.getHadoopJobId()==""){
							synchronized(toDo){
								System.out.println("wciaz nie mam JobId "+toDo.getHadoopJobId());
							}
							sleep(5000l);
						}
								
					while (progress < 100) {
						sleep(10000l);
						System.out.println("petla progress");
						 try {			                
							 	Process child = Runtime.getRuntime().exec("hadoop job -status "+toDo.getHadoopJobId());
			                    child.waitFor();
			                    
			                    
			                    String line;
			                    BufferedReader input = new BufferedReader(new InputStreamReader(child.getInputStream()));
			                    BufferedReader error = new BufferedReader(new InputStreamReader(child.getErrorStream()));
			                    float progress=0;
			                    while ((line = input.readLine()) != null) {
			                    		System.out.println(line);
			                            if (line.contains("completion:")){
			                            	String[] s = line.split(" ");
			                            	progress+=Float.parseFloat(s[s.length-1]);
			                            }
			                    }
			                    progress*=50;
			                    toDo.setProgress((int)progress);
			                    toDo.save();
			                    input.close();
			                    
			                    while ((line = error.readLine()) != null) {
			                            System.out.println(line);
			                    }
			                    error.close();
			                    
			            } catch (IOException e) {
			                    e.printStackTrace();
			            } catch (InterruptedException e) {
			                    e.printStackTrace();
			            }
					}
					
					*/
					while (toDo.getStatus()!="1" && toDo.getStatus()!="-1"){
						synchronized(toDo){
							if (toDo.getStatus()=="1")
								System.out.println("Zadanie zakonczone sukcesem.");
						}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	public static void main(String args[]) {
		
		TaskQueue queueMonitor = new TaskQueue();
		System.out.println("monitor kolejki startuje");
		queueMonitor.start();
		
		/*
		//toTests
		String [] job =  {"dijkstra.jar", "hdfs://user/wind/zpi/w1", "4"};
		Queue q = new Queue();
		TaskQueue t = new TaskQueue();
		ThreadJob tj = t.new ThreadJob(job,q);
		tj.start();
		*/
		
	}
	
	class KillerMonitor extends Thread{
		Thread actual;
		Queue aq;
		
		KillerMonitor(Thread actual, Queue q){
			this.actual=actual;
			aq=q;
			System.out.println("Tworze obiekt KillerMonitor");
		}
		
		
		@SuppressWarnings("deprecation")
		public void run(){
			while (aq.getEndedAtTimestamp()==null){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("KillerMonitor wykryl date zakonczenia zadania");
			if (actual!= null && actual.isAlive()){
				System.out.println("KillerMonitor zatrzymuje zadanie");
				actual.stop();
			}
		}
	}

}
