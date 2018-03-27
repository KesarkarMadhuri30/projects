package d.newnavigationapp.model;



public class ProductModel {
    private Integer productId;
    private String productName;
    private Integer productPrize;
    private String productimage;
    private String productCategory;
    private Integer mrp;
    private  String name;
    private String email;
    private Integer quantity;
    private  Integer updateprize;
    private Integer qtyprize;
    private Integer totalcost;

    public ProductModel(Integer productID, String productName, Integer productPrize, String image, String category, Integer mrp,String name,String email) {
        this.productId=productID;
        this.productName=productName;
        this.productPrize=productPrize;
        this.productimage=image;
        this.productCategory=category;
        this.mrp=mrp;
        this.name=name;
        this.email=email;
    }

    public ProductModel(Integer productId, String productName, Integer productPrize, String image, Integer integer, String category, Integer quantity, Integer mrp, String email) {
        this.productId=productId;
        this.productName=productName;
        this.productPrize=productPrize;
        this.productimage=image;
        this.productCategory=category;
        this.mrp=mrp;
        this.email=email;
    }

    public ProductModel(int id, String name, int prize, String img, String category, int mrp) {
        this.productId=id;
        this.productName=name;
        this.productPrize=prize;
        this.productimage=img;
        this.productCategory=category;
        this.mrp=mrp;
    }

    public ProductModel(Integer productId, String productName, Integer productPrize, String image, String category, Integer mrp, String email, Integer quantity, Integer updatePrize, int productprize) {
        this.productId=productId;
        this.productName=productName;
        this.productPrize=productPrize;
        this.productimage=image;
        this.productCategory=category;
        this.mrp=mrp;
        this.email=email;
        this.quantity=quantity;
        this.updateprize=updatePrize;
        this.qtyprize=productprize;
    }

    public ProductModel(Integer productId, String productName, Integer productPrize, String image, Integer mrp, String category, Integer quantity, Integer updatePrize, String email1, Integer totalCost) {
        this.productId=productId;
        this.productimage=productName;
        this.productPrize=productPrize;
        this.productimage=image;
        this.mrp=mrp;
        this.productCategory=category;
        this.quantity=quantity;
        this.updateprize=updatePrize;
        this.email=email1;
        this.totalcost=totalCost;

    }


    public Integer getQtyprize() {
        return qtyprize;
    }

    public void setQtyprize(Integer qtyprize) {
        this.qtyprize = qtyprize;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUpdateprize() {
        return updateprize;
    }

    public void setUpdateprize(Integer updateprize) {
        this.updateprize = updateprize;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductPrize() {
        return productPrize;
    }

    public void setProductPrize(Integer productPrize) {
        this.productPrize = productPrize;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMrp() {
        return mrp;
    }

    public void setMrp(Integer mrp) {
        this.mrp = mrp;
    }
}
