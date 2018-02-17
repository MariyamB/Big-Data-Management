package myudfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.data.DataType;

public class BankTeller extends EvalFunc<DataBag> {
	public DataBag exec(Tuple input) throws IOException {
		BagFactory bf = BagFactory.getInstance();
		DataBag db = bf.newDefaultBag();
		final TupleFactory tf = TupleFactory.getInstance();
		if (null == input || input.size() == 0) {
			return null;
		}

		try {

			// process the line using split string
			String[] fields = input.get(0).toString().split(",");
			String[] bills = fields[1].split("\\W+");

			for (String bill : bills) {
				List<String> a = new ArrayList<String>();

				a.add(fields[0]);
				a.add(bill.trim());

				db.add(tf.newTuple(a));
			}

			return db;

		} catch (Exception e) {
			// Any problems? Just return null so this tuple doesn't get
			// 'generated' by pig
			return null;
		}
	}

	public Schema outputSchema(Schema input) {
		try {
			Schema s = new Schema();

			s.add(new Schema.FieldSchema("name", DataType.CHARARRAY));
			s.add(new Schema.FieldSchema("bill", DataType.INTEGER));

			return s;
		} catch (Exception e) {
			// Any problems? Just return null...there probably won't be any
			// problems though.
			return null;
		}
	}

}
