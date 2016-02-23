/*  Qian Yao
REDID 815260362
masc account number cssc0016
*/

package data_structures;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class  LinearList<E> implements LinearListADT<E>{
   public static final int DEFAULT_MAX_CAPACITY = 10000;
   private Node head;
   private Node tail;
   private int N;
   private int max_size;

   public LinearList(){
     head = new Node((E) null);
     tail = new Node((E) null);
     head.next = tail;
     tail.prev = head;
     N = 0;
     max_size = DEFAULT_MAX_CAPACITY;
   }

   public LinearList(int arraySize){
     head = new Node((E) null);
     tail = new Node((E) null);
     head.next = tail;
     tail.prev = head;
     N = 0;
     max_size = arraySize;
   }

   protected class Node{
     E item;
     Node next;
     Node prev;

     public Node(E element){
       item = element;
       next = null;
       prev = null;
     }

     public Node(E element, Node prevNode, Node nextNode){
       item = element;
       prev = prevNode;
       prevNode.next = this;
       this.prev = prevNode;

       next = nextNode;
       nextNode.prev = this;
       this.next = nextNode;
     }

     public void remove(){
       prev.next = next;
       next.prev = prev;
     }
     
   }

   //  Adds the Object obj to the beginning of list and returns true if the list is not full.
   //  returns false and aborts the insertion if the list is full.
   public boolean addFirst(E obj){
      if(isFull()){
         return false;
      }else if(obj == ((E) null)){
         throw new NullPointerException("data is null");
      }else{
         Node newNode = new Node(obj, head, head.next);
         N++;
         return true;
      }
   }
    

   //  Adds the Object obj to the end of list and returns true if the list is not full.
   //  returns false and aborts the insertion if the list is full..  
   public boolean addLast(E obj){
      if(isFull()){
         return false;
      }else if(obj == (E) (null)){
         return false;
      }else{
         Node newNode = new Node(obj,tail.prev, tail);
         N++;
         return true;
      }
   }  

   //  Removes and returns the parameter object obj in first position in list if the list is not empty,  
   //  null if the list is empty. 
   public E removeFirst(){
      if(isEmpty()){
        return ((E)null);
      }else{
        E item = head.next.item;
        head.next.next.prev = head;
        head.next = head.next.next; 
        N--;
        return item;
      }
   }  

   //  Removes and returns the parameter object obj in last position in list if the list is not empty, 
   //  null if the list is empty. 
   public E removeLast(){
      if(isEmpty()){
        return ((E) null);
      }else{
        E item = tail.prev.item;
        tail.prev.prev.next = tail;
        tail.prev = tail.prev.prev; 
        N--;
        return item;
      }
   }
   
   //  Removes and returns the parameter object obj from the list if the list contains it, null otherwise.
   //  The ordering of the list is preserved.  The list may contain duplicate elements.  This method
   //  removes and returns the first matching element found when traversing the list from first position.
   //  Note that you may have to shift elements to fill in the slot where the deleted element was located.
   public E remove(E obj){
       Node current;
       if(obj == ((E)null)){
          return null;
       }
       if(isEmpty()){
          return null;
       }
       if(contains(obj)){
          current = head.next;
          while(current != null){
             if(obj != null && ((Comparable<E>)(current.item)).compareTo(obj) == 0){
                 current.remove();
                 N--;
                return obj;
             }
             current = current.next;
          }
       }
       return null;   
   }
   //  Returns the first element in the list, null if the list is empty.
   //  The list is not modified.
   public E peekFirst(){
      if(isEmpty()){
         return null;
      }
      return head.next.item;
   }

   //  Returns the last element in the list, null if the list is empty.
   //  The list is not modified.
   public E peekLast(){
      if(isEmpty()){
        return null;
      }
      return tail.prev.item;
   }                       

   //  Returns true if the parameter object obj is in the list, false otherwise.
   //  The list is not modified.
   public boolean contains(E obj){ 
      return (find(obj) != null);
   }

   //  Returns the element matching obj if it is in the list, null otherwise.
   //  In the case of duplicates, this method returns the element closest to front.
   //  The list is not modified.
   public E find(E obj){
       Node current = head.next;
       if(obj == ((E)null)){
          return null;
       }
       while(current.item != null){
           if(((Comparable<E>)(current.item)).compareTo(obj) == 0){
               return current.item;
           }
           current = current.next;
       }
       return ((E)null);
   }      
       

   //  The list is returned to an empty state.
   public void clear(){
       N = 0;
       head.next = tail;
       tail.prev = head;
   }

   //  Returns true if the list is empty, otherwise false
   public boolean isEmpty(){
      return (N == 0);
   }
     
   //  Returns true if the list is full, otherwise false
   public boolean isFull(){
      return (N == max_size);
   }   

   //  Returns the number of Objects currently in the list.
   public int size(){
       return N;
   }

   //  Returns an Iterator of the values in the list, presented in
   //  the same order as the underlying order of the list. (front first, rear last)
   public Iterator<E> iterator(){
       return new LinearListIterator();
   }

   private class LinearListIterator implements Iterator<E>{
       private Node left, right;
       private int idx;

       public LinearListIterator(){
          left = head;
          right = left.next;
          idx = 0;
       }

       public boolean hasNext(){
         return (right.item != (E) null);
       }
       
       public boolean hasPrevious(){
         return (left.item != (E) null);
       }

       public E next(){
         E item1;
         if(idx >= N) throw new ConcurrentModificationException();
         if(!hasNext()){
            throw new NoSuchElementException("No such element in iterator next");
         }else{
            item1 = right.item;
            left = right;
            right = right.next;
            idx++;
         }
         return item1;
       } 
   
       public void remove(){
             if(right != null){
               Node tmp = right;
               right = right.next;
               tmp.remove();
               N--;
             }
       }
   }
}
