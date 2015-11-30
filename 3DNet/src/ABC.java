public class ABC implements Comparable<ABC> {
    int a;
    int b;
    double valueC;

    ABC(int a1, int b1, double dist2) {
        a = a1;
        b = b1;
        valueC = dist2;
    }

    @Override
    public int compareTo(ABC arg0) {
        //backwards
        if (valueC < arg0.valueC) {
            return -1;
        } else if (valueC > arg0.valueC) {
            return 1;
        }
        return 0;
    }

}
