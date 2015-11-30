import java.util.LinkedList;

public class Net {
    static double umin = 0;
    static double umax = 2 * Math.PI;
    static double vmin = 0;
    static double vmax = 2 * Math.PI;
    static final int totalPoints = 100;
    static final int candidates = 100; // scales time linearly

    static double[][] points = new double[totalPoints][];
    static int[][] triangles;
    static int numPoints = 1;

    public static void main(String[] args) {
        getPoints();
        connect();

        /**
         * System.out.print("polyhedron( points=["); for(int
         * i=0;i<totalPoints;i++){
         * System.out.print("["+points[i][0]+", "+points[
         * i][1]+", "+points[i][2]+"], ");
         * System.out.print("["+(points[i][0]+1)+
         * ", "+(points[i][1]+1)+", "+(points[i][2]+0)+"], ");
         * System.out.print("["
         * +(points[i][0]+0)+", "+(points[i][1]+0)+", "+(points[i][2]+1)+"]");
         * if(i!=totalPoints-1){ System.out.println(", "); } }
         * System.out.println("], \nfaces=["); for(int i=0;i<totalPoints;i++){
         * System.out.print("["+(3*i)+","+(3*i+1)+","+(3*i+2)+"]");
         * if(i!=totalPoints-1){ System.out.println(", "); } }
         * System.out.println("]);");
         **/
    }

    static void connect() {
        LinkedList<int[]> tier1 = new LinkedList<int[]>(); //part of 2 triangles, not considered
        LinkedList<int[]> tier2 = new LinkedList<int[]>(); //part of 1 triangle
        LinkedList<int[]> tier3 = new LinkedList<int[]>(); //no triangles
        LinkedList<Integer> points= new LinkedList<Integer>();
        LinkedList<int[]> triangles = new LinkedList<int[]>();
        boolean[] blacklist=new boolean[totalPoints]; //false means not blacklisted
        
        blacklist[0]=true;
        tier3.add(new int[] {0,getNearest(0,touching(0,tier1,tier2,tier3))});
        blacklist[0]=false;
        points.add(0);
        points.add(tier3.get(0)[1]);
        
        boolean done=false;
        while(!done){
            System.out.println(tier1.size()+" "+tier2.size()+" "+tier3.size());
            
            int[] candidate={0,0}; // an existing edge
            
            if(tier2.isEmpty()){
                //up to tier 3
                if(tier3.isEmpty()){
                    done=true;
                    break;
                }
                candidate=tier3.get((int)(tier3.size()*Math.random())); //[(int)(2*Math.random())];
                
            }else{
                //tier 2 element 1
                candidate=tier2.get((int)(tier2.size()*Math.random()));
            }
            
            blacklist[candidate[0]]=true;
            blacklist[candidate[1]]=true;
            int next=getNearest(candidate[1], touching(candidate[1], tier1, tier2, tier3)); //candidate point
            if(points.contains(next)){
                System.out.println("Debug A "+candidate[0]+" "+candidate[1]);
                
                //wrap around
                //points.add(candidate[1]);
                //points.add(next);
                
                if(touching(next, tier1, tier2, tier3).contains(candidate[0])){
                    System.out.println("Triangle completed");
                    //completed a triangle
                    int[] temp={candidate[0],candidate[1],next};
                    triangles.add(temp); //check winding
                    
                    int index=contains3(candidate[1], next, tier2);
                    if(index!=-1){
                        tier2.remove(index);
                        int[] temp1={candidate[1],next};
                        tier3.add(temp1);
                    }
                    index=contains3(candidate[1], next, tier3);
                    if(index!=-1){
                        tier3.remove(index);
                        int[] temp1={candidate[1],next};
                        tier2.add(temp1);
                    }
                    
                    index=contains3(candidate[1], candidate[0], tier2);
                    if(index!=-1){
                        tier2.remove(index);
                        int[] temp1={candidate[0],candidate[1]};
                        tier2.add(temp1);
                    }
                    index=contains3(candidate[1], candidate[0], tier3);
                    if(index!=-1){
                        tier3.remove(index);
                        int[] temp1={candidate[0],candidate[1]};
                        tier2.add(temp1);
                    }

                    
                }else{
                    int[] temp={candidate[1], next};
                    tier2.add(temp);
                }
                
            }else{
                System.out.println("Adding");
                points.add(next);
                int[] temp={candidate[1],next};
                tier3.add(temp);
            }
            
        }
        
        

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
            if (dist(points, numPoints, candidate) > max) {
                toReturn = candidate;
            }
        }
        return toReturn;
    }

    // returns square of distance to nearest point
    static double dist(double[][] points, int numPoints, double[] candidate) {
        double min = Double.POSITIVE_INFINITY;
        double dist2;
        for (int i = 0; i < numPoints; i++) {
            dist2 = points[i][0] * points[i][0] + candidate[0] * candidate[0]
                    + points[i][1] * points[i][1] + candidate[1] * candidate[1]
                    + points[i][2] * points[i][2] + candidate[2] * candidate[2];
            if (dist2 < min) {
                min = dist2;
            }
        }

        return min;
    }

}
