package io.github.tranngockhoa.nasus.graph;

import java.util.List;

public class TestSort {
    public static void main(String[] args) {
        BasicTopologialGraph basicTopologialGraph = new BasicTopologialGraph();
        BasicNode one = new BasicNode("1");
        BasicNode two = new BasicNode("2");
        BasicNode three = new BasicNode("3");
        BasicNode four = new BasicNode("4");
        BasicNode five = new BasicNode("5");
        BasicNode six = new BasicNode("6");

        basicTopologialGraph.addVertex(one);
        basicTopologialGraph.addVertex(two);
        basicTopologialGraph.addVertex(three);
        basicTopologialGraph.addVertex(four);
        basicTopologialGraph.addVertex(five);
        basicTopologialGraph.addVertex(six);

        basicTopologialGraph.addEdge(one, two);
        basicTopologialGraph.addEdge(one, four);
        basicTopologialGraph.addEdge(two, three);
        basicTopologialGraph.addEdge(three, six);
        basicTopologialGraph.addEdge(three, five);
        basicTopologialGraph.addEdge(six, five);
//        basicTopologialGraph.addEdge(five, one);

        List<Node> sort = basicTopologialGraph.sort();

        for (Node node : sort) {
            System.out.println(node);
        }


        System.out.println("=====");
        BasicTopologialGraph basicTopologialGraph1 = new BasicTopologialGraph();

        basicTopologialGraph1.addVertex(one);
        basicTopologialGraph1.addVertex(two);
        basicTopologialGraph1.addVertex(three);
        basicTopologialGraph1.addVertex(four);
        basicTopologialGraph1.addVertex(five);

        basicTopologialGraph1.addEdge(one, two);
        basicTopologialGraph1.addEdge(one, five);
        basicTopologialGraph1.addEdge(two, three);
        basicTopologialGraph1.addEdge(three, four);
        basicTopologialGraph1.addEdge(one, five);
//        basicTopologialGraph.addEdge(five, one);

        List<Node> sort1 = basicTopologialGraph1.sort();

        for (Node node : sort1) {
            System.out.println(node);
        }
    }
}
