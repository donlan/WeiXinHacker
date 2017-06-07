package dong.lan.base.bean;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobObject;

/**
 * 消息实体
 */

public class Message extends BmobObject {


    private User owner;
    private String msgId;
    private String msgSvrId;
    private Integer type;
    private Integer status;
    private Integer isSend;
    private Integer isShowTimer;
    private Long createTime;
    private String talker;
    private String content;
    private String imgPath;
    private Integer reserved;
    private String transContent;
    private String transBrandWording;
    private String talkerId;
    private String bizClientMsgId;
    private String bizChatId;
    private String bizChatUserId;
    private String msgSeq;
    private Integer flag;
    private String displayText;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Message() {
    }


    public Message(Cursor cursor) {
        msgId = cursor.getString(cursor.getColumnIndex("msgId"));
        msgSvrId = cursor.getString(cursor.getColumnIndex("msgSvrId"));
        type = cursor.getInt(cursor.getColumnIndex("type"));
        status = cursor.getInt(cursor.getColumnIndex("status"));
        isSend = cursor.getInt(cursor.getColumnIndex("isSend"));
        isShowTimer = cursor.getInt(cursor.getColumnIndex("isShowTimer"));
        createTime = cursor.getLong(cursor.getColumnIndex("createTime"));
        talker = cursor.getString(cursor.getColumnIndex("talker"));
        content = cursor.getString(cursor.getColumnIndex("content"));
        imgPath = cursor.getString(cursor.getColumnIndex("imgPath"));
        reserved = cursor.getInt(cursor.getColumnIndex("reserved"));
        transContent = cursor.getString(cursor.getColumnIndex("transContent"));
        transBrandWording = cursor.getString(cursor.getColumnIndex("transBrandWording"));
        talkerId = cursor.getString(cursor.getColumnIndex("talkerId"));
        bizClientMsgId = cursor.getString(cursor.getColumnIndex("bizClientMsgId"));
        bizChatId = cursor.getString(cursor.getColumnIndex("bizChatId"));
        bizChatUserId = cursor.getString(cursor.getColumnIndex("bizChatUserId"));
        msgSeq = cursor.getString(cursor.getColumnIndex("msgId"));
        flag = cursor.getInt(cursor.getColumnIndex("flag"));
    }

    public void parse() {
        if (type == 1) {
            displayText = "<p class='lead'>" + content + "</p>";
        } else if (type == 10000) {
            displayText = "系统提示";
        } else if (type == 285212721) {
            try {

                String reg = "(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(content);
                List<String> urls = new ArrayList<>();
                while (m.find()) {
                    String url = m.group();
                    if (url.contains("http://mmbiz.qpic.cn"))
                        continue;
                    urls.add(url);
                }

                String s1 = content.replaceAll("digest", "#").replaceAll("title", "&")
                        .replaceAll("[^\u4e00-\u9fa5|&|#]", "").trim()
                        .replaceAll("[ ]{1,}", "&");
                System.out.println(s1);
                String ss[] = s1.split("[&]{1,}");
                StringBuilder sb = new StringBuilder();
                sb.append("<div class='list-group'>");
                int c = 0;
                for (int i = 0; i < ss.length; i++) {
                    if (ss[i].contains("#"))
                        continue;
                    sb.append("<a class='list-group-item' href='").append(urls.get(c++)).append("'>");
                    sb.append(ss[i]);
                    sb.append("</a>");
                }
                sb.append("</div>");
                displayText = sb.toString();
            } catch (Exception e) {
                String s1 = content.replaceAll("digest", "#").replaceAll("title", "&")
                        .replaceAll("[^\u4e00-\u9fa5|&|#]", "").trim()
                        .replaceAll("[ ]{1,}", "&");
                System.out.println(s1);
                String ss[] = s1.split("[&]{1,}");
                StringBuilder sb = new StringBuilder();
                sb.append("<div class='list-group'>");
                int c = 0;
                for (int i = 0; i < ss.length; i++) {
                    if (ss[i].contains("#"))
                        continue;
                    sb.append("<li class='list-group-item'>");
                    sb.append(ss[i]);
                    sb.append("</li>");
                }
                sb.append("</div>");
                displayText = sb.toString();
            }
        } else if (type == 34) {
            displayText = "语音";
        } else if (type == 47) {
            try {

                String s = content.substring(content.indexOf("cdnurl") + 17,
                        content.indexOf("designerid=") - 2);
                displayText = "<img src='http://" + s + "' />";
            } catch (Exception e) {
                displayText = "<p class='lead'>无法解析的图片资源</p>";
            }
        } else if (type == 48) {
            String s1 = content.replaceAll("[^\u4e00-\u9fa5| ]", "").trim()
                    .replaceAll("[ ]{1,}", ",");
            String ss[] = s1.split(",");
            displayText = "<p>" + ss[0] + "</p>";

        } else if (type == 318767153) {
            displayText = "微信团队";
        } else if (type == 436207665) {
            displayText = "<img src='http://wx.gtimg.com/hongbao/1701/hb.png'>";
        } else
            displayText = "未知的消息数据";
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgSvrId() {
        return msgSvrId;
    }

    public void setMsgSvrId(String msgSvrId) {
        this.msgSvrId = msgSvrId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public Integer getIsShowTimer() {
        return isShowTimer;
    }

    public void setIsShowTimer(Integer isShowTimer) {
        this.isShowTimer = isShowTimer;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public String getTransContent() {
        return transContent;
    }

    public void setTransContent(String transContent) {
        this.transContent = transContent;
    }

    public String getTransBrandWording() {
        return transBrandWording;
    }

    public void setTransBrandWording(String transBrandWording) {
        this.transBrandWording = transBrandWording;
    }

    public String getTalkerId() {
        return talkerId;
    }

    public void setTalkerId(String talkerId) {
        this.talkerId = talkerId;
    }

    public String getBizClientMsgId() {
        return bizClientMsgId;
    }

    public void setBizClientMsgId(String bizClientMsgId) {
        this.bizClientMsgId = bizClientMsgId;
    }

    public String getBizChatId() {
        return bizChatId;
    }

    public void setBizChatId(String bizChatId) {
        this.bizChatId = bizChatId;
    }

    public String getBizChatUserId() {
        return bizChatUserId;
    }

    public void setBizChatUserId(String bizChatUserId) {
        this.bizChatUserId = bizChatUserId;
    }

    public String getMsgSeq() {
        return msgSeq;
    }

    public void setMsgSeq(String msgSeq) {
        this.msgSeq = msgSeq;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }


    @Override
    public String toString() {
        return "Message{" +
                "msgId='" + msgId + '\'' +
                ", type=" + type +
                ", createTime=" + createTime +
                ", talker='" + talker + '\'' +
                ", content='" + content + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", talkerId='" + talkerId + '\'' +
                '}';
    }
}
