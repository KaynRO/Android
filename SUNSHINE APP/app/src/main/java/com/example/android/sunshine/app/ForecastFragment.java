package com.example.android.sunshine.app;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Grigoras on 04.01.2017.
 */

public class ForecastFragment extends Fragment {

    ListView list ;

    String[] text = {
            "1" ,
            "2" ,
            "3" ,
            "4" ,
            "5" ,
            "6" ,
            "7" ,
    } ;
    Integer[] imageId = {
            R.mipmap.art_clear ,
            R.mipmap.art_storm ,
            R.mipmap.art_rain ,
            R.mipmap.art_light_rain ,
            R.mipmap.art_light_clouds ,
            R.mipmap.art_snow ,
            R.mipmap.art_clouds ,

    } ;

    CustomAdapter adapter ;

    public ForecastFragment() {
    }

    @Override
    public void onStart() {

        super.onStart() ;
        updateWeather() ;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = new CustomAdapter(getActivity(), text, imageId);
        list = (ListView) rootView.findViewById(R.id.listview_forecast);
        list.setAdapter(adapter);

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {

        FetchForecastTask weatherTask = new FetchForecastTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default)) ;

        weatherTask.execute(location);
    }

    public class FetchForecastTask extends AsyncTask <String, Void, String[] > {

        @Override
        protected String[] doInBackground(String... params) {

            if ( params.length == 0 )
                return null ;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            String format = "json" ;
            String units = "metric" ;
            int nrDays = 7 ;
            String API_Key = "bed134bc100449c199f35380232370c7" ;

            try {
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?" ;
                final String QUERY_PARAM = "q" ;
                final String FORMAT_PARAM = "mode" ;
                final String UNITS_PARAM = "units" ;
                final String DAYS_PARAM = "cnt" ;
                final String APY_PARAM = "APPID" ;

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(nrDays))
                        .appendQueryParameter(APY_PARAM, API_Key)
                        .build() ;

                URL url = new URL(builtUri.toString()) ;

                Log.v("ForecastFragment" , "Build URI" + builtUri.toString() ) ;

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.v("ForecastFragment", "Forecast JSON String: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e("ForecastFragment", "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }

            }
            try {
                getWeatherDataFromJson(forecastJsonStr, nrDays) ;
            } catch (JSONException e) {
                Log.e("ForecastFragment" , e.getMessage(), e) ;
                e.printStackTrace();
            }
            return null;
        }

        private String getReadableDateString(long time){

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd") ;
            return dateFormat.format(time) ;

        }

        private String formatHighLows(double high, double low) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
            String unitType = prefs.getString(getString(R.string.pref_unit_key), getString(R.string.pref_unit_metric)) ;

            if( unitType.equals(getString(R.string.pref_unit_imperial))) {

                high = ( high * 1.8 ) + 32 ;
                low = ( low * 1.8 ) + 32 ;
            } else if ( !unitType.equals(getString(R.string.pref_unit_metric)) ) {

                Log.d("ForecastFragment", "Unit type not found : " + unitType ) ;
            }

            long highRounded = Math.round(high) ;
            long lowRounded = Math.round(low) ;

            String HighLowStr = highRounded + " / " + lowRounded ;
            return HighLowStr ;

        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Integer getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DESCRIPTION = "description" ;
            final String OWN_ID = "id" ;

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            Time dayTime = new Time();
            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
            dayTime = new Time();

            for(int i = 0; i < weatherArray.length(); i++) {

                String day;
                String description;
                String weatherID ;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime;
                // Cheating to convert this to UTC time, which is what we want anyhow
                dateTime = dayTime.setJulianDay(julianStartDay+i);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                weatherID = weatherObject.getString(OWN_ID) ;

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                int weatherId = Integer.valueOf(weatherID) ;


                highAndLow = formatHighLows(high, low);
                text[i] = day + "    -     " + highAndLow + '\n' + "       " + description;

                if (weatherId >= 200 && weatherId <= 232) {
                    imageId[i] =  R.mipmap.art_storm;
                } else if (weatherId >= 300 && weatherId <= 321) {
                    imageId[i] =  R.mipmap.art_light_rain;
                } else if (weatherId >= 500 && weatherId <= 504) {
                    imageId[i] =  R.mipmap.art_rain;
                } else if (weatherId == 511) {
                    imageId[i] =  R.mipmap.art_snow;
                } else if (weatherId >= 520 && weatherId <= 531) {
                    imageId[i] =  R.mipmap.art_rain;
                } else if (weatherId >= 600 && weatherId <= 622) {
                    imageId[i] =  R.mipmap.art_snow;
                } else if (weatherId >= 701 && weatherId <= 761) {
                    imageId[i] =  R.mipmap.art_fog;
                } else if (weatherId == 761 || weatherId == 781) {
                    imageId[i] =  R.mipmap.art_storm;
                } else if (weatherId == 800) {
                    imageId[i] =  R.mipmap.art_clear;
                } else if (weatherId == 801) {
                    imageId[i] =  R.mipmap.art_light_clouds;
                } else if (weatherId >= 802 && weatherId <= 804) {
                    imageId[i] =  R.mipmap.art_clouds;
                }
            }

            return 1 ;
        }

        @Override
        protected void onPostExecute( String[] result ) {
            if ( result != null ) {
                text = new String[8];
                imageId = new Integer[8];
            }

            adapter.notifyDataSetChanged();
        }
    }
}

