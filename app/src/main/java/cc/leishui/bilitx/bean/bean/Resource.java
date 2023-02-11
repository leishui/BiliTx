package cc.leishui.bilitx.bean.bean;

public class Resource {
    public long id;
    public String url;
    public long up_id;

    @Override
    public String toString() {
        return "?id=" + up_id + "&name=" + url;
    }
}
