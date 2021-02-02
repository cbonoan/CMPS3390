package cbonoan.a3;

/**
 * Oval class that extends Circle class
 * @author Charles Bonoan
 * @version 1.0
 */
public class Oval extends Circle{
    private float radius2;

    //Constructors
    public Oval() {
        super();
        this.setType(Type.OVAL);
        this.radius2 = 0;
    }
    public Oval(float radius, float radius2) {
        this.setType(Type.OVAL);
        this.setRadius(radius);
        this.radius2 = radius2;
    }

    //Getter and setter functions
    public float getRadius2() { return this.radius2; }
    public void setRadius2(float radius2) { this.radius2 = radius2; }
    @Override
    public double getArea() { return (pi*this.getRadius()*this.radius2); }
    @Override
    public String toString() {
        return String.format("Type: %-12s| Area %-4.2f| Radius: %-4.2f| Radius2: %-4.2f",
                this.getType(), this.getArea(), this.getRadius(), this.radius2);
    }
}
