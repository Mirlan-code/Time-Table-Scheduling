package algorithms;

import java.util.HashMap;

public class YearGroup{
    private StudentsGroup parent;
    private HashMap<String, StudentsGroup> child = new HashMap<>();


    public YearGroup(StudentsGroup parent) { this.parent = parent; }

    public StudentsGroup getParent() {
        return parent;
    }

    public HashMap<String, StudentsGroup> getChild() {
        return new HashMap<>(child);
    }

    public void addChild(StudentsGroup child) {
        this.child.put(child.name, child);
    }

    @Override
    public String toString() {
        return "YearGroup{" +
                "parent=" + parent +
                ", child=" + child +
                '}';
    }
}