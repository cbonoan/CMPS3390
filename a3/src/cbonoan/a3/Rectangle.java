package cbonoan.a3;

/**
 * Rectangle class represents a rectangle where width and height are different
 */
public class Rectangle extends Square{
    private float height;

    public Rectangle() {
        super();
        this.setType(Type.RECTANGLE);
        this.height = 0;
    }
    public Rectangle(float width, float height) {
        this.setType(Type.RECTANGLE);
        this.setWidth(width);
        this.height = height;
    }

    public float getHeight() { return this.height; }
    public void setHeight(float height) { this.height = height; }

    @Override
    public double getArea() { return (this.getWidth()*this.height); }

    @Override
    public String toString() {
        return String.format("Type: %-12s| Area: %-4.2f| Width: %-4.2f| Height: %-4.2f",
                this.getType(), this.getArea(), this.getWidth(), this.height);
    }
}
