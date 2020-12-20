import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;


public class DWGraph_AlgoTest {

    @Test
    void copy() {
        DWGraph_Algo graphAlgo = new DWGraph_Algo();
        DWGraph_Algo graphAlgoCopy = new DWGraph_Algo();
        directed_weighted_graph graph = simple_graph();
        directed_weighted_graph graphcopy = new DWGraph_DS();

        for(int i = 0; i < 800; i++){
            int x = (int) (Math.random() * 799);
            int y = (int) (Math.random() * 799);
            double w = (Math.random() * 10);
            graph.connect(x,y,w);
        }
        graphAlgo.init(graph);
        graphcopy = graphAlgo.copy();
        graphAlgoCopy.init(graphcopy);

        if(graphAlgo.isConnected())
            assertTrue(graphAlgoCopy.isConnected());
        else assertFalse(graphAlgoCopy.isConnected());

        for(int i = 0; i < 1000;i++) {
            int x = (int) (Math.random() * 499);
            int y = (int) (Math.random() * 499);
            if (graph.getEdge(x, y) != null) {
                double shortp = graphAlgo.shortestPathDist(x, y);
                double shortpcopy = graphAlgoCopy.shortestPathDist(x, y);
                assertEquals(shortp, shortpcopy, 0.0);

                List<node_data> path1 =  graphAlgo.shortestPath(x,y);
                List<node_data> path2 =  graphAlgoCopy.shortestPath(x,y);

                if(path1 != null && path2 != null)
                assertEquals(path1.size(),path2.size());

                if(path1 != null) {
                    for (int j = 0; j < path1.size(); j++) {
                        if(path1.get(j) != null && path2.get(j) != null)
                        assertEquals(path1.get(j).getKey(), path2.get(j).getKey());
                    }

                }
            }
        }

        for(int i = 0; i < 500; i++)
            assertNotNull(graphcopy.getNode(i));


        int s = graph.nodeSize();
        int sc = graphcopy.nodeSize();
        assertEquals(s,sc);

        int e = graph.edgeSize();
        int ec = graphcopy.edgeSize();
        assertEquals(e,ec);

        for(int i = 0; i < 2000;i++){
            int x = (int) (Math.random() * 799);
            int y = (int) (Math.random() * 799);
            if(graph.getEdge(x,y) != null){
                assertNotNull(graphcopy.getEdge(x, y));
                double w1 = graph.getEdge(x,y).getWeight();
                double w2 = graphcopy.getEdge(x,y).getWeight();
                assertEquals(w1, w2, 0.0);
            }
        }

        for(int i =0; i < 2000;i++) {
            int x = (int) (Math.random() * 499);
            int y = (int) (Math.random() * 499);
            double w =  (Math.random()*100);
            graph.removeNode(x);
            graph.removeEdge(x,y);
            assertNotNull(graphcopy.getNode(x));
            assertNull(graph.getNode(x));

            if (graph.getEdge(x,y) != null) {
                assertNull(graph.getEdge(x, y));
                assertNotNull(graphcopy.getEdge(x, y));
            }
            if(graph.getEdge(x,y) == null && graphcopy.getEdge(x,y) == null){
                graph.connect(x,y,w);
                assertNull(graphcopy.getEdge(x, y));
            }

            if(graph.getEdge(x,y) != null && graphcopy.getEdge(x,y) != null){
                graphcopy.removeEdge(x,y);
                assertNull(graphcopy.getEdge(x, y));
                assertNotNull(graph.getEdge(x, y));
            }

        }
    }
    @Test
    void isConnected() {

        directed_weighted_graph graph = small_graph();
        dw_graph_algorithms graphAlgo = new DWGraph_Algo();
        graphAlgo.init(graph);

        assertFalse(graphAlgo.isConnected());

        graph.connect(2,3,1);
        for(node_data i : graph.getV())
            for(node_data j : graph.getV())
                assertNotNull(graphAlgo.shortestPath(i.getKey(), j.getKey()));

        graph.removeEdge(7,5);
        assertFalse(graphAlgo.isConnected());

        graph.connect(7,6,1);
        assertTrue(graphAlgo.isConnected());

        graph.removeEdge(1,2);
        assertFalse(graphAlgo.isConnected());

        graph.connect(1,2,1);
        graph.removeNode(2);
        assertTrue(graphAlgo.isConnected());

        node_data node = new NodeData(10);
        graph.addNode(node);
        graph.connect(7,10,2);
        assertFalse(graphAlgo.isConnected());

        graph.connect(10,5,2);
        assertTrue(graphAlgo.isConnected());

        graph.connect(10,6,1);
        graph.removeEdge(3,7);
        assertFalse(graphAlgo.isConnected());

        graph.connect(5,10,1);
        graph.connect(10,7,1);

        assertTrue(graphAlgo.isConnected());

    }

