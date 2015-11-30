import java.util.ArrayList;


public class Edge2 {
    int a;
    int b;
    
    public Edge2(int a1, int b1){
        a=a1;
        b=b1;
    }

    public boolean equals(Object o){
        if (o == null) return false;
        if (o == this) return true;
        Edge2 e=(Edge2)o;
        if((a==e.a&&b==e.b) || (a==e.b&&b==e.a)){
            return true;
        }
        return false;
    }
    
    public Triangle[] getAdjacent(ArrayList<Triangle> list){
        Triangle[] toReturn=new Triangle[2];
        int found=0;
        for(Triangle t: list){
            if(t.hasEdge2(this)){
                toReturn[found]=t;
                found++;
                if(found==2){
                    break;
                }
            }
        }
        
        if(found!=2){
            System.out.println("ERROR: edge was not part of 2 triangles");
            return new Triangle[0];
        }
        return toReturn;
    }
}
