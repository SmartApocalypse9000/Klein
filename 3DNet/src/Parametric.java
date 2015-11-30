
public class Parametric {

    public static void main(String[] args){
        double umin = 0;
        double umax = 2*Math.PI;
        int upoints=20;
        
        double vmin = 0;
        double vmax = 2*Math.PI+.1;
        int vpoints=50;
        
        double[][] points=new double[upoints*vpoints][];
        int[][] triangles=new int[upoints*vpoints*2][];
        
        
        for(int vnum=0;vnum<vpoints;vnum++){
            double v=vnum*(vmax-vmin)/(vpoints-1);
            for(int unum=0;unum<upoints;unum++){
                double u=unum*(umax-umin)/(upoints-1);
                points[vnum*upoints+unum]=klein(u,v);
            }
        }
        
        for(int vnum=0;vnum<vpoints-1;vnum++){
            for(int unum=0;unum<upoints;unum++){
                int[] temp0={vnum*upoints+unum, (vnum+1)%vpoints*upoints+(unum+1)%upoints, (vnum+1)%vpoints*upoints+unum};
                int[] temp1={vnum*upoints+unum, vnum*upoints+(unum+1)%upoints, (vnum+1)%vpoints*upoints+(unum+1)%upoints};
                triangles[(vnum*upoints+unum)*2]=temp0;
                triangles[(vnum*upoints+unum)*2+1]=temp1;
                
            }
        }



        //System.exit(0);
        
        //print
        int totalPoints=upoints*vpoints;
        System.out.print("polyhedron( points=[");
        for(int i=0;i<totalPoints;i++){
            System.out.print("["+points[i][0]+", "+points[i][1]+", "+points[i][2]+"]");
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
    
    
    
    private static double[] klein(double u, double v){
        //System.out.println(u+"   "+v);
        double x=10;
        double y=10;
        double z=10;
        
        v*=6;
        
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


        }else if(v<=Math.PI*12){
            
            v%=Math.PI*2;
            double v2=-Math.cos(v/2)+1; //from 0 to 2

            x=-1*Math.cos(u)+v2;
            y=3*v/Math.PI/2+1;
            z=Math.sin(u);

        }

        return triple(round(x,5),round(y,5),round(z,
                5));
        
    }
    
    
    
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
    
    
    
    
    
    private static double[] triple(double x, double y, double z) {
        double[] toReturn={x,y,z};
        return toReturn;
    }
    
    
    static double round(double n, double precision) {
        return Math.round(Math.pow(10, precision) * n)
                / Math.pow(10, precision);
    }
}
