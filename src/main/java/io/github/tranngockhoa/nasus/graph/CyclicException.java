package io.github.tranngockhoa.nasus.graph;

public class CyclicException extends RuntimeException {

    public CyclicException(Node from, Node to) {
        super("Contains cyclic in graph " + from  + " => " + to);
    }
}
