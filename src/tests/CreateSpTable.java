package tests;

import iterator.*;
import heap.*;
import global.*;
import index.*;

import java.io.*;
import java.util.*;
import java.lang.*;

import diskmgr.*;
import bufmgr.*;
import btree.*;
import catalog.*;

/**
 * Here is the implementation for the tests. There are N tests performed. We
 * start off by showing that each operator works on its own. Then more
 * complicated trees are constructed. As a nice feature, we allow the user to
 * specify a selection condition. We also allow the user to hardwire trees
 * together.
 */

// Define the Sailor schema
class Cola_Markets {
	public int sid;
	public String sname;
	public Shape_Geometry shape;

	public Cola_Markets(int _sid, String _sname, Shape_Geometry _shape) {
		sid = _sid;
		sname = _sname;
		shape = _shape;
	}
}

class CreateTBDriver implements GlobalConst {

	private boolean OK = true;
	private boolean FAIL = false;
	private Vector sailors;
	private Vector colaMarkets;
	private Vector reserves;

	/**
	 * Constructor
	 */
	public CreateTBDriver() {

		// build Sailor, Boats, Reserves table
		sailors = new Vector();

		colaMarkets = new Vector();
		colaMarkets.addElement(new Cola_Markets(1, "Cola_A",
				new Shape_Geometry(1, 1, 6, 6)));
		colaMarkets.addElement(new Cola_Markets(2, "Cola_B",
				new Shape_Geometry(3, 3, 5, 5)));

		// sailors.addElement(new Sailor(53, "Bob Holloway", 9, 53.6));

		boolean status = OK;
		int numsailors = 25;
		int numcolamarkets = 2;
		int numcolamarket_attrs = 3;

		String dbpath = "/tmp/" + System.getProperty("user.name")
				+ ".minibase.jointestdb";
		String logpath = "/tmp/" + System.getProperty("user.name") + ".joinlog";

		String remove_cmd = "/bin/rm -rf ";
		String remove_logcmd = remove_cmd + logpath;
		String remove_dbcmd = remove_cmd + dbpath;
		String remove_joincmd = remove_cmd + dbpath;

		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
			Runtime.getRuntime().exec(remove_joincmd);
		} catch (IOException e) {
			System.err.println("" + e);
		}

		/*
		 * ExtendedSystemDefs extSysDef = new ExtendedSystemDefs(
		 * "/tmp/minibase.jointestdb", "/tmp/joinlog", 1000,500,200,"Clock");
		 */

		SystemDefs sysdef = new SystemDefs(dbpath, 1000, NUMBUF, "Clock");

		// creating the ColaTable relation
		AttrType[] Stypes = new AttrType[3];
		Stypes[0] = new AttrType(AttrType.attrInteger);
		Stypes[1] = new AttrType(AttrType.attrString);
		Stypes[2] = new AttrType(AttrType.attrShape);

		// SOS
		short[] Ssizes = new short[1];
		Ssizes[0] = 30; // first elt. is 30

		Tuple t = new Tuple();
		try {
			t.setHdr((short) 3, Stypes, Ssizes);
		} catch (Exception e) {
			System.err.println("*** error in Tuple.setHdr() ***");
			status = FAIL;
			e.printStackTrace();
		}

		int size = t.size();

		// inserting the tuple into file "Cola Table"
		RID rid;
		Heapfile f = null;
		try {
			f = new Heapfile("cola_market.in");
		} catch (Exception e) {
			System.err.println("*** error in Heapfile constructor ***");
			status = FAIL;
			e.printStackTrace();
		}

		t = new Tuple(size);
		try {
			t.setHdr((short) 3, Stypes, Ssizes);
		} catch (Exception e) {
			System.err.println("*** error in Tuple.setHdr() ***");
			status = FAIL;
			e.printStackTrace();
		}

		// numcolamarkets