    @Test
    void shortestPathDist() {

        directed_weighted_graph graph = small_graph();
        DWGraph_Algo graphAlgo = new DWGraph_Algo();
        graphAlgo.init(graph);

        double w0 = graphAlgo.shortestPathDist(3,4);
        assertEquals(6, w0, 0.0);
        w0 = graphAlgo.shortestPathDist(4,2);
        assertEquals(5, w0, 0.0);

        for(int i = 1; i  <= 7 ; i++){
            double w1 = graphAlgo.shortestPathDist(i,8);
            assertEquals(-1, w1, 0.0);
        }

        double w2 = graphAlgo.shortestPathDist(6,2);
        assertEquals(7, w2, 0.0);

        graph.removeNode(1);

        for(int i = 3; i <= 7 ; i++){
            double w3 = graphAlgo.shortestPathDist(i,2);
            assertEquals(-1, w3, 0.0);
        }

        graph.removeEdge(5,3);
        graph.removeEdge(3,7);

        for(int i = 2 ; i <= 7; i++) {
            if (i != 3) {
                double w4 = graphAlgo.shortestPathDist(i, 3);
                assertEquals(-1, w4, 0.0);
            }
        }
        double w4 = graphAlgo.shortestPathDist(3, 3);
        assertEquals(0, w4, 0.0);


        graph.connect(3,2,15.76);
        assertEquals(15.76, graphAlgo.shortestPathDist(3, 2), 0.0);


        graph.removeEdge(6,4);
        graph.removeEdge(1,4);

        for(int i = 2 ; i <= 7 ; i++) {
            if (i != 4) {
                double w6 = graphAlgo.shortestPathDist(i, 4);
                assertEquals(-1, w6, 0.0);
            }
        }
        double w6 = graphAlgo.shortestPathDist(4, 4);
        assertEquals(0, w6, 0.0);
    }

    @Test
    void shortestPath() {

        directed_weighted_graph graph = small_graph();
        dw_graph_algorithms graphAlgo = new DWGraph_Algo();
        graphAlgo.init(graph);

        List<node_data> nodeList = graphAlgo.shortestPath(3,4);

        assertEquals(4, nodeList.get(nodeList.size()-1).getKey());
        assertEquals(6, nodeList.get(nodeList.size() - 1).getWeight(), 0.0);

        nodeList = graphAlgo.shortestPath(8,3);
        assertNull(nodeList);
        nodeList = graphAlgo.shortestPath(8,2);
        assertNull(nodeList);
        nodeList = graphAlgo.shortestPath(8,10);
        assertNull(nodeList);
        nodeList = graphAlgo.shortestPath(9,3);
        assertNull(nodeList);

        graph.removeNode(1);
        for(int i = 0 ; i < 8 ; i++) {
            nodeList = graphAlgo.shortestPath(1, i);
            assertNull(nodeList);
            nodeList = graphAlgo.shortestPath(i, 1);
            assertNull(nodeList);
        }
        graph.removeEdge(5,3);
        graph.removeEdge(3,7);
        for(int i = 1 ; i < 8 ; i++) {
            if(i != 3) {
                nodeList = graphAlgo.shortestPath(3, i);
                assertNull(nodeList);
                nodeList = graphAlgo.shortestPath(i, 3);
                assertNull(nodeList);
            }
        }


        nodeList = graphAlgo.shortestPath(7, 4);
        assertEquals(7, nodeList.get(0).getKey());
        assertEquals(5, nodeList.get(1).getKey());
        assertEquals(6, nodeList.get(2).getKey());
        assertEquals(4, nodeList.get(3).getKey());

        graph.removeNode(3);
        graph.removeNode(3);
        node_data node = new NodeData(1);
        graph.addNode(node);
        graph.connect(5,1,1.8);
        nodeList = graphAlgo.shortestPath(6, 1);
        assertEquals(6, nodeList.get(0).getKey());
        assertEquals(4, nodeList.get(1).getKey());
        assertEquals(5, nodeList.get(2).getKey());
        assertEquals(1, nodeList.get(3).getKey());

        graph.connect(4,1,3.9);
        nodeList = graphAlgo.shortestPath(6, 1);
        assertEquals(6, nodeList.get(0).getKey());
        assertEquals(4, nodeList.get(1).getKey());
        assertEquals(1, nodeList.get(2).getKey());

    }

