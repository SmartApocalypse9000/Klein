import java.util.ArrayList;


public class Structure {
    static ArrayList<Vertex> vertices=new ArrayList<Vertex>();
    static ArrayList<Edge> edges=new ArrayList<Edge>();
    static ArrayList<Face> faces=new ArrayList<Face>();
    
    
    public static void main(String[] args){
        generate();
        //deleteEdge(edges.get(35));
        print();
        //draw(edges);
        
    }


    private static void generate() {
        double umin = 0;
        double umax = 2*Math.PI;
        int upoints=40;
        
        double vmin = 0;
        double vmax = 1*Math.PI;
        int vpoints=40;

        for(double v=0;v<=vmax;v+=(vmax-vmin)/(vpoints-1)){
            for(double u=0;u<=umax;u+=(umax-umin)/(upoints-1)){
                //System.out.println(u+"   "+v);
                vertices.add(new Vertex(klein2(u,v), pair(u,v)));
                //System.out.println(vertices.get(vertices.size()-1));
            }
        }
        
        //int totalPoints=upoints*vpoints;
        for(int x=0;x<upoints;x++){
            for(int y=0;y<vpoints;y++){
                //int vertexNum=y*upoints+x;
                Vertex v0=vertices.get(y*upoints + x);
                Vertex vh=vertices.get(y*upoints + (x+1)%upoints);
                Vertex vv=vertices.get(((y+1)%vpoints)*upoints + x);
                Vertex vd=vertices.get(((y+1)%vpoints)*upoints + (x+1)%upoints);
                
                
                
                
                Edge eh=new Edge(v0, vh);
                Edge ev=new Edge(v0, vv);
                Edge ed=new Edge(v0, vd);
                
                v0.addEdge(eh);
                v0.addEdge(ev);
                v0.addEdge(ed);
                
                vh.addEdge(eh);
                vv.addEdge(ev);
                vd.addEdge(ed);
                
                
                
                //FACES
                Face f0=new Face(v0, vd, vh);
                Face f1=new Face(v0, vv, vd);

                edges.add(eh);
                edges.add(ev);
                edges.add(ed);
                
                faces.add(f0);
                faces.add(f1);
                
                
            }
        }
        
        
        for(int y=0;y<upoints;y++){
            for(int x=0;x<vpoints;x++){
                // now that all the edges are created, add them to faces
                Edge eb=edges.get((y*upoints + x)*3);
                Edge el=edges.get((y*upoints + x)*3+1);
                Edge ed=edges.get((y*upoints + x)*3+2);
                Edge et=edges.get((y*upoints + (x+1)%upoints)*3);
                Edge er=edges.get((((y+1)%vpoints)*upoints + x)*3+1);
                //System.out.println((y*upoints + x)+"   "+(((y+1)%vpoints)*upoints + x));
                
                Edge[] t0={ed, er, eb};
                Edge[] t1={el, et, ed};
                
                Face f0=faces.get((y*upoints+x)*2);
                Face f1=faces.get((y*upoints+x)*2+1);
                
                //draw(f0);
                //draw(f1);
                
                f0.setEdges(t0);
                f1.setEdges(t1);
                
                eb.addFace(f0);
                el.addFace(f1);
                ed.addFace(f0);
                ed.addFace(f1);
                er.addFace(f0);
                et.addFace(f1);
                
                
            }
        }
        
        
        //draw(edges);
        //print();
        //System.out.println("DONE");
        
    }
    
    
    


