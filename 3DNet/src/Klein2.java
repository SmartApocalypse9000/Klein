import java.util.ArrayList;


public class Klein2 {

    //As thickness increases, the smallest diameter
    // (the bottle neck at the bottom) decreases
    static final double thickness=.3;
    
    
    public static void main(String[] args){
        //System.out.println("Hello");
        double umin = 0;
        double umax = 2*Math.PI;
        int upoints=40;
        
        double vmin = 0;
        double vmax = 2*Math.PI-.01;
        int vpoints=80;
        
        ArrayList<double[]> points=new ArrayList<double[]>(); //original
        ArrayList<double[]> points2=new ArrayList<double[]>(); //outer 
        
        ArrayList<int[]> triangles=new ArrayList<int[]>(); //original & outer
        ArrayList<int[]> triangles2=new ArrayList<int[]>(); //end caps
        
        //For thin tube thing
        ArrayList<double[]> points3=new ArrayList<double[]>();
        ArrayList<double[]> points4=new ArrayList<double[]>();
        ArrayList<int[]> triangles3=new ArrayList<int[]>();
        ArrayList<int[]> triangles4=new ArrayList<int[]>();

        
        for(int vnum=0;vnum<vpoints;vnum++){
            double v=vnum*(vmax-vmin)/(vpoints-1);
            for(int unum=0;unum<upoints;unum++){
                double u=unum*(umax-umin)/(upoints);
                points.add(klein1(u,v));
            }
        }
        
        for(int vnum=0;vnum<vpoints;vnum++){
            for(int unum=0;unum<upoints;unum++){
                int[] temp0={vnum*upoints+unum, (vnum+1)%vpoints*upoints+(unum+1)%upoints, (vnum+1)%vpoints*upoints+unum};
                int[] temp1={vnum*upoints+unum, vnum*upoints+(unum+1)%upoints, (vnum+1)%vpoints*upoints+(unum+1)%upoints};
                if(vnum!=vpoints-1){
                    triangles.add(temp0);
                    triangles.add(temp1);
                }
                
                
                int num=vnum*upoints+unum;
                //System.out.println(num);
                
                double[] normal=normal(temp0, points);
                if(vnum==vpoints-1){
                    //fix end case
                    
                    //FUDGE makes the inside bow out a little to overlap with
                    // the other section and make sure it becomes 1 solid
                    // also this isn't really a good way to do it because
                    // fudge is scaled by thickness, but fudging isn't 
                    // good on its own anyway
                    
                    
                    //double FUDGE=2;
                    
                    normal=normal(triangles.get(((vnum-1)*upoints+unum)*2), points);
                    //normal[1]-=FUDGE;
                }
                
                
                
                double[] temp2={points.get(num)[0]-normal[0]*thickness,
                        points.get(num)[1]-normal[1]*thickness,
                        points.get(num)[2]-normal[2]*thickness};                
                
                points2.add(temp2);
                //System.out.println(temp2[0]);
                
            }
        }
        
        
        

        int totalPoints=upoints*vpoints;
        int offset=points.size();
        
        //Now add 2 "caps" between layers to close the solid

        //TODO uncomment this
        
        for(int i=0;i<upoints;i++){
            int[] temp0={(vpoints-1)*upoints+i, 
                    (vpoints-1)*upoints+(i+1)%upoints,
                    (vpoints-1)*upoints+i+offset};
            int[] temp1={(vpoints-1)*upoints+(i+1)%upoints,
                    (vpoints-1)*upoints+(i+1)%upoints+offset,
                    (vpoints-1)*upoints+i+offset};
            triangles.add(temp0);
            triangles.add(temp1);
        }
        
        
        
        
        for(int i=0;i<upoints;i++){
            int[] temp0={i,
                    i+offset,
                    (i+1)%upoints};
            int[] temp1={(i+1)%upoints,
                    i+offset,
                    (i+1)%upoints+offset};
            triangles2.add(temp0);
            triangles2.add(temp1);
        }
        
        
        
        
        //now for the thin inner tube
        for(int vnum=0;vnum<vpoints/4;vnum++){
            double v=vnum*(vmax-vmin)/(vpoints/4-1);
            for(int unum=0;unum<upoints;unum++){
                double u=unum*(umax-umin)/(upoints);
                points3.add(klein2(u,v));
            }
        }
        
        for(int vnum=0;vnum<vpoints/4;vnum++){
            for(int unum=0;unum<upoints;unum++){
                int[] temp0={vnum*(upoints)+unum, (vnum+1)%(vpoints/4)*(upoints)+(unum+1)%(upoints), (vnum+1)%(vpoints/4)*(upoints)+unum};
                int[] temp1={vnum*(upoints)+unum, vnum*(upoints)+(unum+1)%(upoints), (vnum+1)%(vpoints/4)*(upoints)+(unum+1)%(upoints)};
                if(vnum!=(vpoints/4)-1){
                    triangles3.add(temp0);
                    triangles3.add(temp1);
                }
                
                
                int num=vnum*(upoints)+unum;
                //System.out.println(num);
                
                double[] normal=normal(temp0, points3);
                if(vnum==(vpoints/4)-1){
                    //fix end case
                    
                    //FUDGE makes the inside bow out a little to overlap with
                    // the other section and make sure it becomes 1 solid
                    // also this isn't really a good way to do it because
                    // fudge is scaled by thickness, but fudging isn't 
                    // good on its own anyway
                    
                    
                    //double FUDGE=2;
                    
                    normal=normal(triangles3.get(((vnum-1)*(upoints)+unum)*2), points3);
                    //normal[1]-=FUDGE;
                }
                
                
                
                double[] temp2={points3.get(num)[0]+normal[0]*thickness,
                        points3.get(num)[1]+normal[1]*thickness,
                        points3.get(num)[2]+normal[2]*thickness};                
                
                points4.add(temp2);
                //System.out.println("Xnormal : "+temp2[0]);
                
            }
        }
        
        
        
        //Now add 2 "caps" between layers to close the solid
        int offset2=points3.size();
        int smallOffset=(vpoints/4-1)*upoints;
        
        //triangles4 does not adjust for points and points2
        
        
        for(int i=0;i<upoints;i++){
            int[] temp0={smallOffset+i,
                    smallOffset+i+offset2, 
                    smallOffset+(i+1)%upoints};
            int[] temp1={smallOffset+(i+1)%upoints,
                    smallOffset+i+offset2,
                    smallOffset+(i+1)%upoints+offset2};
            triangles4.add(temp0);
            triangles4.add(temp1);
        }
        
        
        
        for(int i=0;i<upoints;i++){
            int[] temp0={i,
                    (i+1)%(upoints),
                    i+offset2};
            int[] temp1={(i+1)%(upoints),
                    (i+1)%(upoints)+offset2,
                    i+offset2};
            triangles4.add(temp0);
            triangles4.add(temp1);
        }
        
        
        
        
        

        //System.exit(0);
        
        //print
        System.out.println("union(){");
        
        System.out.print("polyhedron( points=[");
        for(int i=0;i<totalPoints;i++){
            System.out.print("["+round(points.get(i)[0],3)+", "+round(points.get(i)[1],3)+", "+round(points.get(i)[2],3)+"]");
             System.out.println(", ");
        }
        
        for(int i=0;i<totalPoints;i++){
            System.out.print("["+round(points2.get(i)[0],3)+", "+round(points2.get(i)[1],3)+", "+round(points2.get(i)[2],3)+"]");
             System.out.println(", ");
        }
        
        for(int i=0;i<points3.size();i++){
            System.out.print("["+round(points3.get(i)[0],3)+", "+round(points3.get(i)[1],3)+", "+round(points3.get(i)[2],3)+"]");
             System.out.println(", ");
        }

        for(int i=0;i<points4.size();i++){
            System.out.print("["+round(points4.get(i)[0],3)+", "+round(points4.get(i)[1],3)+", "+round(points4.get(i)[2],3)+"]");
            if(i!=totalPoints-1) System.out.println(", ");
        }

        /**
        System.out.println("], \nfaces=[");
        for(int i=0;i<totalPoints*2-upoints*2;i++){
            System.out.print("["+triangles.get(i)[0]+","+
                    triangles.get(i)[2]+","+
                    triangles.get(i)[1]+"],\n");
            
            System.out.print("["+(triangles.get(i)[0]+offset)+","+
                    (triangles.get(i)[1]+offset)+","+
                    (triangles.get(i)[2]+offset)+"]");
            
            if(i!=totalPoints*2-1){ 
                System.out.println(", "); 
            }
        }
        **/
        System.out.println("], \nfaces=[");
        
        //end caps
        for(int i=0;i<triangles2.size();i++){
            System.out.print("["+triangles2.get(i)[0]+","+
                    triangles2.get(i)[2]+","+
                    triangles2.get(i)[1]+"],\n");
        }


        int bigOffset=points.size()+points2.size();
        int smallInnerOffset=points3.size();
        
        //System.out.println("SIZE IS "+points3.size());
        
        for(int i=0;i<triangles3.size();i++){
            //System.out.println("HI");
            System.out.print("["+(triangles3.get(i)[0]+bigOffset)+","+
                    (triangles3.get(i)[1]+bigOffset)+","+
                    (triangles3.get(i)[2]+bigOffset)+"],\n");
            
            System.out.print("["+(triangles3.get(i)[0]+bigOffset+smallInnerOffset)+","+
                    (triangles3.get(i)[2]+bigOffset+smallInnerOffset)+","+
                    (triangles3.get(i)[1]+bigOffset+smallInnerOffset)+"],\n");
        }        
        
        
        for(int i=0;i<triangles4.size();i++){
            //System.out.println("HI");
            System.out.print("["+(triangles4.get(i)[0]+bigOffset)+","+
                    (triangles4.get(i)[2]+bigOffset)+","+
                    (triangles4.get(i)[1]+bigOffset)+"],\n");
        }
        

        
        //both layers
        for(int i=0;i<triangles.size();i++){
            System.out.print("["+triangles.get(i)[0]+","+
                    triangles.get(i)[2]+","+
                    triangles.get(i)[1]+"],\n");
            
            System.out.print("["+(triangles.get(i)[0]+offset)+","+
                    (triangles.get(i)[1]+offset)+","+
                    (triangles.get(i)[2]+offset)+"]");
                    
            
            if(i!=triangles.size()-1){ 
                System.out.println(", "); 
            }
        }
        
        
        System.out.println("]);\n}");
        

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
            //double r=(1+v/Math.PI/2);
            double r=(.5+3*v/Math.PI/4);
            
            
            x= r*Math.cos(u)*Math.cos(v/2) + 2*Math.cos(v/2);
            y= 4 + r*Math.cos(u)*Math.sin(v/2) + 2*Math.sin(v/2);
            z= r*Math.sin(u);
            
        }else if(v<=4*Math.PI){
            //wide curvey part
            // starts at x=0, 
            v%=Math.PI*2;
            //double v2=(Math.sin(v/4)+.5*Math.sin(2*v/4))*2*Math.PI;

            x= -(2+v/Math.PI)*Math.cos(u) +v/Math.PI-2;//- Math.cos(v/2)-1;
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
            // -sqrt(1-(v/2PI -1)^2) +2
            double r=(-Math.sqrt(1-Math.pow(v/Math.PI/2-1,2))+2);
            //if(u==0) System.out.println("r is: "+r);
            x=-r*Math.cos(u);
            y=v/Math.PI/2;
            z=r*Math.sin(u);
            
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
        // matches 1-thickness at start and .5 at end
        double r=((1.0-thickness)+(v/Math.PI/2)*(.5 - (1.0-thickness)));

        // Stretches the small peiece to overlap with the big one
        double FUDGE=.2;
        
        x=-1*r*Math.cos(u)+v2;
        y=((3+FUDGE*2)/3.0)*3*v/Math.PI/2+1-FUDGE;
        z=r*Math.sin(u);


        //TODO DELETE THIS WHEN DONE DEBUGGING
        //z+=7;

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

    
    // returns UNIT VECTOR normal to the triangle
    public static double[] normal(int[] triangle, ArrayList<double[]> points){
        double[] a= points.get(triangle[0]);
        double[] b= points.get(triangle[1]);
        double[] o= points.get(triangle[2]);
        
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
