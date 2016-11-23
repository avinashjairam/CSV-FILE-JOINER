import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class  CsvFileJoiner{
	
	private File file1;
	private File file2;
	
	private Scanner stdin1;
	private Scanner stdin2;
	
	public CsvFileJoiner(String input1, String input2){
		initializeInputObjects(input1, input2);
	}

	public void initializeInputObjects(String input1, String input2){

		try{
			file1 = new File(input1);
			file2 = new File(input2);
			
			stdin1 = new Scanner(file1);
			stdin2 = new Scanner(file2);
	

		//	System.out.println(stdin2.next());	
		}
		catch (FileNotFoundException ex){
			System.out.println("Unable to read file");
		}
	}		

	
	public static void main(String [] args){
		try {
			CsvFileJoiner obj = new CsvFileJoiner(args[1],args[2]);
			obj.selectJoinType(args[0]);
		
			//System.out.println(obj.getKey("cow,horse"));
			//System.out.println(args[0] + " " + args[1] + " " + args[2]);
			//obj.readFile();
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	
		Connection c = null;
		Statement stmt = null;

		/*	
		try{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/avi", "avi", "avi");
			c.setAutoCommit(false);

			stmt=c.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM DIVIDENDS LIMIT 10;");

			while(rs.next()){
				System.out.println(rs.getString("tdate"));


			}

			rs.close();
			stmt.close();
			c.close();
			
		} catch (Exception e){
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");	*/


	}

	public void selectJoinType(String  type){
		if("merge_join".equals(type)){
			System.out.println("merge join");
		}
		else if ("inner_loop_join".equals(type)){
			innerLoopJoin();
			System.out.println(type);
		}
		else if("hash_join".equals(type)){
			System.out.println("hash join");
		}
		else{
			System.out.println("Invalid Input! Please enter the correct join, for example, \'merge_join\'");
		}
	}

	public  void readFile(){
		while(stdin1.hasNext()){
			
		}

		while(stdin2.hasNext()){
			System.out.println(stdin2.next());
		}

		/*

		int count = 0;

		while(count<10){
			System.out.println(input6.next());
			count++;
		} */
	}

	public String getKey(String value){
		return value.substring(0,value.indexOf(","));
	}

	public String getMatchingRecord(String value){
		return value.substring(value.indexOf(",") + 1 );
	}



	public void innerLoopJoin(){
		while(this.stdin1.hasNext()){
			while(this.stdin2.hasNext()){
				if( this.getKey(stdin1.next()).equals( this.getKey(stdin2.next())) ){
				//	System.out.println("ehy");
				//	System.out.println(this.getKey(stdin1.next()) + " " + this.getKey(stdin2.next()) );
				//	this.getKey(stdin1.next()).equals( this.getKey(stdin2.next())
				//System.out.println(this.getKey(stdin1.next()) + " " + this.getMatchingRecord(stdin1.next()) + " " + this.getMatchingRecord(stdin2.next()) );
				System.out.println(this.stdin2.next());	
			}
			//	System.out.println(this.stdin1.next() + " " + this.stdin2.next());
			//	System.out.println(this.getKey(stdin1.next()) + " " + this.getKey(stdin2.next()));
			}
		}
		
	}	
		





}


