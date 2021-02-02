package cbonoan.a3;

public class Square extends Shape{
    private float width;

    public Square() {
        super();
        this.setType(Type.SQUARE);
        this.width = 0;
    }
    public Square(float width) {
        this.setType(Type.SQUARE);
        this.width = width;
    }

    public float getWidth() { return this.width; }
    public void setWidth(float width) { this.width = width; }
    public double getArea() { return (width*width); }

    @Override
    public String toString() {
        return String.format("Type: %-12s| Area: %-4.2f| Width: %-4.2f",
                this.getType(), this.getArea(), this.width);
    }
}
