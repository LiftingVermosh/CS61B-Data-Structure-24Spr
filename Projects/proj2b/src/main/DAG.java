package main;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DAG {
    private HashMap<Integer, List<Integer>> adjList;
    private HashMap<Integer, List<Integer>> reversedAdjList;
    private HashMap<Integer,String> idx2Synset;

    public DAG() {
        adjList = new HashMap<>();
        reversedAdjList = new HashMap<>();
        idx2Synset = new HashMap<>();
    }

    public void registerNode(int node, String synset) {
        if (adjList.containsKey(node)) {
            System.out.println("Warning: node " + node + " is already registered");
            return;
        }
        if(synset.isEmpty()) {
            System.out.println("Warning: no synset found");
        }
        idx2Synset.put(node,synset);
        adjList.put(node, new LinkedList<>());
        reversedAdjList.put(node, new LinkedList<>());
    }

    public void addEdge(int src, int dest) {
        if(!adjList.containsKey(src)) {
            throw new IllegalArgumentException("src vertex does not exist");
        }
        adjList.get(src).add(dest);
        reversedAdjList.get(src).add(dest);
    }

    public void removeEdge(int src, int dest) {
        if(!adjList.containsKey(src)) {
            throw new IllegalArgumentException("src vertex does not exist");
        }
        adjList.get(src).remove(dest);
    }

    public String getSynset(int src) {
        return idx2Synset.get(src);
    }

    public List<Integer> getDests(int src) {
        List<Integer> res = adjList.get(src);

        if(res == null) {
            throw new IllegalArgumentException("src vertex does not exist");
        }
        return Collections.unmodifiableList(res);
    }

    public List<String> getDestSynset(List<Integer> dests){
        List<String> res = new LinkedList<>();
        for(Integer dest : dests) {
            res.add(getSynset(dest));
        }
        return Collections.unmodifiableList(res);
    }

    public String destSynsetToString(int src) {
         List<String> res = getDestSynset(getDests(src));
         StringBuilder sb = new StringBuilder();
         sb.append("[");
         for(String s : res) {
             sb.append(s);
             sb.append(", ");
         }
         sb.append("]");
         return sb.toString();
    }
}
