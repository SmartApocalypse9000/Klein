
public class First {
    public static void main(String[] args){
        
        
        
        double umin=0;
        double umax=Math.PI*2;
        int upoints=20;
        double vmin=0;
        double vmax=Math.PI*2;
        int vpoints=40;
        
        double u=umin;
        double v=vmin;
        
        double ustep=(umax-umin)/(upoints-1);
        double vstep=(vmax-vmin)/(vpoints-1);
        
        double[][][] points= new double[upoints][vpoints][3];
        
        double radius=5;
        
        System.out.println("polyhedron(points=[");
        for(int vnum=0;vnum<vpoints;vnum++){
            
            for(int unum=0;unum<upoints;unum++){
                
                
                points[unum][vnum][0]=round(radius*Math.cos(u)*Math.cos(v)-3*radius*Math.cos(v),3);
                points[unum][vnum][1]=round(radius*Math.sin(u),3);
                points[unum][vnum][2]=round(radius*Math.sin(v)*Math.cos(u)-3*radius*Math.sin(v),3);
                u+=ustep;
                
                
                
                
                System.out.print("["+points[unum][vnum][0]+", "+points[unum][vnum][1]+", "+points[unum][vnum][2]+"]");
                if(unum!=upoints-1 || vnum!= vpoints-1){
                    System.out.println(", ");
                }
                
            }
            u=umin;
            v+=vstep;
        }
        
        System.out.print("],\nfaces=[");
        
        for(int vnum=0;vnum<vpoints-1;vnum++){
            for(int unum=0;unum<upoints-1;unum++){
                System.out.print("["+(unum+vnum*upoints)+", "+(unum+1+vnum*upoints)+", "+(unum+1+(vnum+1)*upoints)+"], ");
                System.out.print("["+(unum+(vnum)*upoints)+", "+(unum+1+(vnum+1)*upoints)+", "+(unum+(vnum+1)*upoints)+"]");
                if(unum!=upoints-2 || vnum!= vpoints-2){
                    System.out.println(", ");
                }
            }
        }
        System.out.println("] );");
        
        
    }
    
    static double round(double n, double precision){
        return Math.round(Math.pow(10,precision)*n)/Math.pow(10, precision);
    }
}
