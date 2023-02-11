package cc.leishui.bilitx.bean.biliBean;

import java.util.List;

public class PlayUrl {
    /**
     * code : 0
     * data : {"accept_format":"mp4,mp4","last_play_cid":0,"durl":[{"size":3719133,"ahead":"","length":123855,"vhead":"","backup_url":["https://upos-sz-estgoss.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=upos&oi=1863412659&trid=25a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=799302bd6a71f3a6f740c483b47087d0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=alibv&oi=1863412659&trid=25a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=384157a324a16e8e8ecac071fe82004a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=40000000"],"url":"https://cn-ahhn-cm-01-02.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=bcache&oi=1863412659&trid=000025a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=d0ade00a587bc7a6cf185216d6ee4c86&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&cdnid=10099&bvc=vod&nettype=0&orderid=0,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=80000000","order":1}],"seek_param":"start","format":"mp4","support_formats":[{"display_desc":"1080P","superscript":"","codecs":null,"format":"mp4","quality":80,"new_description":"1080P 高清"},{"display_desc":"360P","superscript":"","codecs":null,"format":"mp4","quality":16,"new_description":"360P 流畅"}],"message":"","accept_quality":[80,16],"high_format":null,"quality":16,"timelength":123855,"result":"suee","seek_type":"second","last_play_time":0,"from":"local","video_codecid":7,"accept_description":["高清 1080P","流畅 360P"]}
     * message : 0
     * ttl : 1
     */
    private int code;
    private DataEntity data;
    private String message;
    private int ttl;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getTtl() {
        return ttl;
    }

    public class DataEntity {
        /**
         * accept_format : mp4,mp4
         * last_play_cid : 0
         * durl : [{"size":3719133,"ahead":"","length":123855,"vhead":"","backup_url":["https://upos-sz-estgoss.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=upos&oi=1863412659&trid=25a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=799302bd6a71f3a6f740c483b47087d0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=alibv&oi=1863412659&trid=25a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=384157a324a16e8e8ecac071fe82004a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=40000000"],"url":"https://cn-ahhn-cm-01-02.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=bcache&oi=1863412659&trid=000025a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=d0ade00a587bc7a6cf185216d6ee4c86&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&cdnid=10099&bvc=vod&nettype=0&orderid=0,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=80000000","order":1}]
         * seek_param : start
         * format : mp4
         * support_formats : [{"display_desc":"1080P","superscript":"","codecs":null,"format":"mp4","quality":80,"new_description":"1080P 高清"},{"display_desc":"360P","superscript":"","codecs":null,"format":"mp4","quality":16,"new_description":"360P 流畅"}]
         * message :
         * accept_quality : [80,16]
         * high_format : null
         * quality : 16
         * timelength : 123855
         * result : suee
         * seek_type : second
         * last_play_time : 0
         * from : local
         * video_codecid : 7
         * accept_description : ["高清 1080P","流畅 360P"]
         */
        private String accept_format;
        private int last_play_cid;
        private List<DurlEntity> durl;
        private String seek_param;
        private String format;
        private List<Support_formatsEntity> support_formats;
        private String message;
        private List<Integer> accept_quality;
        private String high_format;
        private int quality;
        private int timelength;
        private String result;
        private String seek_type;
        private int last_play_time;
        private String from;
        private int video_codecid;
        private List<String> accept_description;

        public void setAccept_format(String accept_format) {
            this.accept_format = accept_format;
        }

        public void setLast_play_cid(int last_play_cid) {
            this.last_play_cid = last_play_cid;
        }

        public void setDurl(List<DurlEntity> durl) {
            this.durl = durl;
        }

