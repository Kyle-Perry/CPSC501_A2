import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.List;

import org.junit.BeforeClass;

public class testInspector {
	private class tester implements Comparable{
		public int val1;
		public char val2;
		public String val3;
		public static final boolean val4 = true;
		byte val5;
		private short val6;
		protected long val7;
		public double val8;
		public float[] val9 = new float[4];
		public int[][] val10 = new int[3][3];
		public String[] val11 = new String[3];
		public List<Integer> val12;


		public tester() {
		}

		@Override
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		public void func(int a) {}
		private int foo(byte b[], byte b2, boolean z) {return 0;}
		protected char[] bar() {char[] a = {'a','b','c'}; return a;}
	}
	static tester testObj;
	static Inspector testInsp;

	@BeforeClass
	public static void intializeTest() {

		testInspector temp = new testInspector();
		testInsp = new Inspector();

		testObj = temp.new tester();
		testObj.val1 = 2147483647;
		testObj.val2 = 'a';
		testObj.val3 = "test string";
		testObj.val5 = 0b01011100;
		testObj.val6 = 10000;
		testObj.val7 = Long.MAX_VALUE;
		testObj.val8 = 123.456789;
		testObj.val9[0] = (float) 1.0;
		testObj.val9[1] = (float) 2.1;
		testObj.val9[2] = (float) 3.2;
		testObj.val9[3] = (float) 4.3;
		for(int i = 0; i < 9; i++) {
			testObj.val10[i/3][i%3] = i;
		}
		testObj.val11[0] = "this ";
		testObj.val11[1] = "is a ";
		testObj.val11[2] = "string array";
		testObj.val12 = null;


	}

	@Test
	public void testModifiers1() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[0];
		int mods = cur.getModifiers();
		assertTrue("Testing public int val1 modifiers, expected \"public \"", testInsp.modifierString(mods).compareTo("public ") == 0);
	}

	@Test
	public void testModifiers2() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[3];
		int mods = cur.getModifiers();
		assertTrue("Testing public static final boolean val4 modifiers, expected \"public static final  \"", testInsp.modifierString(mods).compareTo("public static final ") == 0);
	}

	@Test
	public void testModifiers3() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[4];
		int mods = cur.getModifiers();
		assertTrue("Testing byte val5 modifiers, expected \"\"", testInsp.modifierString(mods).compareTo("") == 0);
	}

	@Test
	public void testModifiers4() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[5];
		int mods = cur.getModifiers();
		assertTrue("Testing private short val6 modifiers, expected \"private \"", testInsp.modifierString(mods).compareTo("private ") == 0);
	}

	@Test
	public void testModifiers5() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[6];
		int mods = cur.getModifiers();
		assertTrue("Testing protected long val7 modifiers, expected \"protected \"", testInsp.modifierString(mods).compareTo("protected ") == 0);
	}

	@Test
	public void testFieldVal1() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[0];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val1 expecting 2147483647", testInsp.getFieldVal(cur.getType(), val).compareTo("2147483647") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal2() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[1];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val2 expecting 'a'", testInsp.getFieldVal(cur.getType(), val).compareTo("a") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal3() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[2];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val3 expecting String@hashcode", testInsp.getFieldVal(cur.getType(), val).compareTo("test string") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal4() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[3];
		try {
			Object val = cur.get(null);
			assertTrue("Testing field val for val4 expecting true", testInsp.getFieldVal(cur.getType(), val).compareTo("true") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal5() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[4];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val5 expecting " + 0b01011100, testInsp.getFieldVal(cur.getType(), val).compareTo("" + 0b01011100) == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal6() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[5];
		try {
			cur.setAccessible(true);
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val6 expecting 10000", testInsp.getFieldVal(cur.getType(), val).compareTo("10000") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal7() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[6];
		try {
			cur.setAccessible(true);
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val7 expecting " + Long.MAX_VALUE, testInsp.getFieldVal(cur.getType(), val).compareTo("" + Long.MAX_VALUE) == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal8() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[7];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val8 expecting 123.456789", testInsp.getFieldVal(cur.getType(), val).compareTo("123.456789") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal9() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[8];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val9 expecting 2147483647", testInsp.getFieldVal(cur.getType(), val).compareTo("[1.0, 2.1, 3.2, 4.3]") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal10() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[9];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val10 expecting 2147483647", testInsp.getFieldVal(cur.getType(), val).compareTo("[[0, 1, 2], [3, 4, 5], [6, 7, 8]]") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal11() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[10];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val11 expecting [String@hashcode, String@hashcode, String@hashcode]", testInsp.getFieldVal(cur.getType(), val).compareTo("[this , is a , string array]") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}

	@Test
	public void testFieldVal12() {
		Field[] fields = testObj.getClass().getDeclaredFields();
		Field cur = fields[11];
		try {
			Object val = cur.get(testObj);
			assertTrue("Testing field val for val12 expecting null", testInsp.getFieldVal(cur.getType(), val).compareTo("null") == 0);
		}
		catch (Exception e) {
			assertTrue(e.toString() + " thrown when retrieving value, test has failed", false);
		}
	}
	
	@Test
	public void testParameter1() {
		Method[] ms = tester.class.getDeclaredMethods();
		String mString = testInsp.getParameterString(ms[0].getParameters());
		assertTrue("Testing parameter list for constructor of tester, actual=\""+ mString +"\" expected=\"java.lang.Object\"", mString.compareTo("java.lang.Object") == 0);
	}
	
	@Test
	public void testParameter2() {
		Method[] ms = tester.class.getDeclaredMethods();
		String mString = testInsp.getParameterString(ms[1].getParameters());
		assertTrue("Testing parameter list for constructor of tester, actual=\""+ mString +"\" expected=\"int\"", mString.compareTo("int") == 0);
	}
	
	@Test
	public void testParameter3() {
		Method[] ms = tester.class.getDeclaredMethods();
		String mString = testInsp.getParameterString(ms[2].getParameters());
		assertTrue("Testing parameter list for constructor of tester, actual=\""+ mString +"\" expected=\"\"", mString.compareTo("byte[], byte, boolean") == 0);
	}
	
	@Test
	public void testParameter4() {
		Method[] ms = tester.class.getDeclaredMethods();
		String mString = testInsp.getParameterString(ms[3].getParameters());
		assertTrue("Testing parameter list for constructor of tester, actual=\""+ mString +"\" expected=\"\"", mString.compareTo("") == 0);
	}
}
