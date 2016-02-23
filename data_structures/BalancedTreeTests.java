/**
 * CS 310: Data Structures
 * Fall 2015 @author Shawn Healey
 */

package data_structures;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BalancedTreeTests extends AbstractDictionaryTests {

  @Before
  public void setUp() throws Exception {
    sut = new BalancedTree<Integer,String>();
  }

}
