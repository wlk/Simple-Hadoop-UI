package pl.wroc.pwr.zpi.algorithms;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

//import org.apache.hadoop.util.*;

// Algorytm PYMK wyznacza N propozycji znajomo�ci dla danego u�ytkownika
// Domy�lnie N = 5

// Wej�cie: Lista znajomo�ci postaci: userid1 userid2
//Wyj�cie: Posortowana lista rekomendacji postaci: userid proponowana znajomo�� => liczba wsp�lnych znajomych,...

	public class PYMK {
		
		public static class AdjacencyListMap extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
			       private Text first = new Text();
			       private Text second = new Text();
			           
			       public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			           String line = value.toString();
			           StringTokenizer tokenizer = new StringTokenizer(line);
			               first.set(tokenizer.nextToken());
			               second.set(tokenizer.nextToken());
			               context.write(new IntWritable(Integer.parseInt(first.toString())), new IntWritable(Integer.parseInt(second.toString())));
			               
			           }
			       }
			     
			           
			    public static class AdjacencyListReduce extends Reducer<IntWritable, IntWritable, IntWritable, Text> {
			    	private Text t = new Text();
			    	
			       public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) 
			         throws IOException, InterruptedException {
			           StringBuffer sb = new StringBuffer();
			           for (IntWritable val : values) 
			        	   if(sb.length() == 0){
			        		   sb = sb.append(val.toString());
			        	   }
			        	   else{
			               sb = sb.append(",".concat(val.toString()));
			        	   }
			            
			           t.set(sb.toString());
			           context.write(key, t);
			       }
			    }
			    
			    
			    public static class NumberOfMutualFriendsMap extends Mapper<LongWritable, Text, Text, IntWritable> {
				       private String person = "";
				       private String adjacencyList = "";
				       private Text t = new Text();
				       List<Integer> list;  
				       public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
				           String line = value.toString();
				           StringTokenizer tokenizer = new StringTokenizer(line);
				           list = new ArrayList<Integer>();  
				               person = tokenizer.nextToken();
				               adjacencyList = tokenizer.nextToken();
				               tokenizer = new StringTokenizer(adjacencyList);
				               while(tokenizer.hasMoreTokens()){
				            	   
				            	   list.add(Integer.parseInt(tokenizer.nextToken(",")));
				               }
				               Collections.sort(list);
				               for(int friend1 : list){
				            	   String pair = person+" "+friend1;
				            	   t.set(pair);
				            	   context.write(t, new IntWritable(0));
				            	   
				            	   for(int friend2 : list){

				            		   	if(friend2 != friend1){
				            		   	   pair = friend1+" "+friend2;
						            	   t.set(pair);
						            	   context.write(t, new IntWritable(1));
				            		 	   
				            		   	}
				            	   
				            	   }
				               }
				           }
				       }
				     
				           
				    public static class NumberOfMutualFriendsReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
				    	private Text t = new Text();
				    	
				       public void reduce(Text key, Iterable<IntWritable> values, Context context) 
				         throws IOException, InterruptedException {
				           int sum=0;
				           for (IntWritable val : values) {
				               sum += val.get();
				           }
				           
				           if(sum > 0 )context.write(key, new IntWritable(sum));
				       }
				    }
				    
				    
				    public static class RecommendationListMap extends Mapper<LongWritable, Text, IntWritable, Text> {
					       
					       private Text t = new Text();
					       List<Integer> list;  
					       public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
					           String line = value.toString();
					           StringTokenizer tokenizer = new StringTokenizer(line);
					           String p1,p2, count;
					           p1 = tokenizer.nextToken();
					           p2 = tokenizer.nextToken();
					           count = tokenizer.nextToken();
					           t.set(p2+" "+count);
					           context.write(new IntWritable(Integer.parseInt(p1)), t);
					           				                        
					       }
				    }
					           
					    public static class RecommendationListReduce extends Reducer<IntWritable, Text, IntWritable, Text> {
					    	private Text t =  new Text();
					       public void reduce(IntWritable key, Iterable<Text> values, Context context) 
					         throws IOException, InterruptedException {
					    	 String text = "";
					    	 int p1,p2;
					    	 List<Point2D> list = new ArrayList<Point2D>();
					           for (Text val : values) {
					        	   StringTokenizer tokenizer = new StringTokenizer(val.toString());
					        	   p1 = Integer.parseInt(tokenizer.nextToken());
						           p2 = Integer.parseInt(tokenizer.nextToken());
						           list.add(new Point(p1,p2));
					        	   
					           }
					        
					           Collections.sort(list,new Comparator<Point2D>() {

					        	   public int compare(Point2D o1, Point2D o2) {
					        	       return (int)(o2.getY() - o1.getY());
					        	   }
					        	   });
					        if(list.size() > 5)   
					        for(int i = 0; i<5; i++){
					        	text = text.concat("( "+(int)(list.get(i).getX())+ " => "+(int)(list.get(i).getY())+" ) ");
					        }  
					        else
					        	for(Point2D p : list){
					        		text = text.concat("( "+(int)(p.getX())+ " => "+(int)(p.getY())+" ) ");
					        	}
					        t.set(text);   
							context.write(key, t);
								
					       }
					    
					    
					    }
			    
			    
			    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
				
					Configuration conf = new Configuration();
					         
					          Job job1 = new Job(conf, args[2]+"AdjacencyList");
					      
					       job1.setOutputKeyClass(IntWritable.class);
					       job1.setOutputValueClass(IntWritable.class);
					           
					      job1.setMapperClass(AdjacencyListMap.class);
					       job1.setReducerClass(AdjacencyListReduce.class);
					           
					       job1.setInputFormatClass(TextInputFormat.class);
					       job1.setOutputFormatClass(TextOutputFormat.class);
					          
					       FileInputFormat.addInputPath(job1, new Path(args[0]));
					       FileOutputFormat.setOutputPath(job1, new Path(args[1]+"/AdjacencyList/"));
					           
					       job1.waitForCompletion(true);
					       
					       Job job2 = new Job(conf, args[2]+"NumberOfMutualFriends");
						      
					       job2.setOutputKeyClass(Text.class);
					       job2.setOutputValueClass(IntWritable.class);
					           
					       job2.setMapperClass(NumberOfMutualFriendsMap.class);
					       job2.setReducerClass(NumberOfMutualFriendsReduce.class);
					           
					       job2.setInputFormatClass(TextInputFormat.class);
					       job2.setOutputFormatClass(TextOutputFormat.class);
					          
					       FileInputFormat.addInputPath(job2, new Path(args[1]+"/AdjacencyList/"));
					       FileOutputFormat.setOutputPath(job2, new Path(args[1]+"/NumberOfMutualFriends/"));
					           
					       job2.waitForCompletion(true);
					       
					       
					       Job job3 = new Job(conf, args[2]+"RecommendationList");
						      
					       job3.setOutputKeyClass(IntWritable.class);
					       job3.setOutputValueClass(Text.class);
					           
					       job3.setMapperClass(RecommendationListMap.class);
					       job3.setReducerClass(RecommendationListReduce.class);
					           
					       job3.setInputFormatClass(TextInputFormat.class);
					       job3.setOutputFormatClass(TextOutputFormat.class);
					          
					       FileInputFormat.addInputPath(job3, new Path(args[1]+"/NumberOfMutualFriends/"));
					       FileOutputFormat.setOutputPath(job3, new Path(args[1]+"/RecommendationList/"));
					           
					       job3.waitForCompletion(true);

				}
				    
	}


