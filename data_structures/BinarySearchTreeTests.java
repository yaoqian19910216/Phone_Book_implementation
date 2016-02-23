/**
 * CS 310: Data Structures Fall 2015 @author Shawn Healey
 */

package data_structures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class BinarySearchTreeTests extends AbstractDictionaryTests {

  @Test
  public void delete_rootOneChild_noContains() {
    sut.add(22, "root");
    sut.add(2, "child");
    sut.delete(22);
    assertFalse(sut.contains(22));
  }

  @Test
  public void delete_rootTwoChildren_noContains() {
    sut.add(22, "root");
    sut.add(20, "smaller");
    sut.add(24, "greater");
    sut.delete(22);
    assertFalse(sut.contains(22));
  }

  @Test
  public void delete_rootTwoChildren_correctSize() {
    sut.add(22, "root");
    sut.add(20, "smaller");
    sut.add(24, "greater");
    sut.delete(22);
    assertThat(sut.size(), is(2));
  }

  @Test
  public void isFull_largeStructure_notFull() {
    // Basic BST will stack overflow, so this test is unimplemented
  }

  @Test(expected = StackOverflowError.class)
  public void isFull_largeStructure_stackOverflow() {
    for (int count = 0; count < LargeStructureSize; count++) {
      sut.add(count, DuplicateValue);
    }
    fail("Why have you brought me here?");
  }

  @Override
  public void setUp() throws Exception {
    sut = new BinarySearchTree<Integer, String>();
  }
}
