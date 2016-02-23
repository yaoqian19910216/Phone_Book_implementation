/*  Qian Yao
REDID 815260362
masc account number cssc0016
*/


package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class Hashtable<K,V> implements DictionaryADT<K,V> {
  int currentSize;
  int maxSize;
  int tableSize;
  long modCounter;
  LinearList<DictionaryNode<K,V>>[] list;

  private class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>>{
    K key;
    V value;
    public DictionaryNode(K k, V v){
     key = k;
     value = v;
    }

    public int compareTo(DictionaryNode<K,V> node){
     return ((Comparable<K>)key).compareTo((K)node.key);
    }
  }

  public Hashtable(int n){
     currentSize = 0;
     maxSize = n;
     modCounter = 0;
     tableSize = (int) (maxSize * 1.3f);
     list =  new LinearList[tableSize];
     for (int i = 0; i < tableSize; i++){
      list[i] = new LinearList<DictionaryNode<K,V>>();
     }
  }
// Returns true if the dictionary has an object identified by
// key in it, otherwise false.
public boolean contains(K key){
   return list[getHashCode(key)].contains(new DictionaryNode<K,V>(key,null));
}
// Adds the given key/value pair to the dictionary.  Returns
// false if the dictionary is full, or if the key is a duplicate.
// Returns true if addition succeeded.
public boolean add(K key, V value){
   if(isFull()){
    return false;
   }
   if(list[getHashCode(key)] != null && list[getHashCode(key)].contains(new DictionaryNode<K,V>(key,null))){
    return false;
   }
   list[getHashCode(key)].addLast(new DictionaryNode<K,V>(key,value));
   currentSize++;
   modCounter++;
   return true;
}
   
// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
public boolean delete(K key){
   if(isEmpty()){
       return false;
   }
   if(!list[getHashCode(key)].contains(new DictionaryNode<K,V>(key,null))){
       return false;
   }
   list[getHashCode(key)].removeFirst();
   currentSize--;
   modCounter--;
   return true;
}

// Returns the value associated with the parameter key.  Returns
// null if the key is not found or the dictionary is empty.
public V getValue(K key){
   DictionaryNode<K,V> tmp = list[getHashCode(key)].find(new DictionaryNode<K,V>(key,null));
   if(tmp == null)return null;
   return tmp.value;
}
// Returns the key associated with the parameter value.  Returns
// null if the value is not found in the dictionary.  If more
// than one key exists that matches the given value, returns the
// first one found.
public K getKey(V value){
   K va = null;
   for(int i = 0; i < tableSize; i++){
      if(list[i] == null){
         continue;
      }
      LinearList<DictionaryNode<K,V>> tmp1 = new LinearList<DictionaryNode<K,V>>();
      while(list[i].size() > 0){
      DictionaryNode<K,V> tmp = list[i].removeFirst();
      tmp1.addLast(tmp);
      if(((Comparable<V>)tmp.value).compareTo((V)value) == 0){
        va = tmp.key;
        while(list[i].size() > 0) tmp1.addLast(list[i].removeFirst());
      }
      }
      list[i] = tmp1;
      if(va != null) return va;
   }
   return null;
}
   
// Returns the number of key/value pairs currently stored
// in the dictionary
public int size(){
   return currentSize;
}

// Returns true if the dictionary is at max capacity
public boolean isFull(){
   return (currentSize == maxSize);
}

// Returns true if the dictionary is empty
public boolean isEmpty(){
   return (currentSize == 0);
}
// Returns the Dictionary object to an empty state.
public void clear(){
   currentSize = 0;
   for(int i = 0; i < tableSize; i++){
      list[i] = new LinearList<DictionaryNode<K,V>>();
   }
}

// Returns an Iterator of the keys in the dictionary, in ascrighting
// sorted order.  The iterator must be fail-fast.
public Iterator<K> keys(){
 return new KeyIteratorHelper();
}

private class KeyIteratorHelper extends IteratorHelper<K>{
 public KeyIteratorHelper(){
   super();
 }
 public K next(){
 return (K) nodes[idx++].key;
 }
}

// Returns an Iterator of the values in the dictionary.  The
// order of the values must match the order of the keys.
// The iterator must be fail-fast.
public Iterator<V> values(){
 return new ValueIteratorHelper();
}

private class ValueIteratorHelper extends IteratorHelper<V>{
   public ValueIteratorHelper(){
    super();
   }
   public V next(){
        return (V) nodes[idx++].value;
  }
}

abstract class IteratorHelper<E> implements Iterator<E>{
     protected DictionaryNode<K,V>[] nodes;
     protected int idx;
     protected long modCheck;
     public IteratorHelper(){
       nodes = new DictionaryNode[currentSize];
       idx = 0;
       int j = 0;
       modCheck = modCounter;
       for(int i = 0; i < tableSize; i++)
           for(DictionaryNode n : list[i])
               nodes[j++] = n;
       quickSort(nodes,0,currentSize-1);
     }
     
     public boolean hasNext(){
      if(modCheck != modCounter){
         throw new ConcurrentModificationException();
      }
      return idx < currentSize;
      }

     public abstract E next();

     public void remove(){
       throw new UnsupportedOperationException();
     }
}
     private void quickSort(DictionaryNode<K,V>[] nodes, int low, int high){
       int left = low;
       int right = high;
       int mid = (right - left ) / 2 + left;
       DictionaryNode<K,V> pivot = nodes[mid];
       while(left <= right){        
         while((nodes[left]).compareTo(pivot) < 0){
           left++;
         } 
         while((nodes[right]).compareTo(pivot) > 0){
           right--;
         }
         if(left <= right){
           DictionaryNode<K,V> temp = nodes[left];
           nodes[left] = nodes[right];
           nodes[right] = temp;
           left++;
           right--;
         }
       }
       if(low < right) quickSort(nodes, low, right);
       if(left < high) quickSort(nodes, left, high);    
     }



private int getHashCode(K key){
    return key.hashCode() % tableSize;
}

}
