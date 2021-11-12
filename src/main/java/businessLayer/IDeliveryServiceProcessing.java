package businessLayer;

import model.*;

import java.io.IOException;
import java.util.HashSet;

public interface IDeliveryServiceProcessing {

    //Administrator methods
    void importProducts();

    void importOrders();

    void exportOrders();

    void exportProducts() throws IOException;

    boolean addProduct(BaseProduct product);

    void deleteProduct(BaseProduct baseProduct);

    void modifyProduct(BaseProduct baseProduct);

    void createCustomProduct(CompositeProduct compositeProduct);

    void generateReports();

    //Client methods
    Order createOrder(User user, HashSet<MenuItem> currentOrderProducts);

}
