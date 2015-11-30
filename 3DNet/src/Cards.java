public class Cards {
    static final int DECK_SIZE = 32;

    public static void main(String args) {
        boolean[] taken = new boolean[32]; // whether this arrangement is taken
        boolean[] deck = new boolean[DECK_SIZE]; // whether this card is taken
        int[] cards = new int[DECK_SIZE];
        int current = 0;
        int found = 0;
        while (found < 52) {

            // check next 5
            int num = bin5(cards, current);
            if (current < 5 || taken[num]) {
                // Nice! moving forward
                found++;
                current++;
                taken[num]=true;
            } else {
                // next arrangement
                deck[cards[current]] = false;
                do {
                    cards[current]++;

                    if (cards[current] > DECK_SIZE) {
                        // move back :(
                        found--;
                        System.out.println("Stepping back at " + current);
                        cards[current] = 0;
                        current--;
                        deck[cards[current]] = false;
                        cards[current]++;

                        if (current < 0) {
                            System.out.println("NO SOLUTIONS");
                            System.exit(0);
                        }
                    }

                } while (!deck[cards[current]]);

                deck[cards[current]] = true;
            }

        }
        
        
        print(cards);
    }

    private static void print(int[] cards) {
        for(int i: cards){
            System.out.println(i);
        }
        
    }

    // returns true if this is a new sequence
    // just swap out this method for different stuff
    private static int bin5(int[] cards, int current) {
        return 16 * (cards[current] % 2) + 8 * (cards[current - 1] % 2)
                + 4 * (cards[current - 2] % 2) + 2
                * (cards[current - 3] % 2) + 1 * (cards[current - 4] % 2);
    }

}
