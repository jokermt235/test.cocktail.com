package org.mutalip.cocktail.instance;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileInstance implements DbInstance {
    private static  String TAG = "FileInstance";
    private JSONArray data;
    private String filePath;
    public FileInstance(String path) throws IOException {
        filePath = path;
        Log.d(TAG, path);
        fileData();
    }
    private void fileData(){
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String response = stringBuilder.toString();
            if(response.isEmpty()){
                response = "[]";
            }
            data = new JSONArray(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public JSONArray getData(){
        return data;
    }
    public void save(JSONObject json){
        fileData();
        if(data != null) {
            data.put(json);
            File file = new File(filePath);
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(data.toString());
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void syncLocal(JSONArray data){
        this.saveAll(data);
    }

    private void saveAll(JSONArray data){
        File file = new File(filePath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
