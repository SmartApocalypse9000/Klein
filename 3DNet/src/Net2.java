import java.util.Collections;
import java.util.LinkedList;

public class Net2 {
    static double umin = 0;
    static double umax = 2 * Math.PI;
    static double vmin = 0;
    static double vmax = 2 * Math.PI;
    static final int totalPoints = 200;
    static final int candidates = 1000; // scales time linearly

    static double[][] points = new double[totalPoints][];
    static LinkedList<int[]> triangles=new LinkedList<int[]>();
    //static int[][] triangles;
    static int numPoints = 1;

    public static void main(String[] args) {
        getPoints();
        connect();
        
        




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

    }

    static void connect() {
        double threshhold=80;
        LinkedList<int[]> candidateEdges=new LinkedList<int[]>();
        LinkedList<int[]> confirmedEdges=new LinkedList<int[]>();
        LinkedList<ABC> temp=new LinkedList<ABC>();
        
        //populate candidate edges
        for(int a=0;a<totalPoints-1;a++){
            for(int b=a+1;b<totalPoints;b++){
                double dist2=getDist2(points[a], points[b]);
                if(dist2<threshhold){
                    temp.add(new ABC(a,b,dist2));
                }
            }
        }
        Collections.sort(temp);
        for(int i=0;i<temp.size();i++){
            candidateEdges.add(pair(temp.get(i).a,temp.get(i).b));
            //System.out.println(temp.get(i).valueC);
        }
        //System.exit(0);
        System.out.println("//Candidate edges number: "+candidateEdges.size());
        temp.clear(); //just to release memory
        
        while(candidateEdges.size()!=0){
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             //System.out.println("//Candidate edges number: "+candidateEdges.size());
            //System.out.println(candidateEdges.size());
            
            int a=candidateEdges.get(0)[0];
            int b=candidateEdges.get(0)[1];
            candidateEdges.remove(0);
            
            LinkedList<Integer> newTriangles=new LinkedList<Integer>();
            
            LinkedList<Integer> touching1= touching2(a, confirmedEdges);
            for(int i=0;i<touching1.size();i++){
                LinkedList<Integer> touching2= touching2(touching1.get(i), confirmedEdges);
                for(int j=0;j<touching2.size();j++){
                    if(touching2.get(j)==b){
                        //found a triangle
                        newTriangles.add(touching1.get(i));
                    }
                }
            }
            
            if(newTriangles.size()>2){
                //Throw it out
                //System.out.println("Throw out C");
            }else if (newTriangles.size()==1){
                int index1=(contains3(newTriangles.get(0), a, confirmedEdges));
                int index2=(contains3(newTriangles.get(0), b, confirmedEdges));
                if(confirmedEdges.get(index1)[2]==2 || confirmedEdges.get(index2)[2]==2){
                    // already full, throw it out
                    //System.out.println("Throw out A");
                }else{
                    confirmedEdges.get(index1)[2]++;
                    confirmedEdges.get(index2)[2]++;
                    confirmedEdges.add(triple(a,b,1));
                    triangles.add(triple(a, b, newTriangles.get(0)));
                }
                
            }else if (newTriangles.size()==2){
                int index1=(contains3(newTriangles.get(0), a, confirmedEdges));
                int index2=(contains3(newTriangles.get(0), b, confirmedEdges));
                int index3=(contains3(newTriangles.get(1), a, confirmedEdges));
                int index4=(contains3(newTriangles.get(1), b, confirmedEdges));
                if(confirmedEdges.get(index1)[2]==2 || confirmedEdges.get(index2)[2]==2 || confirmedEdges.get(index3)[2]==2 || confirmedEdges.get(index4)[2]==2){
                    // already full, throw it out
                    //System.out.println("Throw out B");
                }else{
                    confirmedEdges.get(index1)[2]++;
                    confirmedEdges.get(index2)[2]++;
                    confirmedEdges.get(index3)[2]++;
                    confirmedEdges.get(index4)[2]++;
                    confirmedEdges.add(triple(a,b,2));
                    triangles.add(triple(a, b, newTriangles.get(0)));
                    triangles.add(triple(a, b, newTriangles.get(1)));
                }
            }else{
                //System.out.println("Adding new triangle");
                confirmedEdges.add(triple(a,b,0));
            }
        }
        
        
        
    }
    
    
    
