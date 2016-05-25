/***************************************************************
*CustomWritable for the composite key: CompositeKeyWritable
****************************************************************/
package org.myorg;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;


public class CompositeKeyWritable implements Writable,
        WritableComparable<CompositeKeyWritable> {

        private String ProdCust;
        private String ActTS;

        public CompositeKeyWritable() {
        }

        public CompositeKeyWritable(String ProdCust, String ActTS) {
                this.ProdCust = ProdCust;
                this.ActTS = ActTS;
        }

        @Override
        public String toString() {
                return (new StringBuilder().append(ProdCust).append("\t")
                                .append(ActTS)).toString();
        }

        public void readFields(DataInput dataInput) throws IOException {
                ProdCust = WritableUtils.readString(dataInput);
                ActTS = WritableUtils.readString(dataInput);
        }

        public void write(DataOutput dataOutput) throws IOException {
                WritableUtils.writeString(dataOutput, ProdCust);
                WritableUtils.writeString(dataOutput, ActTS);
        }

        public int compareTo(CompositeKeyWritable objKeyPair) {
                // TODO:
                /*
                 * Note: This code will work as it stands; but when CompositeKeyWritable
                 * is used as key in a map-reduce program, it is de-serialized into an
                 * object for comapareTo() method to be invoked;
                 *
                 * To do: To optimize for speed, implement a raw comparator - will
                 * support comparison of serialized representations
                 */
                int result = ProdCust.compareTo(objKeyPair.ProdCust);
                if (0 == result) {
                        result = ActTS.compareTo(objKeyPair.ActTS);
                }
                return result;
        }

       public String getProdCust() {
                return ProdCust;
        }

        public void setProdCust(String ProdCust) {
                this.ProdCust = ProdCust;
        }

        public String getActTS() {
                return ActTS;
        }

        public void setActTS(String ActTS) {
                this.ActTS = ActTS;
        }
}


