//Student Name: Alexandra Neal
//Student ID: S0906781

package org.me.gcu.neal_alexandra_s0906781;

import java.util.Date;

public class TrafficItem {
    private String title;
    private String desc;
    private String desc0;
    private String link;
    private String geo_rss;
    private String pubDate;
    private Date endDate;
    private Date startDate;
    private long duration;
    public String period;
    public String period2;
    public Boolean inc;
    private String colour;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public Boolean getInc() {return inc;}
    public void setInc(Boolean inc){this.inc = inc;}

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }

    public String getDesc0() { return desc0; }
    public void setDesc0(String desc0){
        this.desc0 = desc0;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link){
        this.link = link;
    }

    public String getGeoRss() {
        return geo_rss;
    }
    public void setGeoRss(String georss){
        this.geo_rss = georss;
    }

    public String getPubDate() {
        return pubDate;
    }
    public void setPubDate(String pubDate){
        this.pubDate = pubDate;
    }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date sdate){ this.startDate = sdate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date edate){ this.endDate = edate; }

    public String getPeriod() { return period; }
    public void setPeriod(String p){ this.period = p; }

    public String getPeriod2() { return period2; }
    public void setPeriod2(String p2){ this.period2 = p2; }

    public long getDuration() { return duration; }
    public void setDuration(long dur){ this.duration = dur; this.colour = calcColour(dur); }

    public String getColour() { return colour; }

    @Override
    public String toString() {
        return "TrafficItem :: Title=" + this.title + " Description=" + this.desc +
                " Link=" + this.link + " Date=" + this.pubDate;
    }

    //Creating the colour coding that appears in the main page for Roadworks and Planned Roadworks
    private String calcColour(Long d){
        String colour = "";
        int duration = d.intValue();
//        Log.e("MyTag",""+duration);

        if (duration >= 672){
            colour = "0400";
        } else if (duration >= 504) {
            colour = "322F";
        } else if (duration >= 336) {
            colour = "7969";
        } else if (duration >=168) {
            colour = "B96A";
        } else if (duration >= 72) {
            colour = "E786";
        } else {
            colour = "F3EB";
        }
        return "#FF"+colour;
    }
}
