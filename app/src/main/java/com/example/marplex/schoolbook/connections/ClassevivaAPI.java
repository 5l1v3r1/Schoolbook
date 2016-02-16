package com.example.marplex.schoolbook.connections;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.marplex.schoolbook.interfaces.ClassevivaAgenda;
import com.example.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.models.Evento;
import com.example.marplex.schoolbook.models.Voto;
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
import java.util.Date;
import java.util.List;

import se.simbio.encryption.Encryption;

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

    public ClassevivaAPI(String username, String password, classeViva methods, Context c){
        this.usr = username;
        this.pws = password;
        this.c = c;
        this.methods = methods;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public ClassevivaAPI(String username, String password, classeViva methods, String session , Context c){
        this.usr = username;
        this.pws = password;
        this.session = session;
        this.methods = methods;
        this.c = c;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public ClassevivaAPI(classeViva methods){
        this.methods = methods;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void doLogin(){
        new JSOUP(LOGIN, this).execute(CLASSEVIVA_LOGIN + "login=" + usr + "&password=" + pws + "&custcode=PNIT0003&mode=custcode&lc=it");
    }
    public void doLogin(boolean custom){
        if(custom){
            String name = SharedPreferences.loadString(c, "user", "user");
            String pw = SharedPreferences.loadString(c, "user", "password");
            final Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
            String password = null;
            try {
                password = encryption.decrypt(pw);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new JSOUP(LOGIN, this).execute(CLASSEVIVA_LOGIN + "login=" + name + "&password=" + password + "&custcode=PNIT0003&mode=custcode&lc=it");
        }
    }
    public void getVotes(ClassevivaVoti callback, Context c){
        this.c = c;
        this.callbackVoti = callback;
        new JSOUP(VOTES, this).execute("https://web.spaggiari.eu/cvv/app/default/genitori_voti.php");
    }
    public void getVotes(ClassevivaVoti callback, String session){
        this.callbackVoti = callback;
        this.session = session;
        new JSOUP(VOTES, this).execute("https://web.spaggiari.eu/cvv/app/default/genitori_voti.php");
    }
    public void getAgenda(ClassevivaAgenda callback, Context c){
        this.c = c;
        this.agendaCallback = callback;
        new JSOUP(AGENDA, this).execute("https://web.spaggiari.eu/cvv/app/default/agenda_studenti.php?ope=get_events&gruppo_id=&start=" + (int) (new Date().getTime() / 1000 - 1209600) + "&end=" + (int) (new Date().getTime() * 7));
    }

    @Override
    public void onDataReceive(int type, String html) {
        if(type==VOTES){
            Document doc = Jsoup.parse(html);
            if(doc.title().equals("La Scuola del futuro, oggi")){
                isVoti=true;
                doLogin(true);
                return;
            }
            Elements trs = doc.getElementsByTag("tr");
            List<Voto> votoList = new ArrayList<>();
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
            callbackVoti.onVotiReceive(votoList);
        }else if(type==LOGIN){
            if(isVoti){
                isVoti=false;
                getVotes(callbackVoti, c);
            }else if(isAgenda){
                isAgenda=false;
                getAgenda(agendaCallback, c);
            }
        }else if(type==AGENDA){
            Document doc = Jsoup.parse(html);
            if(doc.title().equals("La Scuola del futuro, oggi")) {
                isAgenda=true;
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
                    Evento evento = new Evento(data, prof, testo);
                    eventi.add(evento);
                }

                agendaCallback.onAgendaReceive(eventi);

            } catch (Exception e) {e.printStackTrace();}
        }
    }

    public String getSession(){
        return this.session;
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
        protected String doInBackground(String... params) {
            if(type==LOGIN) {
                try {
                    Document doc = getLoginPage(params[0]);
                    html = doc.html();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Document doc = getPage(params[0]);
                    html = doc.html();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    Document getLoginPage(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
        Connection.Response response = connection.execute();
        this.session = response.cookie("PHPSESSID").toString();
        SharedPreferences.saveString(c, "user", "sessionID", this.session);
        return connection.get();
    }
    Document getPage(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").cookie("PHPSESSID",SharedPreferences.loadString(c,"user","sessionID"));
        return connection.get();
    }
    String getMateria(String materia){
        if(materia.startsWith("(chimica)")) return "Chimica";
        else if(materia.startsWith("(fisica)")) return "Fisica";
        else if(materia.startsWith("(scienze ")) return "Scienze";
        else if(materia.startsWith("diritto")) return "Diritto";
        else if(materia.startsWith("lingua e")) return "Italiano";
        else if(materia.startsWith("lingua")) return "Inglese";
        else if(materia.startsWith("matematica")) return "Matematica";
        else if (materia.startsWith("rc")) return "Religione";
        else if(materia.startsWith("scienze")) return "Ginnastica";
        else if(materia.startsWith("storia")) return "Storia";
        else if(materia.startsWith("tecnologie e")) return "Tecnica";
        else if(materia.startsWith("tecnologie")) return "Informatica";
        else return null;
    }
}
interface insideCallback{
    void onDataReceive(int type, String html);
}
