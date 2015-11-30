
public class Face {
    private Edge[] edges; //always 3
    private Vertex[] vertices; //always 3
    
    public Face(Vertex v0, Vertex v1, Vertex v2){
        Vertex[] temp0={v0, v1, v2};
        vertices=temp0;
        //Edge[] temp1={e0, e1, e2};
        //edges=temp1;
    }
    
    public Edge[] getEdges() {
        return edges;
    }
    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }
    public Vertex[] getVertices() {
        return vertices;
    }
    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Vertex getOppositeVertex(Edge e) {
        if(e==edges[0]){
            return vertices[2];
        }
        if(e==edges[1]){
            return vertices[0];
        }
        if(e==edges[2]){
            return vertices[1];
        }
        System.out.println("ERROR: Edge not on face");
        return null;
    }
    
    public void replaceVertex(Vertex v0, Vertex v1){
        if(vertices[0]==v0){
            vertices[0]=v1;
        }else if(vertices[0]==v1){
            vertices[1]=v1;
        }else if(vertices[2]==v1){
            vertices[2]=v1;
        }else{
            System.out.println("ERROR: vertex not on face");
        }
        
    }

    public Edge[] getOtherEdges(Edge e) {
        /**
        StdDraw.setPenColor(0,255,0);
        Structure.draw(edges[0]);
        Structure.draw(edges[1]);
        Structure.draw(edges[2]);
        **/
        
        
        
        Edge[] toReturn= new Edge[2];
        if(e==edges[0]){
            toReturn[0]=edges[1];
            toReturn[1]=edges[2];
        }else if(e==edges[1]){
            toReturn[0]=edges[0];
            toReturn[1]=edges[2];
        }else if(e==edges[2]){
            toReturn[0]=edges[0];
            toReturn[1]=edges[1];
        }else{
            System.out.println("ERROR: Edge not on face");

            int i=5/0;
        }
        return toReturn;
    }

    public Edge[] getAdjacentEdges(Vertex v) {
        Edge[] toReturn=new Edge[2];
        if(vertices[0]==v){
            toReturn[0]=edges[0];
            toReturn[1]=edges[2];
        }else if(vertices[1]==v){
            toReturn[0]=edges[1];
            toReturn[1]=edges[0];
        }else if(vertices[2]==v){
            toReturn[0]=edges[2];
            toReturn[1]=edges[1];
        }else{
            System.out.println("ERROR: vertex not on face");
        }
        return toReturn;
    }

    public void replaceEdgesIfPossible(Edge[] oldEdges, Edge newEdge) {
        for(int i=0;i<3;i++){
            if(edges[i]==oldEdges[0]){
                oldEdges[0]=newEdge;
            }
            if(edges[i]==oldEdges[1]){
                oldEdges[1]=newEdge;
            }
        }
        
    }
    
    
    
}
