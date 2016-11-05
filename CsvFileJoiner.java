import java.io.*;

public class  CsvFileJoiner{
	
	
	public CsvFileJoiner(){

	}

		

	
	public static void main(String [] args){
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






}
