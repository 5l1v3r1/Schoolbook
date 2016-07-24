package com.marco.marplex.schoolbook.connections;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.marco.marplex.schoolbook.interfaces.ClassevivaCallback;
import com.marco.marplex.schoolbook.interfaces.ClassevivaLoginCallback;
import com.marco.marplex.schoolbook.models.Argument;
import com.marco.marplex.schoolbook.models.Comunication;
import com.marco.marplex.schoolbook.models.Evento;
import com.marco.marplex.schoolbook.models.Note;
import com.marco.marplex.schoolbook.models.Voto;
import com.marco.marplex.schoolbook.utilities.Credentials;
import com.marco.marplex.schoolbook.utilities.Cripter;
import com.marco.marplex.schoolbook.utilities.SharedPreferences;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.marco.marplex.schoolbook.utilities.Credentials.getPassword;

/**
 * Created by marco on 1/27/16.
 */
public class ClassevivaCaller {

    private final String[] servers = new String[]{
        "http://schoolbook1.altervista.org/",
        "http://schoolbook2.altervista.org/",
        "http://schoolbook3.altervista.org/",
        "http://schoolbook.x10.mx/"
    };

    protected final String TAG = "Classeviva Login";
    private final String userAgent = "Mozilla/5.0 (compatible;  MSIE 7.01; Windows NT 5.0)";

    ClassevivaLoginCallback mClassevivaLoginCallback;
    ClassevivaCallback mCallback;

    String mUser, mPassword;
    Context c;

    OkHttpClient client;

    private String subject;

    /**
     * ClassevivaCaller constructor. Use it to import user datas.
     *
     * @param username  User's username
     * @param password  User's mPassword
     * @param classevivaLoginCallback  Interface which return the requested html string
     * @param c  Activity context
     *
     */
    public ClassevivaCaller(String username, String password, ClassevivaLoginCallback classevivaLoginCallback, Context c){
        this.mUser = username;
        this.mPassword = password;
        this.c = c;
        this.mClassevivaLoginCallback = classevivaLoginCallback;
        this.client = new OkHttpClient();
    }

    public ClassevivaCaller(ClassevivaCallback callback, Context context) {
        this.mCallback = callback;
        this.mUser = Credentials.getName(context);
        this.mPassword = Credentials.getPassword(context);
        this.client = new OkHttpClient();
        this.c = context;
    }

    public void withSubject(String subject){
        this.subject = subject;
    }

    private void run(final HashMap<String, String> parameters, final EndpointsCallback callback) throws IOException {

        int max = servers.length - 1;
        int randomNumber = new Random().nextInt(max + 1);
        String url = mergeUrl(servers[randomNumber], parameters);

        final Request request = new Request.Builder()
                .url(url)
                .build();

        final Callback switcherCallback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    run(parameters, callback);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if (body.startsWith("{") || body.startsWith("[")) callback.onResponse(body);
                else {
                    try {
                        run(parameters, callback);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };

        client.newCall(request).enqueue(switcherCallback);
    }

    private void run(String url, final EndpointsCallback callback) throws IOException {

        String session = Credentials.getSession(c);
        final Request request = new Request.Builder()
                .url(url)
                .header("Set-Cookie", "PHPSESSID="+session)
                .header("Cookie", "PHPSESSID="+session)
                .header("User-Agent", userAgent)
                .build();

        final Callback switcherCallback = new Callback() {
            @Override public void onFailure(Call call, IOException e) {}
            @Override public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                callback.onResponse(body);
            }
        };

        client.newCall(request).enqueue(switcherCallback);
    }