    static void deleteEdge(Edge e){
        StdDraw.setPenColor(0,0,255);
        draw(e);
        try {
            Thread.sleep(0);
        } catch (InterruptedException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        
        
        Vertex oldV0=e.getVertices()[0];
        //System.out.println(vertices.size());
        //System.out.println("Size: "+oldV0.getEdges().size());
        Vertex oldV1=e.getVertices()[1];
        
        double u=(oldV0.getuvCoordinates()[0] + oldV1.getuvCoordinates()[0])/2;
        double v=(oldV0.getuvCoordinates()[1] + oldV1.getuvCoordinates()[1])/2;
        Vertex newVertex= new Vertex(klein2(u, v), pair(u,v));
        
        Face f0=e.getFaces()[0];
        Face f1=e.getFaces()[1];
        
        Vertex farVertex0= f0.getOppositeVertex(e);
        Vertex farVertex1= f1.getOppositeVertex(e);
        
        Edge[] oldEdges0=f0.getAdjacentEdges(farVertex0);
        Edge[] oldEdges1=f1.getAdjacentEdges(farVertex1);
        
        Edge newEdge0=new Edge(newVertex, farVertex0);
        Edge newEdge1=new Edge(newVertex, farVertex1);
        
        farVertex0.replace2Edges(f0.getOtherEdges(e), newEdge0);
        farVertex1.replace2Edges(f1.getOtherEdges(e), newEdge1);
        
        for(Face f: oldV0.getFaces()){
            f.replaceVertex(oldV0, newVertex);
            f.replaceEdgesIfPossible(oldEdges0, newEdge0);
            f.replaceEdgesIfPossible(oldEdges1, newEdge1);
        }
        for(Face f: oldV1.getFaces()){
            f.replaceVertex(oldV1, newVertex);
            f.replaceEdgesIfPossible(oldEdges0, newEdge0);
            f.replaceEdgesIfPossible(oldEdges1, newEdge1);
        }
        //System.out.println("Size: "+oldV0.getEdges().size());
        for(Edge e1: oldV0.getEdges()){
            //StdDraw.setPenColor(255,255,0);
            //draw(e1);
            e1.replaceVertex(oldV0, newVertex);
        }
        for(Edge e1: oldV1.getEdges()){
            e1.replaceVertex(oldV1, newVertex);
        }
        
        
        
        
        edges.remove(e);
        vertices.remove(oldV0);
        vertices.remove(oldV1);
        vertices.add(newVertex);
    }
    
    
    public static void draw(Edge e) {
        StdDraw.setXscale(0,2*Math.PI+2);
        StdDraw.setYscale(0,2*Math.PI+2);
        StdDraw.line(e.getVertices()[0].getuvCoordinates()[0],
                e.getVertices()[0].getuvCoordinates()[1],
                e.getVertices()[1].getuvCoordinates()[0],
                e.getVertices()[1].getuvCoordinates()[1]);
    }
    
    private static void draw(Face f) {
        StdDraw.setXscale(0,2*Math.PI+2);
        StdDraw.setYscale(0,2*Math.PI+2);
        double[] xData={f.getVertices()[0].getuvCoordinates()[0],
                f.getVertices()[1].getuvCoordinates()[0],
                f.getVertices()[2].getuvCoordinates()[0]};
        double[] yData={f.getVertices()[0].getuvCoordinates()[1],
                f.getVertices()[1].getuvCoordinates()[1],
                f.getVertices()[2].getuvCoordinates()[1]};
        
        
        StdDraw.setPenColor(100,100,100);
        StdDraw.filledPolygon(xData, yData);
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        
        
    }
    

    public static void draw(Vertex vertex) {
        StdDraw.setPenColor(128,128,128);
        StdDraw.circle(vertex.getuvCoordinates()[0],vertex.getuvCoordinates()[0],.1);
        
    }
    
    private static void draw(ArrayList<Edge> edges2) {
        
        StdDraw.setXscale(0,2*Math.PI+2);
        StdDraw.setYscale(0,2*Math.PI+2);
        
        for(Edge e:edges2){
            StdDraw.line(e.getVertices()[0].getuvCoordinates()[0],
                    e.getVertices()[0].getuvCoordinates()[1],
                    e.getVertices()[1].getuvCoordinates()[0],
                    e.getVertices()[1].getuvCoordinates()[1]);
            try {
                Thread.sleep(0);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        
    }


    private static double[] sphere(double u, double v){
        double[] toReturn=new double[3];
        double radius=5;
        toReturn[0]=round(radius * Math.cos(u) * Math.sin(v/2), 3);
        toReturn[1] = round(radius * Math.sin(u) * Math.sin(v/2), 3);
        toReturn[2] = round(radius * Math.cos(v/2 - Math.PI),3);
                
        
        
        return toReturn;
    }
    
    
    private static double[] klein(double u, double v){
        v*=4;
        double[] toReturn=new double[3];
        if(v<=2*Math.PI){
            toReturn[0] = round(Math.cos(u)*(Math.cos(v/2)/4+.75)-Math.cos(v/2)+1, 3);
            toReturn[1] = round(Math.sin(u)*(Math.cos(v/2)/4+.75), 3);
            toReturn[2] = round(v/Math.PI, 3);
        }else if(v<=4*Math.PI){
            toReturn[0] = round(.5*Math.cos(u)*Math.cos((v-Math.PI*2)/2)+Math.cos((v-Math.PI*2)/2)+1, 3);
            toReturn[1] = round(.5*Math.sin(u), 3);
            toReturn[2] = round(2+Math.sin((v-Math.PI*2)/2) + Math.cos(u)*.5*Math.sin((v-Math.PI*2)/2), 3);
        }else if(v<=6*Math.PI){
            toReturn[0] = round(-.5*Math.cos(u), 3);
            toReturn[1] = round(.5*Math.sin(u), 3);
            toReturn[2] = round(2-(v-4*Math.PI)/Math.PI, 3);
        }else{
            toReturn[0] = round(0, 3);
            toReturn[1] = round(0, 3);
            toReturn[2] = round(0, 3);
        }
        
        
        
                
        
        
        return toReturn;
    }
    
    
    
    private static double[] klein2(double u, double v){
        double x=0;
        double y=0;
        double z=0;
        
        v*=4;
        
        if(v<=2*Math.PI){
            v%=Math.PI*2;
            
            x= (1+v/Math.PI/2)*Math.cos(u)*Math.cos(v/2) + 2*Math.cos(v/2);
            y= 4 + (1+v/Math.PI/2)*Math.cos(u)*Math.sin(v/2) + 2*Math.sin(v);
            z= (1+v/Math.PI/2)*Math.sin(u);
            
        }else if(v<=4*Math.PI){
            v%=Math.PI*2;
            double v2=Math.sin(v/4)*2*Math.PI;
            
            x= (2+v/Math.PI/2)*Math.cos(u) + Math.cos(v2/2)-1;
            y= -2*v2/Math.PI+4;
            z= (1+v/Math.PI/2)*Math.sin(u);
            
        }else if(v<=6*Math.PI){
            v%=Math.PI*2;
            
            
            
        }else{
            v%=Math.PI*2;
            
            
            
        }
        
        return triple(x,y,z);
        
    }
    
    private static double[] triple(double x, double y, double z) {
        double[] toReturn={x,y,z};
        return toReturn;
    }


    private static void print(){
        int totalPoints=vertices.size();
        System.out.print("polyhedron( points=[");
        for(int i=0;i<totalPoints;i++){
            System.out.print("["+vertices.get(i).getCoordinates()[0]+", "+vertices.get(i).getCoordinates()[1]+", "+vertices.get(i).getCoordinates()[2]+"]");
            if(i!=totalPoints-1){ System.out.println(", "); 
            } 
            
        }
        
        
        
        System.out.println("], \nfaces=[");
        for(int i=0;i<faces.size();i++){
            System.out.print("["+vertices.indexOf(faces.get(i).getVertices()[0])+","+
                    vertices.indexOf(faces.get(i).getVertices()[1])+","+
                    vertices.indexOf(faces.get(i).getVertices()[2])+"]");
            if(i!=faces.size()-1){ 
                System.out.println(", "); 
            }
        }
        System.out.println("]);");
    
    }
    
    
    
    
    
    
    
    
    
    
    private static double getDist2(double[] a, double[] b){
        double dx=a[0]-b[0];
        double dy=a[1]-b[1];
        double dz=a[2]-b[2];
        return dx*dx + dy*dy + dz*dz;
    }
    
    
    private static int[] triple(int a, int b, int c){
        int[] temp={a,b,c};
        return temp;
    }
    
    private static int[] pair(int a, int b){
        int[] temp={a,b};
        return temp;
    }
    
    static double[] pair(double a, double b){
        double[] temp={a,b};
        return temp;
    }
    
    
    static double round(double n, double precision) {
        return Math.round(Math.pow(10, precision) * n)
                / Math.pow(10, precision);
    }


    
    
    
}
