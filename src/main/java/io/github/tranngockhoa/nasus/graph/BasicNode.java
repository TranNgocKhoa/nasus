package io.github.tranngockhoa.nasus.graph;

public class BasicNode implements Node {
    private String name;

    public BasicNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof BasicNode)) {
            return false;
        }

        return this.getName().equals(((BasicNode) obj).getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
