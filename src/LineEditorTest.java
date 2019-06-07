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
		FILE.SetCLN(1); //CLN 1로 정의
		int line = FILE.GetCLN();
		int nl = 2;
		int result = 0;

		Command_Test.Cmd_N(FILE, nl); //CLN 아래로 nl만큼 이동
		result = line + nl;
		assertEquals(result, FILE.GetCLN()); //변경된 CNL을 비교하여 확인
	}

	@Test
	public void testB() {
		FILE.SetCLN(14); //CLN 14로 정의
		int line = FILE.GetCLN();
		int nl = 4;
		int result = 0;

		Command_Test.Cmd_B(FILE, nl); //CLN 위로 nl만큼 이동
		result = line - nl;
		assertEquals(result, FILE.GetCLN()); //변경된 CNL을 비교하여 확인
	}

	@Test
	public void testW() {
		Command_Test.Cmd_W(FILE); //Cmd_W명령어를 사용해서 콘솔에 출력한다.
	      assertEquals("At Edit File Line "+FILE.GetCLN()+System.getProperty("line.separator"),ln.toString() );
	}
	
	@Test
	public void testC() {
		Command_Test.Cmd_C(FILE); //Cmd_C명령어를 사용해서 콘솔에 출력한다.
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

		// txt파일의 CLN부터 CLN+end_line 까지의 행이 console에 저장된 행과 같은지 for문을 통해 비교
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

		// txt파일의 CLN부터 CLN+end_line 까지의 행이 console에 저장된 행과 같은지 for문을 통해 비교
		for (int i = 1; i <= end_line; i++)
			assertEquals(FILE.GetLine(i), FILE_temp.GetLine(i));
		// CLN 값이 변경되었는지 확인
		assertEquals(CLN, FILE.GetCLN());
	}

	@Test
	public void testD() {
		// 원시코드의 수정 없이는 삭제된 문자열을 확인할 수 없기 때문에, CLN과 고의 오류를 통해서 확인한다.
		String exist = FILE.GetLine(2);

		Command_Test.Cmd_D(FILE, 2); // 2번 line 문자열을 삭제 후
		Command_Test.Cmd_Q(FILE, UPDATE_FILE); // 저장한 다음

		// 삭제된 후 저장된 파일의 2번쨰 line문장과 기존 2번째 line문장과 다른지 확인한다.
		assertNotEquals(exist, FILE.GetLine(2));
		// 추가로 마지막으로 삭제할 line이 현재 커서 위치와 동일한지 확인한다.
		assertEquals(1, FILE.GetCLN());
		// error발생! Q에서도 error, D에서도 error있음
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

		// CLN에 있는 추가된 add를 비교
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

		//CLN부터 CLN+end_line까지 find 문자열을 찾는다.
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