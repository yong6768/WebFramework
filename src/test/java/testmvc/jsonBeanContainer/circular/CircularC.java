package testmvc.jsonBeanContainer.circular;

public class CircularC {
    CircularA circularA;
    public CircularC(CircularA circularA){
        this.circularA = circularA;
    }
}
