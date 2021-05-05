package lk.ijse.pos.views.tm;

import javafx.scene.control.Button;

public class ItemTM {
    private String itemCode;
    private String description;
    private int qtyOnHand;
    private double unitPrice;
    private Button btn;

    public ItemTM() {
    }

    public ItemTM(String itemCode, String description, int qtyOnHand, double unitPrice, Button btn) {
        this.itemCode = itemCode;
        this.description = description;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
        this.btn = btn;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
