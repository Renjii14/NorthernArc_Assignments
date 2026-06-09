package Abstraction;

public class Dog extends Animal {

    @Override
    public void eat() {
        System.out.println("Dog is omnivorous");
    }

    @Override
    public void talk() {
        System.out.println("Dog is barking.");
    }

    @Override
    public void shelter() {
       System.out.println("Dog is guarding the house. ");
    }
    
}
