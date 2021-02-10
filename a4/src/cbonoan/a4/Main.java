package cbonoan.a4;

import cbonoan.a3.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Main driver class for a4
 * @author Charles Bonoan
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        System.out.print("Do you want a [S]ingle Thread, [D]ual Thread, or [N]-threads: ");
        char selection = scanner.next().charAt(0);

        System.out.print("How many shapes to sort? ");
        int numShapes = scanner.nextInt();
        Shape[] shapes = new Shape[numShapes];

        float min = 5.0f;
        float max = 20.0f;
        float radius, radius2, width, height;
        for(int i=0; i<numShapes; i++) {
            int t = rand.nextInt(5);
            switch (t) {
                case 0:
                    radius = rand.nextFloat() * (max - min) + min;
                    shapes[i] = new Circle(radius);
                    break;
                case 1:
                    radius = rand.nextFloat() * (max - min) + min;
                    radius2 = rand.nextFloat() * (max - min) + min;
                    shapes[i] = new Oval(radius, radius2);
                    break;
                case 2:
                    width = rand.nextFloat() * (max - min) + min;
                    shapes[i] = new Square(width);
                    break;
                case 3:
                    width = rand.nextFloat() * (max - min) + min;
                    height = rand.nextFloat() * (max - min) + min;
                    shapes[i] = new Rectangle(width, height);
                    break;
                case 4:
                    width = rand.nextFloat() * (max - min) + min;
                    height = rand.nextFloat() * (max - min) + min;
                    shapes[i] = new RightTriangle(width, height);
                    break;
            }
        }


        char convertedSelection = Character.toLowerCase(selection);
        switch (convertedSelection) {
            case 's':
                singleSort(shapes);
                break;
            case 'd':
                dualSort(shapes);
                break;
            case 'n':
                System.out.print("How many threads to use? ");
                int threads = scanner.nextInt();
                while(threads > numShapes) {
                    System.out.print("Please enter a number that is less than or equal to the number of shapes generated: ");
                    threads = scanner.nextInt();
                }
                nSort(shapes, threads);
                break;
        }
    }

    /**
     * Sorts an array of shapes by their array using one thread
     * @param shapes
     * @throws InterruptedException
     */
    private static void singleSort(Shape[] shapes) throws InterruptedException {
        ThreadSort threadSort = new ThreadSort(shapes, 0, shapes.length);

        long startTime = System.nanoTime();
        threadSort.start();
        threadSort.join();
        long endTime = System.nanoTime();

        long duration = (endTime-startTime) / 1000000;

        for(Shape s : threadSort.getThreadShapes()) {
            System.out.println(s);
        }
        System.out.println("Single Thread Sort Time: " + duration);
    }

    /**
     * Sorts an array of shapes using two threads
     * @param shapes
     * @throws InterruptedException
     */
    private static void dualSort(Shape[] shapes) throws InterruptedException {
        int mid = Math.round(shapes.length/2);

        ThreadSort t1 = new ThreadSort(shapes, 0, mid);
        ThreadSort t2 = new ThreadSort(shapes, mid, shapes.length);

        long startTime = System.nanoTime();
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        MergeSort m = new MergeSort(t1.getThreadShapes(), t2.getThreadShapes());
        m.start();
        m.join();
        long endTime = System.nanoTime();

        for(Shape s : m.getSortedShapes()) {
            System.out.println(s);
        }

        long duration = (endTime-startTime) / 1000000;
        System.out.println("Dual Thread Sort Time: " + duration);
    }

    /**
     * Sorting an array of shapes using n number of threads that is defined by the user
     * @param shapes
     * @param threads represents the number of threads the user wants
     * @throws InterruptedException
     */
    private static void nSort(Shape[] shapes, int threads) throws InterruptedException {
        if(threads == 1) singleSort(shapes);
        else if(threads == 2) dualSort(shapes);
        else {
            int elems = Math.round(shapes.length / threads); // Determines how many elements will be dedicated to each thread

            ThreadSort[] t = new ThreadSort[threads];
            int j = 0; //Keep track of how many elements we have passed over in loop
            int threadNum = 0, lowerBound = 0, upperBound;

            long startTime = System.nanoTime();
            while (threadNum < threads) { // While ThreadSort array is not full, keep looping
                if(threadNum+1 == threads) { // If on last thread, pass in remaining Shape array
                    upperBound = shapes.length;
                    t[threadNum] = new ThreadSort(shapes, lowerBound, upperBound);
                    t[threadNum].start();
                    t[threadNum].join();
                    threadNum++;
                } else if (j == elems) { // Once we've looped "elems" times, we create a new ThreadSort
                    upperBound = lowerBound + j;
                    t[threadNum] = new ThreadSort(shapes, lowerBound, upperBound);
                    t[threadNum].start();
                    t[threadNum].join();
                    threadNum++;
                    lowerBound = upperBound;
                    j = 1;
                } else {
                    j++;
                }
            }

            MergeSort mergeSort = new MergeSort(t[0].getThreadShapes(), t[1].getThreadShapes()); //Merge the first two ThreadSort arrays
            mergeSort.start();
            mergeSort.join();
            Shape[] res = mergeSort.getSortedShapes();
            for(int i=2; i<t.length; i++) {
                mergeSort = new MergeSort(res, t[i].getThreadShapes());
                mergeSort.start();
                mergeSort.join();

                res = mergeSort.getSortedShapes();
            }
            long endTime = System.nanoTime();

            for(Shape s : mergeSort.getSortedShapes()) {
                System.out.println(s);
            }

            long duration = (endTime-startTime) / 1000000;
            System.out.println("N Thread Sort Time: " + duration);
        }
    }

}
