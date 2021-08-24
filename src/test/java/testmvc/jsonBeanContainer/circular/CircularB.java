package testmvc.jsonBeanContainer.circular;

public class CircularB {
    CircularC circularC;
    public CircularB(CircularC circularC){
        this.circularC = circularC;
    }
}
