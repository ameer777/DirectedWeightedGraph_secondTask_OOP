import api.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;
public class DWGraph_DSTest {

    @Test
    void getNode(){

        directed_weighted_graph graph1 = small_graph();
        for(int i = 0; i < 50 ; i++){
            int random_nodeKey = (int) ((Math.random()*7) +1);
            int random_nullNode = (int) ((Math.random()*100) +8);
            assertNull(graph1.getNode(random_nullNode));
            node_data node = graph1.getNode(random_nodeKey);
            assertNotNull(node);
        }
        directed_weighted_graph graph2 = small_graph();
        for(int i = 0; i < 50 ; i++){
            int random_nodeKey = (int) ((Math.random()*7) +1);
            graph2.removeNode(random_nodeKey);
            node_data node = graph2.getNode(random_nodeKey);
            assertNull(node);
        }

        directed_weighted_graph graph3 = small_graph();
        for(int i = 0; i < 10 ; i++){
            int random_nodeKey = (int) ((Math.random()*500) +1);
            node_data node = new NodeData(random_nodeKey);
            graph3.addNode(node);
            node_data mynode = graph3.getNode(random_nodeKey);
            assertEquals(node.getKey(),mynode.getKey());
        }
    }

    @Test
    void getEdge(){

        directed_weighted_graph graph1 = small_graph();
        for(int i = 0; i < 50 ; i++){
            int random_nullsrc = (int) ((Math.random()*100) +7);
            int random_nulldest = (int) ((Math.random()*100) +7);
            edge_data null_e = graph1.getEdge(random_nullsrc,random_nulldest);
            assertNull(null_e);
        }

        assertNotNull(graph1.getEdge(3, 1));
        assertNotNull(graph1.getEdge(3, 7));
        assertNotNull(graph1.getEdge(7, 5));
        assertNotNull(graph1.getEdge(5, 1));
        assertNotNull(graph1.getEdge(5, 3));
        assertNotNull(graph1.getEdge(5, 6));
        assertNotNull(graph1.getEdge(6, 4));
        assertNotNull(graph1.getEdge(4, 5));
        assertNotNull(graph1.getEdge(1, 4));
        assertNotNull(graph1.getEdge(1, 2));

        assertNull(graph1.getEdge(3, 2));
        assertNull(graph1.getEdge(1, 3));
        assertNull(graph1.getEdge(6, 5));
        assertNull(graph1.getEdge(5, 7));
        assertNull(graph1.getEdge(5, 4));

        assertNull(graph1.getEdge(1, 1));
        assertNull(graph1.getEdge(2, 2));
        assertNull(graph1.getEdge(3, 3));
        assertNull(graph1.getEdge(4, 4));
        assertNull(graph1.getEdge(5, 5));
        assertNull(graph1.getEdge(6, 6));
        assertNull(graph1.getEdge(7, 7));
        assertNull(graph1.getEdge(9, 9));
        assertNull(graph1.getEdge(721, 721));
        assertNull(graph1.getEdge(710023, 710023));


        directed_weighted_graph graph2 = small_graph();
        for(int i = 0; i < 25 ; i++){
            int random_src = (int) ((Math.random()*7) +1);
            int random_dest = (int) ((Math.random()*7) +1);
            graph2.removeEdge(random_src,random_dest);
            edge_data e = graph2.getEdge(random_src,random_dest);
            assertNull(e);
        }

    }

    @Test
    void addNode(){

        directed_weighted_graph graph = new DWGraph_DS();
        for(int i = 0 ; i < 20 ; i++){
            node_data n = new NodeData(i);
            graph.addNode(n);
            assertNotNull(graph.getNode(i));
            assertEquals(n,graph.getNode(i));
        }

    }

