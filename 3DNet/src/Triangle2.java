import java.util.ArrayList;


public class Triangle2{
    public int a;
    public int b;
    public int c;
    
    public Triangle2(int a1, int b1, int c1){
        a=a1;
        b=b1;
        c=c1;
        wind();
    }
    
    public int hashCode(){
        return (a+b*c)+(b+a*c)+(c+a*b);
    }
    
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        Triangle t=(Triangle)o;
        if(a==t.a){
            if((b==t.b && c==t.c) || (b==t.c && c==t.b)){
                return true;
            }
        }else if(a==t.b){
            if((b==t.a && c==t.c) || (b==t.c && c==t.a)){
                return true;
            }
        }else if(a==t.c){
            if((b==t.b && c==t.a) || (c==t.b && b==t.a)){
                return true;
            }
        }
        return false;
    }
    
    // returns true if it was already wound
    public boolean wind(){
        if(cross(a,c,b)>0){
            return true;
        }else{
            int d=a;
            a=c;
            c=d;
            return false;
        }
    }
    
    static double cross(int o, int a, int b){
        //compute (a-o)x(b-o)
        //ax*by - ay*bx
        //positive if b is to the left of oa (looking from o)
        
        //((a[3]-o[3]) * (b[4]-o[4]))  -  ((b[3]-o[3]) * (a[4]-o[4]))
        //System.out.println(o+"   "+a+"   "+b+"   "+Net4.wrappedPoints.size());
        return ((Net4.wrappedPoints.get(a)[0]-Net4.wrappedPoints.get(o)[0]) * (Net4.wrappedPoints.get(b)[1]-Net4.wrappedPoints.get(o)[1]))  -
                ((Net4.wrappedPoints.get(b)[0]-Net4.wrappedPoints.get(o)[0]) * (Net4.wrappedPoints.get(a)[1]-Net4.wrappedPoints.get(o)[1]));
    }
    
    
    public static void draw(ArrayList<Triangle2> list){
        StdDraw.clear();
        StdDraw.setPenRadius(.001);
        StdDraw.setPenColor(255,0,0);
        for(Triangle2 t: list){
            StdDraw.line(Net4.wrappedPoints.get(t.a)[0], Net4.wrappedPoints.get(t.a)[1], Net4.wrappedPoints.get(t.b)[0], Net4.wrappedPoints.get(t.b)[1]);
            StdDraw.line(Net4.wrappedPoints.get(t.a)[0], Net4.wrappedPoints.get(t.a)[1], Net4.wrappedPoints.get(t.c)[0], Net4.wrappedPoints.get(t.c)[1]);
            StdDraw.line(Net4.wrappedPoints.get(t.b)[0], Net4.wrappedPoints.get(t.b)[1], Net4.wrappedPoints.get(t.c)[0], Net4.wrappedPoints.get(t.c)[1]);
        }
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(0,0,0);
        for(int i=0;i<Net4.totalPoints;i++){
            //System.out.println(i);
            StdDraw.point(Net4.points[i][3],Net4.points[i][4]);
        }
        StdDraw.show(1000);
    }

    public boolean inCircumcircle(int p) {
        
        double ax=Net4.wrappedPoints.get(a)[0];
        double ay=Net4.wrappedPoints.get(a)[1];
        double bx=Net4.wrappedPoints.get(b)[0];
        double by=Net4.wrappedPoints.get(b)[1];
        double cx=Net4.wrappedPoints.get(c)[0];
        double cy=Net4.wrappedPoints.get(c)[1];
        double px=Net4.wrappedPoints.get(p)[0];
        double py=Net4.wrappedPoints.get(p)[1];
        
        double[][] matrix={{ax-px, ay-py, (ax*ax-px*px)+(ay*ay-py*py)},
                {bx-px, by-py, (bx*bx-px*px)+(by*by-py*py)},
                {cx-px, cy-py, (cx*cx-px*px)+(cy*cy-py*py)}};
        
        return det(matrix)<0.0;
    }

    private static double det(double[][] m) {
        // TODO Auto-generated method stub
        return m[0][0]*m[1][1]*m[2][2]+m[0][1]*m[1][2]*m[2][0]+m[0][2]*m[1][0]*m[2][1]  -  (m[0][0]*m[1][2]*m[2][1]+m[0][1]*m[1][0]*m[2][2]+m[0][2]*m[1][1]*m[2][0]);
    }
        
    
    


}
