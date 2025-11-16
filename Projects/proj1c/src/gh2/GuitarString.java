package gh2;

// TODO: maybe more imports
import deque.*;
import java.lang.Math;

//Note: This file will not compile until you complete the Deque61B implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
     private Deque61B<Double> buffer;
     private final int capacity;

    /* Create `a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        this.capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayDeque61B<>(capacity);
        for(int i = 0; i < capacity; i++){
            buffer.addFirst(.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        while (!buffer.isEmpty()) {
            buffer.removeFirst();
        }
        for(int i = 0; i < this.capacity; i++){
            double r = Math.random() - 0.5;
            buffer.addFirst(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double firstEle = buffer.removeFirst();
        if (buffer.isEmpty()){
            return;
        }

        double nextEle = buffer.get(0);
        double newAdded = (firstEle + nextEle) * DECAY / 2;
        buffer.addLast(newAdded);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        if (buffer.isEmpty()){
            return 0.0;
        }
        return buffer.get(0);
    }
}
