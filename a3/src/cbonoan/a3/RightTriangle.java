package cbonoan.a3;

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
