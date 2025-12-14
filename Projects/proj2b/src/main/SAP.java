package main;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;

public class SAP {

    private class PathEntry {
        final int node;
        final int dist;
        PathEntry(int node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }

    public int[] getSap(DAG dag, List<Integer> SIDA, List<Integer> SIDB) {
        int [] res = new int [2];
        HashMap<Integer, Integer> Aid2Dist = new HashMap<>();
        HashMap<Integer, Integer> Bid2Dist = new HashMap<>();
        ArrayDeque<PathEntry> BFSA = new ArrayDeque<>();
        ArrayDeque<PathEntry> BFSB = new ArrayDeque<>();

        res[0] = Integer.MAX_VALUE;
        res[1] = -1;

        for (int curNode : SIDA) {
            int curDepth = 0;
            Aid2Dist.put(curNode, curDepth);
            BFSA.offer(new PathEntry(curNode, curDepth));
        }
        for (int curNode : SIDB) {
            int curDepth = 0;
            Bid2Dist.put(curNode, curDepth);
            BFSB.offer(new PathEntry(curNode, curDepth));
        }

        while (!BFSA.isEmpty() || !BFSB.isEmpty()) {

            if (!BFSA.isEmpty()) {
                res = doBFSStep(dag, BFSA, Aid2Dist, Bid2Dist, res);
            }
            if (!BFSB.isEmpty()) {
                res = doBFSStep(dag, BFSB, Bid2Dist, Aid2Dist, res);
            }

        }
        return res[0] == Integer.MAX_VALUE ? new int[] {-1, -1} : res;
    }

    private int[] doBFSStep(DAG dag, ArrayDeque<PathEntry> currentQueue,
            HashMap<Integer, Integer> currentDistMap, HashMap<Integer, Integer> otherDistMap,
            int[] res) {

        PathEntry current = currentQueue.poll();
        int u = current.node;
        int d = current.dist;
        int currentMinDist = res[0];

        if (d >= currentMinDist) {
            return res;
        }

        for (int v : dag.getDests(u)) {
            // 检查访问
            if (currentDistMap.containsKey(v)) {
                continue;
            }
            // 更新距离，加入队列
            currentDistMap.put(v, d + 1);
            currentQueue.offer(new PathEntry(v, d + 1));

            if (otherDistMap.containsKey(v)) {
                // SAP 长度 = A 走到 v 的距离 + B 走到 v 的距离
                int pathLength = currentDistMap.get(v) + otherDistMap.get(v);
                if(currentMinDist > pathLength) {
                    currentMinDist = pathLength;
                    res[0] = pathLength;
                    res[1] = v;
                }
            }
        }
        return res;
    }

    public int ancestor(DAG dag, List<Integer> SIDA, List<Integer> SIDB){
        return getSap(dag, SIDA, SIDB)[1];
    }

    public int sap(DAG dag, List<Integer> SIDA, List<Integer> SIDB){
        return getSap(dag, SIDA, SIDB)[0];
    }
}
