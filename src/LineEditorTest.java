import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

public class LineEditorTest extends TestCase {
	
	String[] input = { "C:\\Users\\LJS\\workspace\\LineEditor\\test.txt" };
	String[] output = {"C:\\Users\\LJS\\workspace\\LineEditor\\test.txt" };
	
	File_Buffer FILE;
	Init_Exit Start_End;

	File_Buffer FILE_temp;
	Init_Exit temp;

	Cmd_Driver Command_Test;
	UserCmd commandLineTokens;
	private boolean UPDATE_FILE = false;
	
	private final PrintStream systemOut = System.out;
	private ByteArrayOutputStream ln;

	@Before
	public void setUp() throws Exception {
		ln = new ByteArrayOutputStream();
		System.setOut(new PrintStream(ln));
		FILE = new File_Buffer();
		Start_End = new Init_Exit(input, FILE);
		Command_Test = new Cmd_Driver();
		UPDATE_FILE = true;
		super.setUp();
	}
	
	 @After
	   public void restoreStreams() throws IOException {
	      System.setOut(systemOut);
	}	

	@Test
	public void testQ() {
		Command_Test.Cmd_Q(FILE, UPDATE_FILE); 
		
		//assertEquals(FILE.GetLine(1), "@McGinley's early work was primarily shot on 35mm film and using Yashica T4s and Leica R8s. Since 2004, McGinley's style has evolved from documenting his friends in real-life situations towards creating envisioned situations that can be photographed. ");
		
		assertEquals(FILE.NumLins(), 16);
	}

	@Test
	public void testX() {
		Command_Test.Cmd_D(FILE, 1);
		Command_Test.Cmd_X(FILE, UPDATE_FILE); 
		
		//assertEquals(FILE.GetLine(1), "@McGinley's early work was primarily shot on 35mm film and using Yashica T4s and Leica R8s. Since 2004, McGinley's style has evolved from documenting his friends in real-life situations towards creating envisioned situations that can be photographed. ");
		
		assertEquals(FILE.NumLins(), 15);
	}

	@Test
	public void testT() {
		int line = FILE.GetCLN();
		
		Command_Test.Cmd_T(FILE);
		assertEquals(1, FILE.GetCLN());
	}
	
	@Test
	public void testE() {
		int line = FILE.NumLins();
		
		Command_Test.Cmd_E(FILE);
		assertEquals(line, 16);
	}
	
	@Test
	public void testN() {
		FILE.SetCLN(1); //CLN 1�� ����
		int line = FILE.GetCLN();
		int nl = 2;
		int result = 0;

		Command_Test.Cmd_N(FILE, nl); //CLN �Ʒ��� nl��ŭ �̵�
		result = line + nl;
		assertEquals(result, FILE.GetCLN()); //����� CNL�� ���Ͽ� Ȯ��
	}

	@Test
	public void testB() {
		FILE.SetCLN(14); //CLN 14�� ����
		int line = FILE.GetCLN();
		int nl = 4;
		int result = 0;

		Command_Test.Cmd_B(FILE, nl); //CLN ���� nl��ŭ �̵�
		result = line - nl;
		assertEquals(result, FILE.GetCLN()); //����� CNL�� ���Ͽ� Ȯ��
	}

	@Test
	public void testW() {
		Command_Test.Cmd_W(FILE); //Cmd_W��ɾ ����ؼ� �ֿܼ� ����Ѵ�.
	      assertEquals("At Edit File Line "+FILE.GetCLN()+System.getProperty("line.separator"),ln.toString() );
	}
	
	@Test
	public void testC() {
		Command_Test.Cmd_C(FILE); //Cmd_C��ɾ ����ؼ� �ֿܼ� ����Ѵ�.
	      assertEquals("Total Edit File Lines: "+FILE.NumLins()+System.getProperty("line.separator"),ln.toString() );
	}
	
	@Test
	public void testL() throws IOException {
		int end_line = 3;
		FILE_temp = new File_Buffer();
		File file = new File("testL.txt");
		PrintStream p = new PrintStream(new FileOutputStream(file));
		System.setOut(p);

		Command_Test.Cmd_L(FILE, end_line);

		p.close();
		PrintStream console = System.out;
		System.setOut(console);

		temp = new Init_Exit(output, FILE_temp);

		// txt������ CLN���� CLN+end_line ������ ���� console�� ����� ��� ������ for���� ���� ��
		for (int i = 1; i <= end_line; i++)
			assertEquals(FILE.GetLine(i), FILE_temp.GetLine(i));
	}

	@Test
	public void testS() throws IOException {
		int CLN = FILE.GetCLN();
		int end_line = 4;

		FILE_temp = new File_Buffer();
		File file = new File("testS.txt");
		PrintStream p = new PrintStream(new FileOutputStream(file));
		System.setOut(p);

		Command_Test.Cmd_S(FILE, end_line);

		p.close();
		PrintStream console = System.out;
		System.setOut(console);

		temp = new Init_Exit(output, FILE_temp);

		// txt������ CLN���� CLN+end_line ������ ���� console�� ����� ��� ������ for���� ���� ��
		for (int i = 1; i <= end_line; i++)
			assertEquals(FILE.GetLine(i), FILE_temp.GetLine(i));
		// CLN ���� ����Ǿ����� Ȯ��
		assertEquals(CLN, FILE.GetCLN());
	}

