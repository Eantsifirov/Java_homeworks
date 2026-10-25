package org.example;

public class Pair<K, V> {
    private K key;
    private V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K input) {
        key = input;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V input) {
        value = input;
    }

    public Pair<V, K> swap() {
        return new Pair<>(value, key);
    }

    @Override
    public String toString() {
        String result = "Pair{key=" + key + ", value=" + value + "}";
        return result;
    }
}
