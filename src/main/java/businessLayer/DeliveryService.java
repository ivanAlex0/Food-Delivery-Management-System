package businessLayer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Delivery Service class that implements the IDeliveryServiceProcessing and extends Observable
 */
public class DeliveryService extends Observable implements IDeliveryServiceProcessing {

    /**
     * The Map that saves all the orders
     */
    public Map<Order, HashSet<MenuItem>> orders;
    /**
     * HashSet of all the products in the file
     */
    public HashSet<MenuItem> products;
    /**
     * Array of all users
     */
    public ArrayList<User> users;
    String notification;

    /**
     * The constructor initializes the Map, the HashSet and the ArrayList
     */
    public DeliveryService() {
        this.products = new HashSet<>();
        this.users = new ArrayList<>();
        this.orders = new HashMap<>();
    }

    //the invariant for the well-formed method
    //should check all the Order's hashCodes correspond, as well as MenuItems to be distinct
    public boolean isWellFormed() {
        return true;
    }

    /**
     * Function imports the products using JacksonMapper
     * -tag @pre products.isEmpty() == true
     * -tag @post products.size() == 'number of products in the products.csv file' && products contains all the products in the file
     */
    @Override
    public void importProducts() {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        ObjectReader oReader = csvMapper.reader(BaseProduct.class).with(schema);
        try (Reader reader = new FileReader("src/main/resources/products.csv")) {
            MappingIterator<BaseProduct> mi = oReader.readValues(reader);
            ArrayList<MenuItem> currentCompositeItem = new ArrayList<>();
            boolean currentlyAComposite = false;
            while (mi.hasNext()) {
                BaseProduct current = mi.next();
                if (current.getPrice() == -1) {
                    currentlyAComposite = !currentlyAComposite;
                    if (!currentlyAComposite) {
                        CompositeProduct compositeProduct = new CompositeProduct();
                        compositeProduct.setProducts(currentCompositeItem);
                        compositeProduct.setTitle(current.getTitle());
                        System.out.println(currentCompositeItem.size());
                        products.add(compositeProduct);
                    }
                    currentCompositeItem = new ArrayList<>();
                } else {
                    if (!currentlyAComposite)
                        products.add(current);
                    else currentCompositeItem.add(current);
                }
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        System.out.println(products.size());
    }

    /**
     * Function imports the orders using JacksonMapper
     * -tag @pre orders.isEmpty() == true
     * -tag @post orders.size() == 'number of orders in the orders.csv file' && orders contains all the orders in the file
     *
     */
    @Override
    public void importOrders() {
        CsvMapper csvMapperOrders = new CsvMapper();
        CsvMapper csvMapperProducts = new CsvMapper();
        CsvSchema schemaOrders = CsvSchema.emptySchema().withHeader();
        CsvSchema schemaProducts = CsvSchema.emptySchema().withHeader();
        ObjectReader oReaderOrders = csvMapperOrders.reader(Order.class).with(schemaOrders);
        ObjectReader oReaderProducts = csvMapperProducts.reader(BaseProduct.class).with(schemaProducts);
        try (Reader readerOrders = new FileReader("src/main/resources/orders.csv");
             Reader readerProducts = new FileReader("src/main/resources/orderedProducts.csv")) {
            MappingIterator<Order> miOrders = oReaderOrders.readValues(readerOrders);
            MappingIterator<BaseProduct> miProducts = oReaderProducts.readValues(readerProducts);
            while (miOrders.hasNext()) {
                Order order = miOrders.next();
                HashSet<MenuItem> currentOrderProducts = new HashSet<>();
                for (int i = 0; i < order.quantity; i++) {
                    if (miProducts.hasNext()) {
                        BaseProduct baseProduct = miProducts.next();
                        currentOrderProducts.add(baseProduct);
                    }
                }
                orders.put(order, currentOrderProducts);
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    /**
     * Function imports the users using JacksonMapper
     * -tag @pre users.isEmpty() == true
     * -tag @post users.size() == 'number of users in the orders.csv file' && users contains all the users in the file
     */
    public void importUsers()
    {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        ObjectReader oReader = csvMapper.reader(User.class).with(schema);
        try (Reader reader = new FileReader("src/main/resources/clients.csv")) {
            MappingIterator<User> mi = oReader.readValues(reader);
            while (mi.hasNext()) {
                User user = mi.next();
                users.add(user);
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    /**
     * The function adds a BaseProduct to the HashSet
     * @param product the product to be added to the hashSet
     * -tag @pre products.contains(product) == false
     * -tag @post products.contains(products) == true
     * @return true if the product was added to the hashSet and false otherwise
     */
    @Override
    public boolean addProduct(BaseProduct product) {
        return products.add(product);
    }

    /**
     * The function deletes a product from the HashSet
     * -tag @pre products.contains(baseProduct) == true
     * -tag @post products.contains(baseProduct) == false
     * @param baseProduct the baseProduct to be deleted from the csv file
     */
    @Override
    public void deleteProduct(BaseProduct baseProduct) {
        products.remove(baseProduct);
    }

    /**
     * The function modifies the products in the hashSet if it exists
     * -tag @pre baseProduct has its fields unchanged
     * -tag @post baseProduct has its fields changed
     * @param baseProduct the baseProduct to be modified
     */
    @Override
    public void modifyProduct(BaseProduct baseProduct) {
        for (MenuItem product : products) {
            BaseProduct baseProduct1 = (BaseProduct) product;
            if (baseProduct1.equals(baseProduct)) {
                baseProduct1.setAll(baseProduct.rating, baseProduct.calories, baseProduct.protein, baseProduct.fat, baseProduct.sodium, baseProduct.price);
            }
        }
    }

    /**
     * The function adds a composite product to the HashSet of products
     * -tag @pre products.contains(compositeProduct) == false
     * -tag @post products.contains(compositeProduct) == true
     * @param compositeProduct the product to be added
     */
    @Override
    public void createCustomProduct(CompositeProduct compositeProduct) {
        products.add(compositeProduct);
    }

    /**
     * The function creates an order for the user with the products selected
     * @param user the User that creates the order
     * @param currentOrderProducts the HashSet of products that were added to the order
     * @return the Order that was created
     * -tag @pre orders.contains(new Order(user, currentOrderProducts)) == false
     * -tag @post orders.contains(new Order(user, currentOrderProducts)) == true
     */
    @Override
    public Order createOrder(User user, HashSet<MenuItem> currentOrderProducts) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String formattedDate = simpleDateFormat.format(date);
        Date newDate = null;
        try {
            newDate = simpleDateFormat.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Order currentOrder = new Order(orders.size() + 1,
                user.id, newDate, currentOrderProducts.size());
        orders.put(currentOrder, currentOrderProducts);

        StringBuilder notification = new StringBuilder("Order# " + currentOrder.getOrderId() + " composed of: ");
        for (MenuItem currentOrderProduct : currentOrderProducts) {
            notification
                    .append("[")
                    .append(currentOrderProduct.getTitle())
                    .append("] + ");
        }
        notifyEmployee(notification.toString());
        return currentOrder;
    }

    /**
     * The function exports the Products to the .csv file -> writes into it
     * -tag @pre the products.csv file is not updated = does not contain all the data from the products
     * -tag @post the products.csv file is updated = contains all the data from the products
     */
    @Override
    public void exportProducts() {
        CsvMapper mapper = new CsvMapper();
        // we ignore unknown fields or fields not specified in schema, otherwise writing will fail
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

        // initialize the schema
        CsvSchema schema = CsvSchema.builder().addColumn("Title").addColumn("Rating").addColumn("Calories").addColumn("Protein").addColumn("Fat").addColumn("Sodium").addColumn("Price").setUseHeader(true).build();

        // map the bean with our schema for the writer
        ObjectWriter writer = mapper.writerFor(BaseProduct.class).with(schema);

        File tempFile = new File("src/main/resources/products.csv");
        // we write the list of objects
        ArrayList<BaseProduct> baseProductsToBeWritten = new ArrayList<>();
        for (MenuItem product : products) {
            if (product instanceof BaseProduct)
                baseProductsToBeWritten.add((BaseProduct) product);
            else if (product instanceof CompositeProduct) {
                BaseProduct baseProduct = new BaseProduct(product.getTitle(), -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1);
                baseProductsToBeWritten.add(baseProduct);
                for (MenuItem menuItem : ((CompositeProduct) product).getProducts()) {
                    BaseProduct compositeBaseProduct = (BaseProduct) menuItem;
                    baseProductsToBeWritten.add(compositeBaseProduct);
                }
                baseProductsToBeWritten.add(baseProduct);
            }
        }

        try {
            writer.writeValues(tempFile).writeAll(baseProductsToBeWritten);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * The function exports the orders to the .csv file -> writes into it
     * -tag @pre the orders.csv file is not updated = does not contain all the data from the orders
     * -tag @post the orders.csv file is updated = contains all the data from the orders
     */
    @Override
    public void exportOrders() {
        CsvMapper mapperOrders = new CsvMapper();
        CsvMapper mapperProducts = new CsvMapper();
        mapperOrders.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        mapperProducts.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

        CsvSchema schemaOrders = CsvSchema.builder().addColumn("orderId").addColumn("clientId").addColumn("orderDate").addColumn("quantity").setUseHeader(true).build();
        CsvSchema schemaProducts = CsvSchema.builder().addColumn("Title").addColumn("Rating").addColumn("Calories").addColumn("Protein").addColumn("Fat").addColumn("Sodium").addColumn("Price").setUseHeader(true).build();

        ObjectWriter writerOrders = mapperOrders.writerFor(Order.class).with(schemaOrders);
        ObjectWriter writerProducts = mapperProducts.writerFor(BaseProduct.class).with(schemaProducts);

        File tempFileOrders = new File("src/main/resources/orders.csv");
        File tempFileProducts = new File("src/main/resources/orderedProducts.csv");

        ArrayList<MenuItem> orderedProducts = new ArrayList<>();
        for (HashSet<MenuItem> value : orders.values()) {
            for (MenuItem menuItem : value) {
                if (menuItem instanceof BaseProduct)
                    orderedProducts.add(menuItem);
                else if (menuItem instanceof CompositeProduct) {
                    BaseProduct baseProduct = new BaseProduct(menuItem.getTitle(), menuItem.getRating(), menuItem.getCalories(), menuItem.getProtein(), menuItem.getFat(), menuItem.getSodium(), menuItem.computePrice());
                    orderedProducts.add(baseProduct);
                }
            }
        }

        System.out.println(orderedProducts);

        try {
            writerOrders.writeValues(tempFileOrders).writeAll(orders.keySet());
            writerProducts.writeValues(tempFileProducts).writeAll(orderedProducts);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * The function notifies the employee when an order is created
     * @param notification the notification message
     * -tag @pre the listView field from the employee controller does not show the current Order
     * -tag @post the listView field from the employee controller does shows the current Order
     */
    public void notifyEmployee(String notification) {
        this.notification = notification;
        setChanged();
        notifyObservers();
        System.out.println("I notified!");
    }

    /**
     * Generates the reports for the administrator
     * -tag @pre reports are not generated
     * -tag @post reports are generated
     */
    @Override
    public void generateReports() {

    }

    public String getNotification()
    {
        return this.notification;
    }
}
