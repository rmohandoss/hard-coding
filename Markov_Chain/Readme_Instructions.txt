Here is the source code to implement a Markov Chain for a Retailer who is interested in understanding Customer Behavior. The various states are the activities that a customer tend to perform when he/she comes in contact with a product. Some examples of activities are ‘View’, ‘Click’, ‘Add’ (to cart), ‘Buy’ etc. The wikipedia link could be of interest to you - https://en.wikipedia.org/wiki/Markov_chain

Design: 
----------

The solution is a Hadoop based implementation and involves a specific sequence of Map Reduce steps. The implementation exploits the nuances of secondary sort and and the convenience of Hive to get to the target state. The required input dataset is a holistic ‘activity log’ with the following attributes. Please look for the sample file ‘Activity_log.txt’ 
- The input file is a comma delimited file. 
- Each record/line in this file corresponds to a specific customer activity defined by the following attributes
— Unique identifier for the Product
— Unique identifier for the Customer
— Timestamp (casted as a numeric) of when this activity was performed.
— Unique code the specifies that particular activity

Technology prerequisites & steps:
---------------------------------------------

1)	Make sure JRE is installed in your local Mac/PC. Download the JDK from Oracle’s website. 
2)	Ensure the PATH variable includes the path to the JDK location
3)	Verify the Java version post install - $ java -version
4)	Make sure the Hadoop libraries are installed on your local Mac/PC and is appropriately configured. The following links could be of use— https://hadoop.apache.org/docs/r2.7.2/hadoop-project-dist/hadoop-common/SingleCluster.html
5)	Make sure Hive libraries are installed and configured on your local Mac/PC. Hive could be downloaded from http://www.apache.org/dyn/closer.cgi/hive/
6)	For Hive to work properly, the following environment variables need to set appropriately
	- HIVE_HOME -> Set this to the folder that has the hive libraries
	- HADOOP_HOME -> Set this to the folder that has the Hadoop libraries
7)	Ensure the PATH variable is appended with the locations of the ‘bin’ folders for both Hadoop and Hive.Something like the below
	- $ export PATH=$PATH:/Users/<User id>/Hadoop/hadoop-1.2.1/bin/:/Users/<User id>/hive/hive-0.12.0/bin
8)	Compile the various java programs listed in this page and create a jar file. Please check the following link - https://sites.google.com/site/hadoopandhive/home/how-to-run-and-compile-a-hadoop-program
	 javac -classpath <Hadoop library path>/hadoop-core-1.2.1.jar -d SecSort/ *.java
	 jar -cvf SecSort.jar -C SecSort/ .
9)	For the purpose of this implementation, hadoop is configured to use HDFS as the underlying filesystem. Load the input file (Activity_log.txt) to a specific folder in HDFS. For the current implementation, the file is loaded into a HDFS location ‘/input’
10)	Execute the following 3 steps. 
	 hadoop jar SecSort.jar org.myorg.SecondarySortBasicDriver /input/ /out1/
	 hadoop jar SecSort.jar org.myorg.AggregateBasicDriver /out1/ /out2/
	 hadoop jar SecSort.jar org.myorg.TotalBasicDriver /out2/ /out3/
11)     Create 2 external tables in Hive
	 hive> create external table Product_Activity_State(product string, from_activity string, total_count int)                          
    		 > row format delimited                                                                                                         
    		 > fields terminated by '\t'                                                                          
    		 > location '/out3/';    

	 hive> create external table Product_Activity_State_Trans(product string, from_activity string, to_activity string, total_count int)
    		 > row format delimited                                                                                                         
    		 > fields terminated by '\t'                                                                                                    
    		 > location '/out2/';  
12)	Execute the following Hive Query to get the State transition matrix for each <Product, Activity> combination.
	 hive> select A.product, A.from_activity, A.total_count/B.total_count from product_activity_state_trans A join  product_activity_state B
     	 > on A.product = B.product and A.from_activity=B.from_activity;   


