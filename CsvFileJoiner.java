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

	private List<String> list1;
	private List<String>  list2;
		
	public CsvFileJoiner(String input1, String input2){
		initializeInputObjects(input1, input2);
	}

	public void initializeInputObjects(String input1, String input2){

		try{
			file1 = new File(input1);
			file2 = new File(input2);
			
			stdin1 = new Scanner(file1);
			stdin2 = new Scanner(file2);

			list1 = new ArrayList();
			list2 = new ArrayList();
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
			mergeJoin();
		}
		else if ("inner_loop_join".equals(type)){
			innerLoopJoin();
			System.out.println(type);
		}
		else if("hash_join".equals(type)){
			hashJoin();
			System.out.println("hash join");
		}
		else{
			System.out.println("Invalid Input! Please enter the correct join, for example, \'merge_join\'");
		}
	}

	public  void readFile(){
		while(stdin1.hasNext()){
			list1.add(stdin1.next());	
		}

		while(stdin2.hasNext()){
			list2.add(stdin2.next());
		}
	}

	public void hashJoin(){
		int length1 = list1.size();
		int length2 = list2.size();

		Map<String, String> map = new HashMap<String, String>();

		//String hashCode,record;

		/*if(length1 < length2){
			doHash(list1,map);
		}
		else{
			doHash(list2,map);
		}*/

		doHash(map);
	
		System.out.println(list1.size() + " " + list2.size());

		for(Map.Entry<String,String> entry: map.entrySet()){
			String key = entry.getKey().toString();
			String value =entry.getValue();

			System.out.println(key  + " " + value);
		}

		
	}

	public void doHash( Map<String,String> map){
		readFile();

		String hashCode,record;
	//		tableList = new ArrayList();
	//	System.out.println(tableList.size());
		for(int i=0; i < list1.size(); i++){
			hashCode = getHashKey(Integer.parseInt(getKey(list1.get(i))));
			record = list1.get(i);
			map.put(hashCode,record);
//			System.out.println(record);
		}
	}

	public String getHashKey(int index){
		return Integer.toString((index * index) % 10); 

	}

	public void mergeJoin(){
		readFile();
		
		Collections.sort(list1);
		Collections.sort(list2);
		
		int index1 = 0;
		int index2 = 0;

		int compare;
          
		int sizeList1 = list1.size();
		int sizeList2 = list2.size();
		
	

		String record1, record2;

		while(index1  < list1.size()  && index2 < list2.size()){
			record1 = getKey(list1.get(index1));
			record2 = getKey(list2.get(index2));

			compare = record1.compareTo(record2);
			
			if(compare == 0){
				System.out.println(record1 + " " + getMatchingRecord(list1.get(index1)) + " " + getMatchingRecord(list2.get(index2)));
				index2++;
			}
			else if(compare < 0){
				index1++;
			}
			else{
				index2++;
			}
	
	

		
		}

//		printList(list1);
//		printList(list2);

	}

	public void printList(List list){
		for(int i =0; i <list.size(); i++){
			System.out.println(list.get(i));
		}

	}


	public String getKey(String value){
		return value.substring(0,value.indexOf(","));
	}

	public String getMatchingRecord(String value){
		return value.substring(value.indexOf(",") + 1 );
	}

	public void resetFileReaderPosition(){
		stdin2= null;
		try{
			stdin2 = new Scanner(file2);
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}	


	public void innerLoopJoin(){
		String record1;
		String record2;
		while(this.stdin1.hasNext()){
			record1=stdin1.next();
			while(this.stdin2.hasNext()){
				record2=stdin2.next();
				if( this.getKey(record1).equals( this.getKey(record2) ) ){
					System.out.println(this.getKey(record1) + " " + this.getMatchingRecord(record1) + " " + this.getMatchingRecord(record2));
				//	System.out.println(this.getKey(stdin1.next()) + " " + this.getKey(stdin2.next()) );
				//	this.getKey(stdin1.next()).equals( this.getKey(stdin2.next())
				//System.out.println(this.getKey(stdin1.next()) + " " + this.getMatchingRecord(stdin1.next()) + " " + this.getMatchingRecord(stdin2.next()) );
			//		Object one = stdin1.next();
			//		System.out.println(one);	
				}
			//	System.out.println(getKey(record1) + " " + getKey(record2));
			//	System.out.println(this.stdin1.next() /* + " " + this.stdin2.next() */);
			//	System.out.println(this.getKey(stdin1.next()) + " " + this.getKey(stdin2.next()));
			}
			resetFileReaderPosition();
		}
		
	}	
		





}


