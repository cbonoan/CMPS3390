package cbonoan.a3;

/**
 * RightTriangle class represents right triangles with sides (width and height) forming a 90 degree angle
 * Width and height may be the same or different
 */
public class RightTriangle extends Rectangle{
    public RightTriangle() {
        super();
        this.setType(Type.RIGHT_TRIANGLE);
    }
    public RightTriangle(float width, float height) {
        this.setType(Type.RIGHT_TRIANGLE);
        this.setWidth(width);
        this.setHeight(height);
    }

    @Override
    public double getArea() {
        return ((0.5)*this.getWidth()*this.getHeight());
    }
}
