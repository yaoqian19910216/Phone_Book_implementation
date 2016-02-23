/*
 Qian Yao
 cssc0016
 */
import java.util.Iterator;
import data_structures.*;
import java.util.ConcurrentModificationException;

public class PhoneNumber implements Comparable<PhoneNumber> {
String areaCode, prefix, number;
String phoneNumber;

// Constructor.  Creates a new PhoneNumber instance.  The parameter
// is a phone number in the form xxx-xxx-xxxx, which is area code -
// prefix - number.  The phone number must be validated, and an
// IllegalArgumentException thrown if it is invalid.
public PhoneNumber(String n) {
   verify(n);
   phoneNumber = n;
   areaCode = getAreaCode();
   prefix = getPrefix();
   number = getNumber();
}

// Follows the specifications of the Comparable Interface.  
public int compareTo(PhoneNumber n){
   if(areaCode.compareTo(n.getAreaCode()) != 0){
        return areaCode.compareTo(n.getAreaCode());
   }else if(prefix.compareTo(n.getPrefix()) != 0){
        return prefix.compareTo(n.getPrefix());
   }else if(number.compareTo(n.getNumber()) != 0){
        return number.compareTo(n.getNumber());
   }else{
        return 0;
   }
}

// Returns an int representing the hashCode of the PhoneNumber.
public int hashCode(){
   return Integer.parseInt(areaCode)+Integer.parseInt(prefix)+Integer.parseInt(number);
}

// Private method to validate the Phone Number.  Should be called
// from the constructor.   
private void verify(String n){
   if(n.matches("\\d{3}-\\d{3}-\\d{4}")){
   }else{
      throw new IllegalArgumentException("must be in the pattern of xxx-xxx-xxxx");
   }
}
           
// Returns the area code of the Phone Number.
public String getAreaCode(){
   return phoneNumber.substring(0,3);
}

// Returns the prefix of the Phone Number.
public String getPrefix(){
   return phoneNumber.substring(4,7);
}

// Returns the the last four digits of the number.
public String getNumber(){
   return phoneNumber.substring(8,12);
}

// Returns the Phone Number.       
public String toString(){
   String s = areaCode + "-" + prefix + "-" + number;
   return s; 
}
}
