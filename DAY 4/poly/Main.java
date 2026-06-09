package Composition.poly;

public class Main {
    public static void main(String[] args) {
        Calculator c=new Calculator();
        System.out.println(c.add(10,20));

        System.out.println(c.add(10,20));

    }


}

//if we comment long, there is only int and Integer - it takes int only by default.
//if we comment int and long, it takes Integer which is auto widening
//if we comment int, there is long and Integer ten it takes autowidening.
// first auto widening happens then only autoboxing takes place.