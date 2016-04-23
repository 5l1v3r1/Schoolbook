package com.example.marplex.schoolbook.connections;

import android.content.Context;
import android.os.AsyncTask;

import com.example.marplex.schoolbook.interfaces.ClassevivaAgenda;
import com.example.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.models.Evento;
import com.example.marplex.schoolbook.models.Voto;
import com.example.marplex.schoolbook.utilities.Credentials;
import com.example.marplex.schoolbook.utilities.Cripter;
import com.example.marplex.schoolbook.utilities.SharedPreferences;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marco on 1/27/16.
 */
public class ClassevivaAPI implements insideCallback{

    String CLASSEVIVA_LOGIN = "https://web.spaggiari.eu/home/app/default/login.php?";
    protected final String TAG = "Classeviva Login";
    classeViva methods;
    String usr,pws;
    Context c;

    String session;

    private final int LOGIN = 1;
    private final int VOTES = 2;
    private final int AGENDA = 3;

    private boolean isVoti;
    private boolean isAgenda;

    ClassevivaVoti callbackVoti;
    ClassevivaAgenda agendaCallback;

    /**
     * ClassevivaAPI constructor
     *
     * @param username  User's username
     * @param password  User's password
     * @param methods  Interface which return the requested html string
     * @param c  Activity context
     *
     */

    public ClassevivaAPI(String username, String password, classeViva methods, Context c){
        this.usr = username;
        this.pws = password;
        this.c = c;
        this.methods = methods;
    }

    /**
     * ClassevivaAPI constructor
     *
     * @param methods  Interface which return the requested html string
     *
     */

    public ClassevivaAPI(classeViva methods){
        this.methods = methods;
    }

    //Call Classeviva login page with requested credentials
    public void doLogin(){
        //Perform new HTTP call
        new JSOUP(LOGIN, this).execute(CLASSEVIVA_LOGIN + "login=" + usr + "&password=" + pws + "&custcode=PNIT0003&mode=custcode&lc=it");
    }
    //Call Classeviva login page with saved credentials
    public void doLogin(boolean custom){
        if(custom){
            String pw;
            //Get user's credentials from SharedPreference
            usr = Credentials.getName(c);
            pw = Credentials.getPassword(c);

            //Decrypt password usign Encryption class
            pws = Cripter.decriptString(pw);

            //Perform new HTTP call
            new JSOUP(LOGIN, this).execute(CLASSEVIVA_LOGIN + "login=" + usr + "&password=" + pws + "&custcode=PNIT0003&mode=custcode&lc=it");
        }
    }

    /**
     * getVotes() method
     *
     * @param callback  Interface which return the requested ArrayList<Voto> list
     * @param c  Activity context
     * @see ClassevivaVoti
     *
     */

    public void getVotes(ClassevivaVoti callback, Context c){
        this.c = c;
        this.callbackVoti = callback;

        //Perform new HTTP call
        new JSOUP(VOTES, this).execute("https://web.spaggiari.eu/cvv/app/default/genitori_voti.php");
    }

    /**
     * getAgenda() method
     *
     * @param callback  Interface which return the requested ArrayList<Evento> list
     * @param c  Activity context
     * @see ClassevivaVoti
     *
     */

    public void getAgenda(ClassevivaAgenda callback, Context c){
        this.c = c;
        this.agendaCallback = callback;
        new JSOUP(AGENDA, this).execute("https://web.spaggiari.eu/cvv/app/default/agenda_studenti.php?ope=get_events&gruppo_id=&start="
                + (int) (System.currentTimeMillis()/1000 - 1728000) + "&end=" + (int) (System.currentTimeMillis()/1000 + 864000));
    }

