import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
// consumindo api e request
public class PrevisaoApp {
    public static JSObject getPrevisaoTempoData(String locationName) {
        // pegando a locação e coordenadas usando a gelocalização
        JSONArray locationData = getLocationData(locationName);

        //pegando a localização das coordernadas usando api
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("Latitude");
        double longitude = (double) location.get("Longitude");

        //construindo uma request URL

        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude+ "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);



            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()){

                //lendo e armazenando arquivos string
                resultJson.append(scanner.nextLine());

            }

            scanner.close();


            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));


            JSONObject hourly = (JSONObject)  resultJsonObj.get("hourly");

            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);


            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            //pegando a umidade do ar
            JSONArray umidadeRelativa = (JSONArray) hourly.get("relativehumidity_2m");
            long humidity = (long) umidadeRelativa.get(index);

            //pegando velocidade nuvens
            JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
            double windspeed = (double)  windspeedData.get(index);

            //contruindo o acesso ao frontend com
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windspeed", windspeed);



        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    public static JSONArray getLocationData(String locatioName) {
        // request api
        locatioName = locatioName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locatioName + "&count=10&language=pt&format=json";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: could not connect to Api");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());


                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                scanner.close();

                //close url connection
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;

                // javascrip array X Object x JSON
                //  []   x  {} x  "{}"

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static int findIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();

        for (int i = 0; i< timeList.size(); i++){
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)){
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH' :00'");

        String formatterdDateTime = currentDateTime.format((formatter));

        return formatterdDateTime;
    }

    private static String convertWeatherCode(long weathercode){
        String weatherCondition = "";
        if (weathercode == 0L){
            weatherCondition = "Limpo";
        }else if (weathercode <= 3L && weathercode > 0L){
            weatherCondition = "Cloudy";
        } else if ((weathercode>= 51L && weathercode <= 67L)
                    || (weathercode >= 80L && weathercode <= 99L)) {

            weatherCondition = "Rain";
        } else if (weathercode >= 71 && weathercode <= 77L) {
            weatherCondition = "Neve";
        }
        return weatherCondition;
    }
}
