package lk.ijse.pos.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardFormController {
    public AnchorPane contextOfDashBoard;

    public void openItemForm(MouseEvent mouseEvent) throws IOException {
        setUi("ItemForm");
    }

    public void openCustomerForm(MouseEvent mouseEvent) throws IOException {
        setUi("CustomerForm");
    }

    public void openOrderForm(MouseEvent mouseEvent) throws IOException {
        setUi("OrderForm");
    }

    public void openOrderListForm(MouseEvent mouseEvent) throws IOException {
        setUi("OrderListForm");
    }

    private void setUi(String form) throws IOException {
        Stage stage=(Stage) contextOfDashBoard.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/"+form+".fxml"))));
    }
}
