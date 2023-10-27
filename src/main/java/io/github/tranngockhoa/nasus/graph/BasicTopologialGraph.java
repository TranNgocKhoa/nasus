package io.github.tranngockhoa.nasus.graph;

import java.util.*;
import java.util.function.Consumer;

public class BasicTopologialGraph implements Graph, TopologicalSort {
    private final HashMap<Node, HashSet<Node>> adjacencyList;

    public BasicTopologialGraph() {
        this.adjacencyList = new HashMap<>();
    }

    @Override
    public void addVertex(Node node) {
        adjacencyList.put(node, new HashSet<>());
    }

    @Override
    public void addEdge(Node from, Node to) {
        adjacencyList.get(from).add(to);
    }

    @Override
    public List<Node> sort() {
        Set<Node> visitedMap = new HashSet<>();
        Set<Node> inStackMap = new HashSet<>();
        List<Node> reversedTopoSortList = new LinkedList<>();

        for (Node node : this.adjacencyList.keySet()) {
            dfs(node, visitedMap, inStackMap, reversedTopoSortList::add);
        }

        Collections.reverse(reversedTopoSortList);

        return reversedTopoSortList;
    }

    private void dfs(Node node, Set<Node> visitedMap, Set<Node> inStackMap, Consumer<Node> afterFinishVisit) {
        if (visitedMap.contains(node)) {
            return;
        }

        if (inStackMap.contains(node)) {
            for (Node n : adjacencyList.keySet()) {
                if (adjacencyList.get(n).contains(node)) {
                    throw new CyclicException(node, n);
                }
            }
        }

        inStackMap.add(node);

        HashSet<Node> edges = adjacencyList.get(node);
        for (Node nextByNode : edges) {
            dfs(nextByNode, visitedMap, inStackMap, afterFinishVisit);
        }

        afterFinishVisit.accept(node);
        visitedMap.add(node);
    }


}
