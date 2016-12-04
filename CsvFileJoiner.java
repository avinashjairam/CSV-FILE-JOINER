/*******************************************************
NAME: 	  AVINASH JAIRAM
HOMEWORK: #3

********************************************************/


import java.io.*;
import java.util.*;


//The Record class has two members; key (int) and record(String)
//These store the key and the corresponding record from each row in the table
class Record{
	private int key;
	private String record;

	//Constructor initializing member variables
	public Record(int key, String record){
		this.key=key;
		this.record=record;
	}

	//Getting and Setter methods
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
	
	//File objects to facilitate the opening and closing of external files
	//There are two of them since this program joins 2 files
	private File file1;
	private File file2;
	
	//Scanner objects which enables the reading in of data from the external files
	private Scanner stdin1;
	private Scanner stdin2;

	//Two Lists of String type - All the data from the first and second files and immediately stored in
	//list1 and list2 respectively
	private List<String> list1;
	private List<String> list2;

	//Two Lists containing objects of the Record class defined above.
	//records1 have objects which have information for each row in the 1st CSV file.
	//records2 does the same for the 2nd Csv file
	private List<Record> records1 = new ArrayList<Record>();
	private List<Record> records2 = new ArrayList<Record>();
		

	//Main method - It receives the type of the join and the names of the 2 files via the command line
	//The names of the files are to the constructor and the join type is passed to the selectJoinTypeMethod	
	public static void main(String [] args){
		try {
			CsvFileJoiner obj = new CsvFileJoiner(args[1],args[2]);
			obj.selectJoinType(args[0]);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	//Class' constructor which receives the names of the 2 files as strings
	//These names are then sent to the initializeInputObjects method
	public CsvFileJoiner(String input1, String input2){
		initializeInputObjects(input1, input2);
	}
	

	//The initializeInputObjects receives the names of the 2 csv files
	//The appropriate file objects (file1 and file2) are instantiated, followed by the corresponding
	//Scanner input streams (stdin1 and stdin2). Lastly the lists are initialized.
	public void initializeInputObjects(String input1, String input2){
		try{
			file1 = new File(input1);
			file2 = new File(input2);
			
			stdin1 = new Scanner(file1);
			stdin2 = new Scanner(file2);

			//Raw input from the first and second files are stored in list1 and list2 respectively
			list1 = new ArrayList();
			list2 = new ArrayList();

			records1 = new ArrayList<Record>();
			records2 = new ArrayList<Record>();
		}
		catch (FileNotFoundException ex){
			System.out.println("Unable to read file");
		}
	}
	
	//The selectJoinType method receives the type of join to be peformed as a string parameter form the main method
	//After the appropriate join has been determined, the readFile() method is called which reads the raw data from 
	//the 1st and 2nd csv files into the list1 and list2 LISTS respectively. Then, the relevant join method is called.
	public void selectJoinType(String  type){
		if("merge_join".equals(type)){
			readFile();
			mergeJoin();
		}
		else if ("inner_loop_join".equals(type)){
			readFile();
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
	
	//The readFile() method reads the raw data from the 2 csv files into list1 and list2 respectively. 
	//Immediately after a row is read into one of these list, the key and the corresponding record is extracted via the
	// getKey() and getMatchingRecord() methods. Record ojbects are the initialized in the records1 and records2 lists 
	// and the key and record class member variables are set

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
	
	//The getKey() method returns the key in the row read in from the file. The key is considered to be the data before the first comma in the row
	//This method takes the entire table row as a parameter and extracts the key
	public String getKey(String value){
		return value.substring(0,value.indexOf(","));
	}

	//The get matching record returns all of the data in the an individual row that follows the first comma (that is the key) 
	//This method takes the entire table row as a parameter and extracts all of the information that follows the key
	public String getMatchingRecord(String value){
		return value.substring(value.indexOf(",") + 1 );
	}


	//The hashJoin() method
	public void hashJoin(){
                
       		//A 2 dimensional map consisting of type Integer and a String List 
      		 //Data from the smaller talbe is stored here. The hashcode is stored as an integer and the corresponding values in a String List<> 
	        Map<Integer, List<String>> smallerMap = new LinkedHashMap<Integer, List<String>>();
       
       		int hashKey;
                
    		List<String> t1Val;
      		int t1ValKey;
               
	       //Determining the smaller table and looping through it and storing its values in hash (smallerMap) 
               if(records1.size() <= records2.size()){
	
			 //the doHash() method loops through the smaller table and stores the value in a hash (smallerMap)
			doHash(smallerMap,records1);

			//Now looping through the larger table and picking at the hash (smallerMap) for matching hashkeys
			//If a match is found, the key and the corresponding rows are printed. 
                	for(int j=0; j<records2.size(); j++){
                        	hashKey = getHashKey(records2.get(j).getKey());
                        	if(Integer.parseInt(getKey(smallerMap.get(hashKey).get(0))) ==  records2.get(j).getKey() ) {
                                	System.out.println(smallerMap.get(hashKey).get(0) + " " + records2.get(j).getRecord());
                        	}
                	}
              }
              else{
			 //the doHash() method loops through the smaller table and stores the value in a hash (smallerMap)
			 doHash(smallerMap,records2);
			
			//Now looping through the larger table and picking at the hash (smallerMap) for matching hashkeys
			//If a match is found, the key and the corresponding rows are printed. 
			 for(int j=0; j<records1.size(); j++){
                        	hashKey = getHashKey(records1.get(j).getKey());
                       	        if(Integer.parseInt(getKey(smallerMap.get(hashKey).get(0))) ==  records1.get(j).getKey() ){
                              		  System.out.println(smallerMap.get(hashKey).get(0) + " " + records1.get(j).getRecord());
                       		 }
               		  }
       		 }
	}



	

	






	public void doHash( Map<Integer,List<String>> map, List<Record> list){
	
		int hashKey;
		String record;
		
		for(int i=0; i < list.size(); i++){
			hashKey = getHashKey(list.get(i).getKey());
			record = list.get(i).getKey() + "," + list.get(i).getRecord();
		
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
		

		sortRecordList(records1);
		sortRecordList(records2);	
		int index1 = 0;
		int index2 = 0;

		int compare;
          
		int sizeList1 = list1.size();
		int sizeList2 = list2.size();
		


		int key1, key2;

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





	public void innerLoopJoin(){
		int key1,key2;
		
		for(int i=0; i < records1.size(); i++){
			key1= records1.get(i).getKey();
			for(int j=0; j < records2.size();j++){
				key2 = records2.get(j).getKey();
				if(key1 == key2){
					System.out.println(key1 + " " + records1.get(i).getRecord() + " " + records2.get(j).getRecord());
				}
			}
		}
	}



	



}


