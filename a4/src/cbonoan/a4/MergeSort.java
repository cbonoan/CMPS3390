package cbonoan.a4;

import cbonoan.a3.Shape;

/**
 * MergeSort class that extends Thread
 * @author Charles Bonoan
 * @version 1.0
 */
public class MergeSort extends Thread{
    private Shape[] shapes1;
    private Shape[] shapes2;
    private Shape[] sortedShapes;

    public MergeSort(Shape[] s1, Shape[] s2) {
        this.shapes1 = s1;
        this.shapes2 = s2;
        this.sortedShapes = new Shape[s1.length+s2.length];
    }

    /**
     * When MergeSort thread is ran, it will combine two sorted arrays into one sorted array
     */
    @Override
    public void run() {
        System.out.println("Merge Thread Started");
        int i = 0, j = 0, k = 0; // Current index of shapes1, shapes2, sortedShapes respectively

        while(i < shapes1.length && j < shapes2.length) {
            if(this.shapes1[i].getArea() < this.shapes2[j].getArea()) {
                this.sortedShapes[k++] = this.shapes1[i++];
            } else {
                this.sortedShapes[k++] = this.shapes2[j++];
            }
        }

        while(i < this.shapes1.length) {
            this.sortedShapes[k++] = this.shapes1[i++];
        }
        while(j < this.shapes2.length) {
            this.sortedShapes[k++] = this.shapes2[j++];
        }

        System.out.println("Merge Thread Complete");
    }

    public Shape[] getSortedShapes() {
        return sortedShapes;
    }
}