		for (int i = 0; i < numcolamarkets; i++) {
			try {
				t.setIntFld(1, ((Cola_Markets) colaMarkets.elementAt(i)).sid);
				t.setStrFld(2, ((Cola_Markets) colaMarkets.elementAt(i)).sname);
				t.setShapeFld(3,
						((Cola_Markets) colaMarkets.elementAt(i)).shape);
				// System.out.println("In Insert");
				// System.out.println(t.getIntFld(1));
				// System.out.println(t.getStrFld(2));
				// System.out.println(t.getShape_Geometry(3));

			} catch (Exception e) {
				System.err
						.println("*** Heapfile error in Tuple.setStrFld() ***");
				status = FAIL;
				e.printStackTrace();
			}

			/*
			 * for (int i=0; i<numsailors; i++) { try { t.setIntFld(1,
			 * ((Sailor)sailors.elementAt(i)).sid); t.setStrFld(2,
			 * ((Sailor)sailors.elementAt(i)).sname); t.setIntFld(3,
			 * ((Sailor)sailors.elementAt(i)).rating); t.setFloFld(4,
			 * (float)((Sailor)sailors.elementAt(i)).age); } catch (Exception e)
			 * {
			 * System.err.println("*** Heapfile error in Tuple.setStrFld() ***"
			 * ); status = FAIL; e.printStackTrace(); }
			 */

			try {
				rid = f.insertRecord(t.returnTupleByteArray());
			} catch (Exception e) {
				System.err.println("*** error in Heapfile.insertRecord() ***");
				status = FAIL;
				e.printStackTrace();
			}
		}
		if (status != OK) {
			// bail out
			System.err.println("*** Error creating relation for sailors");
			Runtime.getRuntime().exit(1);
		}

