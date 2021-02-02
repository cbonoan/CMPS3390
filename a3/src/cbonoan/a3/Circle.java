package cbonoan.a3;

/**
 * Circle extends Shape and represetns a perfect circle
 */
public class Circle extends Shape{
    private float radius;

    public Circle() {
        super();
        this.setType(Type.CIRCLE);
        this.radius = 0;
    }
    public Circle(float radius) {
        this.setType(Type.CIRCLE);
        this.radius = radius;
        this.setType(Type.CIRCLE);
    }

    public float getRadius() { return this.radius; }
    public void setRadius(float radius) { this.radius = radius; }
    public double getArea() { return (pi*radius*radius); }

    @Override
    public String toString(){
        return String.format("Type: %-12s| Area %-4.2f| Radius: %-4.2f",
                            this.getType(), this.getArea(), this.radius);
    }
}
