package pl.wroc.pwr.zpi.algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
 
/* * 
 * representing graph: Adjacency list
 * 
 * A map task receives
 * Key: node n
 * Value: D (distance from start), points-to (list of nodes reachable from n)
 * ForAll p belong points-to: emit (p, D+1)
 * The reduce task gathers possible distances to a given p and selects the minimum one
 */
public class Dijkstra extends Configured implements Tool {
 
    public static String OUT = "outfile";
    public static String IN = "inputlarger";
    private static int NODE = 1;
 
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
	           String s=Integer.MAX_VALUE+"\t"; //TODO: change for NaN and use it in next map phase
	           //if (key.get()==NODE) s="0 "; //TODO: input parameter (node for calculate distance) instead "1" constant
	           if (key.get()==Integer.parseInt(context.getConfiguration().get("node"))) s="0\t";
	           t.set(s+sb.toString());
	           context.write(key, t);
	       }
	    }
    
    
    public static class TheMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
 
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //Key is node n
            //Value is D, Points-To
            //For every point (or key), look at everything it points to.
            //Emit or write to the points to variable with the current distance + 1
        	Text word = new Text();
            String line = value.toString();//looks like 1 0 2,3,
            String[] sp = line.split("\t");//splits on space
            int distanceadd = Integer.parseInt(sp[1]);
            if (distanceadd < Integer.MAX_VALUE)
            	distanceadd++;
            String[] PointsTo = sp[2].split(",");
            for(int i=0; i<PointsTo.length; i++){
            	if(PointsTo[i].equalsIgnoreCase("UNMODED")) continue;
                word.set("VALUE\t"+distanceadd);//tells me to look at distance value
                context.write(new LongWritable(Integer.parseInt(PointsTo[i])), word);
                word.clear();
            }
            //pass in current node's distance (if it is the lowest distance)
            word.set("VALUE\t"+sp[1]);
            context.write( new LongWritable( Integer.parseInt( sp[0] ) ), word );
            word.clear();
 
            word.set("NODES\t"+sp[2]);//tells me to append on the final tally
            context.write( new LongWritable( Integer.parseInt( sp[0] ) ), word );
            word.clear();
 
        }
    }
 
    public static class TheReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
        public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            //The key is the current point
            //The values are all the possible distances to this point
            //we simply emit the point and the minimum distance value
 
            String nodes = "UNMODED";
            Text word = new Text();
            int lowest = Integer.MAX_VALUE;//start at infinity
 
            for (Text val : values) {//looks like NODES/VALUES 1 0 2,3, we need to use the first as a key
                String[] sp = val.toString().split("\t");//splits on space
                //look at first value
                if(sp[0].equalsIgnoreCase("NODES")){
                    nodes = null;
                    nodes = sp[1];
                }else if(sp[0].equalsIgnoreCase("VALUE")){
                    int distance = Integer.parseInt(sp[1]);
                    lowest = Math.min(distance, lowest);
                }
            }
            word.set(lowest+"\t"+nodes);
            context.write(key, word);
            word.clear();
        }
    }
    
    public static class DijkstraStatisticsMap extends Mapper<LongWritable, Text, IntWritable, LongWritable> {
	      	           
	       public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	           String distance = value.toString().split("\t")[1];
	           System.out.println("distance"+distance);
	           context.write(new IntWritable(Integer.parseInt(distance)), new LongWritable(1l));
	           
	           }
	       }
	     
	           
	    public static class DijkstraStatisticsReduce extends Reducer<IntWritable, LongWritable, IntWritable, LongWritable> {
	    		    	
	       public void reduce(IntWritable key, Iterable<LongWritable> values, Context context) 
	         throws IOException, InterruptedException {
	    	   long size = 0;
	           for (LongWritable val : values) 
	        	   size++;
	           context.write(key, new LongWritable(size));
	       }
	    }
 
    //Almost exactly from http://hadoop.apache.org/mapreduce/docs/current/mapred_tutorial.html
    /*
     * (non-Javadoc)
     * @see org.apache.hadoop.util.Tool#run(java.lang.String[])
     * @parameters: input file (from-to list), output path
     */
    public int run(String[] args) throws Exception {
    	//getConf().set("mapred.textoutputformat.separator", " ");//make the key -> value space separated (for iterations)
    	
    	//set in and out to args.
        IN = args[1]+"/adjList";
        OUT = args[1]+"/dijkstraRes/"; 
        if (args.length>2){
        	NODE = Integer.parseInt(args[3]);
        	System.out.println("parametr przekazywany dobrze: "+NODE);
        }
        getConf().set("node", Integer.toString(NODE));
               	
        ///*
        //JobConf cnf = new JobConf();
        //cnf.set("node", Integer.toString(NODE));
        Job job1 = new Job(getConf(), args[2]+"AdjacencyList");
        
        job1.setJarByClass(Dijkstra.class);
	      
        	job1.setOutputKeyClass(IntWritable.class);
	       job1.setOutputValueClass(IntWritable.class);
	           
	       job1.setMapperClass(AdjacencyListMap.class);
	       job1.setReducerClass(AdjacencyListReduce.class);
	           
	       job1.setInputFormatClass(TextInputFormat.class);
	       job1.setOutputFormatClass(TextOutputFormat.class);
	          
	       FileInputFormat.addInputPath(job1, new Path(args[0]));
	       FileOutputFormat.setOutputPath(job1, new Path(IN));
	       System.out.println("uruchomienie tworzenia listy sasiedztwa");
	       job1.waitForCompletion(true);
	       System.out.println("lista sasiedztwa gotowa");
	      //*/
        
 
        String infile = IN;
        String outputfile = OUT + System.nanoTime();
 
        boolean isdone = false;
        boolean success = false;
        Path ofile = null;
 
        HashMap <Integer, Integer> map = new HashMap<Integer, Integer>();
 
        while(isdone == false){
 
            Job job = new Job(getConf());
            job.setJarByClass(Dijkstra.class);
            job.setJobName(args[2]+"Dijkstra");
            job.setOutputKeyClass(LongWritable.class);
            job.setOutputValueClass(Text.class);
            job.setMapperClass(TheMapper.class);
            job.setReducerClass(TheReducer.class);
            job.setInputFormatClass(TextInputFormat.class); //input data format
            job.setOutputFormatClass(TextOutputFormat.class); // output data format
 
            FileInputFormat.addInputPath(job, new Path(infile));
            FileOutputFormat.setOutputPath(job, new Path(outputfile));
 
            success = job.waitForCompletion(true);
 
            //remove the input file
            if(infile != IN){
                String indir = infile.replace("part-r-00000", "");
                Path ddir = new Path(indir);
                FileSystem dfs = FileSystem.get(getConf());
                dfs.delete(ddir, true);
            }
 
            infile = outputfile+"/part-r-00000";
            outputfile = OUT + System.nanoTime();
 
            //do we need to re-run the job with the new input file??
            isdone = true;//set the job to NOT run again!
            ofile = new Path(infile);
            FileSystem fs = FileSystem.get(new Configuration());
            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(ofile)));
 
            HashMap<Integer, Integer> imap = new HashMap<Integer, Integer>();
            String line=br.readLine();
            while (line != null){
                //each line looks like 0 1 2,3,
                //we need to verify node -> distance doesn't change
                String[] sp = line.split("\t");
                int node = Integer.parseInt(sp[0]);
                int distance = Integer.parseInt(sp[1]);
                imap.put(node, distance);
                line=br.readLine();
            }
            if(map.isEmpty()){
                //first iteration... must do a second iteration regardless!
                isdone = false;
            }else{
                Iterator<Integer> itr = imap.keySet().iterator();
                while(itr.hasNext()){
                    int key = itr.next();
                    int val = imap.get(key);
                    if(map.get(key) != val){
                        //values aren't the same... we aren't at convergence yet
                        isdone = false;
                    }
                }
            }
            if(isdone == false){
                map.putAll(imap);//copy imap to map for the next iteration (if required)
            }
        }
        
        Configuration conf = new Configuration();
        Job job = new Job(conf, args[2]+"Dijkstra Statistics");
        job.setJarByClass(Dijkstra.class);
    	
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(LongWritable.class);

		job.setMapperClass(DijkstraStatisticsMap.class);
		job.setReducerClass(DijkstraStatisticsReduce.class);

		job.setInputFormatClass(TextInputFormat.class); //key - line in file, value line
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, ofile);
		FileOutputFormat.setOutputPath(job, new Path(ofile.toString()+"Statistics"));
		
		job.waitForCompletion(true);
        
        
        return success ? 0 : 1;
    }
 
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Dijkstra(), args);
    	//new Dijkstra().run(args);
    }
}