    /**
     * Call Classeviva login page with saved or requested credentials
     */
    public void doLogin() {
        //Perform new HTTP call
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("usercode", mUser);
            parameters.put("password", mPassword);
            parameters.put("login", "true");
            run(parameters,
                    new EndpointsCallback() {
                @Override
                public void onResponse(String json) {
                    boolean success;
                    try {
                        JSONObject object = new JSONObject(json);
                        success = object.getString("status").equals("OK");

                        if(success) Credentials.saveCredentials(c, mUser, mPassword, object.getString("sessionId"));

                    }catch (JSONException e){
                        //Value cannot be converted to a JSONObject
                        success = false;
                    }

                    mClassevivaLoginCallback.onLoginDone(success);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call ClasseViva votes page with the current session
     */
    public void getVotes(){
        //Perform new HTTP call
        try {
            String session = Credentials.getSession(c);
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("session", session);
            parameters.put("votes", "true");
            run(parameters,
                    new EndpointsCallback() {
                @Override public void onResponse(String json){
                    try {
                        if(!checkStatus(json, ClassevivaCaller.class.getMethod("getVotes", null))) return;

                        ArrayList<String> subjects = new ArrayList<>();
                        ArrayList<String> subjectsNormal = new ArrayList<>();
                        ArrayList<Voto> votes = new ArrayList<>();
                        JSONObject object = new JSONObject(json){
                            @Override
                            public Iterator keys(){
                                TreeSet<Object> sortedKeys = new TreeSet<Object>();
                                Iterator keys = super.keys();
                                while(keys.hasNext()){
                                    sortedKeys.add(keys.next());
                                }
                                return sortedKeys.iterator();
                            }
                        };

                        Iterator<String> keysIterator = object.keys();
                        while (keysIterator.hasNext())
                        {
                            String materia = keysIterator.next();
                            subjects.add(getMateria(materia));
                            subjectsNormal.add(materia.toUpperCase());
                            JSONArray valueObject = object.getJSONArray(materia);

                            for(int x = 0; x<valueObject.length(); x++){
                                JSONObject voteObject = valueObject.getJSONObject(x);
                                String voto = voteObject.getString("vote");
                                String date = voteObject.getString("date");
                                String type = voteObject.getString("type");
                                int period = voteObject.getInt("period");
                                Voto vote = new Voto(voto, getMateria(materia), date, type, period);
                                vote.setSpecial(voteObject.getBoolean("special"));
                                votes.add(vote);
                            }
                        }

                        SharedPreferences.saveString(c, "materie", "materie", new Gson().toJson(subjects));
                        SharedPreferences.saveString(c, "materie", "materieNormal", new Gson().toJson(subjectsNormal));
                        mCallback.onResponse(votes);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call classeviva votes page and parse html in local (for notifications)
     */
    public void getLocalParsedVotes(){

        try {
            run("https://web.spaggiari.eu/cvv/app/default/genitori_voti.php", new EndpointsCallback() {
                @Override
                public void onResponse(String html) {
                    Document doc = Jsoup.parse(html);
                    //Check for expired sesion
                    if(doc.title().equals("La Scuola del futuro, oggi")){
                        Log.d(TAG, "getLocalParsedVotes: Ops, new session!");
                        try {
                        //Set credentials from storage
                        ClassevivaCaller.this.mUser = Credentials.getName(c);
                        ClassevivaCaller.this.mPassword = Cripter.decriptString(getPassword(c));
                        //Get a new session re-performing login
                        newSession(ClassevivaCaller.class.getMethod("getLocalParsedVotes", null));
                        } catch (NoSuchMethodException e) {e.printStackTrace();}
                        return;
                    }

                    //Part of code which fill the votoList with all retrieved votes
                    String subject = null;
                    ArrayList<Voto> votes = new ArrayList();
                    ArrayList<String> subjects = new ArrayList();
                    ArrayList<String> subjectsNormal = new ArrayList();
                    int index = 0;
                    for(Element tr : doc.select("#data_table_2 tr")){
                        if(index > 2){
                            String date, vote, type = null;
                            int period = -1;
                            boolean special, alreadyAssigned = false;
                            if(tr.select("td").first().hasClass("registro")){
                                subject = tr.select("td").get(1).text().replaceAll("\n","");
                                subjects.add(getMateria(subject));
                                subjectsNormal.add(subject.substring(0, subject.length()-2));
                            }else{
                                //Use the latest subject and find the period
                                period = tr.select("td").first().text().contains("1") ? 1 : 2;
                                type = "Scritto/Grafico";
                                alreadyAssigned = true;
                            }

                            for(Element td : tr.select("td.registro_voti_dettaglio_voto_piccolo")){
                                date = td.child(0).text().replaceAll("\n","");
                                if(date != ""){
                                    vote = td.child(1).child(0).text();
                                    special = td.child(1).hasClass("f_reg_voto_dettaglio") ? true : false;
                                    if(!alreadyAssigned){
                                        period = td.hasClass("q1") ? 1 : 2;
                                        if(td.hasClass("voto_1")) type = "Scritto/Grafico";
                                        else if(td.hasClass("voto_2")) type = "Orale";
                                        else if(td.hasClass("voto_3")) type = "Pratico";
                                    }

                                    Voto voto = new Voto(vote, getMateria(subject), date, type, period);
                                    votes.add(voto);
                                }
                            }
                        }
                        index++;
                    }

                    //Save subject in shared preferences and call the interface
                    SharedPreferences.saveString(c, "materie", "materie", new Gson().toJson(subjects));
                    SharedPreferences.saveString(c, "materie", "materieNormal", new Gson().toJson(subjectsNormal));
                    mCallback.onResponse(votes);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a request to classeviva for retrieving all events.
     */
    public void getAgenda(){
        String session = Credentials.getSession(c);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("session", session);
        parameters.put("agenda", "true");
        try {
            run(parameters, new EndpointsCallback() {
                @Override
                public void onResponse(String json){
                    try {
                        if(!checkStatus(json, ClassevivaCaller.class.getMethod("getAgenda", null))) return;

                        ArrayList<Evento> events = new ArrayList<>();

                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String start = jsonObject.getString("start");
                            String dateToParse = start.substring(
                                    0,
                                    start.indexOf(" ")
                            );
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = format.parse(dateToParse);
                            String title = jsonObject.getString("title");
                            String text = jsonObject.getString("nota_2");
                            String autore = jsonObject.getString("autore_desc");

                            Evento event = new Evento(date, title, text, autore);
                            events.add(event);
                        }

                        mCallback.onResponse(events);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a request to classeviva for retrieving all school's comunication.
     */
    public void getSchoolComunication(){
        String session = Credentials.getSession(c);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("session", session);
        parameters.put("circolari", "true");
        try {
            run(parameters, new EndpointsCallback() {
                @Override
                public void onResponse(String json){
                    try {
                        if(!checkStatus(json, ClassevivaCaller.class.getMethod("getSchoolComunication", null))) return;

                        ArrayList<Comunication> comunications = new ArrayList<>();

                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            int id = jsonObject.getInt("id");
                            String date = jsonObject.getString("date");
                            String title = jsonObject.getString("title").replaceAll("&quot;", " ");
                            String link = jsonObject.getString("url");

                            comunications.add(new Comunication(id, title, date, link));
                        }

                        mCallback.onResponse(comunications);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a request to classeviva for retrieving all user's notes.
     */
    public void getNotes(){
        String session = Credentials.getSession(c);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("session", session);
        parameters.put("note", "true");
        try {
            run(parameters, new EndpointsCallback() {
                @Override
                public void onResponse(String json){
                    try {
                        if(!checkStatus(json, ClassevivaCaller.class.getMethod("getNotes", null))) return;
                        ArrayList<Note> notes = new ArrayList<>();

                        JSONArray jsonArray = new JSONArray(json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String date = jsonObject.getString("date").replaceAll("-", "/");
                            String prof = jsonObject.getString("prof");
                            String note = jsonObject.getString("note");
                            String type = jsonObject.getString("type");

                            notes.add(new Note(prof, date, type, note));
                        }

                        mCallback.onResponse(notes);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a request to classeviva to get the arguments of a specific subject
     */
    public void getArguments(){
        String session = Credentials.getSession(c);
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("session", session);
        parameters.put("subject", subject);
        parameters.put("materia", "true");
        try {
            run(parameters, new EndpointsCallback() {
                @Override
                public void onResponse(String json){
                    try {
                        if(!checkStatus(json, ClassevivaCaller.class.getMethod("getArguments", null))) return;
                        ArrayList<Argument> argumentArray = new ArrayList();

                        JSONArray jsonArray = new JSONArray(json);
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String teacher = jsonObject.getString("teacher");
                            String date = jsonObject.getString("date");
                            String content = jsonObject.getString("content");

                            Argument argument = new Argument(date, teacher, content);
                            argumentArray.add(argument);
                        }

                        mCallback.onResponse(argumentArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a request to classeviva to get the name of the user and the school
     */
    public void getUser(){
        String session = Credentials.getSession(c);

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("session", session);
        parameters.put("info", "true");
        //Perform new HTTP call
        try {
            run(parameters, new EndpointsCallback() {
                @Override
                public void onResponse(String json){
                    try {
                        if(!checkStatus(json, ClassevivaCaller.class.getMethod("getUser", null))) return;
                        ArrayList<String[]> userArray = new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(json);
                        String school = jsonObject.getString("scuola");
                        String user = WordUtils.capitalize(jsonObject.getString("name").toLowerCase());
                        String[] array = new String[]{
                                school,
                                user
                        };
                        userArray.add(array);

                        mCallback.onResponse(userArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call Classeviva login page with saved credentials to retrieve a new session
     */
    public void newSession(final Method method) {

        //Perform new HTTP call
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("usercode", mUser);
        parameters.put("password", mPassword);
        parameters.put("login", "true");
        try {
            run(parameters,
                    new EndpointsCallback() {
                @Override
                public void onResponse(String json){
                    try {
                        JSONObject object = new JSONObject(json);

                        boolean success = object.getString("status").equals("OK") ? true : false;
                        if (success) Credentials.saveCredentials(c, mUser, mPassword,  object.getString("sessionId"));

                        method.invoke(ClassevivaCaller.this);
                    }catch (JSONException e) { e.printStackTrace();  }
                    catch (InvocationTargetException e) { e.printStackTrace();  }
                    catch (IllegalAccessException e) { e.printStackTrace(); }
                }
            });
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void newSession() {

        //Descript the password
        mPassword = Cripter.decriptString(getPassword(c));

        //Perform new HTTP call
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("usercode", mUser);
        parameters.put("password", mPassword);
        parameters.put("login", "true");
        try {
            run(parameters,
                    new EndpointsCallback() {
                        @Override
                        public void onResponse(String json){
                            try {
                                JSONObject object = new JSONObject(json);

                                boolean success = object.getString("status").equals("OK") ? true : false;
                                if (success) Credentials.saveCredentials(c, mUser, mPassword, object.getString("sessionId"));

                                mCallback.onResponse(null);
                            }catch (JSONException e) { e.printStackTrace();  }
                        }
                    });
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Check if the status of the response is good.
     * Otherwise, get a new session and a new request.
     *
     * @param body The response text
     * @param method The method to call after the new session
     * @return Returns true if status isn't "error"
     */
    private boolean checkStatus(String body, Method method){
        if(body.equals("{\"status\":\"error\"}")){
            System.out.println("NEW SESSION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            //Set credentials from storage
            ClassevivaCaller.this.mUser = Credentials.getName(c);
            ClassevivaCaller.this.mPassword = Cripter.decriptString(getPassword(c));

            //Get a new session re-performing login
            newSession(method);

            return false;
        }else return true;
    }

    String getMateria(String materia){

        //Replace the current String with something more "readable"
        if(materia.contains("(chimica)")) return "Chimica";
        else if(materia.contains("(fisica)")) return "Fisica";
        else if(materia.contains("(scienze ")) return "Scienze";
        else if(materia.contains("diritto")) return "Diritto";
        else if(materia.contains("lingua e")) return "Italiano";
        else if(materia.contains("lingua")) return "Inglese";
        else if(materia.contains("matematica")) return "Matematica";
        else if(materia.contains("rc")) return "Religione";
        else if(materia.contains("applicate")) return "Scienze Applicate";
        else if(materia.contains("scienze")) return "Ginnastica";
        else if(materia.contains("storia")) return "Storia";
        else if(materia.contains("tecnologie e")) return "Tecnica";
        else if(materia.contains("tecnologie") || materia.contains("informatica")) return "Informatica";
        else return materia;
    }

    private String mergeUrl(String url, HashMap<String, String> parameters){
        String mergedUrl = url + "?";
        for(Map.Entry<String, String> entry : parameters.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            mergedUrl += key + "=" + value + "&";
        }
        return mergedUrl;
    }

    private interface EndpointsCallback{
        void onResponse(String json);
    }
}