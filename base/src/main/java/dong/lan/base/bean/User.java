package dong.lan.base.bean;

import cn.bmob.v3.BmobUser;

/**
 * 用户信息类
 */

public class User extends BmobUser {
    private Long lastUploadTime;

    public Long getLastUploadTime() {
        return lastUploadTime;
    }

    public void setLastUploadTime(Long lastUploadTime) {
        this.lastUploadTime = lastUploadTime;
    }
}
