package ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

public class Inventory {
    private Queue<String> stock=new LinkedList<>();
    //add method called by producer
    public String add(String item) throws InterruptedException {
        stock.add(item);
        return item;
    }
    //remove method called by consumer
    public String remove() throws InterruptedException {
        while(stock.size()<=0) {
            System.out.println("Inventory is empty.Consumer going to wait state.");
            wait();
        }
        return stock.remove();
    }

    public int size() {
        return stock.size();
    }
}
