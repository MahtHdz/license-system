/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maht.id_service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author maht_
 */
public class JsonHandler {
    private String filename = null;
    
    public JSONObject readJSONFile(String JSONPath){
        JSONParser JSONP = new JSONParser();
        JSONObject JSONObj = null;
        try (FileReader file = new FileReader(JSONPath))
        {
            //Read JSON file
            Object obj = JSONP.parse(file);
            JSONObj = (JSONObject)obj; 
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }catch (ParseException ex) {
            Logger.getLogger(JsonHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JSONObj;
    }    
}
