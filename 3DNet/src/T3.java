import java.util.ArrayList;


public class T3 {

    
    public double[] a;
    public double[] b;
    public double[] c;
    
    
    public T3(double[] a1, double[] b1, double[] c1){
        a=a1;
        b=b1;
        c=c1;
        
    }
    
    /**
    private static double[] getCircumcenter(double[] a, double[] b, double[] c){
        double[] toReturn=new double[2];

        double d = 2 * (a[1] * c[0] + b[1] * a[0] - b[1] * c[0] - a[1] * b[0]
                - c[1] * a[0] + c[1] * b[0]);

        toReturn[0] = (b[1] * a[0] * a[0] - c[1] * a[0] * a[0] - b[1] * b[1]
                * a[1] + c[1] * c[1] * a[1] + b[0] * b[0] * c[1] + a[1] * a[1]
                * b[1] + c[0] * c[0] * a[1] - c[1] * c[1] * b[1] - c[0] * c[0]
                * b[1] - b[0] * b[0] * a[1] + b[1] * b[1] * c[1] - a[1] * a[1]
                * c[1])
                / d;

        toReturn[1] = (a[0] * a[0] * c[0] + a[1] * a[1] * c[0] + b[0] * b[0]
                * a[0] - b[0] * b[0] * c[0] + b[1] * b[1] * a[0] - b[1] * b[1]
                * c[0] - a[0] * a[0] * b[0] - a[1] * a[1] * b[0] - c[0] * c[0]
                * a[0] + c[0] * c[0] * b[0] - c[1] * c[1] * a[0] + c[1] * c[1]
                * b[0])
                / d;
        
        return toReturn;
    }
    **/
    
    
    
    double[] getCircumsphere() {
        double[] line0=cross2(a,b,cross(a,b,c));
        double[] line1=cross2(b,c,cross(b,c,a));
        
        // t= (x0-x1)/(v1-v0)
        double t= ( ((b[0]+c[0])/2.0 - (a[0]+b[0])/2.0)/line1[0]  +  ((a[1]+b[1])/2.0-(b[1]+c[1])/2.0)/line1[1] )/
                  ((line0[0]/line1[0]) - (line0[1]/line1[1]));
        
        
        
        double[] toReturn=new double[4];
        toReturn[0]=(a[0]+b[0])/2.0+t*line0[0];
        toReturn[1]=(a[1]+b[1])/2.0+t*line0[1];
        toReturn[2]=(a[2]+b[2])/2.0+t*line0[2];
        toReturn[3]=getDist2(a,toReturn);
        
        
        if(Double.isNaN(t)){
            System.out.println("NAN");
            
            toReturn[0]=0;
            toReturn[1]=0;
            toReturn[2]=0;
            toReturn[3]=1000;//Double.POSITIVE_INFINITY;
        }
        
        
        if(Math.abs(getDist2(c, toReturn)-toReturn[3])>.01 || Math.abs(getDist2(c, toReturn)-toReturn[3])>.01){
            System.out.println("Problem");
        }
        
        return toReturn;
    }
 
    static double getDist2(double[] a, double[] b){
        double dx=a[0]-b[0];
        double dy=a[1]-b[1];
        double dz=a[2]-b[2];
        return dx*dx + dy*dy + dz*dz;
    }


    static boolean inCircumsphere(double[] sphere, double[] p) {
        
        return getDist2(sphere,p) < sphere[3];
    }
    
    
    /**
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
    **/
    
    // returns true if it was already wound
    /**
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
    **/
    
    
    
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
    
    
    static double[] cross2(double o[], double a[], double b[]){
        //compute (a-o)x(b-o)
        //ax*by - ay*bx
        //positive if b is to the left of oa (looking from o)
        
        //((a[3]-o[3]) * (b[4]-o[4]))  -  ((b[3]-o[3]) * (a[4]-o[4]))
        //System.out.println(o+"   "+a+"   "+b+"   "+Net4.wrappedPoints.size());
        
        double ax=a[0]-o[0];
        double ay=a[1]-o[1];
        double az=a[2]-o[2];
        double bx=b[0];
        double by=b[1];
        double bz=b[2];
        
        double[] toReturn=new double[3];
        toReturn[0]=ay*bz-az*by;
        toReturn[1]=az*bx-ax*bz;
        toReturn[2]=ax*by-ay*bx;
        return toReturn;
    }
    
    
    
    private static double det(double[][] m) {
        // TODO Auto-generated method stub
        return m[0][0]*m[1][1]*m[2][2]+m[0][1]*m[1][2]*m[2][0]+m[0][2]*m[1][0]*m[2][1]  -  (m[0][0]*m[1][2]*m[2][1]+m[0][1]*m[1][0]*m[2][2]+m[0][2]*m[1][1]*m[2][0]);
    }
}
