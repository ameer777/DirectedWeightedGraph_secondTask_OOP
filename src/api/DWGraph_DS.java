package api;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;


/**
 * this class implements directed_weighted_graph interfaces.
 * represents an directional weighted graph.
 * this implementation based on efficient compact representation with HashTable
 */

public class DWGraph_DS implements directed_weighted_graph {

    private Hashtable<Integer, Hashtable<Integer, edge_data>> edges;
    private Hashtable<Integer, node_data> graph;

    private int MC;
    private int edgeSize;

    //constructor
    public DWGraph_DS() {
        this.edges = new Hashtable<Integer, Hashtable<Integer, edge_data>>();
        this.graph = new Hashtable<Integer, node_data>();

        this.MC = 0;
        this.edgeSize = 0;
    }

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (this.graph.containsKey(key)) return this.graph.get(key);
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     *
     * @param src  the source node_id
     * @param dest the destination node_id
     * @return the edge_data , null if none
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (graph.containsKey(src) && graph.containsKey(dest) && src != dest && edges.get(src).get(dest)!=null) {
            return edges.get(src).get(dest);
        }
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * @param n - the node
     */
    @Override
    public void addNode(node_data n) {
        if (n != null && !this.graph.containsKey(n.getKey())) {
            this.graph.put(n.getKey(), n);
            this.edges.put(n.getKey(), new Hashtable<Integer, edge_data>());
            ++this.MC;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (src != dest && w >= 0 && this.graph.containsKey(src) && this.graph.containsKey(dest)) {
            if (getEdge(src, dest) == null) ++edgeSize;
            ++MC;
            edges.get(src).put(dest, new edgeData(src, dest, w));
        }
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        if (this.graph != null) return this.graph.values();
        return null;
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     *
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (this.graph.containsKey(node_id) && edges.get(node_id).values()!=null) return edges.get(node_id).values();
        Collection<edge_data> cl = new LinkedList<>();
        return cl;
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     *
     * @param key the node_id
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (!this.graph.containsKey(key)) return null;
        if (getE(key) != null) {
            for (node_data n : getV()) {
                if (edges.get(n.getKey()) != null && edges.get(n.getKey()).containsKey(key)) {
                    edges.get(n.getKey()).remove(key);
                    --this.edgeSize;
                }
            }
        }
        int count = edges.get(key).size();
        edges.remove(key);
        this.edgeSize = this.edgeSize - count;
        return graph.remove(key);
    }

    /**
     * Deletes the edge from the graph,
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (this.graph.containsKey(src) && this.graph.containsKey(dest) && getEdge(src, dest) != null) {
            --this.edgeSize;
            ++this.MC;
            return edges.get(src).remove(dest);
        }
        return null;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     *
     * @return the number of vertices
     */
    @Override
    public int nodeSize() {
        return this.graph.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     *
     * @return the number of edges
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return this.MC;
    }

}