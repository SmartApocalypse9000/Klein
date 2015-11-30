import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class New1 {

    private static final double thickness = .3;
    private final static int unum = 30;
    private final static int vnum = 30;

    private final static double holex = 1.8;
    private final static double holey = 5;
    private static final double holeSize = 1.2;

    private static final double TwoPI = Math.PI * 2;

    public static void main(String[] args) {
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream("output.scad"));

            System.setOut(out);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Point[] listA = { new Point(0, 0, 2), new Point(0, 0, 3),
                new Point(0, 1, 4), new Point(0, 2, 4),
                new Point(0, 4, 2), new Point(-3, 5, 1),
                new Point(-3, 6, 1), new Point(-3, 8, .8),
                new Point(-1, 8, .6), new Point(0, 8, .6), };

        Point[] listB = { new Point(0, 0, 1.99), new Point(0, 0, 1.5),
                new Point(0, 1, 1.2), new Point(0, 2, 1),
                new Point(0, 4, .9), new Point(3, 5, .8),
                new Point(3, 6, .6), new Point(3, 8, .6 + thickness / 3),
                new Point(1, 8, .6 + 2 * thickness / 3),
                new Point(0, 8, .6 + thickness) };

        Bezier big = new Bezier(listA);
        Bezier small = new Bezier(listB);

        Point[][] points = points(big);
        Point[][] shrunkPoints = shrunkPoints(big, -thickness);
        Point[][] points2 = points(small);
        Point[][] shrunkPoints2 = shrunkPoints(small, thickness);

        points2[0] = points2[0];
        shrunkPoints2[0] = shrunkPoints[0];

        int num = points.length - 1;
        // points2[num] = shrunkPoints[num];

        // shrunkPoints2[num] = shrunkPoints[num];

        for (int v = 0; v < vnum; v++) {
            shrunkPoints2[num][v] = points[num][num - v];
            points2[num][v] = shrunkPoints[num][num - v];
        }
        // Bezier.wait(100000);

        
        
        //System.out.println("difference(){");
        System.out.println("union(){");
        System.out.println("difference(){");
        print2(points, shrunkPoints, false);

        System.out.println("translate([" + holex + "," + holey
                + ",0])rotate([90,0,-35])cylinder(h=1, r=" + holeSize
                + ", $fn=50);");
        System.out.println("}");
        System.out.println("translate([.0001,0,0]){");
        print2(points2, shrunkPoints2, false);
        System.out.println("}\n}");
        //holes(points, points2);
        //System.out.println("}");
         
        
        /**
        print1(points2);
        print1(shrunkPoints);
        **/
         

    }

    private static void holes(Point[][] points, Point[][] points2) {
        double buffer = .3;
        double size = .5;

        double buffer2 = 2 * size + buffer;

        Point holeCenter = new Point(holex, holey, 0);
        Random rand = new Random(1);
        int maxu = points.length - 2;
        int maxv = points[0].length - 2;

        ArrayList<Point> candidates = new ArrayList<Point>();
        ArrayList<Point> rotate = new ArrayList<Point>();

        //boolean done = false;
        int failures = 0;
        while (failures < 10) {
            failures++;
            Point[][] current;
            if (rand.nextDouble() < .5) {
                current = points;
            } else {
                current = points2;
            }

            int u = (int) (rand.nextDouble() * maxu + 1);
            int v = (int) (rand.nextDouble() * maxv + 1);

            Point nextCandidate = current[u][v];
            if (current == points
                    && nextCandidate.dist(holeCenter) < (buffer + size + holeSize)) {
                // no good
            } else {
                boolean good = true;
                for (Point p : candidates) {
                    if (nextCandidate.dist(p) < buffer2) {
                        good = false;

                    }

                }
                if (good) {
                    candidates.add(nextCandidate);
                    Point vector = cross(current[u][v], current[u + 1][v],
                            current[u][v + 1]);
                    double theta = Math.atan2(
                            vector.z,
                            Math.sqrt(vector.x * vector.x + vector.y
                                    * vector.y));
                    double phi = Math.atan2(vector.y, vector.x);
                    rotate.add(new Point(theta, phi, size));
                    failures = 0;
                }
            }

        }

        // now print

        for (int i = 0; i < candidates.size(); i++) {
            Point center = candidates.get(i);
            Point rotation = rotate.get(i);

            System.out.println("translate([" + center.x + "," + center.y
                    + "," + center.z + "])rotate([" + rotation.y + ",0,"
                    + rotation.x + "])translate(0,0,-.5)cylinder(r="
                    + rotation.z + ", h=1.5, $fn=10);");
        }

    }

    // 2d shell
    private static void print1(Point[][] points) {
        int upoints = points.length;
        int vpoints = points[0].length;

        System.out.println("polyhedron( points = [\n");
        for (int i = 0; i < upoints; i++) {
            for (int j = 0; j < vpoints; j++) {
                Point point = points[i][j];
                System.out.println("[" + point.x + "," + point.y + ","
                        + point.z + "],");
            }
        }

        System.out.println("], faces = [");

        for (int i = 0; i < upoints - 1; i++) {
            for (int j = 0; j < vpoints; j++) {
                int a = i * vpoints + j;
                int b = ((i + 1) % upoints) * vpoints + j;
                int c = i * vpoints + (j + 1) % vpoints;
                int d = ((i + 1) % upoints) * vpoints + (j + 1) % vpoints;

                System.out.println("[" + a + "," + b + "," + c + "],"
                        + "[" + b + "," + d + "," + c + "],");
            }
        }

        System.out.println("]);");

    }
    
    
    private static void round(Point[][] points){
        for(Point[] a: points){
            for(Point b: a){
                b.x = round(b.x);
                b.y = round(b.y);
                b.z = round(b.z);
            }
        }
    }
    
    private static double round(double n){
        return Math.round(100000*n)/100000.0;
    }

    private static void print2(Point[][] points, Point[][] points2, boolean swap) {
        round(points);
        round(points2);
        int upoints = points.length;
        int vpoints = points[0].length;

        int offset = upoints * vpoints;

        System.out.println("polyhedron( points = [\n");
        for (int i = 0; i < upoints; i++) {
            for (int j = 0; j < vpoints; j++) {
                Point point = points[i][j];
                System.out.println("[" + point.x + "," + point.y + ","
                        + point.z + "],");
            }
        }
        for (int i = 0; i < upoints; i++) {
            for (int j = 0; j < vpoints; j++) {
                Point point = points2[i][j];
                System.out.println("[" + point.x + "," + point.y + ","
                        + point.z + "],");
            }
        }

        System.out.println("], faces = [");

        for (int i = 0; i < upoints - 1; i++) {
            for (int j = 0; j < vpoints; j++) {
                int a = i * vpoints + j;
                int b = ((i + 1) % upoints) * vpoints + j;
                int c = i * vpoints + (j + 1) % vpoints;
                int d = ((i + 1) % upoints) * vpoints + (j + 1) % vpoints;
                
                if(swap){
                    int temp = b;
                    b = c;
                    c = temp;
                }

                System.out.println("[" + a + "," + b + "," + c + "],[" + b
                        + "," + d + "," + c + "],");
            }
        }

        for (int i = 0; i < upoints - 1; i++) {
            for (int j = 0; j < vpoints; j++) {
                int a = i * vpoints + j + offset;
                int b = ((i + 1) % upoints) * vpoints + j + offset;
                int c = i * vpoints + (j + 1) % vpoints + offset;
                int d = ((i + 1) % upoints) * vpoints + (j + 1) % vpoints
                        + offset;

                if(swap){
                    int temp = b;
                    b = c;
                    c = temp;
                }
                
                System.out.println("[" + a + "," + c + "," + b + "],[" + b
                        + "," + c + "," + d + "],");
            }
        }

        // now for the hard part, the caps

        for (int j = 0; j < vpoints; j++) {
            int i = 0;
            int a = i * vpoints + j;
            int b = i * vpoints + j + offset;
            int c = i * vpoints + (j + 1) % vpoints;
            int d = i * vpoints + (j + 1) % vpoints + offset;

            System.out.println("[" + a + "," + c + "," + b + "],[" + b
                    + "," + c + "," + d + "],");

        }

        for (int j = 0; j < vpoints; j++) {
            int i = upoints - 1;
            int a = i * vpoints + j;
            int b = i * vpoints + j + offset;
            int c = i * vpoints + (j + 1) % vpoints;
            int d = i * vpoints + (j + 1) % vpoints + offset;

            System.out.println("[" + a + "," + b + "," + c + "]," + "["
                    + b + "," + d + "," + c + "],");

        }

        System.out.println("]);");

    }

    // bunch of cubes
    private static void quickprint(Point[][] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[0].length; j++) {
                Point point = points[i][j];
                System.out.println("translate([" + point.x + "," + point.y
                        + "," + point.z + "])cube([.1,.1,.1]);");
            }
        }
    }

    private static Point[][] points(Bezier template) {
        int upoints = unum;
        int vpoints = vnum;

        Point[] centers = template.render(upoints + 1);
        return getNet(centers, upoints, vpoints);

    }

    private static Point[][] shrunkPoints(Bezier template,
            double thickness2) {
        int upoints = unum;
        int vpoints = vnum;

        Point[] tempCenters = template.render(upoints + 1);
        Point[] centers = shrink(tempCenters, thickness2);
        return getNet(centers, upoints, vpoints);

    }

    private static Point[][] getNet(Point[] centers, int upoints,
            int vpoints) {
        Point[][] toReturn = new Point[upoints][vpoints];
        // upoints should be one more than centers.length

        for (int u = 0; u < upoints; u++) {
            Point center = centers[u];
            Point next = centers[u + 1];
            for (int v = 0; v < vpoints; v++) {
                double vtheta = TwoPI * v / vpoints; // around the circle
                // System.out.println(vtheta);
                double angle = Math.atan2(next.y - center.y, next.x
                        - center.x); // from center to next
                double r = center.z;

                double x = center.x - r * Math.sin(vtheta)
                        * Math.sin(angle);
                double y = center.y + r * Math.sin(vtheta)
                        * Math.cos(angle);
                double z = r * Math.cos(vtheta);

                toReturn[u][v] = new Point(x, y, z);

            }
            // Bezier.wait(100000);
        }

        return toReturn;
    }

    private static void equation1(double u, double v) {
        int cases = 3;
        int uCase = (int) (u / TwoPI / cases);
        // int uvCase = (int) (v/TwoPI/cases);

        u = (cases * u) % TwoPI;
        // v=(cases*v)%TwoPI;

        switch (uCase) {
        case 0:

        }

    }

    public static Point[] shrink(Point[] points, double offset) {
        Point[] toReturn = new Point[points.length];

        double r = 0;
        for (int i = 0; i < points.length - 1; i++) {
            Point[] A = to2D(points[i], points[i + 1]);
            Point[] B = to2D(points[i + 1], points[i]);
            // left and right are reversed for B (?)
            Point[] C = new Point[2];

            double theta = Math.atan2(B[1].y - A[0].y, B[1].x - A[0].x);
            double dx = offset * Math.cos(theta + Math.PI / 2);
            double dy = offset * Math.sin(theta + Math.PI / 2);
            C[0] = new Point(A[0].x + dx, A[0].y + dy, 0);

            theta = Math.atan2(B[0].y - A[1].y, B[0].x - A[1].x);
            dx = offset * Math.cos(theta - Math.PI / 2);
            dy = offset * Math.sin(theta - Math.PI / 2);
            C[1] = new Point(A[1].x + dx, A[1].y + dy, 0);

            r = C[0].dist(C[1]) / 2.0;

            toReturn[i] = new Point((C[0].x + C[1].x) / 2.0,
                    (C[0].y + C[1].y) / 2.0, r);

        }

        // TODO don't do this
        Point nearLast = toReturn[toReturn.length - 2];
        toReturn[toReturn.length - 1] = new Point(2
                * toReturn[toReturn.length - 2].x
                - toReturn[toReturn.length - 3].x, nearLast.y, r);

        return toReturn;
    }

    public static Point[] to2D(Point a, Point b) {
        Point[] toReturn = new Point[2];

        double theta = Math.atan2(b.y - a.y, b.x - a.x) + Math.PI / 2;
        double xout = (a.z) * Math.cos(theta);
        double yout = (a.z) * Math.sin(theta);

        toReturn[0] = new Point(a.x - xout, a.y - yout, 0);
        toReturn[1] = new Point(a.x + xout, a.y + yout, 0);
        return toReturn;

    }

    static Point cross(Point a, Point b, Point c) {
        double u1 = b.x - a.x;
        double u2 = b.y - a.y;
        double u3 = b.z - a.z;

        double v1 = c.x - a.x;
        double v2 = c.y - a.y;
        double v3 = c.z - a.z;

        return new Point(u2 * v3 - u3 * v2, u3 * v1 - u1 * v3, u1 * v2
                - u2 * v1);
    }

}
