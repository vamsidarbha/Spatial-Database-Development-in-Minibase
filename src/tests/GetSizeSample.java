package tests;

import global.Shape_Geometry;

import java.lang.instrument.Instrumentation;

public class GetSizeSample {
	private static Instrumentation instrumentation;
	public static void main(String args[]) {
		
		Shape_Geometry s=new Shape_Geometry(0,0,0,0);
		System.out.println(s);
		
		System.out.println(GetSizeSample.getObjectSize(s));
		

	}
	public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }
	
	public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }

}
