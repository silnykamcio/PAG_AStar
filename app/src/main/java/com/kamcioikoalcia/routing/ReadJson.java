package com.kamcioikoalcia.routing;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

/**
 * Created by Alicja on 23.11.2017.
 */

public class ReadJson{

    public ReadJson(BufferedReader jsonFile){


        StringBuilder total = new StringBuilder(2048);
        String line;
        try {
            while ((line = jsonFile.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String js = total.toString();

        JsonReader jsonReader = new JsonReader(new StringReader(js));
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (Objects.equals(name, "features")) {
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()){
                        jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String name1 = jsonReader.nextName();

                        if (Objects.equals(name1, "geometry")) {
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String name2 = jsonReader.nextName();
                                if (Objects.equals(name2, "coordinates")) {
                                    jsonReader.beginArray();
                                    while (jsonReader.hasNext()) {
                                        jsonReader.beginArray();
                                        Double x = jsonReader.nextDouble();
                                        Double y = jsonReader.nextDouble();
                                        Log.wtf("Test", "x: " + x + " y: " + y);
                                        jsonReader.endArray();
                                    }
                                    jsonReader.endArray();
                                } else
                                    jsonReader.skipValue();
                            }
                            jsonReader.endObject();
                        } else
                            jsonReader.skipValue();
                    }
                    jsonReader.endObject();
                }
                jsonReader.endArray();
                }
                else
                    jsonReader.skipValue();
            }
            jsonReader.endObject();
            jsonReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
