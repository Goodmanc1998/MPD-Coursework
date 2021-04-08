package com.example.mpd_coursework;
/*
    Name : Connor Goodman
    ID : S1625004
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.DatePicker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private Button btnSearch, btnListView, btnSingleDay, btnDateRange;
    private ListView mainList;
    private ViewSwitcher vwSwitcher;

    private TextView txtDateRange;
    private DatePickerDialog datePicker;

    private ListItemFragment fragMagL, fragDepthL, fragDepthS, fragN, fragE, fragS, fragW;

    private String result = "";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    public ArrayList<Earthquake> eqList = new ArrayList<Earthquake>();
    public CustomArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.e("onCreate","Start");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vwSwitcher = (ViewSwitcher)findViewById(R.id.vwSwitcher);
        vwSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        vwSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);

        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        btnListView = (Button)findViewById(R.id.btnListView);
        btnListView.setOnClickListener(this);

        adapter = new CustomArrayAdapter(this,eqList);

        mainList = (ListView)findViewById(R.id.listView);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.e("onItemClicked","Clicked : " + position);

                Earthquake currWidget = adapter.getItem(position);

                Intent nIntent = new Intent(getApplicationContext(),ItemView.class);
                nIntent.putExtra("Single", currWidget);

                startActivity(nIntent);
            }
        });


        new AsyncParseTask().execute();

        setUpSearch();
        Log.e("onCreate","Finished");

    }

    public void setUpSearch()
    {
        txtDateRange = (TextView)findViewById(R.id.txtDateRange);

        btnSingleDay = (Button)findViewById(R.id.dateButton);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        btnSingleDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                // date picker dialog

                datePicker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                SearchDate(new Date(year - 1900, monthOfYear, dayOfMonth));

                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        btnDateRange = (Button)findViewById(R.id.btnDateRange);
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        builder.setTitleText("Select Date Range");

        builder.setSelection(new Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()));

        MaterialDatePicker materialDatePicker = builder.build();
        btnDateRange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "Date_Picker");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");

                String[] dates = materialDatePicker.getHeaderText().split("[–]");

                Date[] nDates = new Date[dates.length];

                for (int i = 0; i < dates.length; i++)
                {
                    nDates[i] = parseDate(dates[i], dateFormat);
                }

                SearchDateRange(nDates[0], nDates[1]);

            }
        });

        fragMagL = new ListItemFragment();
        fragDepthL = new ListItemFragment();
        fragDepthS = new ListItemFragment();

        fragN = new ListItemFragment();
        fragE = new ListItemFragment();
        fragS = new ListItemFragment();
        fragW = new ListItemFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragMagLarge, fragMagL)
                .replace(R.id.fragDepthLarth, fragDepthL)
                .replace(R.id.fragDepthSmall, fragDepthS)
                .replace(R.id.fragNorth, fragN)
                .replace(R.id.fragEast, fragE)
                .replace(R.id.fragSouth, fragS)
                .replace(R.id.fragWest, fragW)
                .commit();

        SearchDate(new Date());

    }

    public Date parseDate(String date, SimpleDateFormat formater)
    {
        Date nDate = null;
        String sDate = date.trim();

        try {

            nDate = formater.parse(sDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        nDate.setYear(Calendar.getInstance().get(Calendar.YEAR) - 1900);
        return nDate;

    }

    public void onClick(View view)
    {
        Log.e("onClick","Start");
        if(view == btnSearch)
        {
            vwSwitcher.showNext();
            SearchDate(eqList.get(0).getDateTime());
        }
        else if(view == btnListView)
        {
            vwSwitcher.showPrevious();
        }
        Log.e("onClick","Start");
    }

    public void SearchDateRange(Date dateBefore, Date dateAfter)
    {
        Earthquake eqLargestMag = null;
        Earthquake eqDepthL = null;
        Earthquake eqDepthS = null;
        Earthquake eqN = null;
        Earthquake eqE = null;
        Earthquake eqS = null;
        Earthquake eqW = null;

        Log.e("Date Range ", "Before Dates : " + dateBefore);
        Log.e("Date Range ", "After Date: " + dateAfter);

        for (int i = 0; i < eqList.size(); i++)
        {
            if(eqList.get(i).getDateTime().after(dateBefore) && !eqList.get(i).getDateTime().before(dateBefore))
            {
                if(eqList.get(i).getDateTime().before(dateAfter) && !eqList.get(i).getDateTime().after(dateAfter))
                {
                    //Largest Mag
                    if(eqLargestMag == null)
                        eqLargestMag = eqList.get(i);

                    if(eqList.get(i).getMagnitude() > eqLargestMag.getMagnitude())
                        eqLargestMag = eqList.get(i);

                    //Largest Depth
                    if(eqDepthL == null)
                        eqDepthL = eqList.get(i);

                    if(eqList.get(i).getDepth() > eqDepthL.getDepth())
                        eqDepthL = eqList.get(i);

                    //Smallest Depth
                    if(eqDepthS == null)
                        eqDepthS = eqList.get(i);

                    if(eqList.get(i).getDepth() < eqDepthS.getDepth())
                        eqDepthS = eqList.get(i);

                    if(eqN == null || eqE == null || eqS == null || eqW == null)
                    {
                        eqN = eqList.get(i);
                        eqE = eqList.get(i);
                        eqS = eqList.get(i);
                        eqW = eqList.get(i);
                    }

                    if(eqList.get(i).getGeoLat() > eqN.getGeoLat())
                        eqN = eqList.get(i);

                    if(eqList.get(i).getGeoLat() < eqS.getGeoLat())
                        eqS = eqList.get(i);

                    if(eqList.get(i).getGetLong() > eqE.getGetLong())
                        eqE = eqList.get(i);

                    if(eqList.get(i).getGetLong() < eqW.getGetLong())
                        eqW = eqList.get(i);


                }
            }

            fragMagL.setCurrEarthquake(eqLargestMag);
            fragDepthL.setCurrEarthquake(eqDepthL);
            fragDepthS.setCurrEarthquake(eqDepthS);

            fragN.setCurrEarthquake(eqN);
            fragE.setCurrEarthquake(eqE);
            fragS.setCurrEarthquake(eqS);
            fragW.setCurrEarthquake(eqW);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yy");
            txtDateRange.setText(dateFormat.format(dateBefore) + " - " + dateFormat.format(dateAfter));

        }

    }

    public void SearchDate(Date nDate)
    {
        Log.e("SearchDay", "nDate : " + nDate);

        Earthquake eqLargestMag = null;
        Earthquake eqDepthL = null;
        Earthquake eqDepthS = null;
        Earthquake eqN = null;
        Earthquake eqE = null;
        Earthquake eqS = null;
        Earthquake eqW = null;

        if(eqList.size() > 0)
        {
            for (int i = 0; i < eqList.size(); i++)
            {
                if(eqList.get(i).getDateTime().getDate() == nDate.getDate() && eqList.get(i).getDateTime().getDay() == nDate.getDay() && eqList.get(i).getDateTime().getYear() == nDate.getYear())
                {
                    //Largest Mag
                    if(eqLargestMag == null)
                        eqLargestMag = eqList.get(i);

                    if(eqList.get(i).getMagnitude() > eqLargestMag.getMagnitude())
                        eqLargestMag = eqList.get(i);

                    //Largest Depth
                    if(eqDepthL == null)
                        eqDepthL = eqList.get(i);

                    if(eqList.get(i).getDepth() > eqDepthL.getDepth())
                        eqDepthL = eqList.get(i);

                    //Smallest Depth
                    if(eqDepthS == null)
                        eqDepthS = eqList.get(i);

                    if(eqList.get(i).getDepth() < eqDepthS.getDepth())
                        eqDepthS = eqList.get(i);

                    if(eqN == null || eqE == null || eqS == null || eqW == null)
                    {
                        eqN = eqList.get(i);
                        eqE = eqList.get(i);
                        eqS = eqList.get(i);
                        eqW = eqList.get(i);
                    }

                    if(eqList.get(i).getGeoLat() > eqN.getGeoLat())
                        eqN = eqList.get(i);

                    if(eqList.get(i).getGeoLat() < eqS.getGeoLat())
                        eqS = eqList.get(i);

                    if(eqList.get(i).getGetLong() > eqE.getGetLong())
                        eqE = eqList.get(i);

                    if(eqList.get(i).getGetLong() < eqW.getGetLong())
                        eqW = eqList.get(i);

                }
            }


            fragMagL.setCurrEarthquake(eqLargestMag);
            fragDepthL.setCurrEarthquake(eqDepthL);
            fragDepthS.setCurrEarthquake(eqDepthS);

            fragN.setCurrEarthquake(eqN);
            fragE.setCurrEarthquake(eqE);
            fragS.setCurrEarthquake(eqS);
            fragW.setCurrEarthquake(eqW);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yy");
            txtDateRange.setText(dateFormat.format(nDate));


        }
    }

    @Override
    public void onBackPressed() {

        if(vwSwitcher.getCurrentView() != findViewById(R.id.page1))
        {
            vwSwitcher.showNext();
        }
        else
            super.onBackPressed();
    }

    private class AsyncParseTask extends AsyncTask<Void, Earthquake, Void>
    {
        Earthquake widget = null;
        ArrayList<Earthquake> newList = new ArrayList<Earthquake>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mainList.setAdapter(adapter);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("Run","Start");

            try
            {
                Log.e("Run","Start Parse");
                aurl = new URL(urlSource);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("Run","after ready");
                //
                // Throw away the first 2 header lines before parsing
                boolean item = false;

                while ((inputLine = in.readLine()) != null)
                {
                    result += inputLine;
                    //Log.e("MyTag",inputLine);
                }
                in.close();

            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            try
            {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(result));


                //Bool used to store when Data Starts
                boolean dataStart = false;
                //Storing the event
                int eventType = xpp.getEventType();
                //While the doc isnt finished
                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    //Storing the Tag name
                    String tagName = xpp.getName();

                    //If the event is a Start Tag
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(tagName.equalsIgnoreCase("item"))
                        {
                            //Log.e("Widget","New Widget");
                            //Creating a new widget
                            widget = new Earthquake();
                            dataStart = true;

                        }

                        //If within Item Tags
                        if(dataStart)
                        {
                            //Checking for each tag name

                            if(tagName.equalsIgnoreCase("title"))
                            {
                                //Storing the string within the Tags
                                String tempString = xpp.nextText();
                                //Storing the string within the Widget
                                widget.setTitle(tempString);
                            }
                            else if(tagName.equalsIgnoreCase("description"))
                            {
                                String tempString = xpp.nextText();
                                widget.setDescription(tempString);

                                //Storing the array of strings split by ; :
                                String[] strings = tempString.split("[;:]");
                                //For Each of these strings
                                for(int i = 0; i < strings.length; i++)
                                {
                                    //Log.e("MyTag","Description :" + strings[i]);

                                    //If the string is required string
                                    if(strings[i].equals(" Location"))
                                    {
                                        String location = strings[i + 1];
                                        widget.setLocation(location);
                                    }
                                    else if(strings[i].equals(" Depth"))
                                    {
                                        String processed = strings[i + 1].replace("km", "");
                                        //Parsing the string to Int
                                        int depth = Integer.parseInt(processed.trim());
                                        //Storing the Depth
                                        widget.setDepth(depth);
                                    }
                                    else if(strings[i].equals(" Magnitude"))
                                    {
                                        //Parsing the string to Int
                                        float mag = Float.parseFloat(strings[i + 1]);
                                        //Storing the Magnitude
                                        widget.setMagnitude(mag);

                                        //Log.e("MyTag","Mag :" + strings[i+1]);
                                    }
                                }
                            }
                            else if(tagName.equalsIgnoreCase("link"))
                            {
                                String tempString = xpp.nextText();
                                widget.setLink((tempString));
                            }
                            else if(tagName.equalsIgnoreCase("pubDate"))
                            {
                                String tempString = xpp.nextText();
                                widget.setDate(tempString);
                            }
                            else if(tagName.equalsIgnoreCase("category"))
                            {
                                String tempString = xpp.nextText();
                                widget.setCategory(tempString);
                            }
                            else if(tagName.equalsIgnoreCase("lat"))
                            {
                                String tempString = xpp.nextText();
                                widget.setLatString(tempString);

                                float geoLat = Float.parseFloat(tempString);
                                widget.setGeoLat(geoLat);
                            }
                            else if(tagName.equalsIgnoreCase("long"))
                            {
                                String tempString = xpp.nextText();
                                widget.setLongString(tempString);

                                float geoLong = Float.parseFloat(tempString);
                                widget.setGeoLong(geoLong);
                            }
                        }

                    } //If the event is a End Tag
                    else if(eventType == XmlPullParser.END_TAG)
                    {
                        //Checking for each tag name
                        if(tagName.equalsIgnoreCase("item"))
                        {
                            //Storing the widget within list
                            newList.add(widget);
                            //adapter.add(widget);
                            publishProgress(widget);
                            dataStart = false;
                        }
                        else if(tagName.equalsIgnoreCase("channel"))
                        {
                            int size;
                            size = newList.size();
                            Log.e("MyTag","THE SIZE OF THIS STUFF " + size);
                        }
                    }
                    // Get the next event
                    eventType = xpp.next();
                }
            }
            catch (XmlPullParserException ae1)
            {
                Log.e("MyTag","Parsing error  " + ae1.toString());
            }
            catch (IOException ae1)
            {
                Log.e("MyTag","IO error during parsing");
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Earthquake... values) {
            super.onProgressUpdate(values);
            adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Returning the list
            eqList = newList;

        }
    }
}