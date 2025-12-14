package main;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class WordNet {
    private DAG dag;
    private SAP sapSolver;
    private HashMap<String, List<Integer>> synset2Idx;

    public WordNet(String synsetPath, String hypernymPath) {

        dag = new DAG();
        sapSolver = new SAP();
        synset2Idx = new HashMap<>();

        // Processing Synset
        In in = new In(synsetPath);
        while (in.hasNextLine()) {
            String[] curComponents = in.readLine().split(",");
            if(curComponents.length != 3) {
                throw new IllegalArgumentException("Invalid res while splitting synset:\nExcept: 3;\nget:"+curComponents.length);
            }
            int id =  Integer.parseInt(curComponents[0]);
            String word = curComponents[1];
            String Destination =  curComponents[2];
            dag.registerNode(id, word);
            if(!synset2Idx.containsKey(word)) {
                synset2Idx.put(word, new ArrayList<>());
            }
            synset2Idx.get(word).add(id);
        }
        in.close();

        // Processing Hypernym
        in = new  In(hypernymPath);
        while (in.hasNextLine()) {
            String[] curComponents = in.readLine().split(",");
            int sourceId = Integer.parseInt(curComponents[0]);
            for(int j = 1; j < curComponents.length; j++) {
                int destinationId = Integer.parseInt(curComponents[j]);
                dag.addEdge(sourceId, destinationId);
            }
        }
        in.close();
    }

    public boolean isNoun(String word) {
        return synset2Idx.containsKey(word);
    }

    public Iterator<String> nouns() {
        return synset2Idx.keySet().iterator();
    }

    public List<Integer> getSynsetIdx(String word) {
        return synset2Idx.get(word);
    }

    public int sap(String nounA, String nounB) {
        List<Integer> ida =  getSynsetIdx(nounA);
        List<Integer> idb =  getSynsetIdx(nounB);
        if(ida.isEmpty()) {
            throw  new IllegalArgumentException("No synset found: " + nounA);
        }
        if(idb.isEmpty()) {
            throw  new IllegalArgumentException("No synset found: " + nounB);
        }
        return sapSolver.sap(dag, ida, idb);
    }

    public String ancestor(String nounA, String nounB) {
        List<Integer> ida =  getSynsetIdx(nounA);
        List<Integer> idb =  getSynsetIdx(nounB);
        if(ida.isEmpty()) {
            throw  new IllegalArgumentException("No synset found: " + nounA);
        }
        if(idb.isEmpty()) {
            throw  new IllegalArgumentException("No synset found: " + nounB);
        }
        int ancestorId = sapSolver.ancestor(dag, ida, idb);
        return dag.getSynset(ancestorId);
    }
}
