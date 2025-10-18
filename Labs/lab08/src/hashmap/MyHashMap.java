package hashmap;

import picocli.CommandLine;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    private final double loadFactor;
    private int curElements;

    /** Constructors */
    public MyHashMap() {
        this.size = 16;
        this.loadFactor = 0.75;
        this.curElements = 0;

        this.buckets = initBuckets(size);
    }

    public MyHashMap(int initialCapacity) {
        this.size = initialCapacity;
        this.loadFactor = 0.75;
        this.curElements = 0;

        this.buckets = initBuckets(size);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.size = initialCapacity;
        this.loadFactor = loadFactor;
        this.curElements = 0;

        this.buckets = initBuckets(size);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        // TODO: Fill in this method.
        return new LinkedList<Node>();
    }

    /** 通过抑制类型检查初始化函数
     *  Initialize the `buckets` by SuppressWarning and Forced Type Change
     */
    @SuppressWarnings("unchecked")
    private  Collection<Node>[] initBuckets(int size) {
        Collection<Node>[] buckets = (Collection<Node>[]) new Collection[size];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = createBucket();
        }
        return buckets;
    }

    /**
     * Calculate the buckets index for given key
     * @param key
     * @return
     */
    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        int nonNegHash = hash & 0x7fffffff;
        return nonNegHash % size;
    }

    /**
     * Resize the buckets array when the load factor is exceeded
     */
    private void resize() {
        int newSize = (int) (buckets.length * 2);
        this.size = newSize;
        Collection<Node>[] newBuckets = initBuckets(newSize);
        for (Collection<Node> bucket : buckets) {
            for (Node n : bucket) {
                int newIndex = getIndexForResize(n.key, newSize);
                newBuckets[newIndex].add(n);
            }
        }
        this.buckets = newBuckets;
    }

    private int getIndexForResize(K key, int newSize) {
        if  (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        int nonNegHash = hash & 0x7fffffff;
        return  nonNegHash % newSize;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    /**
     * Associates the specified value with the specified key in this map.
     * If the map already contains the specified key, replaces the key's mapping
     * with the value specified.
     *
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node n : bucket) {
            boolean containsKey;
            if (key == null){
                containsKey = n.key == null;
            } else {
                containsKey = n.key.equals(key);
            }

            if (containsKey) {
                n.value = value;
                return;
            }
        }

        Node newNode = new Node(key, value);
        bucket.add(newNode);
        curElements++;

        if(curElements * 1.0 / buckets.length > loadFactor) {
            resize();
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     */
    @Override
    public V get(K key) {
        int index = getBucketIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node n : bucket) {
            boolean containsKey;
            if (key == null){
                containsKey = n.key == null;
            } else {
                containsKey = n.key.equals(key);
            }
            if (containsKey) {
                return n.value;
            }
        }
        return null;
    }

    /**
     * Returns whether this map contains a mapping for the specified key.
     *
     * @param key
     */
    @Override
    public boolean containsKey(K key) {
        int index = getBucketIndex(key);
        Collection<Node> bucket = buckets[index];
        for (Node n : bucket) {
            boolean containsKey;
            if (key == null) {
                containsKey = n.key == null;
            } else {
                containsKey = n.key.equals(key);
            }
            if (containsKey) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the number of key-value mappings in this map.
     */
    @Override
    public int size() {
        return this.curElements;
    }

    /**
     * Removes every mapping from this map.
     */
    @Override
    public void clear() {
        buckets = initBuckets(this.size);
        curElements = 0;
    }

    /**
     * Returns a Set view of the keys contained in this map. Not required for this lab.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            for (Node n : bucket) {
                set.add(n.key);
            }
        }
        return set;
    }

    /**
     * Removes the mapping for the specified key from this map if present,
     * or null if there is no such mapping.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException.
     *
     * @param key
     */
    @Override
    public V remove(K key) {
        int index = getBucketIndex(key);
        Collection<Node> bucket = buckets[index];

        Iterator<Node> it = bucket.iterator();
        while (it.hasNext()) {
            Node n = it.next();

            boolean keyMatch;
            if (key == null) {
                keyMatch = n.key == null;
            } else {
                keyMatch = n.key.equals(key);
            }

            if (keyMatch) {
                V removed = n.value;
                it.remove();
                curElements--;
                return removed;
            }
        }
        return null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private final Iterator<Node> nodeIterator;

        MyHashMapIterator() {
            Collection<Node> allNodes = new LinkedList<>();
            for (Collection<Node> bucket : buckets) {
                allNodes.addAll(bucket);
            }
            this.nodeIterator = allNodes.iterator();
        }

        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }
        @Override
        public K next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            return nodeIterator.next().key;
        }
    }

}
