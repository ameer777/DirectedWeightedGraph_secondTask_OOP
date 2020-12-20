package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2 implements Runnable {

    private static GFrame _win;
    private static loginToGame _log;
    private static Arena _ar;
    private static long dt = 100;
    private static int scenario_num;
    private static long id;

    public static void main(String[] args) {
        _log = new loginToGame();
        _log.loginToGame();

    }

    @Override
    public void run() {
        id = _log.getID();
        scenario_num = _log.getScenario();
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        game.login(id);
        String s = game.getGraph();
        directed_weighted_graph graph = loadGraph(s);
        init(game, graph);
        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());
        int ind = 0;

        while (game.isRunning()) {
            moveAgants(game, graph);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> poks = Arena.json2Pokemons(fs);
        _ar.setPokemons(poks);

        for (int i = 0; i < poks.size(); i++) Arena.updateEdge(poks.get(i), gg);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);

            int id = ag.getID();
            int dest = ag.getNextNode();
            double v = ag.getValue();
            if (dest == -1) {
                int index = ag.getIndex();
                if (ag.hasTarget()) {
                    if (index < ag.getList().size()) {
                        if (index == ag.getList().size() - 1)
                            game.chooseNextEdge(id, index);
                        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + index);
                        index++;
                        ag.setIndex(index);
                    } else {
                        path(ag, gg, poks);//new shortest path }
                        ag.setIndex(1);
                        game.chooseNextEdge(id, index);
                        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + index);
                        index++;
                        ag.setIndex(index);
                    }
                } else {
                    path(ag, gg, poks);//new shortest path }
                    ag.setIndex(1);
                    int temp = ag.getList().get(index).getKey();
                    game.chooseNextEdge(id, temp);
                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + temp);
                    index++;
                    ag.setIndex(index);
                }

            }
        }
    }

    private static void path(CL_Agent ca, directed_weighted_graph gg, List<CL_Pokemon> poks) {
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(gg);

        List<node_data> p = new ArrayList<>();
        List<node_data> min = new ArrayList<>();
        CL_Pokemon curPk = null;
        int i = 0;
        for (int j = 0; j < poks.size(); j++) {
            CL_Pokemon pk = poks.get(j);
            if (pk.isClear()) {
                p = ga.shortestPath(ca.getSrcNode(), pk.get_edge().getSrc());
                node_data node = gg.getNode(pk.get_edge().getDest());
                p.add(node);
                if (i == 0) {
                    min = p;
                    curPk = pk;
                }
                if (p.size() < min.size()) {
                    min = p;
                    curPk = pk;
                }
                i++;
            }

        }
        ca.setList(min);
        if (curPk != null) curPk.setClear(false);
        ca.setTarget(true);
    }

    private void init(game_service game, directed_weighted_graph graph) {
        String pokemons = game.getPokemons();

        _ar = new Arena();
        _ar.setGraph(graph);
        _ar.setPokemons(Arena.json2Pokemons(pokemons));
        String info = game.toString();

        id = _log.getID();
        scenario_num = _log.getScenario();

        _win = new GFrame("test Ex2", scenario_num, game, graph);
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();


        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gs = line.getJSONObject("GameServer");
            int rs = gs.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), graph);
            }
            int temp = 0;

            //check if have 2 pokemons on the same edge
            for (int i = 0; i < cl_fs.size() - 1; i++) {
                CL_Pokemon pk1 = cl_fs.get(i);
                for (int j = i + 1; j < cl_fs.size(); j++) {
                    CL_Pokemon pk2 = cl_fs.get(j);
                    if (pk1.get_edge().getSrc() == pk2.get_edge().getSrc() || pk1.get_edge().getSrc() == pk2.get_edge().getDest()) {
                        temp = pk1.get_edge().getSrc();
                    }
                }
            }
            for (int a = 0; a < rs; a++) {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }
                if (c.getType() > 0) {
                    nn = c.get_edge().getDest();
                }
                if (temp != 0) {
                    nn = temp;
                }
                game.addAgent(nn);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public directed_weighted_graph loadGraph(String g) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DWGraph_DS.class, new GraphJsonDeserializer());
        Gson gson = gsonBuilder.create();
        directed_weighted_graph ds = gson.fromJson(g, DWGraph_DS.class);
        return ds;
    }

}