package cbonoan.a4;

import cbonoan.a3.Shape;

/**
 * ThreadSort class that extends Thread
 * @author Charles Bonoan
 * @version 1.0
 */
public class ThreadSort extends Thread{
    private Shape[] threadShapes;

    public ThreadSort(Shape[] shapes, int lowerBounds, int upperBounds) {
        this.threadShapes = new Shape[upperBounds-lowerBounds];

        System.arraycopy(shapes, lowerBounds, this.threadShapes, 0, (upperBounds-lowerBounds));
    }

    /**
     * When ThreadSort class is ran, it will take in an unsorted Shape array that will be sorted according
     * to their area using bubble sort.
     */
    @Override
    public void run() {
        System.out.println("Thread started");

        int n = this.threadShapes.length;
        Shape tmp;
        for(int i=0; i<n; i++) {
            for(int j=1; j< n-i; j++) {
                if(this.threadShapes[j-1].getArea() > this.threadShapes[j].getArea()) {
                    tmp = this.threadShapes[j-1];
                    this.threadShapes[j-1] = this.threadShapes[j];
                    this.threadShapes[j] = tmp;
                }
            }
        }

        System.out.println("Thread complete");
    }

    public Shape[] getThreadShapes() {
        return threadShapes;
    }

}
