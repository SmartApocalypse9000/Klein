import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


public class Net6 {
    static double umin = 0;
    static double umax = 2 * Math.PI;
    static double vmin = 0;
    static double vmax = 2 * Math.PI;
    static int totalPoints = 200;
    static final int candidates = 100; // scales time linearly

    static double[][] points = new double[totalPoints][]; //x,y,z,u,v
    public static ArrayList<int[]> triangles=new ArrayList<int[]>();
    //static ArrayList<T3> t3s=new ArrayList<T3>();
    static int numPoints = 1;
    
    public static void main(String[] args){
        
        StdDraw.setXscale(0,2*Math.PI+2);
        StdDraw.setYscale(0,2*Math.PI+2);
        
        /**
        double[] a={.01,.02,.03};
        double[] b={.02,.01,1};
        double[] c={0,.003,2};
        T3 test=new T3(a, b, c);
        double[] sphere=test.getCircumsphere();
        System.out.println(sphere[0]+"   "+sphere[1]+"   "+sphere[2]+"   "+sphere[3]);
        
        double[] point={.5,.5,.5};
        System.out.println(T3.inCircumsphere(sphere, point));
        
        System.exit(0);
        **/
        
        
        
        getPoints();
        connect();
        
        System.out.print("polyhedron( points=["); for(int i=0;i<totalPoints;i++){
            System.out.print("["+points[i][0]+", "+points[i][1]+", "+points[i][2]+"]");
            if(i!=totalPoints-1){ System.out.println(", "); 
            } 
            
        }
        //
        System.out.println("], \nfaces=[");
        for(int i=0;i<triangles.size();i++){
            System.out.print("["+triangles.get(i)[0]+","+triangles.get(i)[1]+","+triangles.get(i)[2]+"]");
            if(i!=triangles.size()-1){ 
                System.out.println(", "); 
            }
        }
        System.out.println("]);");
    }
    
    static void connect(){
        //System.out.println("Connecting");
        ArrayList<int[]> singleEdges=new ArrayList<int[]>();
        ArrayList<int[]> doubleEdges=new ArrayList<int[]>();

        double bestL=Double.POSITIVE_INFINITY;
        int[] bestP=new int[3];
        for(int i=0;i<numPoints-2;i++){
            for(int j=i+1;j<numPoints-1;j++){
                for(int k=j+1;k<numPoints;k++){
                    double dist=getDist2(points[i], points[j]) + getDist2(points[i], points[k]) + getDist2(points[j], points[k]);
                    if(dist<bestL){
                        bestL=dist;
                        bestP=triple(i,j,k);
                    }
                }
            }
        }
        
        triangles.add(bestP);
        singleEdges.add(triple(bestP[0],bestP[1],bestP[2]));
        singleEdges.add(triple(bestP[0],bestP[2],bestP[1]));
        singleEdges.add(triple(bestP[1],bestP[2],bestP[0]));
        
        for(int[] edge:singleEdges){
            StdDraw.line(points[edge[0]][3], points[edge[0]][4], points[edge[1]][3], points[edge[1]][4]);
        }
        
        
        
        while(!singleEdges.isEmpty()){
            //System.out.println("SingleEdges: "+singleEdges.size());
            
            int[] current=singleEdges.remove(0);
            ArrayList<Integer> bestIndices=new ArrayList<Integer>();
            ArrayList<Double> bestDists=new ArrayList<Double>();
            for(int i=0;i<10;i++){
               bestIndices.add(0);
               bestDists.add(Double.POSITIVE_INFINITY);
            }
            
            
            for(int i=0;i<numPoints;i++){
                //check triangle to point i
                if(i!= current[0] && i!= current[1] && i!=current[2]){
                    double dist=getDist2(points[current[0]],points[i]) + getDist2(points[current[1]],points[i]);
                    for(int j=0;j<bestDists.size();j++){
                        if(dist<bestDists.get(j)){
                            //System.out.println("found one "+dist);

                            bestDists.remove(bestDists.size()-1);
                            bestIndices.remove(bestIndices.size()-1);

                            bestDists.add(j, dist);
                            bestIndices.add(j,i);
                            break;
                        }
                    }
                }
            }
            //System.out.println("bestIndices: "+bestIndices.size());

            // the 10 best points have been found

            for(int i=0;i<bestIndices.size();i++){

                //System.out.println("Checking "+bestIndices.get(i));
                
                int[] new1=triple(current[0],bestIndices.get(i),current[1]);
                int[] new2=triple(current[1],bestIndices.get(i),current[0]);
                //System.out.println(getAngle(current, bestIndices.get(i)));
                double angle=getAngle(current, bestIndices.get(i));
                if(!containsPairs(doubleEdges, new1, new2) && !Double.isNaN(angle) && angle>1*Math.PI/3){
                    //TODO also check if the angle is too small
                    //System.out.println("GOT IT");
                    
                    
                    
                    //FOUND a good point
                    //System.out.println("Found a good point "+bestIndices.get(i));
                    doubleEdges.add(current);

                    StdDraw.line(points[current[0]][3], points[current[0]][4], points[bestIndices.get(i)][3], points[bestIndices.get(i)][4]);
                    StdDraw.line(points[current[1]][3], points[current[1]][4], points[bestIndices.get(i)][3], points[bestIndices.get(i)][4]);
                    
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    
                    if(containsPairandMove(singleEdges, doubleEdges, new1)){
                        doubleEdges.add(new1);
                    }else{
                        singleEdges.add(new1);
                    }
                    
                    if(containsPairandMove(singleEdges, doubleEdges, new2)){
                        doubleEdges.add(new2);
                    }else{
                        singleEdges.add(new2);
                    }
                    
                    
                    
                    triangles.add(triple(current[0],current[1],bestIndices.get(i)));
                    break;
                    
                }
            }



            
        }
    }
    
    
    /* GetPoints and related methods*/

