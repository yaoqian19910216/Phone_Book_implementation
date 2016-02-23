/*
 Qian Yao
 cssc0016
 */
import data_structures.*;
import java.util.Iterator;
import java.io.*;

public class PhoneBook {
Hashtable<PhoneNumber,String> table;
//BinarySearchTree<PhoneNumber, String> table;
//BalancedTree<PhoneNumber, String> table;
int max;
int currentSize;
// Constructor.  There is no argument-less constructor, or default size
public PhoneBook(int maxSize){
    max = maxSize;
    currentSize = 0;
   table = new Hashtable<PhoneNumber,String>(maxSize);
//   table = new BinarySearchTree<PhoneNumber,String>(max); 
//   table = new BalancedTree<PhoneNumber, String>(maxSize);
}

// Reads PhoneBook data from a text file and loads the data into
// the PhoneBook.  Data is in the form "key=value" where a PhoneNumber
// is the key and a name in the format "Last, First" is the value.
public void load(String filename){
   try{
     BufferedReader in = new BufferedReader(new FileReader(filename));
     String line = " ";
     while((line = in.readLine()) != null){
        String parts[] = line.split("=");
        PhoneNumber a = new PhoneNumber(parts[0]);
        addEntry(a, parts[1]);
     }
     System.out.println(currentSize);
     in.close();
   }
   catch(IOException ex){
     System.out.println("IOException caught");
   }
}

// Returns the name associated sWith the given PhoneNumber, if it is
// in the PhoneBook, null if it is not.
public String numberLookup(PhoneNumber number){
   return table.getValue(number); 
}

// Returns the PhoneNumber associated sWith the given name value.
// There may be duplicate values, return the first one found.
// Return null if the name is not in the PhoneBook.
public PhoneNumber nameLookup(String name){
   return table.getKey(name);
}

// Adds a new PhoneNumber = name pair to the PhoneBook.  All
// names should be in the form "Last, First".
// Duplicate entries are *not* allowed.  Return true if the
// insertion succeeds otherwise false (PhoneBook is full or
// the new record is a duplicate).  Does not change the datafile on disk.
public boolean addEntry(PhoneNumber number, String name){
   if(table.add(number, name)){
      currentSize++;
      return true;
   }  
   return false;
}

// Deletes the record associated sWith the PhoneNumber if it is
// in the PhoneBook.  Returns true if the number was found and
// its record deleted, otherwise false.  Does not change the datafile on disk.
public boolean deleteEntry(PhoneNumber number){
  // table.printpath(number);
   if(table.delete(number)){
      currentSize--;
      return true;
   }
   return false;
}

// Prints a directory of all PhoneNumbers sWith their associated
// names, in sorted order (ordered by PhoneNumber).
public void printAll(){
   Iterator<String> itvalue = table.values();
   for (Iterator<PhoneNumber> it = table.keys();it.hasNext();){
      PhoneNumber key = (PhoneNumber)it.next();
      String value = (String)itvalue.next();
      System.out.println(key.toString()+": "+value);
   }   

}

// Prints all records sWith the given Area Code in ordered
// sorted by PhoneNumber.
public void printByAreaCode(String code){
   Iterator<String> itvalue = table.values();
   for (Iterator<PhoneNumber> it = table.keys();it.hasNext();){
       PhoneNumber key = (PhoneNumber)it.next();
       String value = (String)itvalue.next();
       if(key.toString().startsWith(code)){
         System.out.println(key.toString()+": "+value);
       }
   }
}       

// Prints all of the names in the directory, in sorted order (by name,
// not by number).  There may be duplicates as these are the values.       
public void printNames(){
    String[] nodes = new String[currentSize];
    int i = 0;
    System.out.println(currentSize);
    for (Iterator<String> it = table.values();it.hasNext();){
        nodes[i++] = (String)it.next();
    }
    quickSort(nodes,0,currentSize-1);
    for(i = 0; i < currentSize; i++){
        System.out.println(nodes[i]);
    }
}

private void quickSort(String[] nodes, int low, int high){
       int left = low;
       int right = high;
       int mid = (right - left ) / 2 + left;
       String pivot = nodes[mid];
       while(left <= right){        
         while((nodes[left]).compareTo(pivot) < 0){
           left++;
         } 
         while((nodes[right]).compareTo(pivot) > 0){
           right--;
         }
         if(left <= right){
           String temp = nodes[left];
           nodes[left] = nodes[right];
           nodes[right] = temp;
           left++;
           right--;
         }
       }
       if(low < right) quickSort(nodes, low, right);
       if(left < high) quickSort(nodes, left, high);    
}

public static void main(String[] arg) {
     PhoneBook mySolver = new PhoneBook(30000);
     System.out.println("start");
     mySolver.load("p4_data.txt");
     mySolver.printAll();
     mySolver.printByAreaCode("025");
     mySolver.printNames();
     PhoneNumber a = new PhoneNumber("618-181-5387");
     System.out.println("618-181-5387"+mySolver.numberLookup(a));
     System.out.println(mySolver.nameLookup("Vernon, Betty")+"Vernon, Betty");
     mySolver.printAll();
     if(mySolver.deleteEntry(a)){
         System.out.println("Delete Successful");
     }else{
         System.out.println("Delete Failed");
     }
     mySolver.printAll();
     PhoneNumber b = new PhoneNumber("999-695-3017");
     if(mySolver.addEntry(b,"qian")){
         System.out.println("Add Successful");
     }else{
         System.out.println("Add Failed");
     }
     mySolver.printAll();
          PhoneNumber c = new PhoneNumber("618-181-5387");
     //mySolver.deleteEntry(c);
     System.out.println("618-181-5387"+mySolver.numberLookup(a));
     mySolver.printNames();
}
}
