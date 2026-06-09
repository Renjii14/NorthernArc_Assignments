package Composition;

public class Car {
    private Engine e;
    private Music m;
    private Ac a;

    public Car(Engine e, Music m, Ac a) {
        this.e = e;
        this.m = m;
        this.a = a;
    }

    public Engine getE() {
        return e;
    }

    public void setE(Engine e) {
        this.e = e;
    }

    public Music getM() {
        return m;
    }

    public void setM(Music m) {
        this.m = m;
    }

    // Getter and Setter for Ac
    public Ac getA() {
        return a;
    }

    public void setA(Ac a) {
        this.a = a;
    }

    public String toString() {
        return "Car [e=" + e.toString() + ", m=" + m.toString() + ", a=" + a.toString() + "]";
    }

}
