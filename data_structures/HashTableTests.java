/**
 * CS 310: Data Structures
 * Fall 2015 @author Shawn Healey
 */

package data_structures;

import org.junit.Before;


public class HashTableTests extends AbstractDictionaryTests{

  @Before
  public void setUp() throws Exception {
    sut = new HashTable<Integer, String>();
  }

}

