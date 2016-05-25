package org.myorg;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class SecondarySortBasicReducer
        extends
                Reducer<CompositeKeyWritable, Text, Text, IntWritable> {

        @Override
        public void reduce(CompositeKeyWritable key, Iterable<Text> values,
                        Context context) throws IOException, InterruptedException {
        int one=1;
        int sum=0;
        String Noact="Do_Nothing";
        String prevact="";
        String curract="";
                for (Text value : values) {
                curract=value.toString();
                if (sum==0)
                {
                        sum+=1;
                        prevact=value.toString();
                        continue;
                }
                        context.write(new Text(key.getProdCust().split("\t")[0]+"\t"+ prevact+"\t"+curract), new IntWritable(one));
                        prevact=value.toString();
                        sum=sum+1;
                }
                context.write(new Text(key.getProdCust().split("\t")[0]+"\t"+prevact+"\t"+Noact), new IntWritable(one));

        }
}

