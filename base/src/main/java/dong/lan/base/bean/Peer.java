package dong.lan.base.bean;

import cn.bmob.v3.BmobObject;

/**
 * 会话用户实体
 */

public class Peer extends BmobObject {
    private User owner;
    private String talkerId;
    private String name;
    private String remark;

    public Peer(User owner, String id, String name) {
        this.owner = owner;
        this.talkerId = id;
        this.name = name;
    }

    public Peer(String id, String name) {
        this.talkerId = id;
        this.name = name;
    }

    public Peer() {
    }

    public String getId() {
        return talkerId;
    }


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(String id) {
        this.talkerId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