    @Override
    public void onDataReceive(final int type, final String html) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(type==VOTES){

                    Document doc = Jsoup.parse(html);
                    //Check for expired sesion
                    if(doc.title().equals("La Scuola del futuro, oggi")){
                        isVoti=true;

                        //Re-perform the login to retrieve a new session
                        doLogin(true);

                        return;
                    }

                    //Part of code which fill the votoList with all retrieved votes
                    Elements trs = doc.getElementsByTag("tr");
                    ArrayList<Voto> votoList = new ArrayList<>();
                    ArrayList<String> materie = new ArrayList<>();
                    for(int i = 0; i<trs.size();i++){
                        Element tr = trs.get(i);
                        if(tr.select("td.registro").first()==null) continue;
                        else{
                            String materia = null;

                            if(tr.select("td[class=registro grautext open_sans_condensed_bold font_size_14]").first()==null){
                                if(tr.select("td[class= open_sans_condensed font_size_10]").first()!=null){
                                    materia= trs.get(i-1).select("td[class=registro grautext open_sans_condensed_bold font_size_14]").first().text();
                                }
                            }else materia = tr.select("td[class=registro grautext open_sans_condensed_bold font_size_14]").first().text();

                            materie.add(materia);

                            Elements voti = tr.select("td.registro_voti_dettaglio_voto_piccolo");
                            for(int x = 0; x < voti.size(); x++){
                                if(voti.get(x).hasText()){
                                    Element span = voti.get(x).getElementsByTag("span").first();
                                    Element p = voti.get(x).getElementsByTag("p").first();

                                    int periodo = 0;
                                    String tipo = null;
                                    if( (x+1) <= 15 ){
                                        periodo=1;
                                        if((x+1) <= 5) tipo="Scritto";
                                        else if((x+1) <= 10) tipo="Orale";
                                        else if((x+1) <= 15) tipo="Pratico";
                                    }else if((x+1) <= 30){
                                        periodo=2;
                                        if((x+1) <= 20) tipo="Scritto";
                                        else if((x+1) <= 25) tipo="Orale";
                                        else if((x+1) <= 30) tipo="Pratico";
                                    }

                                    String data = span.text();
                                    String voto = p.text();
                                    Voto v = new Voto(voto, getMateria(materia) , data, tipo, periodo);
                                    votoList.add(v);
                                }
                            }
                        }
                    }

                    //Save all subjects on SharedPreferences
                    ArrayList<String> finalMaterie = new ArrayList<>();
                    for(int i=0; i<materie.size(); i++){
                        if(i==0) finalMaterie.add(getMateria(materie.get(i)));
                        else {
                            String materia = getMateria(materie.get(i));
                            if(materia == getMateria(materie.get(i-1))) continue;
                            else finalMaterie.add(materia);
                        }
                    }
                    SharedPreferences.saveString(c,"materie","materie", new Gson().toJson(finalMaterie));

                    //Call interface method
                    callbackVoti.onVotiReceive(votoList);

                }else if(type==LOGIN){
                    if(isVoti){
                        isVoti=false;
                        getVotes(callbackVoti, c);
                    }else if(isAgenda){
                        isAgenda=false;
                        getAgenda(agendaCallback, c);
                    }else{
                        Credentials.saveCredentials(c, usr, pws, session);
                    }
                }else if(type==AGENDA){
                    Document doc = Jsoup.parse(html);

                    //Check for expired sesion
                    if(doc.title().equals("La Scuola del futuro, oggi")) {
                        isAgenda=true;

                        //Re-perform the login for retrieve the new session
                        doLogin(true);
                        return;
                    }

                    try {
                        ArrayList<Evento> eventi = new ArrayList<>();
                        JSONArray array = new JSONArray(doc.body().text());

                        for(int i=0; i<array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            String prof = object.getString("autore_desc");
                            String testo = object.getString("nota_2");

                            String modifica = object.getString("start");
                            String data = modifica.substring(0, 10);
                            //Fill Evento class with all retrieved datas
                            Evento evento = new Evento(data, prof, testo);
                            //Add evento to an ArrayList
                            eventi.add(evento);
                        }

                        agendaCallback.onAgendaReceive(eventi);
                    } catch (Exception e) {e.printStackTrace();}
                }
            }
        });
        thread.run();
    }

    /**
     * getSession() method
     *
     * @return The current session from SharedPreferences
     *
     */

    public String getSession(){
        return SharedPreferences.loadString(c,"user","sessionID");
    }

    public void setSession(String session){
        this.session = session;
    }

    private class JSOUP extends AsyncTask<String, Void, String> {
        insideCallback callback;
        String html;
        int type;

        JSOUP(int type, insideCallback callback){this.type = type; this.callback = callback;}

        @Override
        protected String doInBackground(final String... params) {
            if(type==LOGIN) {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Document doc = getLoginPage(params[0]);
                            html = doc.html();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                run.run();
            }else{
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Document doc = getPage(params[0]);
                            html = doc.html();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                run.run();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            methods.onPageLoaded(html);
            callback.onDataReceive(type, html);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    /**
     * getLoginPage() method
     *
     * @param url The url
     * @return JSoup Connection
     * @throws IOException
     */
    Document getLoginPage(String url) throws IOException {
        //Get the page with a new session
        Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
        Connection.Response response = connection.execute();
        this.session = response.cookie("PHPSESSID").toString();
        SharedPreferences.saveString(c, "user", "sessionID", this.session);
        return connection.get();
    }

    /**
     * getPage() method
     *
     * @param url The url
     * @return JSoup Connection
     * @throws IOException
     */
    Document getPage(String url) throws IOException {
        //Get the page with the saved session
        Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .cookie("PHPSESSID", SharedPreferences.loadString(c, "user", "sessionID"));
        return connection.get();
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
        else if (materia.contains("rc")) return "Religione";
        else if(materia.contains("scienze")) return "Ginnastica";
        else if(materia.contains("storia")) return "Storia";
        else if(materia.contains("tecnologie e")) return "Tecnica";
        else if(materia.contains("tecnologie") || materia.contains("informatica")) return "Informatica";
        else return materia;
    }
}

interface insideCallback{
    void onDataReceive(int type, String html);
}