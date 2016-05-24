package iterator;

import index.IndexException;

import java.io.IOException;

import tests.ObjectSizeFetcher;
import bufmgr.PageNotReadException;
import global.AttrType;
import global.GlobalConst;
import global.Shape_Geometry;
import global.TupleOrder;
import heap.Heapfile;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

public class SpatialArea extends Iterator implements GlobalConst {

	private AttrType _in1[], _in2[];
	private int in1_len, in2_len;
	private Iterator p_i1, // pointers to the two iterators. If the
			p_i2; // inputs are sorted, then no sorting is done
	private TupleOrder _order; // The sorting order.
	private CondExpr OutputFilter[];

	private boolean get_from_in1, get_from_in2; // state variables for get_next
	private int jc_in1, jc_in2;
	private boolean process_next_block;
	private short inner_str_sizes[];
	private IoBuf io_buf1, io_buf2;
	private Tuple TempTuple1, TempTuple2;
	private Tuple tuple1, tuple2;
	private boolean done;
	private byte _bufs1[][], _bufs2[][];
	private int _n_pages;
	private Heapfile temp_file_fd1, temp_file_fd2;
	private AttrType sortFldType;
	private int t1_size, t2_size;
	private Tuple Jtuple;
	private FldSpec perm_mat[];
	private int nOutFlds;
	private int amt_of_mem;

	public SpatialArea(AttrType[] stypes, int stypes_len, short[] ssizes,
			FileScan am, boolean b, TupleOrder ascending, CondExpr[] outFilter,
			FldSpec[] output, int num_of_ouputFields)
			throws TupleUtilsException, JoinNewFailed, SortException {

		_in1 = new AttrType[stypes.length];
		System.arraycopy(stypes, 0, _in1, 0, stypes.length);
		in1_len = stypes_len;

		Jtuple = new Tuple();
		AttrType[] Jtypes = new AttrType[num_of_ouputFields];
		short[] ts_size = null;
		perm_mat = output;
		nOutFlds = num_of_ouputFields;
		amt_of_mem = 100;

		try {
			ts_size = TupleUtils.setup_op_tuple(Jtuple, Jtypes, stypes,
					stypes_len, ssizes, output, num_of_ouputFields);

		} catch (Exception e) {
			throw new TupleUtilsException(e,
					"Exception is caught by SortMerge.java");
		}
		p_i1 = am;

		// if (!b) {
		// try {
		//
		// p_i1 = new Sort(stypes, (short) stypes_len, ssizes, am,
		// 1, ascending, 3,amt_of_mem / 2);
		// } catch (Exception e) {
		// throw new SortException(e, "Sort failed");
		// }
		// }

		OutputFilter = outFilter;
		get_from_in1 = true;
		io_buf1 = new IoBuf();
		TempTuple1 = new Tuple();
		tuple1 = new Tuple();

		if (io_buf1 == null || TempTuple1 == null || tuple1 == null)
			throw new JoinNewFailed("SpatialArea.java: allocate failed");

		try {
			TempTuple1.setHdr((short) in1_len, _in1, ssizes);
			tuple1.setHdr((short) in1_len, _in1, ssizes);

		} catch (Exception e) {
			throw new SortException(e, "Set header failed");
		}
		t1_size = tuple1.size();
		// t2_size = tuple2.size();

		process_next_block = true;
		done = false;

		_n_pages = 1;
		_bufs1 = new byte[_n_pages][MINIBASE_PAGESIZE];
		// _bufs2 = new byte [_n_pages][MINIBASE_PAGESIZE];

		temp_file_fd1 = null;
		try {
			temp_file_fd1 = new Heapfile(null);

		} catch (Exception e) {
			throw new SortException(e, "Create heap file failed");
		}

		 //sortFldType = _in1[jc_in1-1];

	}

	@Override
	public Tuple get_next() throws IOException, JoinsException, IndexException,
			InvalidTupleSizeException, InvalidTypeException,
			PageNotReadException, TupleUtilsException, PredEvalException,
			SortException, LowMemException, UnknowAttrType,
			UnknownKeyTypeException, Exception {
		// TODO Auto-generated method stub
		int comp_res;
		Tuple _tuple1, _tuple2;
		if (done)
			return null;

		while (true) {
			if (process_next_block) {
				process_next_block = false;
				if (get_from_in1)
					if ((tuple1 = p_i1.get_next()) == null) {
						done = true;
						return null;
					}
				get_from_in1 = get_from_in2 = false;
			}
			//Shape_Geometry sg;
			//long size=ObjectSizeFetcher.getObjectSize(sg);
			int area=0;
			//area=tuple1.getShape_Geometry(perm_mat[0].offset);
//			int a=sg.getX1();
//			int b=sg.getX2();
//			int c=sg.getY1();
//			int d=sg.getY2();
//			double area= Math.abs((a-b)*(c-d));
			Jtuple.setIntFld(0, area);
			return Jtuple;
		}

	}

	@Override
	public void close() throws IOException, JoinsException, SortException,
			IndexException {
		// TODO Auto-generated method stub

	}

}
