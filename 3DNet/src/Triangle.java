import java.util.ArrayList;


public class Triangle{
    public int a;
    public int b;
    public int c;
    
    public Triangle(int a1, int b1, int c1){
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
    
    
    public boolean contains(int d){
        //ONLY WORKS IF TRIANGLE IS WOUND CLOCKWISE
        //returns true if on the line
        
        //System.out.println(a+" "+b+" "+c+" "+d);
        //System.out.println(cross(a,c,d));
        //System.out.println(cross(c,b,d));
        //System.out.println(cross(b,a,d));
        
        if(cross(a,c,d)>=0 && cross(c,b,d)>=0 && cross(b,a,d)>=0){
            return true;
        }
        return false;
    }
    
    static double cross(int o, int a, int b){
        //compute (a-o)x(b-o)
        //ax*by - ay*bx
        //positive if b is to the left of oa (looking from o)
        
        //((a[3]-o[3]) * (b[4]-o[4]))  -  ((b[3]-o[3]) * (a[4]-o[4]))
        return ((Net3.points[a][3]-Net3.points[o][3]) * (Net3.points[b][4]-Net3.points[o][4]))  -  ((Net3.points[b][3]-Net3.points[o][3]) * (Net3.points[a][4]-Net3.points[o][4]));
    }
    
    public boolean visible(int v, int p){
        //returns whether point p can see vertex v given opaque triangle
        //v MUST be on the triangle
        //assumes p is not in the triangle (I think it will always return false if so)
        //returns false if on the line
        
        //Note: do not change !(A & B) to (!A | !B) because that would mess with "on the line" cases
        
        if(v==a){
            //System.out.println(cross(a,b,p)+" "+cross(a,c,p));
            return !(cross(a,b,p)<=0 && cross(a,c,p)>=0);
        }else if(v==b){
            return !(cross(b,c,p)<=0 && cross(b,a,p)>=0);
        }else if(v==c){
            return !(cross(c,a,p)<=0 && cross(c,b,p)>=0);
        }else{
            System.out.println("ERROR: v must be a vertex of the triangle");
            return true; //likely to cause more noticeable problems than false
        }
    }
    
    public boolean hasVertex(int v){
        return a==v || b==v || c==v;
    }
    
    public boolean hasEdge2(Edge2 e){
        if(a==e.a){
            if(b==e.b || c==e.b){
                return true;
            }
        }else if(b==e.a){
            if(c==e.b || a==e.b){
                return true;
            }
        }else if(c==e.a){
            if(a==e.b || b==e.b){
                return true;
            }
        }
        return false;
    }
    
    int[] reOrder(Edge2 e){
        int[] toReturn=new int[3];
        
        if(a==e.a){
            if(b==e.b){
                toReturn[0]=c;
                toReturn[1]=a;
                toReturn[2]=b;
                
            }else if(c==e.b){
                toReturn[0]=b;
                toReturn[1]=a;
                toReturn[2]=c;
            }else{
                toReturn[0]=-1;
            }
        }else if(b==e.a){
            if(c==e.b){
                toReturn[0]=a;
                toReturn[1]=c;
                toReturn[2]=b;
                
            }else if(a==e.b){
                toReturn[0]=c;
                toReturn[1]=a;
                toReturn[2]=b;
                
            }else{
                toReturn[0]=-1;
            }
        }else if(c==e.a){
            if(a==e.b){
                toReturn[0]=b;
                toReturn[1]=a;
                toReturn[2]=c;
                
            }else if(b==e.b){
                toReturn[0]=a;
                toReturn[1]=c;
                toReturn[2]=b;
                
            }else{
                toReturn[0]=-1;
            }
        }else{
            toReturn[0]=-1;
        }
        
        if(toReturn[0]==-1){
            System.out.println("ERROR: Edge2 not part of triangle");
        }
        return toReturn;
    }
    
    public static boolean maybeSwap(Triangle[] ts,Edge2 e,ArrayList<Triangle> allTs, ArrayList<Edge2> iffy){
        
        int[] t1=ts[0].reOrder(e);
        int[] t2=ts[1].reOrder(e);
        
        if(Math.acos(dot(t1[0],t1[1],t1[2])) + Math.acos(dot(t2[0],t2[1],t2[2])) > Math.PI){
            //swap
            allTs.remove(ts[0]);
            allTs.remove(ts[1]);
            allTs.add(new Triangle(t1[0],t2[0],t1[1]));
            allTs.add(new Triangle(t1[0],t2[0],t1[2]));
            iffy.add(new Edge2(t1[0],t1[1]));
            iffy.add(new Edge2(t1[0],t1[2]));
            iffy.add(new Edge2(t2[0],t2[1]));
            iffy.add(new Edge2(t2[0],t2[2]));
            
            return true;
        }
        return false;
        
        
    }
    
    static double dot(int o, int a, int b){
        return ((Net3.points[a][3]-Net3.points[o][3]) * (Net3.points[b][3]-Net3.points[o][3]))  +  ((Net3.points[a][4]-Net3.points[o][4]) * (Net3.points[b][4]-Net3.points[o][4]));
    }
    
    public static void draw(ArrayList<Triangle> list){
        StdDraw.clear();
        StdDraw.setPenRadius(.001);
        StdDraw.setPenColor(255,0,0);
        for(Triangle t: list){
            StdDraw.line(Net3.points[t.a][3], Net3.points[t.a][4], Net3.points[t.b][3], Net3.points[t.b][4]);
            StdDraw.line(Net3.points[t.a][3], Net3.points[t.a][4], Net3.points[t.c][3], Net3.points[t.c][4]);
            StdDraw.line(Net3.points[t.c][3], Net3.points[t.c][4], Net3.points[t.b][3], Net3.points[t.b][4]);
        }
        StdDraw.setPenRadius(.003);
        StdDraw.setPenColor(0,0,0);
        for(int i=0;i<Net3.totalPoints;i++){
            //System.out.println(i);
            StdDraw.point(Net3.points[i][3],Net3.points[i][4]);
        }
        StdDraw.show(1000);
    }

    public boolean inCircumcircle(int p) {
        
        double ax=Net3.points[a][3];
        double ay=Net3.points[a][4];
        double bx=Net3.points[b][3];
        double by=Net3.points[b][4];
        double cx=Net3.points[c][3];
        double cy=Net3.points[c][4];
        double px=Net3.points[p][3];
        double py=Net3.points[p][4];
        
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
