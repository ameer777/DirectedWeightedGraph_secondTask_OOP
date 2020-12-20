package gameClient;
import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.List;

public class GFrame extends JFrame {

    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private Image graphImg;
    private int Sl;
    private game_service _game;
    private directed_weighted_graph _graph;

    GFrame(String a, int level, game_service game ,directed_weighted_graph graph ){
        super(a);
        this.Sl = level;
        _game = game;
        _graph = graph;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    public void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        graphImg = createImage(w, h);
        Graphics graphics = graphImg.getGraphics();
        paintComponents(graphics);
        g.drawImage(graphImg, 0, -20, this);
        updateFrame();
    }

    @Override
    public void paintComponents(Graphics g) {
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        drawLevel(g);
        drawTime(g);
        drawResults(g);
    }

    public void drawLevel(Graphics g){
        Font graphFont = new Font("Level",Font.BOLD,30);
        g.setFont(graphFont);
        g.drawString("Level :" + Sl,this.getWidth()/35,90);
    }

    private void drawResults(Graphics g) {
        Font graphFontG = new Font("Level",Font.BOLD,27);
        Font graphFontA = new Font("Level",Font.BOLD,15);
        g.setFont(graphFontA);
        double agentsValue = 0 ;
        String lg =_game.getAgents();
        List<CL_Agent> log = Arena.getAgents(lg,_graph);
        int changeY = 105;
            for(int i = 0; i < log.size() ; i ++) {
                CL_Agent ag = log.get(i);
                double agv = ag.getValue();
                agentsValue+= agv;
                g.drawString("agent " + i + " : " + agv,  this.getWidth()/1 -110, changeY);
                changeY += 20;
            }
            g.setFont(graphFontG);
            g.drawString("GameResult : " +agentsValue, this.getWidth()/1 -280 , 85);
    }
    private void drawTime(Graphics g){
        int sec = (int) (_game.timeToEnd()/1000);
        int min = (int) (_game.timeToEnd()/60000);
        g.drawString("Time :" + min+":"+sec,this.getWidth()/2 - 40,90);
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }

    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {
                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

}