    @Test
    void connect(){

        directed_weighted_graph graph = simple_graph();
        graph.connect(20034,12432,-123.54);
        assertNull(graph.getEdge(20034, 12432));
        for(int i = 0 ; i < 1000 ; i++) {
            int x = (int) (Math.random()* 1499);
            int y = (int) (Math.random()* 1499);
            double w = Math.random();
            if (x != y) {
                graph.connect(x, y, w);
                graph.connect(x, y, w);
                assertEquals(graph.getEdge(x, y).getWeight(), w, 0.0);
                double w2 = Math.random();
                graph.connect(x, y, w2);
                assertEquals(graph.getEdge(x, y).getWeight(), w2, 0.0);
                assertNull(graph.getEdge(x, x));
                assertNull(graph.getEdge(y, y));
                graph.connect(x,x,w);
                graph.connect(y,y,w);
                assertNull(graph.getEdge(x, x));
                assertNull(graph.getEdge(y, y));
                graph.connect(x,y,-w);
                assertTrue(graph.getEdge(x,y).getWeight() != -w);
            }
            else i--;
        }

    }

    @Test
    void getV(){

        directed_weighted_graph graph = simple_graph();
        Collection<node_data> col = graph.getV();
        Iterator<node_data> it = col.iterator();
        while (it.hasNext()){
            node_data n = it.next();
            assertNotNull(n);
        }
        assertEquals(col.size() , graph.nodeSize());
    }

    @Test
    void getE(){

        directed_weighted_graph graph = small_graph();
        assertEquals(graph.getE(1).size(),2);
        assertEquals(graph.getE(2).size(),0);
        assertEquals(graph.getE(3).size(),2);
        assertEquals(graph.getE(4).size(),1);
        assertEquals(graph.getE(5).size(),3);
        assertEquals(graph.getE(6).size(),1);
        assertEquals(graph.getE(7).size(),1);

    }

    @Test
    void removeNode(){

        directed_weighted_graph graph = simple_graph();

        int s0 = graph.nodeSize();
        int e0 = graph.edgeSize();

        for(int i = 0 ; i < 100 ; i ++){
            graph.connect(i,i+1,i);
            graph.removeNode(i+500);
            graph.removeNode(i);
            assertNull(graph.getEdge(i, i + 1));
        }

        for(int i = 0; i < 50; i++)
            graph.removeNode(i);

        assertEquals(1451,s0-50);
        assertEquals(0,e0);
    }

    @Test
    void removeEdge(){

        directed_weighted_graph graph = simple_graph();
        for(int i = 0 ; i < 500 ; i++){
            graph.connect(i,i+2,i+1/4);
            assertNotNull(graph.getEdge(i, i + 2));
            assertNull(graph.getEdge(i + 2, i));
            graph.removeEdge(i,i+2);
            assertNull(graph.getEdge(i, i + 2));
        }
        for(int i = 0 ; i < 500 ; i++){
            graph.connect(i,i+2,i+1/4 + 2);
            graph.connect(i+2,i,i+1/2 + 2);
            assertNotNull(graph.getEdge(i, i + 2));
            assertNotNull(graph.getEdge(i + 2, i));
            assertEquals(graph.getEdge(i, i + 2).getWeight(), i + 1 / 4 + 2, 0.0);
            assertEquals(graph.getEdge(i + 2, i).getWeight(), i + 1 / 2 + 2, 0.0);
        }

    }

    @Test
    void edgeSize(){

        directed_weighted_graph graph = simple_graph();

        for(int i = 0; i < 250 ; i++) {
            int x = i;
            int y = x + 5;
            double w = Math.random();
            graph.connect(x,y,w);
        }

        int e_size0 = graph.edgeSize();
        assertEquals(250,e_size0);

        int counter = 0;
        for(int i = 0; i < 250 ; i++) {
            int x = (int) (Math.random() * 250);
            int y = (int) (Math.random() * 250);
            if(graph.getEdge(x,y) != null) {
                graph.removeEdge(x, y);
                graph.removeEdge(y, x);
                graph.removeEdge(x,x);
                graph.removeEdge(y,y);
                counter++;
            }
        }

        int e_size1 = graph.edgeSize();
        assertEquals(e_size0 - counter,e_size1);

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

}
