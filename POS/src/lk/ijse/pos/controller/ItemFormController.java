package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.DataBase;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.views.tm.ItemTM;

import java.io.IOException;

public class ItemFormController {
    public AnchorPane contextOfItemForm;
    public TextField txtItemCode;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtDescription;
    public Button btnSaveItem;
    public TextField txtSearch;
    public TableView<ItemTM> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOperate;

    ObservableList<ItemTM> obList = FXCollections.observableArrayList();

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOperate.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadAllItems("");
        setItemCode();

        //-----------------------

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
            }
        });

        //-----------------------
    }

    private void setItemCode() {
        if (DataBase.itemList.size()>0){
            String tempNumber=DataBase.itemList.get(DataBase.itemList.size()-1).getItemCode();

            String[] array=tempNumber.split("I-");
            int number=Integer.parseInt(array[1]);
            number++;
            if (number>=100){
                tempNumber="I-"+number;
            }else if (number>=10){
                tempNumber="I-0"+number;
            }else{
                tempNumber="I-00"+number;
            }
            txtItemCode.setText(tempNumber);
        }else{
            txtItemCode.setText("I-001");
        }
    }

    private void setData(ItemTM newValue) {
        txtItemCode.setText(newValue.getItemCode());
        txtDescription.setText(newValue.getDescription());
        txtQtyOnHand.setText(String.valueOf(newValue.getQtyOnHand()));
        txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
        btnSaveItem.setText("Update Item");
    }

    private void loadAllItems(String text) {
        obList.clear();
        for (Item item : DataBase.itemList) {
            Button btn=new Button("Delete");
            if (
                    item.getItemCode().contains(text) ||
                    item.getDescription().contains(text)
            ){
                obList.add(new ItemTM(
                        item.getItemCode(),
                        item.getDescription(),
                        item.getQtyOnHand(),
                        item.getUnitPrice(),
                        btn
                ));
                btn.setOnAction(event -> {
                    if (DataBase.itemList.remove(item)){
                        new Alert(Alert.AlertType.CONFIRMATION,"Deleted").show();
                        loadAllItems("");
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Try Again").show();
                    }
                });
            }
        }
        tblItem.setItems(obList);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) contextOfItemForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/DashboardForm.fxml"))));
    }

    public void addNewItem(ActionEvent actionEvent) {
        txtItemCode.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        btnSaveItem.setText("Save Item");
        setItemCode();
    }

    public void saveItem(ActionEvent actionEvent) {
        Item item=new Item(
                txtItemCode.getText(),
                txtDescription.getText(),
                Integer.parseInt(txtQtyOnHand.getText()),
                Double.parseDouble(txtUnitPrice.getText()));
        if (btnSaveItem.getText().equals("Save Item")){
            if (DataBase.itemList.add(item)){
                new Alert(Alert.AlertType.CONFIRMATION,"New Item was successfully added.");
                loadAllItems("");
            }else{
                new Alert(Alert.AlertType.WARNING,"Adding failed. Please try again...!");
            }
        }else{
            int counter=0;
            for (Item item1 : DataBase.itemList) {
                if (txtItemCode.getText().equals(item1.getItemCode())){
                    DataBase.itemList.get(counter).setDescription(txtDescription.getText());
                    DataBase.itemList.get(counter).setQtyOnHand(Integer.parseInt(txtQtyOnHand.getText()));
                    DataBase.itemList.get(counter).setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
                    loadAllItems("");
                    break;
                }
                counter++;
            }
        }
    }

    public void search(KeyEvent keyEvent) {
        loadAllItems(txtSearch.getText());
    }
}
