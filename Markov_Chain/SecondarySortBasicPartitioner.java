/***************************************************************
*Partitioner: SecondarySortBasicPartitioner
***************************************************************/
package org.myorg;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Partitioner;

public class SecondarySortBasicPartitioner extends
        Partitioner<CompositeKeyWritable, Text> {

        @Override
        public int getPartition(CompositeKeyWritable key, Text value,
                        int numReduceTasks) {

                return Math.abs(key.getProdCust().hashCode() % numReduceTasks);
        }
}

