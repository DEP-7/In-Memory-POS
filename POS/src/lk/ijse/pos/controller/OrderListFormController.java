package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.DataBase;
import lk.ijse.pos.model.Order;
import lk.ijse.pos.views.tm.OrderTM;

import java.io.IOException;

public class OrderListFormController {
    public AnchorPane contextOfOrderListForm;
    public TableView<OrderTM> tblOrderList;
    public TableColumn colOrderId;
    public TableColumn colOrderDate;
    public TableColumn colCustomer;
    public TableColumn colTotalCost;

    public void initialize() {

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        loadAllOrders();

        //-------------------------
        tblOrderList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("../views/OrderDetaiForm.fxml"));
            try {
                Parent root=loader.load();
                OrderDetailFormController controller=loader.getController();
                controller.setData(newValue.getOrderId());
                Stage s=new Stage();
                s.setScene(new Scene(root));
                s.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //-------------------------
    }

    private void loadAllOrders() {
        ObservableList<OrderTM> orderObList = FXCollections.observableArrayList();
        for (Order o : DataBase.orderList) {
            orderObList.add(
                    new OrderTM(
                            o.getOrderId(),
                            o.getOrderDate(),
                            o.getCustomerId(),
                            o.getTotalCost()
                    )
            );
        }
        tblOrderList.setItems(orderObList);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfOrderListForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/DashboardForm.fxml"))));
    }
}
