import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


public class Net3 {
    static double umin = 0;
    static double umax = 2 * Math.PI;
    static double vmin = 0;
    static double vmax = 2 * Math.PI;
    static int totalPoints = 500;
    static final int candidates = 1000; // scales time linearly

    static double[][] points = new double[totalPoints][]; //x,y,z,u,v
    static LinkedList<int[]> triangles=new LinkedList<int[]>();
    //static int[][] triangles;
    static int numPoints = 1;

    public static void main(String[] args) {
        
        /**
        Edge2 e0=new Edge2(5,4);
        Edge2 e1=new Edge2(4,5);
        ArrayList<Edge2> l0=new ArrayList<Edge2>();
        l0.add(e0);
        System.out.println(l0.contains(e1));
        
        
        double[] temp0={0,0,0,0,0};
        double[] temp1={0,0,0,0,.3};
        double[] temp2={0,0,0,.2,0};
        points[0]=temp0;
        points[1]=temp1;
        points[2]=temp2;
        
        double[] temp3={0,0,0,-.01,.2};
        points[3]=temp3;
        
        totalPoints=4;
        
        Triangle t0=new Triangle(0,1,2);
        ArrayList<Triangle> l0=new ArrayList<Triangle>();
        l0.add(t0);
        Triangle.draw(l0);
        //System.out.println(t0.wind());
        System.out.println(t0.visible(2, 3));
        **/
        
        
        
        getPoints();
        StdDraw.setXscale(0,2*Math.PI);
        StdDraw.setYscale(0,2*Math.PI);
        StdDraw.setPenRadius(.01);
        
        StdDraw.setPenColor(255,0,0);
        StdDraw.setPenRadius(.003);
        
        naiveDelaunay();
        


        /**

        System.out.print("polyhedron( points=["); for(int i=0;i<totalPoints;i++){
            System.out.print("["+points[i][0]+", "+points[i][1]+", "+points[i][2]+"], ");
            if(i!=totalPoints-1){ System.out.println(", "); 
            } 
        }
        System.out.println("], \nfaces=[");
        for(int i=0;i<triangles.size();i++){
            System.out.print("["+triangles.get(i)[0]+","+triangles.get(i)[1]+","+triangles.get(i)[2]+"]");
            if(i!=triangles.size()-1){ 
                System.out.println(", "); 
            }
        }
        System.out.println("]);");
        **/

    }
    
    
    static void naiveDelaunay(){
        ArrayList<Triangle> goodTriangles=new ArrayList<Triangle>();
        for(int i=0;i<totalPoints-2;i++){
            for(int j=i+1;j<totalPoints-1;j++){
                for(int k=j+1;k<totalPoints;k++){
                    
                    Triangle t0=new Triangle(i,j,k);
                    boolean good=true;
                    for(int l=0;l<totalPoints;l++){
                        if(l!=i && l!=j && l!=k && t0.inCircumcircle(l)){
                            good=false;
                            break;
                        }
                    }
                    if(good){
                        goodTriangles.add(t0);
                    }
                    
                }
            }
        }
        StdDraw.show(0);
        Triangle.draw(goodTriangles);
        StdDraw.show();
        
        System.out.print("polyhedron( points=["); for(int i=0;i<totalPoints;i++){
            System.out.print("["+points[i][0]+", "+points[i][1]+", "+points[i][2]+"], ");
            if(i!=totalPoints-1){ System.out.println(", "); 
            } 
        }
        System.out.println("], \nfaces=[");
        for(int i=0;i<goodTriangles.size();i++){
            System.out.print("["+goodTriangles.get(i).a+","+goodTriangles.get(i).b+","+goodTriangles.get(i).c+"]");
            if(i!=goodTriangles.size()-1){ 
                System.out.println(", "); 
            }
        }
        System.out.println("]);");
        
    }
    
    
    /* Methods concerned with turning points into a net */
    
