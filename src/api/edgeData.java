package api;

import org.w3c.dom.Node;

public class edgeData implements edge_data{


    private int src , dest;
    private double edgeW;
    private int tag;
    private String info;

    public edgeData(int src , int dest , double weight){
        this.src = src;
        this.dest = dest;
        this.edgeW=weight;
        this.tag=-1;
        this.info="white";
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.edgeW;
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
}