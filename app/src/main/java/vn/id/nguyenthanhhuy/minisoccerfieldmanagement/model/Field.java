package vn.id.nguyenthanhhuy.minisoccerfieldmanagement.model;

import java.sql.Blob;
import java.sql.Timestamp;

public class Field {
    private String id;
    private String name;
    private String status;
    private String type;
    private byte[] image;
    private String combineField1;
    private String combineField2;
    private String combineField3;
    private Boolean isDeleted;
    private Timestamp createAt;
    private Timestamp updateAt;

    public Field() {
    }

    public Field(String id, String name, String status, String type, byte[] image, String combineField1, String combineField2, String combineField3, Boolean isDeleted, Timestamp createAt, Timestamp updateAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.image = image;
        this.combineField1 = combineField1;
        this.combineField2 = combineField2;
        this.combineField3 = combineField3;
        this.isDeleted = isDeleted;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getCombineField1() {
        return combineField1;
    }

    public void setCombineField1(String combineField1) {
        this.combineField1 = combineField1;
    }

    public String getCombineField2() {
        return combineField2;
    }

    public void setCombineField2(String combineField2) {
        this.combineField2 = combineField2;
    }

    public String getCombineField3() {
        return combineField3;
    }

    public void setCombineField3(String combineField3) {
        this.combineField3 = combineField3;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public Timestamp getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Timestamp updateAt) {
        this.updateAt = updateAt;
    }
}