    static LinkedList<Integer> touching2(int a, LinkedList<int[]> confirmed){
        LinkedList<Integer> toReturn=new LinkedList<Integer>();
        for(int i=0;i<confirmed.size();i++){
            if(confirmed.get(i)[0]==a){
                toReturn.add(confirmed.get(i)[1]);
            }else if(confirmed.get(i)[1]==a){
                toReturn.add(confirmed.get(i)[0]);
            }
        }
        return toReturn;
    }
    
    /**
    static void checkTriangle(LinkedList, LinkedList<int[]> confirmed){
        for(int i)
        
        
    }
    **/
    
    
    
    
    
    
    
    static int[] triple(int a, int b, int c){
        int[] temp={a,b,c};
        return temp;
    }
    
    static int[] pair(int a, int b){
        int[] temp={a,b};
        return temp;
    }
    
    static double getDist2(double[] a, double[] b){
        double dx=a[0]-b[0];
        double dy=a[1]-b[1];
        double dz=a[2]-b[2];
        return dx*dx + dy*dy + dz*dz;
    }
    
    static int contains3(int num1, int num2, LinkedList<int[]> list){
        
        for(int i=0;i<list.size();i++){
            
            if(list.get(i)[0]== num1 && list.get(i)[1]==num2){ 
                return i;
            }
            if(list.get(i)[0]== num2 && list.get(i)[1]==num1){
                return i;
            }
        }
        return -1;
    }
    
    static boolean contains2(int num, LinkedList<int[]> list){
        for(int i=0;i<list.size();i++){
            if(list.get(i)[0]== num){ 
                return true;
            }
            if(list.get(i)[1]==num){
                return true;
            }
        }
        return false;
    }
    
    static LinkedList<Integer> touching(int point, LinkedList<int[]> list){
        LinkedList<Integer> toReturn=new LinkedList<Integer>();
        for(int i=0;i<list.size();i++){
            if(list.get(i)[0]== point){ 
                toReturn.add(list.get(i)[1]);
            }
            if(list.get(i)[1]==point){
                toReturn.add(list.get(i)[0]);
            }
        }
        return toReturn;
    }
    
    static LinkedList<Integer> touching(int point, LinkedList<int[]> a, LinkedList<int[]> b, LinkedList<int[]> c){
        LinkedList<Integer> toReturn=new LinkedList<Integer>();
        toReturn.add(point);
        for(int i=0;i<a.size();i++){
            if(a.get(i)[0]== point){ 
                toReturn.add(a.get(i)[1]);
            }
            if(a.get(i)[1]==point){
                toReturn.add(a.get(i)[0]);
            }
        }
        for(int i=0;i<b.size();i++){
            if(b.get(i)[0]== point){ 
                toReturn.add(b.get(i)[1]);
            }
            if(b.get(i)[1]==point){
                toReturn.add(b.get(i)[0]);
            }
        }
        for(int i=0;i<c.size();i++){
            if(c.get(i)[0]== point){ 
                toReturn.add(c.get(i)[1]);
            }
            if(c.get(i)[1]==point){
                toReturn.add(c.get(i)[0]);
            }
        }
        return toReturn;
    }
    
    static int getNearest(int point, LinkedList<Integer> blacklist) {
        double min = Double.POSITIVE_INFINITY;
        double dist2;
        int best=0;
        for (int i = 0; i < totalPoints; i++) {
            if(!blacklist.contains(i)){
                dist2 = points[i][0] * points[i][0] + points[point][0] * points[point][0]
                        + points[i][1] * points[i][1] + points[point][1] * points[point][1]
                                + points[i][2] * points[i][2] + points[point][2] * points[point][2];
                if (i!=point && dist2 < min) {
                    min = dist2;
                    best=i;
                }
            }
        }

        return best;
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
        
        
        temp[0] = round(
                radius * Math.cos(u) * Math.cos(v/2) - 3 * radius * Math.cos(v/2),
                3);
        temp[1] = round(radius * Math.sin(u), 3);
        temp[2] = round(
                radius * Math.sin(v/2) * Math.cos(u) - 3 * radius * Math.sin(v/2),
                3);
        
        return temp;
    }

    static double[] rand() {
        return equation(umin + (umax - umin) * Math.random(), vmin
                + (vmax - vmin) * Math.random());
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

}
