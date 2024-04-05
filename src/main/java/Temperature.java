import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Temperature {
    private static final int num_temp_sensors = 8;
    private static final int minutes = 60;
    private static final int hours = 24;
    private static final CyclicBarrier barrier = new CyclicBarrier(num_temp_sensors);
    public static ArrayList<Integer> readings = new ArrayList<>();
    //returns the largest diff for the hour slot and the starting minute of the 10 min window.
    public static int[] getDifference(){
        int window = 10;
        int maxDif = Integer.MIN_VALUE;
        int startingMin = 0;

        for(int i = 0; i < num_temp_sensors; i++){
            for(int j = i * minutes; j < minutes - window + 1; j++){
                int max = Collections.max(readings.subList(j, j + window));
                int min = Collections.min(readings.subList(j, j + window));
                int diff = max - min;
                if(diff > maxDif){
                    maxDif = diff;
                    startingMin = j;
                }
            }
        }
        return new int[]{maxDif, startingMin};
    }
    //returns either the top 5 highest or lowest temperatures
    public static List<Integer> top5temps(boolean isReversed){
        List<Integer> sortReading = new ArrayList<>(readings);
        if(isReversed)
            sortReading.sort(Collections.reverseOrder());
        else
            Collections.sort(sortReading);

        ArrayList<Integer> five = new ArrayList<>();
        for (Integer integer : sortReading) {
            if (five.contains(integer)) continue;

            five.add(integer);
            if (five.size() == 5) break;
        }
        return five;
    }
    //writes the report
    public static void report(int hour){
        System.out.println("Hour " + hour);

        List<Integer> top5 = top5temps(true);
        List<Integer> bot5 = top5temps(false);
        int[] diff = getDifference();
        int largestDiff = diff[0];
        int startMin = diff[1];

        System.out.println("Top 5 temperatures (no repeats): " + top5);
        System.out.println("Bottom 5 temperatures (no repeats): " + bot5);
        System.out.println("The largest difference was " + largestDiff + "F between " + (hour - 1) + ":" + startMin + " and " + (hour - 1) + ":" + (startMin + 10));
        System.out.println();
    }
    //gets the temperature readings
    public static void getTemps(int threadIdx){
        Random random = new Random();
        for(int i = 0; i < hours; i++){
            for(int j = 0; j < minutes; j++){
                int temp = random.nextInt(-100, 71);
                readings.set(j + (threadIdx * minutes), temp);
            }
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            if(threadIdx == 0){
                report(i + 1);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {

        Thread[] sensors = new Thread[num_temp_sensors];
        for(int i = 0; i < num_temp_sensors * minutes; i++){
            readings.add(-101);
        }
        for(int i = 0; i < num_temp_sensors; i++){
            int index = i;
            sensors[i] = new Thread(() -> getTemps(index));
        }

        for (Thread thread : sensors) {
            thread.start();
        }

        for (Thread thread : sensors) {
            thread.join();
        }


    }
}
