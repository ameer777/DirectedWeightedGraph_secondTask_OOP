package api;

import com.google.gson.*;

import java.lang.reflect.Type;


public class GraphJsonDeserializer implements JsonDeserializer<DWGraph_DS> {
    @Override
    public DWGraph_DS deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray nodes = jsonObject.get("Nodes").getAsJsonArray();
        JsonArray edges = jsonObject.get("Edges").getAsJsonArray();


        DWGraph_DS ds = new DWGraph_DS();

        for(int i=0 ; i<nodes.size(); i++){
            JsonObject n = nodes.get(i).getAsJsonObject();
            String pos =n.get("pos").getAsString();
            NodeData node = new NodeData(n.get("id").getAsInt() , new geoLocation(pos));
            ds.addNode(node);
        }
        for (int i=0  ; i<edges.size() ; i++){
            JsonObject ed = edges.get(i).getAsJsonObject();
            edge_data e =new edgeData(ed.get("src").getAsInt() , ed.get("dest").getAsInt() , ed.get("w").getAsDouble());
            ds.connect(e.getSrc() , e.getDest() , e.getWeight());
        }

        return ds;
    }
}