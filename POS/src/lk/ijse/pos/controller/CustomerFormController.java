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
import lk.ijse.pos.model.Customer;
import lk.ijse.pos.views.tm.CustomerTM;

import java.io.IOException;
import java.util.Locale;

public class CustomerFormController {
    public AnchorPane contextOfCustomerForm;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerSalary;
    public TextField txtCustomerAddress;
    public Button btnSave;
    public TextField txtSearch;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCustomerId;
    public TableColumn colCustomerName;
    public TableColumn colCustomerAddress;
    public TableColumn colCustomerSalary;
    public TableColumn colCustomerOperate;

    public void initialize() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colCustomerSalary.setCellValueFactory(new PropertyValueFactory<>("customerSalary"));
        colCustomerOperate.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadAllCustomers();
        setCustomerId();

        //-----------------------

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue);
            }
        });

        //-----------------------
    }

    private void setCustomerId() {
        if (DataBase.customerList.size()>0){
            String tempNumber=DataBase.customerList.get(DataBase.customerList.size()-1).getCustomerId();

            String[] array=tempNumber.split("C-");
            int number=Integer.parseInt(array[1]);
            number++;
            if (number>=100){
                tempNumber="C-"+number;
            }else if (number>=10){
                tempNumber="C-0"+number;
            }else{
                tempNumber="C-00"+number;
            }
            txtCustomerId.setText(tempNumber);
        }else{
            txtCustomerId.setText("C-001");
        }
    }

    private void setData(CustomerTM newValue) {
        txtCustomerId.setText(newValue.getCustomerId());
        txtCustomerName.setText(newValue.getCustomerName());
        txtCustomerAddress.setText(newValue.getCustomerAddress());
        txtCustomerSalary.setText(String.valueOf(newValue.getCustomerSalary()));
        btnSave.setText("Update Customer");
    }

    private void loadAllCustomers() {
        ObservableList<CustomerTM> customerObList = FXCollections.observableArrayList();
        for (Customer customer : DataBase.customerList) {
            Button btn = new Button("Delete");
            customerObList.add(new CustomerTM(
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    customer.getCustomerAddress(),
                    customer.getCustomerSalary(),
                    btn
            ));
            btn.setOnAction(event -> {
                if (DataBase.customerList.remove(customer)) {
                    loadAllCustomers();
                }
            });
        }
        tblCustomer.setItems(customerObList);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfCustomerForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../views/DashboardForm.fxml"))));
    }

    public void addNewCustomer(ActionEvent actionEvent) {
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerSalary.clear();
        txtCustomerAddress.clear();
        btnSave.setText("Save Customer");
        setCustomerId();
    }

    public void saveCustomer(ActionEvent actionEvent) {
        Customer customer1 = new Customer(txtCustomerId.getText(), txtCustomerName.getText(), txtCustomerAddress.getText(), Double.parseDouble(txtCustomerSalary.getText()));
        if (btnSave.getText().equals("Save Customer")) {
            if (DataBase.customerList.add(customer1)) {
                new Alert(Alert.AlertType.CONFIRMATION, "New Customer was successfully added.").show();
                loadAllCustomers();
            } else {
                new Alert(Alert.AlertType.WARNING, "Adding failed. Please try again...!").show();
            }
        } else {
            for (int i = 0; i < DataBase.customerList.size(); i++) {
                if (txtCustomerId.getText().equals(DataBase.customerList.get(i).getCustomerId())){
                    DataBase.customerList.remove(i);
                    if (DataBase.customerList.add(customer1)) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Successfully Updated.").show();
                        loadAllCustomers();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Update failed. Please try again...!").show();
                    }
                    break;
                }
            }
        }
    }

    public void search(KeyEvent keyEvent) {
        ObservableList<CustomerTM> searchObList = FXCollections.observableArrayList();
        for (Customer customer : DataBase.customerList) {
            if (
                    customer.getCustomerId().toLowerCase().contains(txtSearch.getText().toLowerCase()) ||
                            customer.getCustomerName().toLowerCase().contains(txtSearch.getText().toLowerCase()) ||
                            customer.getCustomerAddress().toLowerCase().contains(txtSearch.getText().toLowerCase())
            ) {
                Button btn = new Button("Delete");
                searchObList.add(new CustomerTM(
                        customer.getCustomerId(),
                        customer.getCustomerName(),
                        customer.getCustomerAddress(),
                        customer.getCustomerSalary(),
                        btn
                ));
                btn.setOnAction(event -> {
                    if (DataBase.customerList.remove(customer)) {
                        txtSearch.clear();
                        loadAllCustomers();
                    }
                });
            }
        }
        tblCustomer.setItems(searchObList);
    }
}
