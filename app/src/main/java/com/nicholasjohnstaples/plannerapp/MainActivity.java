package com.nicholasjohnstaples.plannerapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//import org.json.JSONException;
//import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String openWeatherString = "http://api.openweathermap.org/data/2.5/weather?zip=01545,us&APPID=be01d222728ea928de4ac69bd5678b63";
        new downloadWeather().execute(openWeatherString);

    }
    private class downloadWeather extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings){
            File weatherFile = new File(getFilesDir(), "weather.json");
            try {
                URL website = new URL(strings[0]);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos;
                fos = openFileOutput("weather.json", Context.MODE_PRIVATE);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();

                JSONObject jsonPageObject = (JSONObject) (new JSONParser().parse(new FileReader(weatherFile)));
                JSONObject main = (JSONObject) jsonPageObject.get("main");
                JSONArray weather = (JSONArray) jsonPageObject.get("weather");
                String s = "MAIN: " + main.toJSONString() + "\n\nWEATHER: " + weather.toJSONString();
                return s;
            } catch (MalformedURLException e){
                //new URL() throw
                System.out.println("Malformed!");
            } catch (FileNotFoundException e){
                //openFileOutput() throw
                System.out.println("File not found!");
            } catch (IOException e){
                //getChannel.transferFrom() throw
                System.out.println("IO Exception!");
            } catch (ParseException e){
                //thrown by JSONParser()
                System.out.println("Parse Exception!");
            }
            return "";
        }
        @Override
        protected void onProgressUpdate(Integer... progress){

        }
        @Override
        protected void onPostExecute(String result){
                TextView t = findViewById(R.id.textview1);
                t.setText(result);
        }

    }
}
