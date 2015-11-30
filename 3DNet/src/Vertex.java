import java.util.ArrayList;


public class Vertex {
    private double[] coordinates;
    private double[] uvCoordinates;
    private ArrayList<Edge> edges=new ArrayList<Edge>();
    private ArrayList<Face> faces=new ArrayList<Face>();
    
    public Vertex(double[] coord, double[] uvCoord){
        coordinates=coord;
        uvCoordinates=uvCoord;
    }
    
    public void addEdge(Edge e){
        edges.add(e);
        //System.out.println("New size: "+edges.size()+"   "+this);
    }
    
    public double[] getCoordinates(){
        return coordinates;
    }
    
    public double[] getuvCoordinates(){
        return uvCoordinates;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void replace2Edges(Edge[] otherEdges, Edge e) {
        StdDraw.setPenColor(255,0,0);
        Structure.draw(otherEdges[0]);
        Structure.draw(otherEdges[1]);
        
        if(edges.contains(otherEdges[0]) && edges.contains(otherEdges[1])){
            edges.remove(otherEdges[0]);
            edges.remove(otherEdges[1]);
            edges.add(e);
        }else{
            System.out.println("ERROR: Edges not on vertex");
        }
    }
    
    
}
