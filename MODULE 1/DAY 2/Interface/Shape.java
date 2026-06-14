package OOP.Interface;

public interface Shape{
    void calculateArea();
    String color="Red";
    static void Color(){
        System.out.println("Color of the shape is:"+color);
    }

    public static void main(String[] args) {
        Shape c=new Circle(20);
        Shape r=new Rectangle(5,2);
        Shape t=new Triangle(6,7);
        c.calculateArea();
        r.calculateArea();
        t.calculateArea();
        Shape.Color();
//        Rectangle.Color(); // cause error

    }
}
class Rectangle implements Shape{
    int length;
    int breadth;
    public Rectangle(int length,int breadth){
        this.length=length;
        this.breadth=breadth;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBreadth() {
        return breadth;
    }

    public void setBreadth(int breadth) {
        this.breadth = breadth;
    }

    @Override
    public void calculateArea() {
        System.out.println("Area is: "+this.length*this.breadth);
    }
}
class Circle implements Shape{
    double radius;
    public Circle(double radius){
        this.radius=radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void calculateArea() {
        System.out.println("Area is: "+Math.PI*radius*radius);
    }
}
class Triangle implements Shape{
    int breadth;
    int height;
    public Triangle(int breadth,int height){
        this.breadth=breadth;
        this.height=height;
    }

    public int getBreadth() {
        return breadth;
    }

    public void setBreadth(int breadth) {
        this.breadth = breadth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void calculateArea(){
        System.out.println("Area is: "+0.5*breadth*height);
    }
}
