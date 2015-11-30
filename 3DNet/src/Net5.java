import java.util.ArrayList;


public class Net5 {
    static double umin = 0;
    static double umax = 2 * Math.PI;
    static double vmin = 0;
    static double vmax = 2 * Math.PI;
    static int totalPoints = 200;
    static final int candidates = 1000; // scales time linearly

    static double[][] points = new double[totalPoints][]; //x,y,z,u,v
    public static ArrayList<int[]> triangles=new ArrayList<int[]>();
    static ArrayList<T3> t3s=new ArrayList<T3>();
    static int numPoints = 1;
    
    public static void main(String[] args){
        
        
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
        wind();
        
        System.out.print("polyhedron( points=["); for(int i=0;i<totalPoints;i++){
            System.out.print("["+points[i][0]+", "+points[i][1]+", "+points[i][2]+"], ");
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
    
    
    static void wind() {
        // The arrays of ints should always be passed by reference, I'm just editing te old ones
        
        ArrayList<int[]> unwound=new ArrayList<int[]>();
        ArrayList<int[]> wound= new ArrayList<int[]>();
        
        
        for(int[] e: triangles){
            unwound.add(e);
            //System.out.println(e[0]+"   "+e[1]+"   "+e[2]);
        }
        //unwound=(ArrayList<int[]>) triangles.clone();
        wound.add(unwound.remove(0)); //assumes 0th is wound
        
        
        while(unwound.size()!=0){
            //System.out.println();
            int[] temp=unwound.remove(0);
            if(!windAttempt(wound, temp)){
                unwound.add(temp); // at the end
            }else{
                wound.add(temp);
            }
        }
        
        triangles=wound;
    }

    // winds and returns true if possible
    // returning false means it can't determine
    static boolean windAttempt(ArrayList<int[]> wound, int[] bad) {
        //System.out.println(bad[0]+"   "+bad[1]+"   "+bad[2]);
        for(int[] good: wound){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    //System.out.println(good[i]+"   "+bad[j]);
                    if(good[i]==bad[j] && good[(i+1)%3]==bad[(j+1)%3]){
                        //unwound
                        int temp=bad[0];
                        bad[0]=bad[1];
                        bad[1]=temp;
                        return true;
                    }else if(good[i]==bad[(j+1)%3] && good[(i+1)%3]==bad[j]){
                        //already wound
                        return true;
                    }else{
                        //System.out.println("unknown");
                    }
                }
            }
        }
        return false;
    }


    static void connect(){
        for(int i=0;i<numPoints-2;i++){
            for(int j=i+1;j<numPoints-1;j++){
                for(int k=j+1;k<numPoints;k++){
                    
                    T3 triangle =new T3(points[i],points[j],points[k]);
                    double[] sphere=triangle.getCircumsphere();
                    
                    boolean good=true;
                    for(int l=0;l<numPoints;l++){
                        if(l!=i && l!=j && l!=k && T3.inCircumsphere(sphere, points[l])){
                            good=false;
                            break;
                        }
                    }
                    if(good){
                        if(sphere[3]<10){
                            //really not sure why this is here
                            triangles.add(triple(i,j,k));
                            //System.out.println("Should be adding");
                        }else{
                            //System.out.println(sphere[0]+" "+sphere[1]+" "+sphere[2]+" "+sphere[3]);
                            //System.out.println(i+"  "+j+"   "+k);
                        }
                        //System.out.println(sphere[3]);
                        
                    }
                    
                }
            }
        }
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
}
