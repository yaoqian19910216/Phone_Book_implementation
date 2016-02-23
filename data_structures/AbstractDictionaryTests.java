/**
 * CS 310: Data Structures Fall 2015 @author Shawn Healey
 */
package data_structures;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public abstract class AbstractDictionaryTests {

  protected static final Integer DeletedKey = 24;
  protected static final String DeletedValue = "Rainbow Warrior";

  protected static final Integer DuplicateKey = 48;
  protected static final String DuplicateValue = "Six Time";

  protected static final int LargeStructureSize = 2551977;
  protected static final int MedStructureSize = 5500;

  protected static final Integer MissingKey = 20;
  protected static final String MissingValue = "Dastardly";

  protected static final Integer PresentKey = 22;
  protected static final String PresentValue = "Sliced Bread";

  public static void addSequenceOfItems(int quantity,
      DictionaryADT<Integer, String> underTest) {
    for (int count = 0; count < quantity; count++) {
      underTest.add(count, Integer.toString(count));
    }
  }

  DictionaryADT<Integer, String> sut;

  @Test
  public void add_duplicate_differentReturns() {
    // The first add should be true, and the second false
    assertThat(sut.add(DuplicateKey, PresentValue),
        is(not(sut.add(DuplicateKey, MissingValue))));
  }

  @Test
  public void add_duplicate_secondInsertionFalse() {
    sut.add(DuplicateKey, DuplicateValue);
    assertFalse(sut.add(new Integer(DuplicateKey), MissingValue));
  }

  @Test
  public void add_duplicate_sizeCorrect() {
    sut.add(PresentKey, PresentValue);

    sut.add(DuplicateKey, DuplicateValue);
    sut.add(DuplicateKey, MissingValue);
    assertThat(sut.size(), is(equalTo(2)));
  }

  @Test
  public void add_duplicate_valueUnchanged() {
    sut.add(PresentKey, PresentValue);

    sut.add(DuplicateKey, DuplicateValue);
    sut.add(DuplicateKey, MissingValue);
    assertThat(sut.getValue(DuplicateKey), is(DuplicateValue));
  }

  @Test
  public void add_firstItem_returnsTrue() {
    assertTrue(sut.add(PresentKey, PresentValue));
  }

  @Test
  public void add2Delete1_sequence_discardValueMissing() {
    int cycles;
    for (cycles = 0; cycles < MedStructureSize; cycles++) {
      sut.add(cycles + 1, "Discard");
      sut.add(cycles, "Keep");
      sut.delete(cycles + 1);
    }
    assertThat(sut.getKey("Discard"), is(equalTo(null)));
  }

  @Test
  public void add2Delete1_sequence_sizeCorrect() {
    int cycles;
    for (cycles = 0; cycles < MedStructureSize; cycles++) {
      sut.add(cycles + 1, "Discard");
      sut.add(cycles, "Keep");
      sut.delete(cycles + 1);
    }
    assertThat(sut.size(), is(cycles));
  }

  @Test
  public void addAndDelete_multipleCycles_iterationCountCorrect() {

    // add and delete from the structure
    for (int cycles = 0; cycles < 5; cycles++) {
      generateDataSetRandom(MedStructureSize, sut);
      for (int i = 0; i < MedStructureSize - 1; i++) {
        sut.delete(i);
      }
    }

    // add the only items that should now be in the structure
    generateDataSetRandom(MedStructureSize, sut);

    int itemCount = 0;
    Iterator<Integer> keySet = sut.keys();
    while (keySet.hasNext()) {
      keySet.next();
      itemCount++;
    }
    assertThat(itemCount, is(MedStructureSize));
  }

  @Test
  public void addAndDelete_multipleCycles_sizeCorrect() {

    // add and delete from the structure
    for (int cycles = 0; cycles < 5; cycles++) {
      generateDataSetRandom(MedStructureSize, sut);
      for (int i = 0; i < MedStructureSize; i++) {
        sut.delete(i);
      }
    }

    // add the only items that should now be in the structure
    generateDataSetRandom(MedStructureSize, sut);

    assertThat(sut.size(), is(MedStructureSize));
  }

  @Test
  public void clear_emptyStructureAddAfter_sizeCorrect() {
    sut.clear();
    sut.add(PresentKey, PresentValue);
    assertThat(sut.size(), is(1));
  }

  @Test
  public void clear_populatedStructure_isEmpty() {
    for (int count = 0; count < MedStructureSize; count++) {
      sut.add(count, DuplicateValue);
    }
    sut.clear();
    assertThat(sut.isEmpty(), is(true));
  }

  @Test
  public void clear_populatedStructureAddAfter_sizeCorrect() {
    for (int count = 0; count < MedStructureSize; count++) {
      sut.add(count, DuplicateValue);
    }
    sut.clear();
    sut.add(PresentKey, PresentValue);
    assertThat(sut.size(), is(1));
  }

  @Test
  public void contains_emptyStructure_false() {
    assertFalse(sut.contains(new Integer(MissingKey)));
  }

  @Test
  public void contains_missingEntry_false() {
    sut.add(PresentKey, PresentValue);
    assertFalse(sut.contains(new Integer(MissingKey)));
  }

  @Test
  public void contains_presentEntry_true() {
    sut.add(PresentKey, PresentValue);
    assertTrue(sut.contains(new Integer(PresentKey)));
  }

  @Test
  public void delete_afterClear_false() {
    sut.add(DeletedKey, DeletedValue);
    sut.clear();
    assertThat(sut.delete(DeletedKey), is(false));
  }

  @Test
  public void delete_emptyStructure_returnsFalse() {
    assertFalse(sut.delete(new Integer(MissingKey)));
  }

  @Test
  public void delete_onlyItem_noContains() {
    sut.add(DeletedKey, DeletedValue);
    sut.delete(DeletedKey);
    assertFalse(sut.contains(DeletedKey));
  }

  @Test
  public void delete_secondItem_noContains() {
    sut.add(PresentKey, PresentValue);
    sut.add(DeletedKey, DeletedValue);
    sut.delete(DeletedKey);
    assertFalse(sut.contains(DeletedKey));
  }

  @Test
  public void delete_onlyItem_sizeCorrect() {
    sut.add(DeletedKey, DeletedValue);
    sut.delete(DeletedKey);
    assertThat(sut.size(), is(equalTo(0)));
  }

  private void generateDataSetRandom(int size,
      DictionaryADT<Integer, String> target) {
    Vector<Integer> unusedKeys = new Vector<Integer>();
    for (int i = 0; i < size; i++) {
      unusedKeys.add(i);
    }

    // test must be independent and repeatable, so we need a dedicated rng
    Random generator = new Random(310);
    int insertOrder = 0;
    while (unusedKeys.isEmpty() == false) {
      sut.add(unusedKeys.remove(generator.nextInt(unusedKeys.size())),
          "Inserted: " + insertOrder++);
    }
  }

  @Test
  public void deleteMultiple_randomInsertionOrder_noOddKeys() {
    generateDataSetRandom(MedStructureSize, sut);

    for (int i = 1; i < MedStructureSize; i += 2) {
      sut.delete(i);
    }

    int oddKeyCount = 0;
    Iterator<Integer> it = sut.keys();
    while (it.hasNext()) {
      int entry = it.next();
      if (entry % 2 > 0) {
        oddKeyCount++;
      }
    }
    assertThat(oddKeyCount, is(0));
  }

  @Test
  public void getKey_populatedStructure_correct() {

    sut.add(PresentKey, PresentValue);
    for (int count = 0; count < MedStructureSize; count++) {
      sut.add(count, DuplicateValue);
    }
    assertThat(sut.getKey(PresentValue), is(PresentKey));
  }

  @Test
  public void getValue_onlyItem_correct() {
    sut.add(PresentKey, PresentValue);
    assertThat(sut.getValue(PresentKey), is(equalTo(PresentValue)));
  }

  @Test(timeout = 5000)
  public void getValue_randomInsertionOrder_correct() {

    Random generator = new Random(310);

    sut.add(PresentKey, PresentValue);
    for (int count = 0; count < MedStructureSize; count++) {
      Integer value = generator.nextInt();
      sut.add(value, value.toString());
    }
    assertThat(sut.getValue(new Integer(PresentKey)), is(PresentValue));
  }

  @Test(timeout = 5000)
  public void isFull_largeStructure_notFull() {
    for (int count = 0; count < LargeStructureSize; count++) {
      sut.add(count, DuplicateValue);
    }
    assertThat(sut.isFull(), is(false));
  }

  @Test(timeout = 5000)
  public void isFull_medStructure_notFull() {
    for (int count = 0; count < MedStructureSize; count++) {
      sut.add(count, DuplicateValue);
    }
    assertThat(sut.isFull(), is(false));
  }

  @Test
  public void keys_medStructure_inOrder() {

    for (int i = 0; i < MedStructureSize; i++) {
      sut.add(i, PresentValue);
    }

    addSequenceOfItems(MedStructureSize, sut);
    Iterator<Integer> it = sut.keys();
    int previous = 0;
    int current = 0;
    while (it.hasNext() && current >= previous) {
      previous = current;
      current = it.next();
    }
    assertThat(current, is(MedStructureSize - 1));
  }

  @Test
  public void keys_medStructure_correctCount() {

    for (int i = 0; i < MedStructureSize; i++) {
      sut.add(i, PresentValue);
    }

    int itemCount = 0;
    Iterator<Integer> it = sut.keys();
    while (it.hasNext()) {
      itemCount++;
      it.next();
    }

    assertThat(itemCount, is(sut.size()));
  }

  @Test(expected = ConcurrentModificationException.class)
  public void keys_medStructure_concurrentModification() {

    for (int i = 0; i < MedStructureSize; i++) {
      sut.add(i, PresentValue);
    }

    int itemCount = 0;
    Iterator<Integer> it = sut.keys();
    while (it.hasNext()) {
      itemCount++;
      sut.delete(itemCount);
      it.next();
    }

    fail("Deletion failed to throw Concurrent modification");
  }

  @Test(expected = ConcurrentModificationException.class)
  public void keys_clearingMedStructure_concurrentModification() {

    for (int i = 0; i < MedStructureSize; i++) {
      sut.add(i, PresentValue);
    }

    Iterator<Integer> it = sut.keys();
    while (it.hasNext()) {
      sut.clear();
      it.next();
    }

    fail("Adding failed to throw Concurrent modification");
  }

  @Test(expected = NoSuchElementException.class)
  public void keys_callingNextPastHasNext_noSuchElementException() {

    sut.add(PresentKey, PresentValue);

    Iterator<Integer> it = sut.keys();
    while (it.hasNext()) {
      it.next();
    }

    it.next();

    fail("Note: iterating past hasNext did not produce an exception");
  }

  @Test
  public void replaceExistingValue_replaced() {
    sut.add(PresentKey, DeletedValue);
    for (int count = 0; count < MedStructureSize; count++) {
      sut.add(count, DuplicateValue);
    }
    sut.delete(PresentKey);
    sut.add(PresentKey, PresentValue);
    assertThat(sut.getValue(PresentKey), is(PresentValue));
  }

  @Before
  public abstract void setUp() throws Exception;

  @Test
  public void size_afterConstruction_zero() {
    assertThat(sut.size(), is(0));
  }

}
