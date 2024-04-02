
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Gifts {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final int num_servants = 4;
    private static final int num_presents = 50;
    private static int num_cards = 0;
    private static final ArrayList<Integer> bag = new ArrayList<>();
    private static final CoarseList list = new CoarseList();

    public static void doTask() {
        while(num_cards < num_presents){
            Random random = new Random();
            int randomValue = random.nextInt(3);

            //adds to linked list
            if(randomValue == 0){
                lock.lock();
                if(!bag.isEmpty()){
                    int temp = bag.removeFirst();
                    System.out.println("Added " + temp + " to the list");
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
                list.delete(list.head.data);
                num_cards++;
                lock.unlock();

            }
            //checks if a given gift is present in the chain
            else{
                lock.lock();
                randomValue = random.nextInt(num_presents);
                if(list.contains(randomValue)){
                    System.out.println("We have found " + randomValue);
                }
                else {
                    System.out.println("We have not found " + randomValue);
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

        for (Thread thread : servants) {
            thread.start();
        }

        for (Thread thread : servants) {
            thread.join();
        }

        System.out.println(num_cards);
    }
}
