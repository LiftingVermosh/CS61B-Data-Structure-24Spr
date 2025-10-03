import java.util.*;

public class BSTMap<K, V> implements Map61B<K, V> {

    private class BSTNode {
        final K key;
        V value;
        BSTNode left;
        BSTNode right;
        int height;  // 可选：用于平衡检查

        BSTNode(K key, V value){
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
        }

        // 安全的更新方法
        void updateInfo(){
            int leftHeight = (left == null) ? 0 : left.height;
            int rightHeight = (right == null) ? 0 : right.height;
            this.height = 1 + Math.max(leftHeight, rightHeight);
        }
    }

    private BSTNode root;
    private int size;
    private final Comparator<? super K> comparator;

    public BSTMap(){
        this.comparator = null;
    }

    public BSTMap(Comparator<? super K> comparator){
        this.comparator = comparator;
    }

    @Override
    public void put(K key, V value) {
        if(key == null){
            throw new IllegalArgumentException("Invalid Argument:Key is null");
        }
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode node, K key, V value){
        if(node == null){
            size++;
            return new BSTNode(key, value);
        }

        int cmp = compare(key, node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }

        node.updateInfo();
        return node;
    }

    @Override
    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("Invalid Argument:Key is null");
        return get(root, key);
    }

    private V get(BSTNode node, K key){
        if(node == null) return null;

        int cmp = compare(key, node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if(cmp > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    @Override
    public boolean containsKey(K key) {
        if(key == null) throw new IllegalArgumentException("Invalid Argument:Key is null");
        return get(root, key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        keySet(root, keys);
        return keys;
    }

    private void keySet(BSTNode node, Set<K> keys){
        if(node == null) return;
        keys.add(node.key);
        keySet(node.left, keys);
        keySet(node.right, keys);
    }

    @Override
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("Invalid Argument:Key is null");

        V oldValue = get(key);
        if (oldValue != null) {
            root = remove(root, key);
        }
        return oldValue;
    }

    private BSTNode remove(BSTNode node, K key){
        if(node == null) return null;

        int cmp = compare(key, node.key);
        if (cmp < 0) {
            node.left = remove(node.left, key);
        } else if (cmp > 0) {
            node.right = remove(node.right, key);
        } else {
            // 找到要删除的节点
            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }

            // 有两个子节点：用后继节点替换
            BSTNode successor = min(node.right);
            // 创建新节点（因为key是final）
            BSTNode newNode = new BSTNode(successor.key, successor.value);
            newNode.left = node.left;
            newNode.right = removeMin(node.right);
            node = newNode;
            // removeMin中已经减少size，这里不需要再减
        }

        if (node != null) {
            node.updateInfo();
        }
        return node;
    }

    private BSTNode min(BSTNode node){
        if (node == null) return null;
        while (node.left != null){
            node = node.left;
        }
        return node;
    }

    private BSTNode removeMin(BSTNode node){
        if (node == null) return null;
        if (node.left == null){
            size--;
            return node.right;
        }
        node.left = removeMin(node.left);
        if (node != null) {
            node.updateInfo();
        }
        return node;
    }

    @SuppressWarnings("unchecked")
    private int compare(K k1, K k2){
        if(comparator != null){
            return comparator.compare(k1, k2);
        } else {
            return ((Comparable<? super K>)k1).compareTo(k2);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTIterator();
    }

    private class BSTIterator implements Iterator<K> {
        private final Stack<BSTNode> stack;

        public BSTIterator(){
            stack = new Stack<>();
            pushLeft(root);
        }

        private void pushLeft(BSTNode node){
            while(node != null){
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            if (!hasNext()) throw new NoSuchElementException();

            BSTNode node = stack.pop();
            pushLeft(node.right);
            return node.key;
        }
    }
}
