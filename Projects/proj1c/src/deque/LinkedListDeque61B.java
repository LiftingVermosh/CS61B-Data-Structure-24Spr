package deque;
import java.util.*;

public class LinkedListDeque61B<T> implements Deque61B<T> {

    private class Node {
        T data;
        Node next;
        Node prev;

        public Node() {
            this.data = null;
            this.next = null;
            this.prev = null;
        }

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        public Node(T data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node dummyHead;
    private Node dummyTail;
    private int size;



    public LinkedListDeque61B() {
        this.size = 0;
        this.dummyHead = new Node();
        this.dummyTail = new Node();
        this.dummyHead.next = this.dummyTail;
        this.dummyTail.prev = this.dummyHead;
    }

    /**
     * Add {@code x} to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addFirst(T x) {
        Node newNode = new Node(x);
        Node curNext = this.dummyHead.next;
        this.dummyHead.next = newNode;
        newNode.prev = this.dummyHead;
        newNode.next = curNext;
        curNext.prev = newNode;
        ++this.size;
    }

    /**
     * Add {@code x} to the back of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    @Override
    public void addLast(T x) {
        Node newNode = new Node(x);
        Node curPrev = this.dummyTail.prev;
        this.dummyTail.prev = newNode;
        newNode.next = this.dummyTail;
        newNode.prev = curPrev;
        curPrev.next = newNode;
        ++this.size;
    }

    /**s
     * Returns a List copy of the deque. Does not alter the deque.
     *
     * @return a new list copy of the deque.
     */
    @Override
    public List<T> toList() {
        ArrayList<T> list = new ArrayList<>();
        Node current = this.dummyHead.next;
        while (current != this.dummyTail) {
            list.add(current.data);
            current = current.next;
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
        return this.dummyHead.next == this.dummyTail;
    }

    /**
     * Returns the size of the deque. Does not alter the deque.
     *
     * @return the number of items in the deque.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Remove and return the element at the front of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeFirst() {
        if (this.size == 0) {
            return null;
        }
        Node temp = this.dummyHead.next;
        temp.next.prev = dummyHead;
        dummyHead.next = temp.next;
        --this.size;
        return temp.data;
    }

    /**
     * Remove and return the element at the back of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    @Override
    public T removeLast() {
        if (this.size == 0) {
            return null;
        }
        Node temp = this.dummyTail.prev;
        temp.prev.next = dummyTail;
        dummyTail.prev = temp.prev;
        --this.size;
        return temp.data;
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
        if(index < 0 || index > this.size - 1) {
            return null;
        } else if(index < this.size / 2) {
            Node temp = this.dummyHead.next;
            for(int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp.data;
        } else {
            Node temp = this.dummyTail.prev;
            for(int i = 0; i < this.size - index - 1; i++) {
                temp = temp.prev;
            }
            return temp.data;
        }
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
        return getRecursiveHelper(index, this.dummyHead.next);
    }

    private T getRecursiveHelper(int index, Node current) {
        if(current == this.dummyTail) {
            return null;
        }

        if(index == 0){
            return current.data;
        }

        if(index > 0){
            return getRecursiveHelper(index - 1, current.next);
        }

        return null;
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node current;

        public LinkedListIterator() {
            this.current = dummyHead.next;
        }

        @Override
        public boolean hasNext() {
            return current != dummyTail;
        }

        @Override
        public T next() {
            if(current == dummyTail) {
                return null;
            } else {
                T res = current.data;
                current = current.next;
                return res;
            }
        }

        public T previous() {
            if (current == dummyHead.next) {
                throw new NoSuchElementException();
            } else {
                current = current.prev;
                return current.data;
            }
        }
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedListDeque61B<?> that = (LinkedListDeque61B<?>)o;
        if (this.size() != that.size()) return false;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != that.get(i)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.size(); i++) {
            sb.append(this.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}
