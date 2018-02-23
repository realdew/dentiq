package dentiq.api.util;

public class FlagTest {
	
	public static void main(String[] args) throws Exception {
		FlagTest test = new FlagTest();
		test.exec();
	}
	
	public void exec() {
		
		System.out.println("ATTR_NONE ==> " + ATTR_NONE);
		System.out.println("ATTR_1 ==> " + ATTR_1);
		System.out.println("ATTR_2 ==> " + ATTR_2);
		System.out.println("ATTR_3 ==> " + ATTR_3);
		System.out.println("ATTR_4 ==> " + ATTR_4);
		System.out.println("ATTR_ALL ==> " + ATTR_ALL);
		
		System.out.println("\n\n");
		
		
		int my = ATTR_1 | ATTR_3;
		
		System.out.println("my : " + my);
	}
	
	int ATTR_NONE	= 0;
	int ATTR_1		= 1 << 0;
	int ATTR_2		= 1 << 1;
	int ATTR_3		= 1 << 2;
	int ATTR_4		= 1 << 3;
	
	int ATTR_ALL	= Integer.MAX_VALUE;
	
	

}
