public class UnionFind {
    // TODO: Instance variables
    final int N;
    int [] parent;  // 父节点数组
    int [] rank;    // Rank
    int [] size;    // 大小
    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        // 初始化方法
        this.N = N;
        this.parent = new int[N];
        this.rank = new int[N];
        this.size = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        if (v > N || v < 0){
            throw new IllegalArgumentException("Index out of bounds: " + v);
        }
        // 返回根节点的 Size
        return size[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        if (v > N || v < 0){
            throw new IllegalArgumentException("Index out of bounds: " + v);
        }
        int root = find(v);
        return root == v ? -size[root] : parent[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        if (v1 > N || v2 > N || v1 < 0 || v2 < 0) {
            throw new IllegalArgumentException("Index out of bounds: " + v1 + " or " + v2);
        }
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v > N || v < 0){
            throw new IllegalArgumentException("Index out of bounds: " + v);
        }
        if(this.parent[v] == v) return v;
        return this.parent[v] = find(this.parent[v]);  // 路径压缩
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (v1 > N || v2 > N || v1 < 0 || v2 < 0) {
            throw new IllegalArgumentException("Index out of bounds: " + v1 + " or " + v2);
        }
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == root2) {
            return;
        }

        if (size[root1] < size[root2]) {
            parent[root1] = root2;
            size[root2] += size[root1];
        } else if (size[root1] > size[root2]) {
            parent[root2] = root1;
            size[root1] += size[root2];
        } else  {
            parent[root1] = root2;
            ++rank[root2];
            size[root2] += size[root1];
        }
    }

}
