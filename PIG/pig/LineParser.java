// Java code for LineParser.java
//
package myudfs;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.data.DataType;
import org.apache.pig.data.DefaultTuple;

public class LineParser extends EvalFunc<Tuple>
{
    public Tuple exec(Tuple input) throws IOException {
        if (null == input || input.size() == 0) {
            return null;
        }

        String line = (String) input.get(0);
        try {

            TupleFactory tf = TupleFactory.getInstance();
            Tuple t = tf.newTuple();
// process the line in specific way you want
//
			char[] l = line.toCharArray();
			String lnStr = "",miStr="",fnStr="",nidStr="",iniStr="";
			int c1=0, c2=0, c3=0, c4=0;

			for (int i = 0; i<l.length; i++)
			{
				if (l[i] == ','){c1 = i;break;}
				lnStr = lnStr + l[i];
			}
			for (int i = c1+1; i<l.length; i++)
			{
				if (l[i] == ','){c2 = i;break;}
				miStr = miStr + l[i];
			}
			for (int i = c2+1; i<l.length; i++)
			{
				if (l[i] == ','){c3 = i;break;}
				fnStr = fnStr + l[i];
			}
			for (int i = c3+1; i<l.length; i++)
			{
				if (l[i] == ','){c4 = i;break;}
				nidStr = nidStr + l[i];
			}
			for (int i = c4+1; i<l.length; i++)
			{
				iniStr = iniStr + l[i];
			}
//end of prcossing the line
            t.append(iniStr);
            t.append(nidStr);
            t.append(fnStr+" "+ miStr+" "+lnStr);

            // The tuple we are returning now has 3 elements, all strings.
            // In order they are: Initials, NetID, and FullName.

            return t;
        } catch (Exception e) {
            // Any problems? Just return null so this tuple doesn't get
            // 'generated' by pig
            return null;
        }
    }

    public Schema outputSchema(Schema input) {
        try {
            Schema s = new Schema();

            s.add(new Schema.FieldSchema("initials", DataType.CHARARRAY));
            s.add(new Schema.FieldSchema("netid", DataType.CHARARRAY));
            s.add(new Schema.FieldSchema("fullname", DataType.CHARARRAY));

            return s;
        } catch (Exception e) {
            // Any problems? Just return null...there probably won't be any
            // problems though.
            return null;
        }
    }

}