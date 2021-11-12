package presentationLayer;

import javafx.scene.control.ListView;
import model.MenuItem;
import model.Order;
import model.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdministratorReports extends SceneController {

    @FXML
    TextField startHourField;
    @FXML
    TextField endHourField;
    @FXML
    TextField numberOfOrders;
    @FXML
    TextField orderNumber;
    @FXML
    TextField priceValue;
    @FXML
    TextField day;
    @FXML
    TextField count;
    @FXML
    ListView listView;

    @FXML
    public void initialize() {
        deliveryService.importUsers();
        deliveryService.orders.clear();
        deliveryService.importOrders();
    }

    /**
     * Generates the time report (minHour, maxHour)
     */
    @FXML
    public void timeReport() {
        List<Order> orders = deliveryService.orders.keySet().stream()
                .filter(order -> (order.getOrderDate().getHours() >= Integer.parseInt(startHourField.getText())
                        && order.getOrderDate().getHours() <= Integer.parseInt(endHourField.getText())))
                .collect(Collectors.toList());

        listView.getItems().clear();
        for (Order order : orders) {
            listView.getItems().add(order);
            System.out.println(order.getClientId() + " " + order.getOrderDate());

        }
    }

    /**
     * Generates a report with regard to a number of orders for a product
     */
    @FXML
    public void numberOfOrdersReport() {
        List<MenuItem> allMenuItems = new ArrayList<>();
        for (HashSet<MenuItem> value : deliveryService.orders.values()) {
            allMenuItems.addAll(value);
        }

        Map<String, Long> counterMenuItems = allMenuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getTitle, Collectors.counting()));

        List<String> countedItems = counterMenuItems.keySet().stream()
                .filter(menuItem -> counterMenuItems.get(menuItem).intValue() >= Integer.parseInt(numberOfOrders.getText()))
                .collect(Collectors.toList());

        listView.getItems().clear();
        for (Object countedItem : countedItems) {
            listView.getItems().add(countedItem);
            System.out.println(countedItem);
        }
    }

    /**
     * Generates a report to get the clients that have ordered more than a number of times and the price of one order was greater than a value inputted
     */
    @FXML
    public void numberOfOrdersByClient() {
        Map<Integer, Long> clientsOrders = deliveryService.orders.keySet().stream()
                .collect(Collectors.groupingBy(Order::getClientId, Collectors.counting()));

        List<Integer> clients = clientsOrders.keySet().stream()
                .filter(client -> clientsOrders.get(client).intValue() >= Integer.parseInt(orderNumber.getText()))
                .collect(Collectors.toList());

        List<User> clientsReported = new ArrayList<>();
        for (Integer client : clients) {
            boolean hasPriceGreater = false;
            List<Order> currentClientOrders = deliveryService.orders.keySet().stream()
                    .filter(order -> order.getClientId().equals(client)).collect(Collectors.toList());
            for (Order currentClientOrder : currentClientOrders) {
                int price = 0;
                for (MenuItem menuItem : deliveryService.orders.get(currentClientOrder)) {
                    price += menuItem.computePrice();
                }
                if (price >= Integer.parseInt(priceValue.getText())) {
                    hasPriceGreater = true;
                    break;
                }
            }
            if (hasPriceGreater) {
                clientsReported.add(deliveryService.users.get(client - 1));
            }
        }

        listView.getItems().clear();
        for (User user1 : clientsReported) {
            listView.getItems().add(user1.getUsername());
            System.out.println(user1.getUsername());
        }
    }

    /**
     * Generates a report that says how many products have been ordered in a day with a number greater than an inputted value
     */
    @FXML
    public void ordersWithinADayWithCounting() {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd");
        List<Order> ordersOfDay = deliveryService.orders.keySet().stream()
                .filter(order -> dateFormat.format(order.getOrderDate()).equals(day.getText()))
                .collect(Collectors.toList());

        List<MenuItem> menuItemsOfDay = new ArrayList<>();
        for (Order order : ordersOfDay) {
            for (MenuItem menuItem : deliveryService.orders.get(order)) {
                menuItemsOfDay.add(menuItem);
            }
        }

        Map<String, Long> countedMenuItems = menuItemsOfDay.stream()
                .collect(Collectors.groupingBy(MenuItem::getTitle, Collectors.counting()));

        List<String> finalItems = countedMenuItems.keySet().stream()
                .filter(item -> countedMenuItems.get(item).intValue() >= Integer.parseInt(count.getText()))
                .collect(Collectors.toList());

        listView.getItems().clear();
        for (String finalItem : finalItems) {
            listView.getItems().add(finalItem);
            System.out.println(finalItem);
        }
    }
}
