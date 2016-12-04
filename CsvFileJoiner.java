import java.io.*;
import java.util.*;

class Record{
	private int key;
	private String record;

	public Record(int key, String record){
		this.key=key;
		this.record=record;
	}

	public void setKey(int key){
		this.key=key;
	}

	public void setRecord(String record){
		this.record=record;
	}

	public int getKey(){
		return this.key;
	}

	public String getRecord(){
		return this.record;
	}
}

public class  CsvFileJoiner{
	
	private File file1;
	private File file2;
	
	private Scanner stdin1;
	private Scanner stdin2;

	private List<String> list1;
	private List<String> list2;

	private List<Record> records1 = new ArrayList<Record>();
	private List<Record> records2 = new ArrayList<Record>();
		
	public CsvFileJoiner(String input1, String input2){
		initializeInputObjects(input1, input2);
	}
	
	public static void main(String [] args){
		try {
			CsvFileJoiner obj = new CsvFileJoiner(args[1],args[2]);
			obj.selectJoinType(args[0]);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
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

	public void hashJoin(){
		int length1 = list1.size();
		int length2 = list2.size();


		Map<Integer, List<String>> smallerMap = new LinkedHashMap<Integer, List<String>>();
		Map<Integer, List<String>> largerMap = new LinkedHashMap<Integer, List<String>>();

		
		int hashKey;
		
		List<String> t1Val;
		int t1ValKey;
		
		//the doHash() method loops through the smaller table and stores teh value in a hash (smallerMap)
		if(list1.size() <= list2.size()){

			doHash(smallerMap,list1);
	
			for(int j=0; j<list2.size(); j++){
				hashKey = getHashKey(Integer.parseInt(getKey(list2.get(j))));
				if(Integer.parseInt(getKey(smallerMap.get(hashKey).get(0))) ==  Integer.parseInt(getKey(list2.get(j))) ){
					System.out.println(smallerMap.get(hashKey).get(0) + " " + getMatchingRecord(list2.get(j)));
				}
			}
		}
		else{
			doHash(smallerMap,list2);
	
			for(int j=0; j<list1.size(); j++){
				hashKey = getHashKey(Integer.parseInt(getKey(list1.get(j))));
				if(Integer.parseInt(getKey(smallerMap.get(hashKey).get(0))) ==  Integer.parseInt(getKey(list1.get(j))) ){
					System.out.println(smallerMap.get(hashKey).get(0) + " " + getMatchingRecord(list1.get(j)));
				}
			}
		}

	
	}






	

	

	public void selectJoinType(String  type){
		if("merge_join".equals(type)){
			readFile();
			mergeJoin();
		}
		else if ("inner_loop_join".equals(type)){
			innerLoopJoin();
		}
		else if("hash_join".equals(type)){
			readFile();
			hashJoin();
		}
		else{
			System.out.println("Invalid Input! Please enter the correct join, for example, \'merge_join\'");
		}
	}

	public  void readFile(){
		int list1Counter=0,list2Counter=0;
		while(stdin1.hasNext()){
			list1.add(stdin1.next());
			records1.add(new Record(Integer.parseInt(getKey(list1.get(list1Counter))),getMatchingRecord(list1.get(list1Counter))));	
			list1Counter++;
		}

		while(stdin2.hasNext()){
			list2.add(stdin2.next());
			records2.add(new Record(Integer.parseInt(getKey(list2.get(list2Counter))),getMatchingRecord(list2.get(list2Counter))));
			list2Counter++;
		}
	}


	public void doHash( Map<Integer,List<String>> map, List<String> list){
	
		int hashKey;
		String record;
		
		for(int i=0; i < list.size(); i++){
			hashKey = getHashKey(Integer.parseInt(getKey(list.get(i))));
			record = list.get(i);
		
			if(!map.containsKey(hashKey)){
				map.put(hashKey, new LinkedList<String>());

			}
			map.get(hashKey).add(record);
		}
	}

	public  int  getHashKey(int index){
		return (index * index) % 10; 

	}

	public void mergeJoin(){
		
	//	Collections.sort(list1);
	//	Collections.sort(list2);
	
		sortRecordList(records1);
		sortRecordList(records2);	
		int index1 = 0;
		int index2 = 0;

		int compare;
          
		int sizeList1 = list1.size();
		int sizeList2 = list2.size();
		
//		System.out.println(records1);
//		System.out.println(records2);

/*		for(Record record: records1){
			System.out.println(record.getKey() + " " + record.getRecord());
		}

		for(Record record: records2){
			System.out.println(record.getKey() + " " + record.getRecord());
		}*/


		int key1, key2;

	/*	while(index1  < list1.size()  && index2 < list2.size()){
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
		}*/
		while(index1  < records1.size()  && index2 < records2.size()){
			key1 = records1.get(index1).getKey();
			key2 = records2.get(index2).getKey();

			
			if(key1 == key2){
				System.out.println(key1 + " " + records1.get(index1).getRecord() + " " + records2.get(index2).getRecord()) ;
				index2++;
			}
			else if(key1 < key2){
				index1++;
			}
			else{
				index2++;
			}
		}







	}

	public void sortRecordList(List<Record>list){
 		Collections.sort(list, new Comparator<Record>() {
            
			public int compare(Record r1, Record r2) {

              		  	if(r1.getKey() > r2.getKey()) {
                   			 return 1;
               			 }
                		else if(r1.getKey() < r2.getKey()) {
                    			return -1;
               			 }
                
               			 return 0;
           		 }
       		 });
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
				}
				resetFileReaderPosition();
			}
		}
	}
}


