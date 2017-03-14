package dong.lan.sqlcipher.bean;

import android.content.ContentValues;

import com.alibaba.fastjson.JSON;

import net.sqlcipher.Cursor;

import java.sql.Blob;

/**
 * Created by 梁桂栋 on 17-3-11 ： 下午3:14.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: WeiXinHacker
 */

public class Message {

    public String msgId;
    public String msgSvrId;
    public int type;
    public int status;
    public int isSend;
    public int isShowTimer;
    public long createTime;
    public String talker;
    public String content;
    public String imgPath;
    public int reserved;
    public byte[] lvbuffer;
    public String transContent;
    public String transBrandWording;
    public String talkerId;
    public String bizClientMsgId;
    public String bizChatId;
    public String bizChatUserId;
    public String msgSeq;
    public int flag;

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
        lvbuffer = cursor.getBlob(cursor.getColumnIndex("lvbuffer"));
        transContent = cursor.getString(cursor.getColumnIndex("transContent"));
        transBrandWording = cursor.getString(cursor.getColumnIndex("transBrandWording"));
        talkerId = cursor.getString(cursor.getColumnIndex("talkerId"));
        bizClientMsgId = cursor.getString(cursor.getColumnIndex("bizClientMsgId"));
        bizChatId = cursor.getString(cursor.getColumnIndex("bizChatId"));
        bizChatUserId = cursor.getString(cursor.getColumnIndex("bizChatUserId"));
        msgSeq = cursor.getString(cursor.getColumnIndex("msgId"));
        flag = cursor.getInt(cursor.getColumnIndex("flag"));
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId='" + msgId + '\'' +
                ", msgSvrId='" + msgSvrId + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", isSend=" + isSend +
                ", isShowTimer=" + isShowTimer +
                ", createTime=" + createTime +
                ", talker='" + talker + '\'' +
                ", content='" + content + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", reserved=" + reserved +
                ", lvbuffer='" + lvbuffer + '\'' +
                ", transContent='" + transContent + '\'' +
                ", transBrandWording='" + transBrandWording + '\'' +
                ", talkerId='" + talkerId + '\'' +
                ", bizClientMsgId='" + bizClientMsgId + '\'' +
                ", bizChatId='" + bizChatId + '\'' +
                ", bizChatUserId='" + bizChatUserId + '\'' +
                ", msgSeq='" + msgSeq + '\'' +
                ", flag=" + flag +
                '}';
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("msgId", msgId);
        contentValues.put("msgSvrId", msgSvrId);
        contentValues.put("type", type);
        contentValues.put("status", status);
        contentValues.put("isSend", isSend);
        contentValues.put("isShowTimer", isShowTimer);
        contentValues.put("createTime", createTime);
        contentValues.put("talker", talker);
        contentValues.put("content", content);
        contentValues.put("imgPath", imgPath);
        contentValues.put("reserved", reserved);
        contentValues.put("lvbuffer", lvbuffer);
        contentValues.put("transContent", transContent);
        contentValues.put("transBrandWording", transBrandWording);
        contentValues.put("talkerId", talkerId);
        contentValues.put("bizClientMsgId", bizClientMsgId);
        contentValues.put("bizChatId", bizChatId);
        contentValues.put("bizChatUserId", bizChatUserId);
        contentValues.put("msgSeq", msgSeq);
        contentValues.put("flag", flag);
        return contentValues;
    }
}
