package com.itheima.googleplay.bean;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class ItemBean {
    public long id;
    public String des;
    public String packageName;
    public float stars;
    public String iconUrl;
    public String name;
    public String downloadUrl;
    public long size;
    /*--------------- 添加详情页面里面的额外字段 ---------------*/

    public String author;// 黑马程序员
    public String date;//  2015-06-10
    public String downloadNum;//	40万+
    public String version;// 1.1.0605.0
    public List<ItemSafeBean> safe;// Array
    public List<String> screen;//Array

    public class ItemSafeBean {
        public String safeDes;// 已通过安智市场安全检测，请放心使用
        public int safeDesColor;//	0
        public String safeDesUrl;//	app/com.itheima.www/safeDesUrl0.jpg
        public String safeUrl;// app/com.itheima.www/safeIcon0.jpg

        @Override
        public String toString() {
            return "ItemSafeBean{" +
                    "safeDes='" + safeDes + '\'' +
                    ", safeDesColor=" + safeDesColor +
                    ", safeDesUrl='" + safeDesUrl + '\'' +
                    ", safeUrl='" + safeUrl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "id=" + id +
                ", des='" + des + '\'' +
                ", packageName='" + packageName + '\'' +
                ", stars=" + stars +
                ", iconUrl='" + iconUrl + '\'' +
                ", name='" + name + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", size=" + size +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", downloadNum='" + downloadNum + '\'' +
                ", version='" + version + '\'' +
                ", safe=" + safe +
                ", screen=" + screen +
                '}';
    }
}
