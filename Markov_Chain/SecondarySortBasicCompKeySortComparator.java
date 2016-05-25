/***************************************************************
*SortComparator: SecondarySortBasicCompKeySortComparator
*****************************************************************/
package org.myorg;

import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableComparable;

public class SecondarySortBasicCompKeySortComparator extends WritableComparator {

  protected SecondarySortBasicCompKeySortComparator() {
                super(CompositeKeyWritable.class, true);
        }

        @Override
        public int compare(WritableComparable w1, WritableComparable w2) {
                CompositeKeyWritable key1 = (CompositeKeyWritable) w1;
                CompositeKeyWritable key2 = (CompositeKeyWritable) w2;

                int cmpResult = key1.getProdCust().compareTo(key2.getProdCust());
                if (cmpResult == 0)// same ProdCust
                {
                        return key1.getActTS()
                                        .compareTo(key2.getActTS());
                        //If the minus is taken out, the values will be in
                        //ascending order
                }
                return cmpResult;
        }
}

