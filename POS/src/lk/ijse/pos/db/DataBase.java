package lk.ijse.pos.db;

import lk.ijse.pos.model.Customer;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.model.Order;

import java.util.ArrayList;

public class DataBase {
    public static ArrayList<Customer> customerList=new ArrayList();
    public static ArrayList<Item> itemList=new ArrayList();
    public static ArrayList<Order> orderList=new ArrayList();

    static {
        customerList.add(new Customer("C-001","Dhanushka","Matara",20000));
        customerList.add(new Customer("C-002","Chandimal","Panadura",55775));

        itemList.add(new Item("I-001","Cake",150,200));
        itemList.add(new Item("I-002","Chocolate",300,55.50));
    }
}
