public class Bezier {
    private Point[] points;
    int length;

    Bezier(Point[] points1) {
        points = points1;
        length = points.length;
    }

    static Point interpolate(Point a, Point b, double ratio) {
        return new Point(interpolate2(a.x, b.x, ratio), interpolate2(a.y,
                b.y, ratio), interpolate2(a.z, b.z, ratio));
    }

    static double interpolate2(double a, double b, double ratio) {
        return a + (b - a) * ratio;
    }

    public Point getPoint(double ratio) {
        Point[] sublist = points;
        for (int i = length - 1; i >= 1; i--) {
            Point[] sublist2 = new Point[i];
            for (int j = 0; j < i; j++) {
                // System.out.println(i+"   "+j);
                sublist2[j] = interpolate(sublist[j], sublist[j + 1],
                        ratio);
            }
            sublist = sublist2;
        }
        return sublist[0];
    }

    public void draw() {
        double xoff = -4;
        StdDraw.setPenRadius(.005);
        double step = .002;
        for (double i = 0; i < 1; i += step) {

            StdDraw.setPenRadius(.01);
            Point a = getPoint(i);
            Point b = getPoint(i + step);
            // StdDraw.line(a.x-xoff, a.y, b.x-xoff, b.y);
            double theta = Math.atan2(b.y - a.y, b.x - a.x) + Math.PI / 2;
            double xout = a.z * Math.cos(theta);
            double yout = a.z * Math.sin(theta);

            StdDraw.setPenColor(255, 0, 0);

            // StdDraw.line(a.x-xout-xoff, a.y-yout, a.x+xout-xoff, a.y+yout);
            StdDraw.point(a.x - xout - xoff, a.y - yout);
            StdDraw.point(a.x + xout - xoff, a.y + yout);

            StdDraw.setPenRadius(.005);
            StdDraw.setPenColor(0, 0, 0);
            StdDraw.point(a.x - xoff, a.y);
            // wait(10);
        }

        StdDraw.setPenColor(0, 0, 255);
        StdDraw.setPenRadius(.002);
        for (int i = 0; i < length - 1; i++) {

            StdDraw.setPenColor(0, 255, 0);
            StdDraw.line(points[i].x - xoff, points[i].y, points[i + 1].x
                    - xoff, points[i + 1].y);

            StdDraw.setPenColor(0, 0, 255);
            StdDraw.point(points[i].x - xoff, points[i].y);
        }

    }

    public void draw2() {
        double radiusOffset = .2;
        double xoff = -4;
        StdDraw.setPenRadius(.005);
        double step = .002;
        for (double i = 0; i < 1; i += step) {

            StdDraw.setPenRadius(.01);
            Point a = getPoint(i);
            Point b = getPoint(i + step);
            // StdDraw.line(a.x-xoff, a.y, b.x-xoff, b.y);
            double theta = Math.atan2(b.y - a.y, b.x - a.x) + Math.PI / 2;
            double xout = (a.z - radiusOffset) * Math.cos(theta);
            double yout = (a.z - radiusOffset) * Math.sin(theta);

            StdDraw.setPenColor(255, 0, 0);

            // StdDraw.line(a.x-xout-xoff, a.y-yout, a.x+xout-xoff, a.y+yout);
            StdDraw.point(a.x - xout - xoff, a.y - yout);
            StdDraw.point(a.x + xout - xoff, a.y + yout);

            StdDraw.setPenRadius(.005);
            StdDraw.setPenColor(0, 0, 0);
            StdDraw.point(a.x - xoff, a.y);
            // wait(10);
        }

        StdDraw.setPenColor(0, 0, 255);
        StdDraw.setPenRadius(.002);
        for (int i = 0; i < length - 1; i++) {

            StdDraw.setPenColor(0, 255, 0);
            StdDraw.line(points[i].x - xoff, points[i].y, points[i + 1].x
                    - xoff, points[i + 1].y);

            StdDraw.setPenColor(0, 0, 255);
            StdDraw.point(points[i].x - xoff, points[i].y);
        }

    }

    public static void main(String[] args) {

        /**
         * Point[] listA = { new Point(7.5,3.5,.2), new Point(6,2,.2),
         * 
         * 
         * 
         * new Point(5,1,.4), new Point(5,1,.7),
         * 
         * new Point(5,0,1), new Point(5,0,1.5), new Point(5,0,2)};
         * 
         * Point[] listB = {
         * 
         * 
         * new Point(5,0,2),
         * 
         * new Point(5,0,2.5), //new Point(5,.5,3),
         * 
         * new Point(5,1,4), //new Point(5,1,3),
         * 
         * 
         * new Point(5,3,2), new Point(3,6,1.7), new Point(4,8,1.5), new
         * Point(8,8,1.1), // new Point(9,7,.8), new Point(9,5,.5), //new
         * Point(10,3,0), new Point(8,4,.2),
         * 
         * 
         * new Point(7.5,3.5,.2)};
         **/

        Point[] listA = { new Point(0, 0, 2), new Point(0, 0, 3),
                new Point(0, 1, 4), new Point(0, 2, 4),
                new Point(0, 4, 2), new Point(-3, 5, 1),
                new Point(-3, 6, 1), new Point(-3, 8, 1),
                new Point(-1, 8, .8), new Point(0, 8, .8), };

        Point[] listB = { new Point(0, 0, 1.99), new Point(0, 0, 1.5),
                new Point(0, 1, 1.2), new Point(0, 2, 1),
                new Point(0, 4, .9), new Point(3, 5, .8),
                new Point(3, 6, .7), new Point(3, 8, .6),
                new Point(1, 8, .8), new Point(0, 8.01, .8), };

        Bezier test = new Bezier(listA);

        Bezier testB = new Bezier(listB);
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        test.draw();
        // test.draw2();
        testB.draw();

    }

    // each point represents x, y, r
    public Point[] render(int num) {
        double step = (1.0 / (num - 2));
        Point[] toReturn = new Point[num];
        int count = 0;
        // TODO get rid of the plus step (if I didn't already)
        double ratio = 0;
        for (count = 0; count < toReturn.length; count++) {
            toReturn[count] = getPoint(ratio);
            ratio += step;
        }

        return toReturn;
    }

    public Point[] renderSpecial(int num, double offset) {
        double step = (1.0 / (num - 2));
        Point[] toReturn = new Point[num];
        int count = 0;
        // TODO get rid of the plus step (if I didn't already)
        double ratio = 0;
        for (count = 0; count < toReturn.length; count++) {
            toReturn[count] = getPoint(ratio);
            if (count % 2 == 1) {
                ratio += step * (1 + offset);
            } else {
                ratio += step * (1 - offset);
            }
        }

        return toReturn;
        // ratio+=step;
    }

    public Point[] renderSpecial2(int num, double offset) {
        double step = (1.0 / (num - 2));
        Point[] toReturn = new Point[num];
        int count = 0;
        // TODO get rid of the plus step (if I didn't already)
        double ratio = 0;
        for (count = 0; count < toReturn.length; count++) {
            toReturn[count] = getPoint(ratio);
            if (count % 2 == 0) {
                ratio += step * (1 + offset);
            } else {
                ratio += step * (1 - offset);
            }
            // ratio+=step;
        }

        return toReturn;
    }

    public static void wait(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