    static void connect(){
        //ArrayList<Integer> addedPoints=new ArrayList<Integer>();
        ArrayList<Triangle> allTriangles =new ArrayList<Triangle>();
        ArrayList<ArrayList<Triangle>> trianglesByPoint=new ArrayList<ArrayList<Triangle>>();
        //maybe should be an array of lists
        
        Triangle t0=new Triangle(0,1,2);
        allTriangles.add(t0);
        ArrayList<Triangle> l0=new ArrayList<Triangle>();
        l0.add(t0);
        trianglesByPoint.add(l0);
        ArrayList<Triangle> l1=new ArrayList<Triangle>();
        l1.add(t0);
        trianglesByPoint.add(l1);
        ArrayList<Triangle> l2=new ArrayList<Triangle>();
        l2.add(t0);
        trianglesByPoint.add(l2);
        
        
        for(int i=3;i<totalPoints;i++){
            Triangle.draw(allTriangles);
            System.out.println("Iteration: "+i);
            ArrayList<Triangle> thisPointTriangles=new ArrayList<Triangle>();
            trianglesByPoint.add(thisPointTriangles);
            
            int containingTriangle=-1;
            for(int j=0;j<allTriangles.size();j++){
                if(allTriangles.get(j).contains(i)){
                    containingTriangle=j;
                    break;
                }
            }
            
            ArrayList<Edge2> iffyEdge2s=new ArrayList<Edge2>();
            
            if(containingTriangle==-1){
                //on outside
                System.out.println("Outside");
                ArrayList<Integer> pointsToConnect=new ArrayList<Integer>();
                for(int j=0;j<i;j++){
                    //for each point j
                    
                    //one of the rare times I want assignment by reference
                    ArrayList<Triangle> temp=trianglesByPoint.get(j);
                    System.out.println("j: "+j+"   temp: "+temp.size());
                    
                    
                    boolean good=true;
                    for(int k=0;k<temp.size();k++){
                        // each triangle k (with vertex j)
                        if(!temp.get(k).visible(j, i)){
                            System.out.println("Bad "+k);
                            good=false;
                            break;
                        }else{
                            System.out.println(temp.get(k).a+"  "+temp.get(k).b+"  "+temp.get(k).c );
                            System.out.println(j+"   "+i);
                            System.out.println("Good"+k);
                        }
                    }
                    if(good){
                        pointsToConnect.add(j);
                        System.out.println("ALL good "+j);
                    }
                }
                System.out.println("Points to connect: "+pointsToConnect.size());
                
                if(pointsToConnect.size()<2){
                    System.out.println("ERROR: External point can't see net");
                    // when adding a point not inside an existing triangle,
                    //it should be able to see at least two points with a
                    //connecting edge (to form a triangle with)
                }
                for(int j=0;j<pointsToConnect.size()-1;j++){
                    System.out.println("Point "+j);
                    System.out.println("size A: "+trianglesByPoint.get(j).size());
                    ArrayList<Triangle> temp=(ArrayList<Triangle>) (trianglesByPoint.get(j)).clone();
                    System.out.println("temp size: "+temp.size());
                    for(int k=j+1;k<pointsToConnect.size();k++){
                        //every pointsToConnect pairing
                        
                        
                        for(int l=0;l<temp.size();l++){
                            //every triangle on j
                            
                            //System.out.println("k: "+k+"   l: "+l);
                            if(temp.get(l).hasVertex(k)){
                                
                                //ADD A NEW TRIANGLE!
                                System.out.println("Adding external triangle "+k+" "+l);
                                //iffyEdge2s.add(new Edge2(i,j));
                                iffyEdge2s.add(new Edge2(j,k));
                                //iffyEdge2s.add(new Edge2(k,i));
                                Triangle newT=new Triangle(i,j,k);
                                allTriangles.add(newT);
                                //thisPointTriangles.add(newT);
                                trianglesByPoint.get(i).add(newT);
                                trianglesByPoint.get(j).add(newT);
                                trianglesByPoint.get(k).add(newT);
                            }
                        }
                    }
                    
                    deleteDuplicateTriangles(trianglesByPoint.get(j));
                }
                deleteDuplicateTriangles(allTriangles);
                
                
                
                
                
                
            }else{
                //in triangle # containingTriangle
                System.out.println("Inside");
                Triangle outer=allTriangles.get(containingTriangle);
                iffyEdge2s.add(new Edge2(outer.a, outer.b));
                iffyEdge2s.add(new Edge2(outer.b, outer.c));
                iffyEdge2s.add(new Edge2(outer.c, outer.a));
                iffyEdge2s.add(new Edge2(outer.a, i));
                iffyEdge2s.add(new Edge2(outer.b, i));
                iffyEdge2s.add(new Edge2(outer.c, i));
                
                Triangle toRemove=
                        allTriangles.remove(containingTriangle);
                trianglesByPoint.get(outer.a).remove(toRemove);
                trianglesByPoint.get(outer.b).remove(toRemove);
                trianglesByPoint.get(outer.c).remove(toRemove);
                
                Triangle t3=new Triangle(outer.a, outer.b, i);
                allTriangles.add(t3);
                trianglesByPoint.get(outer.a).add(t3);
                trianglesByPoint.get(outer.b).add(t3);
                trianglesByPoint.get(i).add(t3);
                

                Triangle t4=new Triangle(outer.b, outer.c, i);
                allTriangles.add(t4);
                trianglesByPoint.get(outer.a).add(t4);
                trianglesByPoint.get(outer.b).add(t4);
                trianglesByPoint.get(i).add(t4);
                

                Triangle t5=new Triangle(outer.a, outer.c, i);
                allTriangles.add(t5);
                trianglesByPoint.get(outer.a).add(t5);
                trianglesByPoint.get(outer.c).add(t5);
                trianglesByPoint.get(i).add(t5);
                
            }
            
            
            
            
            deleteDuplicateEdge2s(iffyEdge2s);
            System.out.println("iffyEdge2s: "+iffyEdge2s.size());
            
            while(!iffyEdge2s.isEmpty()){
                
                Edge2 e=iffyEdge2s.remove(0);
                Triangle[] adjacent=e.getAdjacent(allTriangles);
                if(adjacent.length!=0){
                    //would be 0 if this edge is on the outside
                    Triangle.maybeSwap(adjacent, e, allTriangles, iffyEdge2s);
                }
                
                deleteDuplicateEdge2s(iffyEdge2s);
                
            }
            System.out.println("iffyEdge2s after: "+iffyEdge2s.size());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    /* Methods concerned with distributing points */
    
    static double[][] getPoints() {
        // double[][] points=new double[totalPoints][];
        // System.out.println("Debug 1");
        points[0] = rand();
        numPoints = 1;

        for (int i = 1; i < totalPoints; i++) {
            points[i] = getPoint();
            numPoints++;
        }
        return points;

    }

    static double[] equation(double u, double v) {
        double[] temp = new double[3];
        double radius = 5;
        temp[0] = round(
                radius * Math.cos(u) * Math.cos(v) - 3 * radius * Math.cos(v),
                3);
        temp[1] = round(radius * Math.sin(u), 3);
        temp[2] = round(
                radius * Math.sin(v) * Math.cos(u) - 3 * radius * Math.sin(v),
                3);
        
        /**
        temp[0] = round(
                radius * Math.cos(u) * Math.cos(v/2),
                3);
        temp[1] = round(radius * Math.sin(u), 3);
        temp[2] = round(
                radius * Math.sin(v/2) * Math.cos(u),
                3);
                **/
        
        return temp;
    }

    //returns x,y,z,u,v
    static double[] rand() {
        double u=umin + (umax - umin) * Math.random();
        double v= vmin + (vmax - vmin) * Math.random();
        
        double[] toReturn={0,0,0,u,v};
        double[] temp=equation(u,v);
        
        toReturn[0]=temp[0];
        toReturn[1]=temp[1];
        toReturn[2]=temp[2];
        return toReturn;
    }

    static double round(double n, double precision) {
        return Math.round(Math.pow(10, precision) * n)
                / Math.pow(10, precision);
    }

    static double[] getPoint() {
        double max = Double.NEGATIVE_INFINITY;
        double[] toReturn = new double[3];
        for (int i = 0; i < candidates; i++) {
            double[] candidate = rand();
            double dist2=dist(points, numPoints, candidate);
            if (dist2 > max) {
                toReturn = candidate;
                max=dist2;
                
            }
        }
        return toReturn;
    }

    // returns square of distance to nearest point
    static double dist(double[][] points, int numPoints, double[] candidate) {
        double min = Double.POSITIVE_INFINITY;
        double dist2;
        for (int i = 0; i < numPoints; i++) {
            dist2 = getDist2(points[i], candidate);
            if (dist2 < min) {
                min = dist2;
            }
        }

        return min;
    }
    

   
    
    
    
    
    
    /* General utility methods */
    
    static double getDist2(double[] a, double[] b){
        double dx=a[0]-b[0];
        double dy=a[1]-b[1];
        double dz=a[2]-b[2];
        return dx*dx + dy*dy + dz*dz;
    }
    
    
    static int[] triple(int a, int b, int c){
        int[] temp={a,b,c};
        return temp;
    }
    
    static int[] pair(int a, int b){
        int[] temp={a,b};
        return temp;
    }
    
    static void deleteDuplicateEdge2s(ArrayList<Edge2> a){
        ArrayList<Edge2> b=new ArrayList<Edge2>();
        int deleted=0;
        for(Edge2 e: a){
            if(!b.contains(e)){
                b.add(e);
            }else{
                deleted++;
            }
        }
        //System.out.println("Deleted= "+deleted);
        a=b;
    }
    
    
    static void deleteDuplicateTriangles(ArrayList<Triangle> a){
        ArrayList<Triangle> b=new ArrayList<Triangle>();
        int deleted=0;
        for(Triangle e: a){
            if(!b.contains(e)){
                b.add(e);
            }else{
                deleted++;
            }
        }
        System.out.println("Deleted= "+deleted);
        a=b;
    }
}
