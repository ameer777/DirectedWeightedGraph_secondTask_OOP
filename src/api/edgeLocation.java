package api;

public class edgeLocation implements edge_location {

    edge_data edge;

    @Override
    public edge_data getEdge() {
        return edge;
    }

    @Override
    public double getRatio() {
        return 0;
    }
}