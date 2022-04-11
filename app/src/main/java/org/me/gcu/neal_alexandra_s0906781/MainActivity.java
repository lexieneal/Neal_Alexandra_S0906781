//Student Name: Alexandra Neal
//Student ID: S0906781

package org.me.gcu.neal_alexandra_s0906781;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView itemsTitle;

    public ArrayAdapter adapter;

    private String result = "" ;
    private Button startIncidentsButton;
    private Button startPlannedButton;
    private Button startRoadworksButton;
    private Button loadButton;

    // Traffic Scotland URLs
    private String roadworksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private ArrayList<TrafficItem> itemArrayList;

    private ListView lv;
    private EditText search;

    private String titlesP[] = {};
    private String titlesI[] = {};
    private String titlesR[] = {};
    private ArrayList<TrafficItem> itemsI;
    private ArrayList<TrafficItem> itemsR;
    private ArrayList<TrafficItem> itemsP;

    private ProgressBar spinner;
    private String blankTitles[] = {};
    private ArrayList<TrafficItem> test = new ArrayList<>();
    TrafficItem t = new TrafficItem();
    private myAdapter adapter1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Checking process", "Step1");
        //Removes the Top Bar from the App
        getSupportActionBar().hide();

        Log.e("Checking process", "Step2");
        test.add(t);

        Log.e("Checking process", "Step3");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsTitle = (TextView) findViewById(R.id.itemsTitle);

        loadButton = (Button) findViewById(R.id.load);
        loadButton.setOnClickListener(this);

        startIncidentsButton = (Button) findViewById(R.id.incidentsButton);
        startIncidentsButton.setOnClickListener(this);

        startRoadworksButton = (Button) findViewById(R.id.roadworksButton);
        startRoadworksButton.setOnClickListener(this);

        startPlannedButton = (Button) findViewById(R.id.plannedButton);
        startPlannedButton.setOnClickListener(this);


        spinner = (ProgressBar)findViewById(R.id.loadingBar);
        spinner.setVisibility(View.GONE);


        lv = (ListView) findViewById(R.id.trafficList);
            search = (EditText) findViewById(R.id.searchBar);
                search.setVisibility(View.INVISIBLE);
        Log.d("Debugging", "Step4");

        adapter1 = new myAdapter(this, test);
        Log.d("Debugging", "Step5");
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("Checking process", "Step6");
                System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                if (count < before) {
                    adapter1.resetData();
                }
                adapter1.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("Checking process", "Step7");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("Checking process", "Step8");

            }
        });
    }


    public void clear(){
        Log.d("Checking process", "Step9");
        Log.d("MyTag","Clered Incidents: " + titlesI.length);
        Log.d("MyTag","Cleared Planned Roadworks: " + titlesP.length);
        Log.d("MyTag","Cleared Roadworks: " + titlesR.length);

        String blank[] = {};
        adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                blank);
        lv.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        search.setVisibility(View.INVISIBLE);
        Log.e("Checking process", "Step10");

    }


    public void setAdapter(String type){
        ArrayAdapter<String> adapter;
        myAdapter ad;

        switch(type) {
            case "incident":
                Log.e("MyTag","Incidents: "+itemsI);

                adapter1 = new myAdapter(
                        MainActivity.this,
                        itemsI);
                lv.setAdapter(adapter1);
                break;

            case "roadworks":
                Log.e("MyTag","Roadworks: "+itemsR);
                adapter1 = new myAdapter(
                        MainActivity.this,
                        itemsR);
                lv.setAdapter(adapter1);
                break;

            case "planned":
                Log.e("MyTag","Planned Roadworks: "+itemsP);
                adapter1 = new myAdapter(
                        MainActivity.this,
                        itemsP);
                lv.setAdapter(adapter1);
                break;
        }
    }

