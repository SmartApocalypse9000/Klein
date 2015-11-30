public class Point{
    double x;
    double y;
    double z;
    
    Point(double x1, double y1, double z1){
        x=x1;
        y=y1;
        z=z1;
        
    }
    
    public double dist(Point b){
        double dx = x-b.x;
        double dy = y-b.y;
        double dz = z-b.z;
        return Math.sqrt(dx*dx+dy*dy+dz*dz);
    }
}