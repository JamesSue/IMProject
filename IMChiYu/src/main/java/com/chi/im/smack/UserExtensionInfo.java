package com.chi.im.smack;

import org.jivesoftware.smack.packet.ExtensionElement;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/9.
 */
public class UserExtensionInfo implements ExtensionElement ,Serializable{
    private String elementName = "userInfo";
    private String headUrl;//头像
//    private String sex;// "1"表示男   "0"表示女
//    private String telNo;//手机号码
//    private String adr;//地址
    private String motto;//格言
    private String namespace="cccc";




    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public String getElementName() {
        return null;
    }

    @Override
    public CharSequence toXML() {
        StringBuffer  sb=new StringBuffer();

        sb.append("<userInfo>");
        sb.append("<headUrl>");
        sb.append(headUrl);
        sb.append("</headUrl>");
        sb.append("<motto>");
        sb.append(motto);
        sb.append("</motto>");
        sb.append("</userInfo>");

        return sb.toString();
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public String getMotto() {
        return motto;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }


}
