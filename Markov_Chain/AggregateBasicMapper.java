/***************************************************************
*Mapper: AggregateBasicMapper
***************************************************************/
package org.myorg;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class AggregateBasicMapper extends
        Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable key, Text value, Context context)
                        throws IOException, InterruptedException {

                if (value.toString().length() > 0) {
                        String arrAttributes[] = value.toString().split("\t");

                        context.write(
                                        new Text(
                                                        (arrAttributes[0].toString() + "\t" + arrAttributes[1].toString() + "\t" + arrAttributes[2].toString())),
                                                         new IntWritable(Integer.parseInt(arrAttributes[3])));
                }

        }
}

