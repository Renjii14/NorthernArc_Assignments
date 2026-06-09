package Abstraction;

public class Deer extends Animal {

    @Override
    public void eat() {
        System.out.println("Deer is herbivorous.");
    }

    @Override
    public void talk() {
        System.out.println("Deer is bleating.");
    }

    @Override
    public void shelter() {
        System.out.println("Deer is living in forest.");
    }
    
}
