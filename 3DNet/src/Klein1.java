
public class Klein1 {

    public static void main(String[] args){
        double umin = 0;
        double umax = 2*Math.PI;
        int upoints=30;
        
        double vmin = 0;
        double vmax = 2*Math.PI-.01;
        int vpoints=100;
        
        double[][] points=new double[upoints*vpoints][];
        double[][] points2=new double[upoints*vpoints][3];
        
        int[][] triangles=new int[upoints*vpoints*2][];
        int[][] triangles2=new int[upoints*vpoints*2][];
        

        double thickness=.1;
        
        for(int vnum=0;vnum<vpoints;vnum++){
            double v=vnum*(vmax-vmin)/(vpoints-1);
            for(int unum=0;unum<upoints;unum++){
                double u=unum*(umax-umin)/(upoints);
                points[vnum*upoints+unum]=klein1(u,v);
            }
        }
        
        for(int vnum=0;vnum<vpoints;vnum++){
            for(int unum=0;unum<upoints;unum++){
                int[] temp0={vnum*upoints+unum, (vnum+1)%vpoints*upoints+(unum+1)%upoints, (vnum+1)%vpoints*upoints+unum};
                int[] temp1={vnum*upoints+unum, vnum*upoints+(unum+1)%upoints, (vnum+1)%vpoints*upoints+(unum+1)%upoints};
                triangles[(vnum*upoints+unum)*2]=temp0;
                triangles[(vnum*upoints+unum)*2+1]=temp1;
                
                int num=vnum*upoints+unum;
                //System.out.println(num);
                
                double[] normal=normal(temp0, points);
                points2[num][0]= points[num][0]-normal[0]*thickness;
                points2[num][1]= points[num][1]-normal[1]*thickness;
                points2[num][2]= points[num][2]-normal[2]*thickness;
                
            }
        }



        //System.exit(0);
        
        //print
        /**
        int totalPoints=upoints*vpoints;
        System.out.print("polyhedron( points=[");
        for(int i=0;i<totalPoints;i++){
            System.out.print("["+points[i][0]+", "+points[i][1]+", "+points[i][2]+"]");
            if(i!=totalPoints-1) System.out.println(", ");
        }
        **/
        
        
        int totalPoints=upoints*vpoints;
        System.out.print("polyhedron( points=[");
        for(int i=0;i<totalPoints;i++){
            System.out.print("["+points2[i][0]+", "+points2[i][1]+", "+points2[i][2]+"]");
            if(i!=totalPoints-1) System.out.println(", ");
        }



        System.out.println("], \nfaces=[");
        for(int i=0;i<totalPoints*2-upoints*2;i++){
            System.out.print("["+triangles[i][0]+","+
                    triangles[i][1]+","+
                    triangles[i][2]+"]");
            if(i!=totalPoints*2-1){ 
                System.out.println(", "); 
            }
        }
        System.out.println("]);");


    }
    
    
    
    private static double[] klein1(double u, double v){
        //System.out.println(u+"   "+v);
        double x=10;
        double y=10;
        double z=10;
        
        v*=5;
        
        if(v<=2*Math.PI){
            //Top
            v%=Math.PI*2;
            
            x= (1+v/Math.PI/2)*Math.cos(u)*Math.cos(v/2) + 2*Math.cos(v/2);
            y= 4 + (1+v/Math.PI/2)*Math.cos(u)*Math.sin(v/2) + 2*Math.sin(v/2);
            z= (1+v/Math.PI/2)*Math.sin(u);
            
        }else if(v<=4*Math.PI){
            //wide curvey part
            v%=Math.PI*2;
            //double v2=(Math.sin(v/4)+.5*Math.sin(2*v/4))*2*Math.PI;

            x= -(2+v/Math.PI)*Math.cos(u) - Math.cos(v/2)-1;
            y= -1.5*v/Math.PI+4;
            z= (2+v/Math.PI)*Math.sin(u);

        }else if(v<=6*Math.PI){
            v%=Math.PI*2;

            x=-(Math.sqrt(1-Math.pow(v/Math.PI/2,2))+3)*Math.cos(u);
            y=1-v/Math.PI/2;
            z=(Math.sqrt(1-Math.pow(v/Math.PI/2,2))+3)*Math.sin(u);

        }else if(v<=8*Math.PI){
            v%=Math.PI*2;

            x=-(3-v/Math.PI/2)*Math.cos(u);
            y=0;
            z=(3-v/Math.PI/2)*Math.sin(u);
            
        }else if(v<=10*Math.PI){
            v%=Math.PI*2;

            x=-(-Math.sqrt(1-Math.pow(v/Math.PI/2-1,2))+2)*Math.cos(u);
            y=v/Math.PI/2;
            z=(-Math.sqrt(1-Math.pow(v/Math.PI/2-1,2))+2)*Math.sin(u);
            
            /**
            x=-(-Math.sqrt(1-Math.pow(v/Math.PI/2,2))+2)*Math.cos(u);
            y=v/Math.PI/2;
            z=(-Math.sqrt(1-Math.pow(v/Math.PI/2,2))+2)*Math.sin(u);
            **/


        }

        return triple(round(x,5),round(y,5),round(z,
                5));
        
    }

    
    
    
    private static double[] klein2(double u, double v){
        //System.out.println(u+"   "+v);
        double x=10;
        double y=10;
        double z=10;

        v%=Math.PI*2;
        double v2=-Math.cos(v/2)+1; //from 0 to 2

        x=-1*Math.cos(u)+v2;
        y=3*v/Math.PI/2+1;
        z=Math.sin(u);




        return triple(round(x,5),round(y,5),round(z,
                5));

    }
    
    
    
    /**
    private static double[] klein2(double u, double v){
        //System.out.println(u+"   "+v);
        double x=0;
        double y=0;
        double z=10;
        
        v*=2;
        
        if(v<=2*Math.PI){
            v%=Math.PI*2;
            
            x= (1+v/Math.PI/2)*Math.cos(u)*Math.cos(v/2) + 2*Math.cos(v/2);
            y= 4 + (1+v/Math.PI/2)*Math.cos(u)*Math.sin(v/2) + 2*Math.sin(v/2);
            z= (1+v/Math.PI/2)*Math.sin(u);
            
        }else{
            v%=Math.PI*2;
            
            
            
        }
        
        return triple(round(x,5),round(y,5),round(z,
                5));
        
    }
    **/
    
    
    
    
    private static double[] triple(double x, double y, double z) {
        double[] toReturn={x,y,z};
        return toReturn;
    }
    
    
    static double round(double n, double precision) {
        return Math.round(Math.pow(10, precision) * n)
                / Math.pow(10, precision);
    }

    
    // returns UNIT VECTOR normal to the triangle
    public static double[] normal(int[] triangle, double[][] points){
        double[] a= points[triangle[0]];
        double[] b= points[triangle[1]];
        double[] o= points[triangle[2]];
        
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
        
        double mag= Math.sqrt(toReturn[0]*toReturn[0] + toReturn[1]*toReturn[1] + toReturn[2]*toReturn[2]);
        if(mag!=0){
            toReturn[0]/=mag;
            toReturn[1]/=mag;
            toReturn[2]/=mag;
        }else{
            //System.out.println("TRIANGLE IS "+triangle[0]+"   "+triangle[1]+"   "+triangle[2]);
        }
        return toReturn;
        
    }
}