    @Test
    void load(){
        directed_weighted_graph graph = small_graph();
        dw_graph_algorithms graphAlgo = new DWGraph_Algo();
        graphAlgo.init(graph);
        Assertions.assertTrue(graphAlgo.load("data/A0"));
        Assertions.assertTrue(graphAlgo.load("data/A5"));
        Assertions.assertTrue(graphAlgo.load("data/A1"));
        Assertions.assertTrue(graphAlgo.load("data/A2"));
        Assertions.assertTrue(graphAlgo.load("data/A3"));

    }

    @Test
    void save(){
        dw_graph_algorithms graphAlgo = new DWGraph_Algo();
        graphAlgo.load("data/A0");
        Assertions.assertTrue(graphAlgo.save("data/A"));
        try{
            File A0 = new File("data/A");
            File A = new File("data/A0");
            Scanner reader = new Scanner(A);
            String SA = "";
            while(reader.hasNext()){
                SA += reader.nextLine();
            }
            reader = new Scanner(A0);
            String SA0 = "";
            while(reader.hasNext()){
                SA0 += reader.nextLine();
            }
            Assertions.assertNotEquals(SA,SA0);
        }
        catch (Exception e){
            Assertions.fail("Failed to load file");
        }
        DWGraph_DS emptyG = new DWGraph_DS();
        graphAlgo.init(emptyG);
    }



    private directed_weighted_graph small_graph(){

        node_data a = new NodeData(1);//1
        node_data b = new NodeData(2);//2
        node_data c = new NodeData(3);//3
        node_data d = new NodeData(4);//4
        node_data e = new NodeData(5);//5
        node_data f = new NodeData(6);//6
        node_data g = new NodeData(7);//7

        directed_weighted_graph graph = new DWGraph_DS();
        graph.addNode(a);
        graph.addNode(b);
        graph.addNode(c);
        graph.addNode(d);
        graph.addNode(e);
        graph.addNode(f);
        graph.addNode(g);


        graph.connect(3,7,1);
        graph.connect(3,1,2);
        graph.connect(7,5,1);
        graph.connect(5,3,3);
        graph.connect(5,6,2);
        graph.connect(5,1,1);
        graph.connect(6,4,2);
        graph.connect(4,5,3);
        graph.connect(1,4,4);
        graph.connect(1,2,1);

        return graph;
    }

    private directed_weighted_graph simple_graph(){

        directed_weighted_graph graph = new DWGraph_DS();
        for(int i = 0 ; i <= 1500; i++){
            node_data node = new NodeData(i);
            graph.addNode(node);
        }

        return graph;
    }

    private directed_weighted_graph Sun_graph(){

        directed_weighted_graph graph = new DWGraph_DS();
        for(int i = 1 ; i <= 150; i++){
            node_data node = new NodeData(i);
            graph.addNode(node);
        }
        for(int i = 2 ; i <= 150; i++){
            double w = Math.random();
            graph.connect(1,i,w);
        }

        return graph;
    }
}
