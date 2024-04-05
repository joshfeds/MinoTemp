Part 1: Birthday Party

To implement this assignment, I made use of a ReentrantLock and the CoarseList class. Everytime a thread made a decision on which task to complete, I locked the thread until it had completed its task. With the CoarseList, I also locked a given linked list operation to ensure that only one task was being done at a time. While it may seem unneccesary to lock both classes, I found myself running into errors of certain variables being null when they shouldn't due to threads doing conflicting actions at the same time. Another issue I ran into during this assignment was locks not unlocking properly. There were certain conditions (such as checking if the list head was null) that I hadn't considered unlocking for that resulted in the project freezing up. In the context of this assignment, the error that could have happened that led to too many letters was not locking each action that a servant could do. I also found myself having either too many or too few cards being written before I decided to lock each action that a thread could do. All operations either ran in O(1) or O(n) runtime.

Part 2: Temperatures

To implement this problem, I made use of a CyclicBarrier and an integer ArrayList. Once a thread is done capturing temperatures, the CyclicBarrier spins the thread until all eight threads have reached the barrier. Once this happens, the first thread (thread index 0) calls the report function, where it prints the requested temperature data. The ArrayList stores the temperature recordings for each thread. Each thread has their own designated part of the list, thus sharing memory without the need to access outside of its own. Originally, I used a while loop that would spin until all threads were done adding to the list. While this worked, there was some slight improvements in time when switching to CyclicBarrier. A lock was not necessary for this assignment due to the quick nature of gathering data. A given thread only waits a few milliseconds until all threads are done adding data.
