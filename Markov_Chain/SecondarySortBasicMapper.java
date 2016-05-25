/***************************************************************
*Mapper: SecondarySortBasicMapper
***************************************************************/
package org.myorg;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class SecondarySortBasicMapper extends
        Mapper<LongWritable, Text, CompositeKeyWritable, Text> {

        @Override
        public void map(LongWritable key, Text value, Context context)
                        throws IOException, InterruptedException {

                if (value.toString().length() > 0) {
                        String arrAttributes[] = value.toString().split(",");

                        context.write(
                                        new CompositeKeyWritable(
                                                        (arrAttributes[0].toString() + "\t" + arrAttributes[1].toString()),
                                                        (arrAttributes[2].toString())), new Text(arrAttributes[3]));
                }

        }
}

