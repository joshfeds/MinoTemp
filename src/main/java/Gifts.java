
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Gifts {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final int num_servants = 4;
    private static final int num_presents = 500000;
    private static int num_cards = 0;
    private static final ArrayList<Integer> bag = new ArrayList<>();
    private static final CoarseList list = new CoarseList();

    public static void doTask() {
        PrintWriter writer = new PrintWriter(System.out);
        while(num_cards < num_presents){
            Random random = new Random();
            int randomValue = random.nextInt(3);

            //adds to linked list
            if(randomValue == 0){
                lock.lock();
                if(!bag.isEmpty()){
                    int temp = bag.removeFirst();
                    writer.println("Added " + temp + " to the list");
                    list.add(temp);
                }
                lock.unlock();

            }
            //writes thank you and removes present from chain
            else if(randomValue == 1) {
                lock.lock();
                if(list.head == null){
                    lock.unlock();
                    continue;
                }
                writer.println("We have written a thank you card for present id " + list.head.data);
                list.delete(list.head.data);
                num_cards++;
                lock.unlock();

            }
            //checks if a given gift is present in the chain
            else{
                lock.lock();
                randomValue = random.nextInt(num_presents);
                if(list.contains(randomValue)){
                    writer.println("We have found " + randomValue);

                }
                else {
                    writer.println("We have not found " + randomValue);

                }

                lock.unlock();

            }



        }

    }
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < num_presents; i++){
            bag.add(i);
        }

        Collections.shuffle(bag);
        Thread[] servants = new Thread[num_servants];
        for(int i = 0; i < num_servants; i++){
            servants[i] = new Thread(Gifts::doTask);
        }
        long start = System.currentTimeMillis();

        for (Thread thread : servants) {
            thread.start();
        }

        for (Thread thread : servants) {
            thread.join();
        }

        long end = System.currentTimeMillis();
        long duration = (end - start) / 1000;
        System.out.println();
        System.out.println("Finished in " + duration + " seconds");

        System.out.println("Number of cards written: " + num_cards);
    }
}
