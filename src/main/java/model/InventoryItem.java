package model;

public class InventoryItem {

    private int id;
    private String drugName;
    private int quantity;
    private int reorderLevel;

    public InventoryItem(int id, String drugName, int quantity, int reorderLevel) {
        this.id = id;
        this.drugName = drugName;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
    }

    public int getId() { return id; }
    public String getDrugName() { return drugName; }
    public int getQuantity() { return quantity; }
    public int getReorderLevel() { return reorderLevel; }
    public boolean isLowStock() { return quantity <= reorderLevel; }
}