    private static double getAngle(int[] current, int integer) {
        
        return Math.acos(normalDot(cross(points[current[2]],points[current[0]],points[current[1]]), cross(points[integer],points[current[0]],points[current[1]])));
    }

    private static boolean containsPairandMove(ArrayList<int[]> list, ArrayList<int[]> list2, int[] pair1) {
        //int[] pair1,
        //int[] pair2) {
            for(int i=0;i<list.size();i++){
                if((pair1[0]==list.get(i)[0] && pair1[1]==list.get(i)[1]) || (pair1[0]==list.get(i)[1] && pair1[1]==list.get(i)[0]) ){
                    list2.add(list.get(i));
                    list.remove(i);
                    return true;
                }
            }
            return false;
    }

    //returns true if the list contains either pair
    private static boolean containsPairs(ArrayList<int[]> list,
            int[] pair1, int[] pair2) {
        for(int[] pair3:list){
            if((pair1[0]==pair3[0] && pair1[1]==pair3[1]) || (pair1[0]==pair3[1] && pair1[1]==pair3[0]) ||
                    (pair2[0]==pair3[0] && pair2[1]==pair3[1]) || (pair2[0]==pair3[1] && pair2[1]==pair3[0])){
                return true;
            }
        }
        return false;
    }
    
    
    private static boolean containsPair(ArrayList<int[]> list,
            int[] pair1) {
        for(int[] pair3:list){
            if((pair1[0]==pair3[0] && pair1[1]==pair3[1]) || (pair1[0]==pair3[1] && pair1[1]==pair3[0])){
                return true;
            }
        }
        return false;
    }

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
    
    
    static double round(double n, double precision) {
        return Math.round(Math.pow(10, precision) * n)
                / Math.pow(10, precision);
    }
    
    
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
    
    static double[] cross(double o[], double a[], double b[]){
        //compute (a-o)x(b-o)
        //ax*by - ay*bx
        //positive if b is to the left of oa (looking from o)
        
        //((a[3]-o[3]) * (b[4]-o[4]))  -  ((b[3]-o[3]) * (a[4]-o[4]))
        //System.out.println(o+"   "+a+"   "+b+"   "+Net4.wrappedPoints.size());
        
        double ax=a[0]-o[0];
        double ay=a[1]-o[1];
        double az=a[2]-o[2];
        double bx=b[0]-o[0];
        double by=b[1]-o[1];
        double bz=b[2]-o[2];
        
        double[] toReturn=new double[3];
        toReturn[0]=ay*bz-az*by;
        toReturn[1]=az*bx-ax*bz;
        toReturn[2]=ax*by-ay*bx;
        return toReturn;
    }
    
    static double dot(double[] a, double[] b){
        return a[0]*b[0]+a[1]*b[1]+a[2]*b[2];
    }
    
    static double normalDot(double[] a, double[] b){
        //System.out.println("returning "+(dot(a,b)/Math.sqrt(dot(a,a)*dot(b,b))));
        return dot(a,b)/Math.sqrt(dot(a,a)*dot(b,b));
    }
}
