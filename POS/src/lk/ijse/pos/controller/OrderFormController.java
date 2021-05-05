package lk.ijse.pos.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.DataBase;
import lk.ijse.pos.model.Customer;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.model.ItemDetails;
import lk.ijse.pos.model.Order;
import lk.ijse.pos.views.tm.CartTM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderFormController {
    public AnchorPane contextOfOrderForm;
    public TextField txtCustomerAddress;
    public TextField txtCustomerName;
    public TextField txtCustomerSalary;
    public TextField txtDescription;
    public TableView<CartTM> tblCart;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public Label lblOrderId;
    public Label lblDate;
    public Label lblTime;
    public ComboBox<String> cmbCustomerId;
    public ComboBox<String> cmbItemCode;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtQty;
    public Label lblTotal;

    public void initialize() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setDateAndTime();
        loadAllCustomerIds();
        loadAllItemCodes();
        setOrderId();

        //--------------------------------------------.

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerData(newValue);
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemData(newValue);
        });

        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tempCartTM=newValue;
        });

        //--------------------------------------------
    }

    private void setItemData(String newValue) {
        for (Item item : DataBase.itemList) {
            if (item.getItemCode().equals(newValue)) {
                txtDescription.setText(item.getDescription());
                txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
                txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
                break;
            }
        }
    }

    private void setCustomerData(String newValue) {
        for (Customer customer : DataBase.customerList) {
            if (customer.getCustomerId().equals(newValue)) {
                txtCustomerName.setText(customer.getCustomerName());
                txtCustomerAddress.setText(customer.getCustomerAddress());
                txtCustomerSalary.setText(String.valueOf(customer.getCustomerSalary()));
                break;
            }
        }
    }

    private void setDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        Thread timer = new Thread(() -> {
            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String time = f1.format(new Date());
                Platform.runLater(() -> {
                    lblTime.setText(time);
                });
            }
        });
        timer.start();
    }

    private void setOrderId() {
        if (DataBase.orderList.size() > 0) {
            String tempNumber = DataBase.orderList.get(DataBase.orderList.size() - 1).getOrderId();

            String[] array = tempNumber.split("O-");
            int number = Integer.parseInt(array[1]);
            number++;
            if (number >= 100) {
                tempNumber = "O-" + number;
            } else if (number >= 10) {
                tempNumber = "O-0" + number;
            } else {
                tempNumber = "O-00" + number;
            }
            lblOrderId.setText(tempNumber);
        } else {
            lblOrderId.setText("O-001");
        }
    }

    private void loadAllItemCodes() {
        ObservableList<String> itemObList = FXCollections.observableArrayList();
        for (Item item : DataBase.itemList) {
            itemObList.add(item.getItemCode());
        }
        cmbItemCode.setItems(itemObList);
    }

    private void loadAllCustomerIds() {
        ObservableList<String> customerObList = FXCollections.observableArrayList();
        for (Customer customer : DataBase.customerList) {
            customerObList.add(customer.getCustomerId());
        }
        cmbCustomerId.setItems(customerObList);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfOrderForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/DashboardForm.fxml"))));
    }

    CartTM tempCartTM=null;

    public void btnRemove(ActionEvent actionEvent) {
        if (tempCartTM!=null){
            for (CartTM tm : cartObList) {
                if (tm.getItemCode().equals(tempCartTM.getItemCode())){
                    cartObList.remove(tm);
                    tblCart.getColumns().get(0).setVisible(false);
                    tblCart.getColumns().get(0).setVisible(true);
                    calculateTotalCost();
                    break;
                }
            }
        }else{
            new Alert(Alert.AlertType.WARNING,"Please select a row").show();
        }
    }

    ObservableList<CartTM> cartObList = FXCollections.observableArrayList();

    public void addToCart(ActionEvent actionEvent) {
        if (Double.parseDouble(txtQty.getText())<=Double.parseDouble(txtQtyOnHand.getText())) {
            int qty = Integer.parseInt(txtQty.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            double total = unitPrice * qty;
            CartTM tm = new CartTM(cmbItemCode.getValue(), txtDescription.getText(), qty, unitPrice, total);
            if (checkIsExist(tm)) {
                /*tblCart.refresh();*/
                tblCart.getColumns().get(0).setVisible(false);
                tblCart.getColumns().get(0).setVisible(true);
            } else {
                cartObList.add(tm);
                tblCart.setItems(cartObList);
            }
            calculateTotalCost();
        }else{
            new Alert(Alert.AlertType.CONFIRMATION,"Invalid Quantity").show();
        }
    }

    private void calculateTotalCost(){
        double total=0;
        for (CartTM tm:cartObList) {
            total+=tm.getTotal();
        }
        lblTotal.setText(total+"/=");
    }

    private boolean checkIsExist(CartTM tm) {
        for (CartTM temp : cartObList) {
            if (temp.getItemCode().equals(tm.getItemCode())){
                if (Integer.parseInt(txtQtyOnHand.getText())-temp.getQty() >= tm.getQty()) {
                    temp.setQty(tm.getQty() + temp.getQty());
                    temp.setTotal(tm.getTotal() + temp.getTotal());
                    return true;
                } else {
                    new Alert(Alert.AlertType.CONFIRMATION, "Invalid QTY").show();
                    return true;
                }
            }
        }
        return false;
    }

    public void placeOrder(ActionEvent actionEvent) {
        ArrayList<ItemDetails> details=new ArrayList<>();
        for (CartTM tm : cartObList) {
            details.add(new ItemDetails(tm.getItemCode(), tm.getQty(), tm.getUnitPrice()));
            for (Item item:DataBase.itemList) {
                if (item.getItemCode().equals(tm.getItemCode())){
                    item.setQtyOnHand(item.getQtyOnHand()-tm.getQty());
                    break;
                }
            }
        }
        Order order=new Order(
                lblOrderId.getText(),
                lblDate.getText(),
                cmbCustomerId.getValue(),
                Double.parseDouble(lblTotal.getText().split("/=")[0]),
                details
        );
        if (DataBase.orderList.add(order)){
            new Alert(Alert.AlertType.CONFIRMATION,"Order Placed").show();
            cartObList.clear();
            calculateTotalCost();
            setOrderId();
            clearAll();
        }
    }
    private void clearAll(){
        cmbItemCode.getSelectionModel().select(0);
        cmbCustomerId.getSelectionModel().select(0);
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerSalary.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        txtQty.clear();
    }
}
