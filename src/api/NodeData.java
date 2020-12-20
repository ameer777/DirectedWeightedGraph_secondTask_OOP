package api;

/**
 * This interface represents the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 * this implementation based on efficient compact representation with HashTable
 */

public class NodeData implements node_data , Comparable<node_data> {

    private int key;
    private int tag;
    private String info;
    private double nodeW;
    private geo_location locate;

    //constructors
    public NodeData (int key){
        this.key=key;
        this.tag=-1;
        this.info="white";
        nodeW=-1;
        this.locate =new geoLocation();
    }
    public NodeData (int key, geoLocation locate){
        this.key=key;
        this.tag=-1;
        this.info="white";
        nodeW=-1;
        this.locate = locate;
    }

    //getters and setters
    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public geo_location getLocation() {
        return this.locate;
    }

    @Override
    public void setLocation(geo_location p) {
        this.locate =p;
    }

    @Override
    public double getWeight() {
        return this.nodeW;
    }

    @Override
    public void setWeight(double w) {
        this.nodeW=w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info=s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;
    }

    //compere nodes weight using in Dijkstra
    @Override
    public int compareTo(node_data o) {
        if(this.getWeight()-o.getWeight()>0) return 1;
        else if(this.getWeight()-o.getWeight()<0) return -1;
        return 0;
    }
}