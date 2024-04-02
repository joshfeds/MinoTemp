import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseList {
    public Node head;

    private Lock lock = new ReentrantLock();
    public CoarseList() {
        head = null;
    }

    public void add(int data) {
        lock.lock();
        try {
            Node newNode = new Node(data);
            if (head == null) {
                head = newNode;
            } else {
                Node temp = head;
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = newNode;
            }
        } finally {
            lock.unlock();
        }
    }

    public void delete(int data) {
        lock.lock();
        try {
            if (head == null) {
                return;
            }
            if (head.data == data) {
                head = head.next;
                return;
            }
            Node prev = null;
            Node current = head;
            while (current != null && current.data != data) {
                prev = current;
                current = current.next;
            }
            if (current != null) {
                prev.next = current.next;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean contains(int data) {
        lock.lock();
        try {
            Node temp = head;
            while (temp != null) {
                if (temp.data == data) {
                    return true;
                }
                temp = temp.next;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
