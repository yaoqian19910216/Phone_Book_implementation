/*  Qian Yao
REDID 815260362
masc account number cssc0016
*/

package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.TreeMap;

public class BalancedTree<K,V> implements DictionaryADT<K,V> {
  int currentSize;
  int maxSize;
  int modCounter;
  Node<K,V> root;
  private static final boolean RED = true;
  private static final boolean BLACK = false;

  private class Node<K,V> implements Comparable<Node<K,V>>{
   K key;
   V value;
   Node<K,V> left;
   Node<K,V> right;
   boolean color;
   int N;

   public Node(K k, V v, boolean c, int N1){
     key = k;
     value = v;
     color = c;
     N = N1;
   }

   public int compareTo(Node<K,V> node){
     return ((Comparable<K>)key).compareTo((K)node.key);
   }
  } 
   public BalancedTree(int max){
     currentSize = 0;
     maxSize = max;
     Node<K,V> root = null;
   }

   private boolean isRed(Node x){
    if(x == null) return false;
    return x.color == RED;
   }

   private int subSize(Node x){
    if(x == null) return 0;
    return x.N;
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
   root = insert(root,key,value);
   currentSize++;
   modCounter++;
   return true;
}

private Node<K,V> insert(Node<K,V> h, K k, V v){
    if(h == null) return new Node(k,v,RED,1);
    int cmp = ((Comparable<K>)k).compareTo(h.key);
    if(cmp < 0) h.left = insert(h.left,k,v);
    if(cmp > 0) h.right = insert(h.right,k,v);

    if(isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
    if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
    if (isRed(h.left)  &&  isRed(h.right))     flipColors(h);
    h.N = subSize(h.left) + subSize(h.right) + 1;
    
    return h;
}

private Node rotateRight(Node h){
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = x.right.color;
    x.right.color = RED;
    x.N = h.N;
    h.N = subSize(h.left) + subSize(h.right) + 1;
    return x;

}

private Node rotateLeft(Node h){
    Node x = h.right;
    h.right = x.left;
    x.left = h;
    x.color = x.left.color;
    x.left.color = RED;
    x.N = h.N;
    h.N = subSize(h.left) + subSize(h.right) + 1;
    return x;
}

private void flipColors(Node h){
    h.color = !h.color;
    h.left.color = !h.left.color;
    h.right.color = !h.right.color;
}
// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
public boolean delete(K key){
    if(!contains(key)) return false;
    if(!isRed(root.left) && !isRed(root.right)) root.color = RED;
    root = deleteHelper(root,key);
    if(!isEmpty()) root.color = BLACK;
    currentSize--;
    modCounter--;
    return true;
}

private Node<K,V> deleteHelper(Node<K,V> h, K key){
    if(((Comparable<K>)key).compareTo(h.key) < 0){
       if(!isRed(h.left) && !isRed(h.left.left))
           h = moveRedLeft(h);
       h.left = deleteHelper(h.left,key);
    }
    else{
       if(isRed(h.left)) h = rotateRight(h);
       if(((Comparable<K>)key).compareTo(h.key) == 0 && (h.right == null))
           return null;
       if(!isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
       if (((Comparable<K>)key).compareTo(h.key) == 0) {
           Node<K,V> x = min(h.right);
           h.key = x.key;
           h.value = x.value;
           // h.val = get(h.right, min(h.right).key);
           // h.key = min(h.right).key;
           h.right = deleteMin(h.right);
       }
       else h.right = deleteHelper(h.right, key);
    }
    return balance(h);
}

private Node<K,V> moveRedLeft(Node<K,V> h){
     flipColors(h);
     if(isRed(h.right.left)){
       h.right = rotateRight(h.right);
       h = rotateLeft(h);
       flipColors(h);
     }
     return h;
}

private Node<K,V> moveRedRight(Node<K,V> h) {
     // assert (h != null);
     // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
     flipColors(h);
     if (isRed(h.left.left)) { 
       h = rotateRight(h);
       flipColors(h);
     }
     return h;
}

private Node<K,V> balance(Node<K,V> h){
     if (isRed(h.right))                      h = rotateLeft(h);
     if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
     if (isRed(h.left) && isRed(h.right))     flipColors(h);
     
     h.N = subSize(h.left) + subSize(h.right) + 1;
     return h;
}

private Node<K,V> deleteMin(Node<K,V> h){
     if(h.left == null)
     return null;
     
     if (!isRed(h.left) && !isRed(h.left.left))
     h = moveRedLeft(h);
     
     h.left = deleteMin(h.left);
     return balance(h);
}
// Returns the value associated with the parameter key.  Returns
// null if the key is not found or the dictionary is empty.
public V getValue(K key){
   return get(key, root);
}

private V get(K key, Node<K,V> n){
    while( n != null){
      int cmp = ((Comparable<K>)key).compareTo(n.key);
      if      (cmp < 0) n = n.left;
      else if (cmp > 0) n = n.right;
      else    return n.value;
    }
    return null;
}

private Node<K,V> min(Node<K,V> x) { 
     // assert x != null;
     if (x.left == null) return x; 
     else                return min(x.left); 
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
