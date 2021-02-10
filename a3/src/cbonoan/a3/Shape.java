package cbonoan.a3;
import static java.lang.Math.PI;

/**
 * Shape class is the base class for all other Shape Types
 * @author Charles Bonoan
 * @version 1.0
 */
public class Shape {
    private Type type;

    public double getArea() { return 0; } //Need this function in order to use getArea() function in child classes
    public Type getType() { return this.type; }
    public void setType(Type type) { this.type = type; }
    public double pi = PI;

    @Override
    public String toString() {
        return String.format("Type: %-12s", this.type);
    }
}
