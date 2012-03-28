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


	public class Algorithm {
		
		public static class AlgorithmMapper extends Mapper<LongWritable, Text, Text, Text> {
			           
			       public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			           //edit start
			    	   
			    	   //edit end			    	   
			           }
			       }
			     
			           
			    public static class AlgorithmReducer extends Reducer<Text, Text, Text, Text> {
			    	
			       public void reduce(Text key, Iterable<Text> values, Context context) 
			         throws IOException, InterruptedException {
			    	   //edit start
			    	   
			    	   //edit end			    	   
			       }
			    }
			    
			    
			    
			    
			    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
				
						Configuration conf = new Configuration();
					         
					    Job job1 = new Job(conf, args[2]+"My algorithm"); //Please, edit name "My algorithm"
					      
					       job1.setOutputKeyClass(Text.class);
					       job1.setOutputValueClass(Text.class);
					           
					       job1.setMapperClass(AlgorithmMapper.class);
					       job1.setReducerClass(AlgorithmReducer.class);
					           
					       job1.setInputFormatClass(TextInputFormat.class);
					       job1.setOutputFormatClass(TextOutputFormat.class);
					          
					       FileInputFormat.addInputPath(job1, new Path(args[0]));
					       FileOutputFormat.setOutputPath(job1, new Path(args[1]));
					           
					       job1.waitForCompletion(true);				    

				}
				    
	}


