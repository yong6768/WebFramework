package testmvc.jsonBeanContainer.circular;

public class CircularA {
    CircularB circularB;
    public CircularA(CircularB circularB) {
        this.circularB = circularB;
    }
}
