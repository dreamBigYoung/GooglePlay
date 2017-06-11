package com.itheima.googleplay.bean;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class CategoryInfoBean {

    public String name1;//经营
    public String name2;//竞速
    public String name3;
    public String url1;//image/category_game_9.jpg
    public String url2;//image/category_game_10.jpg
    public String url3;
    public String title;
    /*--------------- 额外添加一个字段 ---------------*/
    public boolean isTitle;

    @Override
    public String toString() {
        return "CategoryInfoBean{" +
                "name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3='" + name3 + '\'' +
                ", url1='" + url1 + '\'' +
                ", url2='" + url2 + '\'' +
                ", url3='" + url3 + '\'' +
                ", title='" + title + '\'' +
                ", isTitle=" + isTitle +
                '}';
    }
}
