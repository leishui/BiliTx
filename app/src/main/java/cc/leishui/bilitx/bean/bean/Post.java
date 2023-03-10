package cc.leishui.bilitx.bean.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {
    public long postId;
    public String post_name;
    public String post_content;
    public String post_resources;
    public long uploader_id;
    public boolean source_type;
    public long view_count;
    public long comment_count;
    public long collection_count;
    public long like_count;
    public long upload_time;
    public long post_type;
    public User user;
    public ArrayList<String> postResourcesList;
}
