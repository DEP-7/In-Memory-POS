package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.DataBase;
import lk.ijse.pos.model.ItemDetails;
import lk.ijse.pos.model.Order;

import java.io.IOException;

public class OrderDetailFormController {
    public AnchorPane contextOfOrderDetailsForm;
    public TableView<ItemDetails> tblOrderList;
    public TableColumn colItemCode;
    public TableColumn colQuantity;
    public TableColumn colUnitPrice;

    public void initialize() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
    }

    public void setData(String orderId) {
        for (Order o : DataBase.orderList) {
            if (o.getOrderId().equals(orderId)) {
                ObservableList<ItemDetails> detailsObList = FXCollections.observableArrayList(o.getItems());
                tblOrderList.setItems(detailsObList);
                return;
            }
        }
    }
}
