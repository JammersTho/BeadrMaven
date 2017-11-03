import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Customer;
import model.Model;
import model.Order;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

    private Model theModel;

    @FXML
    private ListView customerList;
    @FXML
    private ListView orderList;

    public MainScreen()
    {
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb)
    {
        theModel = new Model();
        setCustomerList();
        customerList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Customer selectedCustomer = (Customer) customerList.getSelectionModel().getSelectedItem();
                setOrderList(selectedCustomer.getCustomerId());
            }
        });
    }

    private void setOrderList(int customerId)
    {
        ObservableList<Order> orders = FXCollections.observableArrayList(theModel.getOrdersByCustomerId(customerId));

        orderList.getItems().clear();
        orderList.setItems(orders);
        orderList.setCellFactory(param -> new ListCell<Order>() {
            @Override
            protected void updateItem(Order item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getDate() == null) {
                    setText(null);
                } else {
                    setText("ID: " +item.getOrderId());
                }
            }
        });
    }

    @FXML
    public void setCustomerList()
    {
        ObservableList<Customer> customers = FXCollections.observableArrayList(theModel.getCustomers());

        customerList.getItems().clear();
        customerList.setItems(customers);
        customerList.setCellFactory(param -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getCompany() == null) {
                    setText(null);
                } else {
                    setText(item.getCompany());
                }
            }
        });
    }

    @FXML
    public void createTable()
    {
        theModel.createTable();
    }
}
