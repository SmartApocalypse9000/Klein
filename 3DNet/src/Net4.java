import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


public class Net4 {
    static double umin = 0;
    static double umax = 2 * Math.PI;
    static double vmin = 0;
    static double vmax = 2 * Math.PI;
    static int totalPoints = 100;
    static final int candidates = 1000; // scales time linearly

    static double[][] points = new double[totalPoints][]; //x,y,z,u,v
    static LinkedList<int[]> triangles=new LinkedList<int[]>();
    //static int[][] triangles;
    static int numPoints = 1;

    public static ArrayList<double[]> wrappedPoints=new ArrayList<double[]>();
    public static void main(String[] args) {
        
        
        
        getPoints();
        StdDraw.setXscale(0,2*Math.PI+2);
        StdDraw.setYscale(0,2*Math.PI+2);
        StdDraw.setPenRadius(.01);
        
        StdDraw.setPenColor(255,0,0);
        StdDraw.setPenRadius(.003);
        
        naiveDelaunay();
        



    }
    
    
    static void naiveDelaunay(){
       // ArrayList<double[]> wrappedPoints=new ArrayList<double[]>();
        
        double cutoff=0.5;
        double width=umax-umin;
        double height=vmax-vmin;
        //double xCutoff=umin+cutoff*(umax-umin);
        //double yCutoff=vmin+cutoff*(vmax-vmin);
        for(int i=0;i<totalPoints;i++){
            double x=points[i][3];
            double y=points[i][4];
            wrappedPoints.add(pair(x,y));
            if(x<umin+width*cutoff){
                wrappedPoints.add(triple(x+width,y,i));
            }
            if(y<vmin+height*cutoff){
                wrappedPoints.add(pair(x,y+height));
            }
            if(x>umax-width*cutoff){
                wrappedPoints.add(triple(x-width,y,i));
            }
            if(y>vmax-height*cutoff){
                wrappedPoints.add(pair(x,y+height));
            }
            
            if(x<umin+width*cutoff && y<vmin+height*cutoff){
                wrappedPoints.add(triple(x+width,y-height,i));
            }
            if(x<umin+width*cutoff && y>vmax-height*cutoff){
                wrappedPoints.add(triple(x+width,y-height,i));
            }
            if(x<umin+width*cutoff && y<vmin+height*cutoff){
                wrappedPoints.add(triple(x+width,y-height,i));
            }
            if(x<umin+width*cutoff && y<vmin+height*cutoff){
                wrappedPoints.add(triple(x+width,y-height,i));
            }

            
        }
        
        int totalWrappedPoints=wrappedPoints.size();
        System.out.println(totalWrappedPoints);
        ArrayList<Triangle2> goodTriangles=new ArrayList<Triangle2>();
        for(int i=0;i<totalWrappedPoints-2;i++){
            for(int j=i+1;j<totalWrappedPoints-1;j++){
                for(int k=j+1;k<totalWrappedPoints;k++){
                    
                    Triangle2 t0=new Triangle2(i,j,k);
                    boolean good=true;
                    for(int l=0;l<totalWrappedPoints;l++){
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
        Triangle2.draw(goodTriangles);
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
    
    private static double[] triple(double a, double b, int c) {
        double[] temp={a,b,c};
        return temp;
    }
    
    static int[] pair(int a, int b){
        int[] temp={a,b};
        return temp;
    }
    
    static double[] pair(double a, double b){
        double[] temp={a,b};
        return temp;
    }
    
    static void deleteDuplicateEdge2s(ArrayList<Edge2> a){
        ArrayList<Edge2> b=new ArrayList<Edge2>();
        //int deleted=0;
        for(Edge2 e: a){
            if(!b.contains(e)){
                b.add(e);
            }else{
                //deleted++;
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