        public void setSeek_param(String seek_param) {
            this.seek_param = seek_param;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public void setSupport_formats(List<Support_formatsEntity> support_formats) {
            this.support_formats = support_formats;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setAccept_quality(List<Integer> accept_quality) {
            this.accept_quality = accept_quality;
        }

        public void setHigh_format(String high_format) {
            this.high_format = high_format;
        }

        public void setQuality(int quality) {
            this.quality = quality;
        }

        public void setTimelength(int timelength) {
            this.timelength = timelength;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public void setSeek_type(String seek_type) {
            this.seek_type = seek_type;
        }

        public void setLast_play_time(int last_play_time) {
            this.last_play_time = last_play_time;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public void setVideo_codecid(int video_codecid) {
            this.video_codecid = video_codecid;
        }

        public void setAccept_description(List<String> accept_description) {
            this.accept_description = accept_description;
        }

        public String getAccept_format() {
            return accept_format;
        }

        public int getLast_play_cid() {
            return last_play_cid;
        }

        public List<DurlEntity> getDurl() {
            return durl;
        }

        public String getSeek_param() {
            return seek_param;
        }

        public String getFormat() {
            return format;
        }

        public List<Support_formatsEntity> getSupport_formats() {
            return support_formats;
        }

        public String getMessage() {
            return message;
        }

        public List<Integer> getAccept_quality() {
            return accept_quality;
        }

        public String getHigh_format() {
            return high_format;
        }

        public int getQuality() {
            return quality;
        }

        public int getTimelength() {
            return timelength;
        }

        public String getResult() {
            return result;
        }

        public String getSeek_type() {
            return seek_type;
        }

        public int getLast_play_time() {
            return last_play_time;
        }

        public String getFrom() {
            return from;
        }

        public int getVideo_codecid() {
            return video_codecid;
        }

        public List<String> getAccept_description() {
            return accept_description;
        }

        public class DurlEntity {
            /**
             * size : 3719133
             * ahead :
             * length : 123855
             * vhead :
             * backup_url : ["https://upos-sz-estgoss.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=upos&oi=1863412659&trid=25a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=799302bd6a71f3a6f740c483b47087d0&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=1,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=40000000","https://upos-sz-mirrorali.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=alibv&oi=1863412659&trid=25a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=384157a324a16e8e8ecac071fe82004a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&bvc=vod&nettype=0&orderid=2,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=40000000"]
             * url : https://cn-ahhn-cm-01-02.bilivideo.com/upgcxcode/70/55/977375570/977375570_nb3-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1674575829&gen=playurlv2&os=bcache&oi=1863412659&trid=000025a775b8167c4cfe81bbdeeecbda99d7u&mid=100842571&platform=pc&upsig=d0ade00a587bc7a6cf185216d6ee4c86&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform&cdnid=10099&bvc=vod&nettype=0&orderid=0,3&buvid=DA45F02F-F4ED-2538-E21C-622579B04B5C51105infoc&build=0&agrr=0&bw=30236&logo=80000000
             * order : 1
             */
            private int size;
            private String ahead;
            private int length;
            private String vhead;
            private List<String> backup_url;
            private String url;
            private int order;

            public void setSize(int size) {
                this.size = size;
            }

            public void setAhead(String ahead) {
                this.ahead = ahead;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public void setVhead(String vhead) {
                this.vhead = vhead;
            }

            public void setBackup_url(List<String> backup_url) {
                this.backup_url = backup_url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public int getSize() {
                return size;
            }

            public String getAhead() {
                return ahead;
            }

            public int getLength() {
                return length;
            }

            public String getVhead() {
                return vhead;
            }

            public List<String> getBackup_url() {
                return backup_url;
            }

            public String getUrl() {
                return url;
            }

            public int getOrder() {
                return order;
            }
        }

        public class Support_formatsEntity {
            /**
             * display_desc : 1080P
             * superscript :
             * codecs : null
             * format : mp4
             * quality : 80
             * new_description : 1080P 高清
             */
            private String display_desc;
            private String superscript;
            private String codecs;
            private String format;
            private int quality;
            private String new_description;

            public void setDisplay_desc(String display_desc) {
                this.display_desc = display_desc;
            }

            public void setSuperscript(String superscript) {
                this.superscript = superscript;
            }

            public void setCodecs(String codecs) {
                this.codecs = codecs;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public void setQuality(int quality) {
                this.quality = quality;
            }

            public void setNew_description(String new_description) {
                this.new_description = new_description;
            }

            public String getDisplay_desc() {
                return display_desc;
            }

            public String getSuperscript() {
                return superscript;
            }

            public String getCodecs() {
                return codecs;
            }

            public String getFormat() {
                return format;
            }

            public int getQuality() {
                return quality;
            }

            public String getNew_description() {
                return new_description;
            }
        }
    }
}
