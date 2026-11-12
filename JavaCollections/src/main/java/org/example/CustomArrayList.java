package org.example;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomArrayList<E> implements CustomList<E> {
    private Object[] elems;
    private int size;
    private int capacity;

    /**
     * Конструктор с дефолтной ёмкостью в 10.
     */
    public CustomArrayList() {
        this(10);
    }

    /**
     * Конструктор с пользовательской ёмкостью
     * @param capacity начальная ёмкость массива
     */
    public CustomArrayList(int capacity) {
        this.elems = new Object[capacity];
        this.size = 0;
        this.capacity = capacity;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Добавление элемента по индексу со сдвигом остальных.
     * @param element индекс для добавления элемента.
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        if (capacity == 0) {
            capacity = 1;
            elems = new Object[1];
        }

        if (size == capacity) {
            if (capacity % 2 != 0) {
                capacity = (int) (capacity * 1.5) + 1;
            } else {
                capacity = (int) (capacity * 1.5);
            }

            int count = 1;
            Object[] newElems = new Object[capacity];
            for (Object elem : elems) {
                newElems[count - 1] = elem;
                count++;
            }

            elems = newElems;
        }

        elems[size] = element;
        size++;
    }

    /**
     * Удаление элемента по индексу
     * @param index индекс элемента
     * @return удалённый элемент
     */
    @Override
    public E remove(int index) {
        if (!((0 <= index) && (index < size))) {
            throw new IndexOutOfBoundsException();
        }

        E result = (E) elems[index];
        for (int i = index + 1; i <= size - 1; i++) {
            elems[i - 1] = elems[i];
        }

        size--;
        elems[size] = null;

        return result;
    }

    /**
     * Получение элемента по указанному индексу
     * @param index индекс элемента
     * @return элемент
     */
    @Override
    public E get(int index) {
        if (!((0 <= index) && (index < size))) {
            throw new IndexOutOfBoundsException();
        }

        return (E) elems[index];
    }

    /**
     * Создание итератора по массиву (интерфейсный метод)
     * @return новый объёкт класса Iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<E> {
        private int cursor = 0;

        /**
         * Проверка наличия следующего элемента
         * @return true (элемент есть), иначе false
         */
        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        /**
         * Получение элемента и сдвиг указателя итератора на следующий
         * @return текущий элемент итерации
         */
        @Override
        public E next() {
            if (hasNext()) {
                return (E) elems[cursor++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
