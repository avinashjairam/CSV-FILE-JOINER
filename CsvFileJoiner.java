import java.io.*;
import java.util.*;

public class  CsvFileJoiner{
	
	private File file1;
	private File file2;
	private File file3;
	private File file4;
	private File file5;
	private File file6;
	
	private Scanner input1;
	private Scanner input2;
	private Scanner input3;
	private Scanner input4;
	private Scanner input5;
	private Scanner input6;
	
	public CsvFileJoiner(){
		initializeInputObjects();
	}

	public void initializeInputObjects(){

		try{
			file1 = new File("dividends.head");
			file2 = new File("splits.head");
			file3 = new File("cts.head");
			file4 = new File("dividends.dump.csv");
			file5 = new File("splits.dump.csv");
			file6 = new File("cts.dump.csv");
			
			input1 = new Scanner(file1);
			input2 = new Scanner(file2);
			input3 = new Scanner(file3);
			input4 = new Scanner(file4);
			input5 = new Scanner(file5);
			input6 = new Scanner(file6);
		}
		catch (FileNotFoundException ex){
			System.out.println("Unable to read file");

		}




	}		

	
	public static void main(String [] args){
		CsvFileJoiner obj = new CsvFileJoiner();
		obj.selectJoinType(args);
		obj.readFile();


	}

	public void selectJoinType(String [] args){
		if("merge_join".equals(args[0])){
			System.out.println("merge join");
		}
		else if ("inner_loop_join".equals(args[0])){
			System.out.println("inner loop join");
		}
		else if("hash_join".equals(args[0])){
			System.out.println("hash join");
		}
		else{
			System.out.println("Invalid Input! Please enter the correct join, for example, \'merge_join\'");
		}
	}

	public  void readFile(){
		/*while(input1.hasNext()){
			System.out.println(input1.next());
		}

		while(input2.hasNext()){
			System.out.println(input2.next());
		}

		while(input3.hasNext()){
			System.out.println(input3.next());
		}

		while(input2.hasNext()){
			System.out.println(input2.next());
		}*/

		int count = 0;

		while(count<10){
			System.out.println(input6.next());
			count++;
		}
	}
}
