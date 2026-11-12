package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {

    @Test
    public void newListHasSizeZero() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        assertEquals(0, list.size());
    }

    @Test
    public void newListIsEmpty() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        assertTrue(list.isEmpty());
    }

    @Test
    public void listIsNotEmptyAfterAdd() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(9);

        assertFalse(list.isEmpty());
    }

    @Test
    public void addOneElement() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(20);

        assertEquals(20, list.get(0));
    }

    @Test
    public void addMultipleElems() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 0; i <= 5; i++) {
            list.add((i * 10));
        }

        assertEquals(0, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(20, list.get(2));
        assertEquals(30, list.get(3));
        assertEquals(40, list.get(4));
        assertEquals(50, list.get(5));
    }

    @Test
    public void addBeyondInitialCapacity() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 0; i < 25; i++) {
            list.add(i);
        }

        assertEquals(25, list.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    public void getFirstMiddleLast() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");

        assertEquals("A", list.get(0));
        assertEquals("C", list.get(2));
        assertEquals("E", list.get(4));
    }

    @Test
    public void getThrowsIndexOutOfBounds() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(1);

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    public void removeThrowsIndexOutOfBounds() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        list.add(1);

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @ParameterizedTest
    @CsvSource({"0, 10", "2, 30", "4, 50"})
    void removeFirstMiddleLast(int index, int expected) {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add((i + 1) * 10);
        }

        assertEquals(expected, list.remove(index));
        assertEquals(4, list.size());

        CustomArrayList<Integer> test = new CustomArrayList<>();

        for (int i = 0; i < 4; i++) {
            if (i < index) {
                test.add((i + 1) * 10);
            } else {
                test.add((i + 2) * 10);
            }
        }

        assertIterableEquals(test, list);
    }

    @Test
    void addThrowsNullPointer() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    void iteratorGeneralTest() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }

        Iterator<Integer> iterator = list.iterator();
        for (int i = 1; i <= 5; i++) {
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    void iteratorThrowsNoSuchElem() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 1; i <= 3; i++) {
            list.add(i);
        }

        Iterator<Integer> iterator = list.iterator();
        for (int i = 1; i <= 3; i++) {
            iterator.next();
        }

        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }
}
