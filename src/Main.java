public class Main {
    public static void main(String[] args) {
        Knapsack k = new Knapsack(15, new Integer[]{5, 23, 6}, new Integer[]{9, 12, 6}, 10000);
        k.findBest();
    }
}
