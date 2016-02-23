/*  Qian Yao
REDID 815260362
masc account number cssc0016
*/


package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class BinarySearchTree<K,V> implements DictionaryADT<K,V> {
  int currentSize;
  int maxSize;
  int modCounter;
  Node<K,V> root;
  private class Node<K,V> implements Comparable<Node<K,V>>{
   K key;
   V value;
   Node<K,V> left;
   Node<K,V> right;
   public Node(K k, V v){
     key = k;
     value = v;
   }

   public int compareTo(Node<K,V> node){
     return ((Comparable<K>)key).compareTo((K)node.key);
   }
  } 
   public BinarySearchTree(int max){
     currentSize = 0;
     maxSize = max;
     Node<K,V> root = null;
   }
// Returns true if the dictionary has an object identified by
// key in it, otherwise false.
public boolean contains(K key){
   return (getValue(key) != null);
}
// Adds the given key/value pair to the dictionary.  Returns
// false if the dictionary is full, or if the key is a duplicate.
// Returns true if addition succeeded.
public boolean add(K key, V value){
   if(contains(key)) return false;
   if(isFull()) return false;
   if(root == null)
       root = new Node<K,V>(key,value);
   else
       insert(key,value,root,null,false);
   currentSize++;
   modCounter++;
   return true;
}

private void insert(K k, V v, Node<K,V> n, Node<K,V> parent, boolean wasLeft){
   if(n == null){
      if(wasLeft) parent.left = new Node<K,V>(k,v);
      else parent.right = new Node<K,V>(k,v);
   }else if(((Comparable<K>)k).compareTo((K)n.key) < 0){
      insert(k,v,n.left,n,true);
   }else{
      insert(k,v,n.right,n,false);
   }
}
// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
public boolean delete(K key){
   if(contains(key)){
     root = deleteHelper(key,root);
     currentSize--;
     modCounter--;
     return true;
   }
   return false;
}

private Node<K,V> deleteHelper(K k, Node<K,V> t){
  if(((Comparable<K>)k).compareTo((K)t.key) < 0){
    t.left =  deleteHelper(k,t.left);
  }else if(((Comparable<K>)k).compareTo((K)t.key) > 0){
    t.right =  deleteHelper(k,t.right);
  }else if(t.left != null && t.right != null){
     t.key = findMin(t.right).key;
     t.right = deleteHelper(t.key,t.right);
  }else{
     t = (t.left != null) ? t.left : t.right;
  }
  return t;
}

private Node<K,V> findMin(Node<K,V> t){
   if(t == null) return null;
   else if(t.left == null)
       return t;
   return findMin(t.left);
}
// Returns the value associated with the parameter key.  Returns
// null if the key is not found or the dictionary is empty.
public V getValue(K key){
   return find(key, root);
}

private V find(K key, Node<K,V> n){
    if(n == null) return null;
    if(((Comparable<K>)key).compareTo(n.key) < 0)
        return find(key, n.left);
    else if(((Comparable<K>)key).compareTo(n.key) > 0)
        return find(key, n.right);
    else
        return (V) n.value;
}
// Returns the key associated with the parameter value.  Returns
// null if the value is not found in the dictionary.  If more
// than one key exists that matches the given value, returns the
// first one found.
public K getKey(V value){
    return findV(value, root);
}
   
private K findV(V value, Node<K,V> n){
    K key1;
    K key2;
    if(n == null) return null;
    System.out.println(n.value);
    if(((Comparable<V>)value).compareTo(n.value) != 0){
       key1 = findV(value, n.left);
       key2 = findV(value, n.right);
       if(key1 != null){
          return key1;
       }
       if(key2 != null){
          return key2;
       }
       return null;
    }else{
       return (K) n.key;
    }
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
// Returns the  object to an empty state.
public void clear(){
   currentSize = 0;
   root = null;
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
     protected Node<K,V>[] nodes;
     protected int idx;
     protected int modCheck;
     protected int k;
     public IteratorHelper(){
       nodes = new Node[currentSize];
       idx = 0;
       int j = 0;
       k = 0;
       modCheck = modCounter;
       makeArray(root); 
       for (int i = 0; i < currentSize ; i++){
       //   System.out.println(nodes[i].key+"+"+nodes[i].value+"beforesort");
       }
     }
     
     private void makeArray(Node<K,V> t){
       if(t.left != null)
       makeArray(t.left);
      
       nodes[k++] = t;

       if(t.right != null)
       makeArray(t.right);
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
}