//For when you select an option of either roadworks, incidents & planned
//roadworks from the main page. Loads from feed.
    public void onClick(View view) {
        String type;
        Log.d("MyTag","User Made Selection");

        switch(view.getId()){

            //For the Incidents button
            case R.id.incidentsButton:
                type = "incident";
                Log.d("MyTag","Selection Chosen: "+type);
                itemsTitle.setText("Incidents");
                if (titlesI.length != 0){
                    Log.e("MyTag","incident titles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(incidentsURL, type);
                }
                break;

            //For the Roadworks button
            case R.id.roadworksButton:
                type = "roadworks";
                Log.d("MyTag","Selection Chosen: "+type);
                itemsTitle.setText("Roadworks");
                if (titlesR.length != 0){
                    Log.e("MyTag","roadworks titles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(roadworksURL, type);
                }
                break;

            //For the Planned Roadworks button
            case R.id.plannedButton:
                type = "planned";
                Log.d("MyTag","Selection Chosen: "+type);
                itemsTitle.setText("Planned Roadworks");
                if (titlesP.length != 0){
                    Log.e("MyTag","planned titles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(plannedURL, type);
                    }
                break;

            case R.id.load:
                startPlannedButton.setVisibility((startPlannedButton.getVisibility() == View.VISIBLE)
                        ? View.GONE
                        : View.VISIBLE);
                startRoadworksButton.setVisibility((startRoadworksButton.getVisibility() == View.VISIBLE)
                        ? View.GONE
                        : View.VISIBLE);
                startIncidentsButton.setVisibility((startIncidentsButton.getVisibility() == View.VISIBLE)
                        ? View.GONE
                        : View.VISIBLE);
        }
    }

    public void startProgress(String url, String type) {
        String[] urlAndType = {url, type};
        new downloadTask().execute(urlAndType);
    }

    public ArrayList handleResult(String data){
        ParseIncident p = new ParseIncident();
        p.parse(data);
        return p.getApplications();
    }

    private class downloadTask extends AsyncTask<String[], Void, ArrayList<TrafficItem>> {

        String tType;

        @Override
        public ArrayList<TrafficItem> doInBackground(String[]... params) {
            tType = params[0][1];

            Log.d("MyTag","In background, params: "+ params[0][0]+ tType);
            return getData(params[0][0]);
        }

        @Override
        protected void onPostExecute(ArrayList<TrafficItem> result) {
            super.onPostExecute(result);
            spinner.setVisibility(View.GONE);
            if(result.get(0).getTitle() == "No Incidents Found") {
            }else{
                search.setVisibility(View.VISIBLE);
            }

            setAdapter(tType);
        }

        private ArrayList<TrafficItem> getData(String url)
        {
            URL url1;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            try{
                result = "";
                url1 = new URL(url);
                yc = url1.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.d("MyTag","Retrieving data from:  " + url);

                while ((inputLine = in.readLine()) != null){
                    result = result + inputLine;
                }
                Log.i("MyTag", "Successful. Result: " + result);
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "Error: IOException");
            }

            // Once XML data is collected it is then parsed

            ArrayList<TrafficItem> items= handleResult(result);
            Log.d("MyTag", "Total items parsed: "+ items.size());

            String titles[];
            int i = 0;
            titles = new String [items.size()];
            for (TrafficItem o : items){
                titles[i] = o.getTitle();
                i++;
            }

            if (items.size() == 0) {
                TrafficItem neu = new TrafficItem();
                neu.setTitle("No Incidents Found");
                neu.setDesc("None");
                neu.setPubDate("Mon, 01 Jan 2020 00:00:00 GMT");
                neu.setLink("");
                neu.setInc(true);
                items.add(neu);
            }

            switch(tType) {
                case "incident":
                    Log.d("MyTag", "Incidents " + items);
                    titlesI = titles;
                    itemsI = items;
                    break;
                case "roadworks":
                    Log.d("MyTag", "Roadworks " + items);
                    titlesR = titles;
                    itemsR = items;
                    break;
                case "planned":
                    Log.d("MyTag", "Planned Roadworks " + items);
                    titlesP = titles;
                    itemsP = items;
                    break;
            }
            return items;
        }
    }

}