	@Test
	public void testD() {
		// �����ڵ��� ���� ���̴� ������ ���ڿ��� Ȯ���� �� ���� ������, CLN�� ���� ������ ���ؼ� Ȯ���Ѵ�.
		String exist = FILE.GetLine(2);

		Command_Test.Cmd_D(FILE, 2); // 2�� line ���ڿ��� ���� ��
		Command_Test.Cmd_Q(FILE, UPDATE_FILE); // ������ ����

		// ������ �� ����� ������ 2���� line����� ���� 2��° line����� �ٸ��� Ȯ���Ѵ�.
		assertNotEquals(exist, FILE.GetLine(2));
		// �߰��� ���������� ������ line�� ���� Ŀ�� ��ġ�� �������� Ȯ���Ѵ�.
		assertEquals(1, FILE.GetCLN());
		// error�߻�! Q������ error, D������ error����
	}

	@Test
	public void testA() {
		String add = "Testing";
		int CLN = FILE.GetCLN();
		try {
			Command_Test.Cmd_A(FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// CLN�� �ִ� �߰��� add�� ��
		assertEquals(FILE.GetLine(FILE.GetCLN()), add);
	}

	@Test
	public void testF() throws IOException {
		int CLN = FILE.GetCLN();

		String find = "Yashica";
		int end_line = 7;
		
		FILE_temp = new File_Buffer();
		File file = new File("testF.txt");
		temp = new Init_Exit(output, FILE_temp);
		PrintStream p = new PrintStream(new FileOutputStream(file));
		System.setOut(p);

		//CLN���� CLN+end_line���� find ���ڿ��� ã�´�.
		Command_Test.Cmd_F(FILE, end_line, find);
		
		p.close();
		PrintStream console = System.out;
		System.setOut(console);

		assert(FILE_temp.GetLine(1).contains(find));
	}

	@Test
	public void testR() {
		int CLN = FILE.GetCLN();
		
		String before = "roll";
		String after = "jinsub";
		int end_line = 5;
		
		Command_Test.Cmd_R(FILE, end_line, before, after);

		assertEquals(after, FILE.GetLine(CLN+end_line) );
	}

	@Test
	public void testY() {
		int CLN = FILE.GetCLN();
		int end_line = 3;

		Command_Test.Cmd_Y(FILE, end_line);
		assertEquals(CLN + end_line - 1, FILE.GetCLN());
	}
	
	@Test
	public void testZ() {
		int CLN = FILE.GetCLN();
		int end_line = 1;
		
		Command_Test.Cmd_Z(FILE, end_line);
		assertEquals(CLN +end_line-1, FILE.GetCLN());
	}

	@Test
	public void testP() {
		int CLN = FILE.GetCLN();
		int end_line = 7;

		Command_Test.Cmd_Y(FILE, end_line);
		Command_Test.Cmd_P(FILE);
		System.out.println(FILE.GetCLN());
		assertEquals(CLN + end_line * 2 - 1, FILE.GetCLN());
	}

	@Test
	public void testI() {
		int CLN = FILE.GetCLN();
		Command_Test.Cmd_I(FILE);

		assertNotEquals(CLN, FILE.GetCLN());
	}

	@Test
	public void testK() throws IOException {
		FILE_temp = new File_Buffer();
		File file = new File("testK.txt");
		PrintStream p = new PrintStream(new FileOutputStream(file));
		System.setOut(p);

		String key = "Yashica";
		Command_Test.Cmd_I(FILE);
		Command_Test.Cmd_K(key);
		p.close();

		PrintStream console = System.out;
		System.setOut(console);

		temp = new Init_Exit(output, FILE_temp);

		assertEquals(true, FILE_temp.GetLine(1).contains(key));
	}

	@Test
	public void testO() {
		
		int end_line = 5;
		String expect = "In 2009, McGinley returned to the studio as he began experimenting within the confines of traditional studio portraiture.";
		Command_Test.Cmd_O(FILE, end_line);
		Command_Test.Cmd_Q(FILE, UPDATE_FILE);

		assertEquals(expect, FILE.GetLine(5));
	}

	@Test
	public void testM() throws IOException {
		int left, right;

		FILE_temp = new File_Buffer();
		File file = new File("testM.txt");
		PrintStream p = new PrintStream(new FileOutputStream(file));
		System.setOut(p);
		
		temp = new Init_Exit(output, FILE_temp);

		left = 4;
		right = 0;

		Command_Test.Cmd_M(left, right);

		p.close();
		
		PrintStream console = System.out;
		System.setOut(console);
		
		assertEquals(FILE_temp.GetLine(3), "REVERSED OR BACKWARDS COLUMN RANGES ARE ILLEGAL:  No action taken.");
		
	}
}