package com.nicholasjohnstaples.plannerapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        String openWeatherString = "api.openweathermap.org/data/2.5/weather?zip=01545,us";
        new downloadWeather().execute(openWeatherString);

    }
    private class downloadWeather extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... strings){
            File weatherFile = new File(getFilesDir(), "weather.txt");
            try {
                URL website = new URL(strings[0]);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos;
                fos = openFileOutput("weather.txt", Context.MODE_PRIVATE);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();

                byte[] encoded = Files.readAllBytes(Paths.get("weather.txt"));
                String s = new String(encoded, "UTF-8");
                System.out.println(encoded.length);
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
