package com.vo;

/**
 * zTree 树实体
 * @author ljh
 *
 */
public class ZTreeNodes implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long pid;

    private String name;

    private boolean open;

    private boolean checked;

    private boolean isParent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    @Override
    public String toString() {
        return "ZNodes{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", open=" + open +
                ", checked=" + checked +
                ", isParent=" + isParent +
                '}';
    }

}