		else {
			System.out
					.println("Spatial Table Created and inserted records Successfully");
		}

	}

	public boolean runTests() throws JoinsException, IndexException,
			InvalidTupleSizeException, InvalidTypeException,
			PageNotReadException, TupleUtilsException, PredEvalException,
			SortException, LowMemException, UnknowAttrType,
			UnknownKeyTypeException, IOException, Exception {

		Disclaimer();
		// Query1();

		// Query2();
		// Query3();
		//
		//
		// Query4();
		// Query5();
		// Query6();
		AreaQuery();
		InterSectionQuery();
		DistanceQuery();

		System.out.print("Finished joinss testing" + "\n");

		return true;
	}

	private void DistanceQuery() throws JoinsException,
			InvalidTupleSizeException, InvalidTypeException,
			PageNotReadException, PredEvalException, UnknowAttrType,
			FieldNumberOutOfBoundException, WrongPermat,
			ClassNotFoundException, IOException {
		// TODO Auto-generated method stub

		boolean status = OK;
		System.out
				.println("******************************************Distance Query*****************************************");
		System.out.println("Find the Spatial Distance for given shapes\n");

		CondExpr[] outFilter = new CondExpr[3];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();
		outFilter[2] = new CondExpr();

		Query1_CondExpr(outFilter);

		Tuple t = new Tuple();
		AttrType[] Stypes = new AttrType[3];
		Stypes[0] = new AttrType(AttrType.attrInteger);
		Stypes[1] = new AttrType(AttrType.attrString);
		Stypes[2] = new AttrType(AttrType.attrShape);

		short[] Ssizes = new short[1];
		Ssizes[0] = 30;

		FldSpec[] Sprojection = new FldSpec[3];
		Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);

		CondExpr[] selects = new CondExpr[1];
		selects = null;
		FldSpec[] output = new FldSpec[1];
		output[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);

		AttrType[] outputType = new AttrType[1];
		outputType[0] = new AttrType(AttrType.attrInteger);

		FileScan inter = null;
		try {
			inter = new FileScan("cola_market.in", Stypes, Ssizes, (short) 3,
					(short) 3, Sprojection, null);
		} catch (Exception e) {
			status = FAIL;
			System.err.println("" + e);
		}

		Tuple t3;
		Tuple t4;

		t3 = inter.get_next();
		Shape_Geometry sg1 = (t3.getShape_Geometry(3));
		t4 = inter.get_next();
		Shape_Geometry sg2 = (t4.getShape_Geometry(3));

		double distance = checkDistance(sg1, sg2);
		System.out.println("Distance:" + distance);
	}

	private double checkDistance(Shape_Geometry s1, Shape_Geometry s2) {
		double[] distance = new double[16];
		double min = 0.0;
		if (!overlap(s1, s2)) {
			distance[0] = Math.sqrt(Math.pow(s1.getX1() - s2.getX1(), 2)
					+ Math.pow(s1.getY1() - s2.getY1(), 2));
			distance[1] = Math.sqrt(Math.pow(s1.getX1() - s2.getX2(), 2)
					+ Math.pow(s1.getY1() - s2.getY1(), 2));
			distance[2] = Math.sqrt(Math.pow(s1.getX1() - s2.getX1(), 2)
					+ Math.pow(s1.getY1() - s2.getY2(), 2));
			distance[3] = Math.sqrt(Math.pow(s1.getX1() - s2.getX2(), 2)
					+ Math.pow(s1.getY1() - s2.getY2(), 2));

			distance[4] = Math.sqrt(Math.pow(s1.getX2() - s2.getX1(), 2)
					+ Math.pow(s1.getY1() - s2.getY1(), 2));
			distance[5] = Math.sqrt(Math.pow(s1.getX2() - s2.getX2(), 2)
					+ Math.pow(s1.getY1() - s2.getY1(), 2));
			distance[6] = Math.sqrt(Math.pow(s1.getX2() - s2.getX1(), 2)
					+ Math.pow(s1.getY1() - s2.getY2(), 2));
			distance[7] = Math.sqrt(Math.pow(s1.getX2() - s2.getX2(), 2)
					+ Math.pow(s1.getY1() - s2.getY2(), 2));

			distance[8] = Math.sqrt(Math.pow(s1.getX1() - s2.getX1(), 2)
					+ Math.pow(s1.getY2() - s2.getY1(), 2));
			distance[9] = Math.sqrt(Math.pow(s1.getX1() - s2.getX2(), 2)
					+ Math.pow(s1.getY2() - s2.getY1(), 2));
			distance[10] = Math.sqrt(Math.pow(s1.getX1() - s2.getX1(), 2)
					+ Math.pow(s1.getY2() - s2.getY2(), 2));
			distance[11] = Math.sqrt(Math.pow(s1.getX1() - s2.getX2(), 2)
					+ Math.pow(s1.getY2() - s2.getY2(), 2));

			distance[12] = Math.sqrt(Math.pow(s1.getX2() - s2.getX1(), 2)
					+ Math.pow(s1.getY2() - s2.getY1(), 2));
			distance[13] = Math.sqrt(Math.pow(s1.getX2() - s2.getX2(), 2)
					+ Math.pow(s1.getY2() - s2.getY1(), 2));
			distance[14] = Math.sqrt(Math.pow(s1.getX2() - s2.getX1(), 2)
					+ Math.pow(s1.getY2() - s2.getY2(), 2));
			distance[15] = Math.sqrt(Math.pow(s1.getX2() - s2.getX2(), 2)
					+ Math.pow(s1.getY2() - s2.getY2(), 2));
			min = distance[0];
			for (int i = 1; i < 16; i++) {
				if (distance[i] < min)
					min = distance[i];
			}
		}

		return min;
	}

	private boolean overlap(Shape_Geometry shape1, Shape_Geometry shape2) {
		int s1_left, s1_right, s1_top, s1_bottom;
		int s2_left, s2_right, s2_top, s2_bottom;
		boolean h_overlap = true;
		boolean v_overlap = true;

		s1_left = Math.min(shape1.getX1(), shape1.getX2());
		s1_right = Math.max(shape1.getX1(), shape1.getX2());
		s1_bottom = Math.min(shape1.getY1(), shape1.getY2());
		s1_top = Math.max(shape1.getY1(), shape1.getY2());

		s2_left = Math.min(shape2.getX1(), shape2.getX2());
		s2_right = Math.max(shape2.getX1(), shape2.getX2());
		s2_bottom = Math.min(shape2.getY1(), shape2.getY2());
		s2_top = Math.max(shape2.getY1(), shape2.getY2());

		if (s1_left >= s2_right || s1_right <= s2_left)
			h_overlap = false;
		if (s1_top <= s2_bottom || s1_bottom >= s2_top)
			v_overlap = false;
		return (h_overlap && v_overlap);
	}

	private void InterSectionQuery() throws FieldNumberOutOfBoundException,
			IOException, ClassNotFoundException, JoinsException,
			InvalidTupleSizeException, InvalidTypeException,
			PageNotReadException, PredEvalException, UnknowAttrType,
			WrongPermat {
		// TODO Auto-generated method stub
		System.out.println("Find the Intersection for given points");

		boolean status = OK;
		System.out
				.println("******************************************InterSection Query*****************************************");
		System.out
				.println("Find the Spatial Intersection for given co-ordinates\n");

		CondExpr[] outFilter = new CondExpr[3];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();
		outFilter[2] = new CondExpr();

		Query1_CondExpr(outFilter);

		Tuple t = new Tuple();
		AttrType[] Stypes = new AttrType[3];
		Stypes[0] = new AttrType(AttrType.attrInteger);
		Stypes[1] = new AttrType(AttrType.attrString);
		Stypes[2] = new AttrType(AttrType.attrShape);

		short[] Ssizes = new short[1];
		Ssizes[0] = 30;

		FldSpec[] Sprojection = new FldSpec[3];
		Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);

		CondExpr[] selects = new CondExpr[1];
		selects = null;
		FldSpec[] output = new FldSpec[1];
		output[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);

		AttrType[] outputType = new AttrType[1];
		outputType[0] = new AttrType(AttrType.attrInteger);

		FileScan inter = null;
		try {
			inter = new FileScan("cola_market.in", Stypes, Ssizes, (short) 3,
					(short) 3, Sprojection, null);
		} catch (Exception e) {
			status = FAIL;
			System.err.println("" + e);
		}

		// Tuple t2;
		// List<Integer> x=new ArrayList<Integer>();
		// List<Integer> y=new ArrayList<Integer>();
		// while((t2=inter.get_next()) != null){
		//
		// //t2.print(Stypes);
		// System.out.println(t2.getIntFld(1));
		// System.out.println(t2.getStrFld(2));
		// //System.out.println(t2.getShape_Geometry(3));
		// Shape_Geometry sg=t2.getShape_Geometry(3);
		// int x1=sg.getX1();
		// int x2=sg.getX2();
		// int y1=sg.getY1();
		// int y2=sg.getY2();
		//
		// x.add(x1);
		// x.add(x2);
		// y.add(y1);
		// y.add(y2);
		//
		// // int area=Math.abs(x1-x2)*Math.abs(y1-y2);
		// // System.out.println(area);
		// }

		// Collections.sort(x);
		// Collections.sort(y);
		//
		// int a=x.get(1);
		// int b=x.get(2);
		//
		// int c=y.get(1);
		// int d=y.get(2);
		//
		// System.out.println("The points of intersection are: ("+a+","+c+") and ("+b+","+d+")");

		Tuple t3;
		Tuple t4;

		t3 = inter.get_next();
		Shape_Geometry sg1 = (t3.getShape_Geometry(3));
		t4 = inter.get_next();
		Shape_Geometry sg2 = (t4.getShape_Geometry(3));

		boolean status_val = checkIntersection(sg1, sg2);

	}

	private boolean checkIntersection(Shape_Geometry shape1,
			Shape_Geometry shape2) {
		int s1_left, s1_right, s1_top, s1_bottom;
		int s2_left, s2_right, s2_top, s2_bottom;
		Shape_Geometry result = new Shape_Geometry(0, 0, 0, 0);
		boolean h_overlap = true;
		boolean v_overlap = true;
		boolean flag;

		s1_left = Math.min(shape1.getX1(), shape1.getX2());
		s1_right = Math.max(shape1.getX1(), shape1.getX2());
		s1_bottom = Math.min(shape1.getY1(), shape1.getY2());
		s1_top = Math.max(shape1.getY1(), shape1.getY2());

		s2_left = Math.min(shape2.getX1(), shape2.getX2());
		s2_right = Math.max(shape2.getX1(), shape2.getX2());
		s2_bottom = Math.min(shape2.getY1(), shape2.getY2());
		s2_top = Math.max(shape2.getY1(), shape2.getY2());

		if (s1_left >= s2_right || s1_right <= s2_left)
			h_overlap = false;
		if (s1_top <= s2_bottom || s1_bottom >= s2_top)
			v_overlap = false;
		if (h_overlap && v_overlap) {
			result.setX1(Math.max(s1_left, s2_left));
			result.setY1(Math.max(s1_bottom, s2_bottom));
			result.setX2(Math.min(s1_right, s2_right));
			result.setY2(Math.min(s1_top, s2_top));
			System.out
					.println("(" + result.getX1() + " " + result.getY1() + ") "
							+ " (" + result.getX2() + " " + result.getY2()
							+ ")");
			flag = true;
		} else {
			System.out.println("No Intersection");
			flag = false;
		}
		return flag;
	}

	private void AreaQuery() throws JoinsException, IndexException,
			InvalidTupleSizeException, InvalidTypeException,
			PageNotReadException, TupleUtilsException, PredEvalException,
			SortException, LowMemException, UnknowAttrType,
			UnknownKeyTypeException, IOException, Exception {
		// TODO Auto-generated method stub
		System.out
				.println("****************************Spatial Area Query*********************************");
		boolean status = OK;
		System.out.println("Find the Spatial Area for the Area\n");

		CondExpr[] outFilter = new CondExpr[3];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();
		outFilter[2] = new CondExpr();

		Query1_CondExpr(outFilter);

		Tuple t = new Tuple();
		AttrType[] Stypes = new AttrType[3];
		Stypes[0] = new AttrType(AttrType.attrInteger);
		Stypes[1] = new AttrType(AttrType.attrString);
		Stypes[2] = new AttrType(AttrType.attrShape);

		short[] Ssizes = new short[1];
		Ssizes[0] = 30;

		FldSpec[] Sprojection = new FldSpec[3];
		Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);

		CondExpr[] selects = new CondExpr[1];
		selects = null;
		FldSpec[] output = new FldSpec[1];
		output[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);

		AttrType[] outputType = new AttrType[1];
		outputType[0] = new AttrType(AttrType.attrInteger);

		FileScan am = null;
		try {
			am = new FileScan("cola_market.in", Stypes, Ssizes, (short) 3,
					(short) 3, Sprojection, null);
		} catch (Exception e) {
			status = FAIL;
			System.err.println("" + e);
		}

		Tuple t2;
		while ((t2 = am.get_next()) != null) {

			// t2.print(Stypes);
			System.out.println(t2.getIntFld(1));
			System.out.println(t2.getStrFld(2));
			// System.out.println(t2.getShape_Geometry(3));
			Shape_Geometry sg = t2.getShape_Geometry(3);
			int x1 = sg.getX1();
			int x2 = sg.getX2();
			int y1 = sg.getY1();
			int y2 = sg.getY2();

			int area = Math.abs(x1 - x2) * Math.abs(y1 - y2);
			System.out.println(area);
		}

		// if (status != OK) {
		// // bail out
		// System.err.println("*** Error setting up scan for Cola Market");
		// Runtime.getRuntime().exit(1);
		// }
		//
		// TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
		// SpatialArea sa=null;
		//
		// try {
		// sa = new SpatialArea(Stypes, 3, Ssizes,am,false, ascending,
		// outFilter,
		// output, 1);
		// } catch (Exception e) {
		// System.err.println("*** join error in SpatialArea constructor ***");
		// status = FAIL;
		// System.err.println("" + e);
		// e.printStackTrace();
		// }
		//
		//
		// t = null;
		// System.out.println(sa.get_next());
		//
		// try {
		// while ((t = sa.get_next()) != null) {
		// t.print(outputType);
		//
		// //qcheck1.Check(t);
		// }
		// }
		// catch (Exception e) {
		// System.err.println (""+e);
		// e.printStackTrace();
		// status = FAIL;
		// }
		// if (status != OK) {
		// //bail out
		// System.err.println ("*** Error in get next tuple ");
		// Runtime.getRuntime().exit(1);
		// }
		//
		// //qcheck1.report(1);
		// try {
		// sa.close();
		// }
		// catch (Exception e) {
		// status = FAIL;
		// e.printStackTrace();
		// }
		// System.out.println ("\n");
		// if (status != OK) {
		// //bail out
		// System.err.println ("*** Error in closing ");
		// Runtime.getRuntime().exit(1);
		// }

	}

	private void Query1_CondExpr(CondExpr[] expr) {

		expr[0].next = null;
		expr[0].op = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrSymbol);
		expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
		expr[0].operand2.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), 1);

		expr[1].op = new AttrOperator(AttrOperator.aopEQ);
		expr[1].next = null;
		expr[1].type1 = new AttrType(AttrType.attrSymbol);
		expr[1].type2 = new AttrType(AttrType.attrInteger);
		expr[1].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), 2);
		expr[1].operand2.integer = 1;

		expr[2] = null;
	}

	public void Query1() {

		System.out
				.print("**********************Query1 strating *********************\n");
		boolean status = OK;

		// Sailors, Boats, Reserves Queries.
		System.out.print("Query: Find the names of sailors who have reserved "
				+ "boat number 1.\n"
				+ "       and print out the date of reservation.\n\n"
				+ "  SELECT S.sname, R.date\n"
				+ "  FROM   Sailors S, Reserves R\n"
				+ "  WHERE  S.sid = R.sid AND R.bid = 1\n\n");

		System.out
				.print("\n(Tests FileScan, Projection, and Sort-Merge Join)\n");

		CondExpr[] outFilter = new CondExpr[3];
		outFilter[0] = new CondExpr();
		outFilter[1] = new CondExpr();
		outFilter[2] = new CondExpr();

		Query1_CondExpr(outFilter);

		Tuple t = new Tuple();

		AttrType[] Stypes = new AttrType[4];
		Stypes[0] = new AttrType(AttrType.attrInteger);
		Stypes[1] = new AttrType(AttrType.attrString);
		Stypes[2] = new AttrType(AttrType.attrInteger);
		Stypes[3] = new AttrType(AttrType.attrReal);

		// SOS
		short[] Ssizes = new short[1];
		Ssizes[0] = 30; // first elt. is 30

		FldSpec[] Sprojection = new FldSpec[4];
		Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
		Sprojection[3] = new FldSpec(new RelSpec(RelSpec.outer), 4);

		CondExpr[] selects = new CondExpr[1];
		selects = null;

		FileScan am = null;
		try {
			am = new FileScan("sailors.in", Stypes, Ssizes, (short) 4,
					(short) 4, Sprojection, null);
		} catch (Exception e) {
			status = FAIL;
			System.err.println("" + e);
		}

		if (status != OK) {
			// bail out
			System.err.println("*** Error setting up scan for sailors");
			Runtime.getRuntime().exit(1);
		}

		AttrType[] Rtypes = new AttrType[3];
		Rtypes[0] = new AttrType(AttrType.attrInteger);
		Rtypes[1] = new AttrType(AttrType.attrInteger);
		Rtypes[2] = new AttrType(AttrType.attrString);

		short[] Rsizes = new short[1];
		Rsizes[0] = 15;
		FldSpec[] Rprojection = new FldSpec[3];
		Rprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		Rprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		Rprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);

		FileScan am2 = null;
		try {
			am2 = new FileScan("reserves.in", Rtypes, Rsizes, (short) 3,
					(short) 3, Rprojection, null);
		} catch (Exception e) {
			status = FAIL;
			System.err.println("" + e);
		}

		if (status != OK) {
			// bail out
			System.err.println("*** Error setting up scan for reserves");
			Runtime.getRuntime().exit(1);
		}

		FldSpec[] proj_list = new FldSpec[2];
		proj_list[0] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		proj_list[1] = new FldSpec(new RelSpec(RelSpec.innerRel), 3);

		AttrType[] jtype = new AttrType[2];
		jtype[0] = new AttrType(AttrType.attrString);
		jtype[1] = new AttrType(AttrType.attrString);

		TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
		SortMerge sm = null;
		try {
			sm = new SortMerge(Stypes, 4, Ssizes, Rtypes, 3, Rsizes, 1, 4, 1,
					4, 10, am, am2, false, false, ascending, outFilter,
					proj_list, 2);
		} catch (Exception e) {
			System.err.println("*** join error in SortMerge constructor ***");
			status = FAIL;
			System.err.println("" + e);
			e.printStackTrace();
		}

		if (status != OK) {
			// bail out
			System.err.println("*** Error constructing SortMerge");
			Runtime.getRuntime().exit(1);
		}

		QueryCheck qcheck1 = new QueryCheck(1);

		t = null;

		try {
			while ((t = sm.get_next()) != null) {
				t.print(jtype);

				qcheck1.Check(t);
			}
		} catch (Exception e) {
			System.err.println("" + e);
			e.printStackTrace();
			status = FAIL;
		}
		if (status != OK) {
			// bail out
			System.err.println("*** Error in get next tuple ");
			Runtime.getRuntime().exit(1);
		}

		qcheck1.report(1);
		try {
			sm.close();
		} catch (Exception e) {
			status = FAIL;
			e.printStackTrace();
		}
		System.out.println("\n");
		if (status != OK) {
			// bail out
			System.err.println("*** Error in closing ");
			Runtime.getRuntime().exit(1);
		}
	}

	private void Disclaimer() {
		System.out
				.print("\n\nAny resemblance of persons in this database to"
						+ " people living or dead\nis purely coincidental. The contents of "
						+ "this database do not reflect\nthe views of the University,"
						+ " the Computer  Sciences Department or the\n"
						+ "developers...\n\n");
	}
}

