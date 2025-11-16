package deque;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;

//public class MaxArrayDeque61B<T> extends ArrayDeque61B<T> implements Deque61B<T> {
public class MaxArrayDeque61B<T> implements Deque61B<T>{
    private ArrayDeque61B<T> innerArrayDeque;
    private Comparator<T> defaultComparator;

    public MaxArrayDeque61B(Comparator<T> c) {
        this.innerArrayDeque = new ArrayDeque61B<>();
        this.defaultComparator = c;
    }

    private T findMax(Comparator<T> c) {
        if (this.innerArrayDeque.isEmpty()) {
            return null;
        }
        T cur = innerArrayDeque.get(0);
        T res = cur;
        for (int i = 1; i < innerArrayDeque.size(); i++) {
            cur = innerArrayDeque.get(i);
            if (c.compare(cur, res) > 0) {
                res = cur;
            }
        }
        return res;
    }


    public T max() {
        return findMax(defaultComparator);
    }

    public T max(Comparator<T> c){
        return findMax(c);
    }


    /**
     * Add {@code x} to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addFirst(T x) {
        this.innerArrayDeque.addFirst(x);
    }

    /**
     * Add {@code x} to the back of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addLast(T x) {
        this.innerArrayDeque.addLast(x);
    }

    /**
     * Returns a List copy of the deque. Does not alter the deque.
     *
     * @return a new list copy of the deque.
     */
    @Override
    public List<T> toList() {
        return this.innerArrayDeque.toList();
    }

    /**
     * Returns if the deque is empty. Does not alter the deque.
     *
     * @return {@code true} if the deque has no elements, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.innerArrayDeque.isEmpty();
    }

    /**
     * Returns the size of the deque. Does not alter the deque.
     *
     * @return the number of items in the deque.
     */
    @Override
    public int size() {
        return this.innerArrayDeque.size();
    }

    /**
     * Remove and return the element at the front of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeFirst() {
        return this.innerArrayDeque.removeFirst();
    }

    /**
     * Remove and return the element at the back of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeLast() {
        return this.innerArrayDeque.removeLast();
    }

    /**
     * The Deque61B abstract data type does not typically have a get method,
     * but we've included this extra operation to provide you with some
     * extra programming practice. Gets the element, iteratively. Returns
     * null if index is out of bounds. Does not alter the deque.
     *
     * @param index index to get
     * @return element at {@code index} in the deque
     */
    @Override
    public T get(int index) {
        return this.innerArrayDeque.get(index);
    }

    /**
     * This method technically shouldn't be in the interface, but it's here
     * to make testing nice. Gets an element, recursively. Returns null if
     * index is out of bounds. Does not alter the deque.
     *
     * @param index index to get
     * @return element at {@code index} in the deque
     */
    @Override
    public T getRecursive(int index) {
        return this.innerArrayDeque.getRecursive(index);
    }
}
