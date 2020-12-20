package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.*;

/**
 * This interface represents a Directed (positive) Weighted Graph Theory Algorithms.
 */

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph graphAlgo;
    private Hashtable<Integer, node_data> parent = new Hashtable<>();

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g the graph
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.graphAlgo = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return the graph.
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.graphAlgo;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * @return the copied graph
     */
    @Override
    public directed_weighted_graph copy() {
        if (this.graphAlgo == null) return null;
        DWGraph_DS copyGraph = new DWGraph_DS();

        //deep copy for all nodes
        for (node_data node : graphAlgo.getV()) {
            node_data temp = new NodeData(node.getKey());
            temp.setWeight(node.getWeight());
            temp.setInfo(node.getInfo());
            temp.setTag(node.getTag());
            temp.setLocation(node.getLocation());
            copyGraph.addNode(temp);
        }

        //connect all edges
        for (node_data node : graphAlgo.getV()) {
            if (graphAlgo.getE(node.getKey()) != null) {
                for (edge_data edge : graphAlgo.getE(node.getKey())) {
                    copyGraph.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
                    copyGraph.getEdge(edge.getSrc(), edge.getDest()).setTag(this.graphAlgo.getEdge(edge.getSrc(), edge.getDest()).getTag());
                    copyGraph.getEdge(edge.getSrc(), edge.getDest()).setInfo(this.graphAlgo.getEdge(edge.getSrc(), edge.getDest()).getInfo());
                }
            }
        }
        return copyGraph;
    }

    /**
     * Returns true if and only if there is a valid path from each node to each other node.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (this.graphAlgo != null && this.graphAlgo.nodeSize() == 0)
            return true;// if there is no nodes in the graph then is connected
        if (this.graphAlgo != null && this.graphAlgo.nodeSize() == 1)
            return true;// If there is one node in the graph then is connected
        if (this.graphAlgo == null) return true;

        Collection<node_data> c = this.graphAlgo.getV();
        Iterator<node_data> j = c.iterator();
        node_data n1 = j.next();
        Iterator<node_data> myNodes = c.iterator();
        while (myNodes.hasNext()) {
            node_data node = myNodes.next();
            if (this.graphAlgo.getE(node.getKey()) == null || this.graphAlgo.getE(node.getKey()).size() == 0)
                return false;
        }
        bfs((DWGraph_DS) this.graphAlgo, this.graphAlgo.getNode(n1.getKey()));
        Iterator<node_data> i = c.iterator();
        while (i.hasNext()) {
            node_data node = i.next();
            if (node != null)
                //if one of the nodes info is not black , the bfs didn't reach the node
                if (!node.getInfo().equals("black")) return false;
        }
        return true;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path, returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest) return 0;
        List<node_data> list = shortestPath(src, dest);
        if (list == null) return -1;
        if (list.get(list.size() - 1).getWeight() == Double.POSITIVE_INFINITY) return -1;
        return list.get(list.size() - 1).getWeight();
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes.
     * if no such path, returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return the shortest path list
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (graphAlgo.getNode(src) == null || graphAlgo.getNode(dest) == null) return null;
        List<node_data> list = new ArrayList<>();
        if (src == dest && graphAlgo.getNode(src) != null) {
            list.add(graphAlgo.getNode(src));
            return list;
        }
        Dijkstra(src);
        node_data vertex = graphAlgo.getNode(dest);
        list.add(vertex);
        int i = 1;
        while (vertex != graphAlgo.getNode(src) && vertex != null) {

            vertex = parent.get(vertex.getKey());
            list.add(vertex);
            i++;
            if (vertex != null && vertex.getKey() == src) break;
            if (i > parent.size()) return null;
        }
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getWeight() == Double.POSITIVE_INFINITY) return null;
        }

        Collections.reverse(list);
        return list;
    }

    /**
     * Saves this graph to the given
     * file name - in JSON format
     *
     * @param file - the file name.
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileWriter f = new FileWriter(file);
            Gson gson = new Gson();

            JsonObject jo = new JsonObject();
            JsonArray nodes = new JsonArray();
            JsonArray edges = new JsonArray();

            for (node_data node : graphAlgo.getV()) {
                JsonObject jsonObject = new JsonObject();
                String s = String.valueOf(node.getLocation().x());
                s = s + ',' + +node.getLocation().y();
                s = s + ',' + node.getLocation().z();
                jsonObject.addProperty("pos", s);
                nodes.add(jsonObject);
                jsonObject.addProperty("id", node.getKey());
            }


            for (node_data n : graphAlgo.getV()) {
                for (edge_data e : graphAlgo.getE(n.getKey())) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("src", e.getSrc());
                    jsonObject.addProperty("w", e.getWeight());
                    jsonObject.addProperty("dest", e.getDest());
                    edges.add(jsonObject);
                }
            }
            jo.add("Edges", edges);
            jo.add("Nodes", nodes);
            f.flush();
            gson.toJson(jo, f);
            f.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(DWGraph_DS.class, new GraphJsonDeserializer());
            Gson gson = gsonBuilder.create();
            FileReader fr = new FileReader(file);
            directed_weighted_graph ds = gson.fromJson(fr, DWGraph_DS.class);
            init(ds);
            return true;

        } catch (FileNotFoundException e) {
            System.err.println("file unsuccessfully loaded, the graph remain as is");
            return false;
        }
    }

    /* this algorithm use for finding the shortest paths between nodes-
       in a  directed Weighted Graph, by using PriorityQueue compare by node weight.
       this method run in O(n+e) when e is number of edges
     */
    public void Dijkstra(int src) {
        node_data n = graphAlgo.getNode(src);
        PriorityQueue<node_data> pq = new PriorityQueue<node_data>();
        pq.add(n);
        //set all nodes tag in white(white = no visited) and add them to PQ
        for (node_data vertex : graphAlgo.getV()) {
            if (vertex.getKey() != src) vertex.setWeight(Double.POSITIVE_INFINITY);
            vertex.setInfo("white");
            pq.add(vertex);
        }
        n.setWeight(0);
        n.setInfo("black");
        while (!pq.isEmpty()) {
            node_data curr = pq.remove();
            if (graphAlgo.getE(curr.getKey()) != null) {
                for (edge_data edge : graphAlgo.getE(curr.getKey())) {
                    if (graphAlgo.getNode(edge.getDest()) != null) {
                        node_data vertex = graphAlgo.getNode(edge.getDest());
                        double dist = curr.getWeight() + this.graphAlgo.getEdge(curr.getKey(), vertex.getKey()).getWeight();
                        if (vertex.getInfo().equals("white") && (dist < vertex.getWeight())) {
                            vertex.setWeight(curr.getWeight());
                            if (vertex.getWeight() < dist) {
                                vertex.setWeight(dist);
                                parent.put(vertex.getKey(), curr);
                                //update queue
                                pq.remove(vertex);
                                pq.add(vertex);
                            }
                        }
                        curr.setInfo("black");
                    }
                }
            }
        }
    }

    public void bfs(DWGraph_DS g , node_data n) {

        if(n != null && g != null) {
            Collection<node_data> c =  g.getV();
            Iterator<node_data> j = c.iterator();
            /* Go through all the nodes in the graph and paint them "white" and gives them tag = 0 at the first */
            while(j.hasNext()) {
                node_data nodeS = j.next();
                if(nodeS != null) {
                    nodeS.setInfo("white");
                }

            }
            g.getNode(n.getKey()).setInfo("gray");

            Queue<node_data> queue = new LinkedList<node_data>();
            queue.add(n);//add the node to the queue

            //Run while queue is not empty
            while(!queue.isEmpty()) {
                node_data u = queue.remove();
                if(u.getInfo() != "black")
                    u.setInfo("gray");
                Collection<edge_data> neighbours = this.graphAlgo.getE(u.getKey());
                Iterator <edge_data> i = neighbours.iterator();

                /* Check if the node is not black: there are two options: - white then paint it gray and add it to the queue,
                 gray and then just paint it black The difference between white and gray is:
                 white it means that the node has not been tested at all,
                 gray means it has been tested but still not sure Then it needs to be checked again,
                 if the node is painted black it means it has been checked and
                 it is connected to another node whose color is black (also connected)*/
                while(i.hasNext()) {
                    edge_data index = i.next();
                    if(this.graphAlgo.getNode(index.getDest()).getInfo() == "white") {
                        this.graphAlgo.getNode(index.getDest()).setInfo("black");
                        queue.add(this.graphAlgo.getNode(index.getDest()));
                    }
                    else if(this.graphAlgo.getNode(index.getDest()).getInfo() == "gray"){
                        this.graphAlgo.getNode(index.getDest()).setInfo("black");
                    }
                }

            }
        }
        return;
    }
}