public class CreateSpTable {
	public static List<String> fileNames = new ArrayList<String>();
	public static Map<String,ArrayList<String>> syntax=new HashMap<String,ArrayList<String>>();

	public static void main(String argv[]) {
		boolean sortstatus = false;
		// SystemDefs global = new SystemDefs("bingjiedb", 100, 70, null);
		// JavabaseDB.openDB("/tmp/nwangdb", 5000);

		CreateTBDriver jjoin = new CreateTBDriver();

		try {
			sortstatus = jjoin.runTests();
		} catch (JoinsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTupleSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PageNotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TupleUtilsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PredEvalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LowMemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknowAttrType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownKeyTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sortstatus != true) {
			System.out.println("Error ocurred during join tests");
		} else {
			System.out.println("join tests completed successfully");
		}
	}

	public static void CreateQuery(String[] arguments, String fileName) {
		// TODO Auto-generated method stub
		AttrType[] Stypes = new AttrType[arguments.length];
		boolean status;
		Heapfile f = null;
		ArrayList<String> attributeList=new ArrayList<String>();
		if (!fileNames.contains(fileName)) {

			for (int i = 0; i < arguments.length; i++) {
				String dataType = null;
				if (arguments[i].contains("VARCHAR2")) {
					dataType = "STRING";
				}
				else if(arguments[i].contains("NUMBER")){
					dataType="NUMBER";
				}
				else if(arguments[i].contains("SDO_GEOMETRY")){
					dataType="SDO_GEOMETRY";
				}
				switch (dataType) {

				case "NUMBER":
					Stypes[i] = new AttrType(AttrType.attrInteger);
					attributeList.add("NUMBER");
					break;

				case "STRING":
					Stypes[i] = new AttrType(AttrType.attrString);
					attributeList.add("STRING");
					break;

				case "SDO_GEOMETRY":
					Stypes[i] = new AttrType(AttrType.attrShape);
					attributeList.add("SDO_GEOMETRY");
					break;

				}
			}

			short[] Ssizes = new short[1];
			Ssizes[0] = 30;

			Tuple t = new Tuple();
			try {
				t.setHdr((short) arguments.length, Stypes, Ssizes);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setHdr() ***");
				status = false;
				e.printStackTrace();
			}

			int size = t.size();

			// inserting the tuple into file "Cola Table"
			
			
			try {
				f = new Heapfile(fileName);
				System.out.println("Table Created");
				fileNames.add(fileName);
				syntax.put(fileName, attributeList);
			} catch (Exception e) {
				System.err.println("*** error in Heapfile constructor ***");
				status = false;
				e.printStackTrace();
			}

			t = new Tuple(size);
			try {
				t.setHdr((short) arguments.length, Stypes, Ssizes);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setHdr() ***");
				status = false;
				e.printStackTrace();
			}
		}
		else{
			System.out.println("The table already exists");
		}
		//return f;

	}

