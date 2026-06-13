package com.city.detective.test_code;
import java.util.LinkedList;

public class Test_to_delete {
    public static void main(String[] args) {
        // Declare as LinkedList to keep index access
        LinkedList<String> fifoQueue = new LinkedList<>();

        // FIFO Insertions (Tail)
        fifoQueue.add("First");
        fifoQueue.add("Second");
        fifoQueue.add("Third");

        // Access by Index (0-based)
        String secondElement = fifoQueue.get(0);
        System.out.println("Element at index 1: " + secondElement); // Prints "Second"

        // FIFO Removals (Head)
        System.out.println("Removed: " + fifoQueue.poll()); // Prints "First"

        // Access by Index (0-based)
        secondElement = fifoQueue.get(0);
        System.out.println("Element at index 1: " + secondElement); // Prints "Second"

        // FIFO Removals (Head)
        System.out.println("Removed: " + fifoQueue.poll()); // Prints "First"

        // Access by Index (0-based)
        secondElement = fifoQueue.get(0);
        System.out.println("Element at index 1: " + secondElement); // Prints "Second"

        // FIFO Removals (Head)
        System.out.println("Removed: " + fifoQueue.poll()); // Prints "First"
    }
}