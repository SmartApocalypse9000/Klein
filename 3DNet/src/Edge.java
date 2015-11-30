
public class Edge {
    Vertex[] vertices; //always 2
    Face[] faces=new Face[2]; //always 2
    
    public Edge(Vertex a, Vertex b){
        vertices=new Vertex[2];
        vertices[0]=a;
        vertices[1]=b;
    }
    
    
    public Vertex[] getVertices() {
        return vertices;
    }
    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }
    public Face[] getFaces() {
        return faces;
    }
    public void setFaces(Face[] faces) {
        this.faces = faces;
    }
    
    public void replaceVertex(Vertex v0, Vertex v1){
        //Structure.draw(vertices[0]);
        //Structure.draw(vertices[1]);
        //Structure.draw(v0);
        
        if(vertices[0]==v0){
            vertices[0]=v1;
        }else if(vertices[1]==v0){
            vertices[1]=v1;
        }else{

            System.out.println("ERROR: vertex not on edge");
        }
        
    }
    
    public void addFace(Face f){
        /**
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        **/
        if(faces[0]==null){
            //StdDraw.setPenColor(255,0,0);
            //Structure.draw(this);
            faces[0]=f;
            //System.out.println("Adding first");
            
        }else if(faces[1]==null){
            //StdDraw.setPenColor(0,255,0);
            //Structure.draw(this);
            faces[1]=f;
            //System.out.println("Adding second");
            
        }else{
            System.out.println("ERROR: this edge already has 2 faces");
            //StdDraw.setPenColor(0,0,255);
            //Structure.draw(this);
           try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
    }
    
    
    
}
