package io.github.tranngockhoa.nasus.bean.wire;

public class Connector {
    private final Wire wire;

    public Connector(Wire wire) {
        this.wire = wire;
    }

    public Connector(Wire wire, Adapter adapter) {
        this.wire = wire;
    }

    public void check() {
        System.out.println("Connector: ");
        System.out.println("Wire: " + wire);
        System.out.println();
    }
}