	public static void connectDb() {
		// TODO Auto-generated method stub
		String dbpath = "/tmp/" + System.getProperty("user.name")
				+ ".minibase.jointestdb";
		String logpath = "/tmp/" + System.getProperty("user.name") + ".joinlog";

		String remove_cmd = "/bin/rm -rf ";
		String remove_logcmd = remove_cmd + logpath;
		String remove_dbcmd = remove_cmd + dbpath;
		String remove_joincmd = remove_cmd + dbpath;

		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
			Runtime.getRuntime().exec(remove_joincmd);
		} catch (IOException e) {
			System.err.println("" + e);
		}

		/*
		 * ExtendedSystemDefs extSysDef = new ExtendedSystemDefs(
		 * "/tmp/minibase.jointestdb", "/tmp/joinlog", 1000,500,200,"Clock");
		 */

		SystemDefs sysdef = new SystemDefs(dbpath, 1000, 50, "Clock");
	}

	public static void InsertQuery(int id, String name, Shape_Geometry sg, String table) throws FieldNumberOutOfBoundException, IOException, HFException, HFBufMgrException, HFDiskMgrException {
		
		if(fileNames.contains(table.toLowerCase()+".in")){
			
			AttrType[] Stypes = new AttrType[3];
			Stypes[0] = new AttrType(AttrType.attrInteger);
			Stypes[1] = new AttrType(AttrType.attrString);
			Stypes[2] = new AttrType(AttrType.attrShape);

			// SOS
			short[] Ssizes = new short[1];
			Ssizes[0] = 30; // first elt. is 30

			Tuple t = new Tuple();
			try {
				t.setHdr((short) 3, Stypes, Ssizes);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setHdr() ***");
				//status = FAIL;
				e.printStackTrace();
			}

			
			t.setIntFld(1,id);
			t.setStrFld(2, name);
			t.setShapeFld(3,sg);
			RID rid;
			
			Heapfile f=new Heapfile(table+".in");
			
			try {
				rid = f.insertRecord(t.returnTupleByteArray());
			} catch (Exception e) {
				System.err.println("*** error in Heapfile.insertRecord() ***");
				//status = FAIL;
				e.printStackTrace();
			}
		}
		else{
			System.out.println("The table does not exist");
		}
		
	}
}
