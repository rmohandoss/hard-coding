package org.myorg;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class AggregateBasicReducer
        extends
                Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                        Context context) throws IOException, InterruptedException {
        int one=1;
        int sum=0;
                for (IntWritable value : values) {
                        sum+=1;
                }
                context.write(key, new IntWritable(sum));

        }
}

