package org.myorg;

import org.apache.hadoop.io.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AggregateBasicDriver extends Configured implements Tool {

  @Override
        public int run(String[] args) throws Exception {

                if (args.length != 2) {
                        System.out
                                        .printf("Two parameters are required for SecondarySortBasicDriver- <input dir> <output dir>\n");
                        return -1;
                }

                Job job = new Job(getConf());
                job.setJobName("Secondary sort example");

                job.setJarByClass(AggregateBasicDriver.class);
                FileInputFormat.setInputPaths(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(args[1]));

                job.setMapperClass(AggregateBasicMapper.class);
                job.setMapOutputKeyClass(Text.class);
                job.setMapOutputValueClass(IntWritable.class);
                job.setReducerClass(AggregateBasicReducer.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);

                job.setNumReduceTasks(1);

                boolean success = job.waitForCompletion(true);
                return success ? 0 : 1;
        }

        public static void main(String[] args) throws Exception {
                int exitCode = ToolRunner.run(new Configuration(),
                                new AggregateBasicDriver(), args);
                System.exit(exitCode);
        }
}
