package cbonoan.a3;
import java.util.Scanner;
import java.util.Random;

/**
 * Main driver class for a3
 * @author Charles Bonoan
 * @version 1.0
 */
public class Main {

    /**
     * Main entry point for assignment
     * @param args
     */
    public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

	    //Ask user how many shapes to generate
	    int numShapes;
	    System.out.println("Number of shapes to generate: ");
	    numShapes = scanner.nextInt();

	    //Loop numShapes times and create a ne Shape object for each iteration
	    String[] shapeNames = {"Circle", "Oval", "Square", "Rectangle", "RightTriangle"};
	    Shape[] shapes = new Shape[numShapes];
	    String shape;
	    int randNum; //Determine randomly which shape to generate
        //min and max to be used to nextFloat() function to
        //determine dimensions of shape being generated
        float min = 0.0f;
        float max = 20.0f;
	    for(int i=0; i<numShapes; i++) {
	        randNum = rand.nextInt(shapeNames.length);
	        shape = shapeNames[randNum];

            float radius = rand.nextFloat() * (max - min) + min;
            float radius2 = rand.nextFloat() * (max - min) + min;

            float width = rand.nextFloat() * (max - min) + min;
            float height = rand.nextFloat() * (max - min) + min;

	        switch (shape) {
                case "Circle":
                    shapes[i] = new Circle(radius);
                    break;
                case "Oval":
                    shapes[i] = new Oval(radius, radius2);
                    break;
                case "Square":
                    shapes[i] = new Square(width);
                    break;
                case "Rectangle":
                    shapes[i] = new Rectangle(width, height);
                    break;
                case "RightTriangle":
                    shapes[i] = new RightTriangle(width, height);
                    break;
            }
            System.out.println(shapes[i].toString());
        }
    }
}
