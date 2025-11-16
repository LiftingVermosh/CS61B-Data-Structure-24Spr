import java.util.ArrayList;
import java.util.List;

public class ArrayDeque61B<T> implements Deque61B<T> {

    private T[] array;
    private int head;
    private int tail;
    private int size;
    private int count;

    @SuppressWarnings("unchecked")
    public ArrayDeque61B() {
        this.size = 8;
        this.array = (T[]) new Object[this.size];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }
    /**
     * Add {@code x} to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addFirst(T x) {
        volumeCheck();
        this.head = (head - 1) & (size - 1);
        this.array[head] = x;
        this.count++;
    }

    /**
     * Add {@code x} to the back of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addLast(T x) {
        volumeCheck();
        array[tail] = x;
        tail = (tail + 1) & (size - 1);
        this.count++;
    }

    private void volumeCheck() {
        if (this.count == this.size) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = this.array.length;
        int newCapacity = oldCapacity * 2;
        T[] newArray = (T[]) new Object[newCapacity];
        // 拷贝从 head 到旧数组末尾的元素
        int length1 = oldCapacity - this.head;
        System.arraycopy(this.array, this.head, newArray, 0, length1);
        // 如果存在环绕, 拷贝从旧数组索引 0 到 tail 之前的元素
        int length2 = this.count - length1;
        if (length2 > 0) {
            System.arraycopy(this.array, 0, newArray, length1, length2);
        }
        this.array = newArray;
        this.tail = this.count;
        this.head = 0;
        this.size = this.array.length;
    }

    /**
     * Returns a List copy of the deque. Does not alter the deque.
     *
     * @return a new list copy of the deque.
     */
    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        int temp = head;
        while (temp != tail) {
            list.add(array[temp]);
            temp = (temp + 1) & (size - 1);
        }
        return list;
    }

    /**
     * Returns if the deque is empty. Does not alter the deque.
     *
     * @return {@code true} if the deque has no elements, {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    /**
     * Returns the size of the deque. Does not alter the deque.
     *
     * @return the number of items in the deque.
     */
    @Override
    public int size() {
        return this.count;
    }

    /**
     * Remove and return the element at the front of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        T res =  array[head];
        array[head] = null;
        --this.count;
        head = (head + 1) & (size - 1);
        return res;
    }

    /**
     * Remove and return the element at the back of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        tail = (tail - 1) & (size - 1);
        T res =  array[tail];
        array[tail] = null;
        --this.count;
        return res;
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
        if (index < 0 || index >= count) {
            return null;
        }
        return array[(this.head + index) & (this.size - 1)];
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
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }
}
