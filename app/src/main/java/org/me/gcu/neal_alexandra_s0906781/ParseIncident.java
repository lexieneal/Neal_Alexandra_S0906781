//Student Name: Alexandra Neal
//Student ID: S0906781

package org.me.gcu.neal_alexandra_s0906781;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class ParseIncident {
    private static final String TAG = "ParseIncidents";
    private ArrayList<TrafficItem> applications;

    public ParseIncident() {
        this.applications = new ArrayList<>();
    }

    private Date[] getDates(String[] parts){
        Date FormatStartDate = null;
        Date FormatEndDate = null;

        try{
        //Locates date in the String 'Description' and extracts the date
        String sDate = parts[0].substring(12);
        String eDate = parts[1].substring(10);

        //Formats this new String into SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMMMM yyyy - HH:mm", Locale.ENGLISH);
        Date sd = sdf.parse(sDate);
        Date ed = sdf.parse(eDate);

        //Reformats this into what we need
        sdf.applyPattern("dd/MM/yy HH:mm");
        String startDate = sdf.format(sd);
        String endDate = sdf.format(ed);
        FormatStartDate = sdf.parse(startDate);
        FormatEndDate = sdf.parse(endDate);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return new Date[]{FormatStartDate, FormatEndDate};
    };

    private long getDur (Date[] dates){
        //Gets the time between these two dates aka the duration
        Date startDate = dates[0];
        Date endDate = dates[1];

        //Converts duration from milliseconds into hours
        long durInMs = Math.abs(endDate.getTime() - startDate.getTime());
        long durInHrs = TimeUnit.HOURS.convert(durInMs, TimeUnit.MILLISECONDS);
//        Log.d(TAG, "DATES, duration: " +durInHrs);

        return durInHrs;
    }

    public ArrayList<TrafficItem> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        TrafficItem currentTrafficItem = null;
        boolean inEntry = false;
        String textValue = "";
        Log.d(TAG, "Starting to parse the XML Data: " + xmlData);

        try {
                Log.d(TAG, "Parse Debug 1");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
//            eventType = xpp.next();
//
            Log.d(TAG, "Parse Debug 2: Before first item");

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if("item".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentTrafficItem = new TrafficItem();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(inEntry) {
                            if("item".equalsIgnoreCase(tagName)) {
                                applications.add(currentTrafficItem);
                                inEntry = false;
                            }else if("title".equalsIgnoreCase(tagName)) {
                                currentTrafficItem.setTitle(textValue);
                            }else if("description".equalsIgnoreCase(tagName)) {
                                Log.d("MyTag", "Unedited Description "+textValue);
                                currentTrafficItem.setDesc0(textValue);
                                if (textValue.contains("Start Date:")) {
                                    String[] parts = textValue.split("<br />");
                                    Date dates[] = getDates(parts);
                                    long duration = getDur(dates);
                                    Log.d("MyTag", ""+parts[0]);
                                    Log.d("MyTag", ""+parts[1]);
                                    Log.d("MyTag", "Hours: "+duration);

                                    String period = dates[0].toString().substring(0, 10) + " - " + dates[1].toString().substring(0, 10) ;
                                    String period2 = dates[0].toString().substring(0, 23) + " - " + dates[1].toString().substring(0, 23) ;

                                    currentTrafficItem.setDesc(parts[2]);
                                    currentTrafficItem.setStartDate(dates[0]);
                                    currentTrafficItem.setEndDate(dates[1]);
                                    currentTrafficItem.setPeriod(period);
                                    currentTrafficItem.setPeriod2(period2);
                                    currentTrafficItem.setDuration(duration);
                                    currentTrafficItem.setInc(false);
                                } else {currentTrafficItem.setInc(true); currentTrafficItem.setDesc(textValue);}
                            }else if("link".equalsIgnoreCase(tagName)) {
                                currentTrafficItem.setLink(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();

            }
            Log.d("ParseIncidents", "Finished Parsing items");
            for (TrafficItem app: applications) {
            }

        } catch(Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }

}