package com.example.marplex.schoolbook.connections;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.example.marplex.schoolbook.interfaces.ClassevivaVoti;
import com.example.marplex.schoolbook.interfaces.classeViva;
import com.example.marplex.schoolbook.models.Voto;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by marco on 1/27/16.
 */
public class ClassevivaAPI implements insideCallback{

    String CLASSEVIVA_LOGIN = "https://web.spaggiari.eu/home/app/default/login.php?";
    protected final String TAG = "Classeviva Login";
    classeViva methods;
    String usr,pws;

    Map<String, String> session;

    private final int LOGIN = 1;
    private final int VOTES = 2;

    ClassevivaVoti callbackVoti;

    public ClassevivaAPI(String username, String password, classeViva methods){
        this.usr = username;
        this.pws = password;
        this.methods = methods;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public ClassevivaAPI(classeViva methods, Map<String, String> session){
        this.methods = methods;
        this.session = session;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void doLogin(){
        new JSOUP(LOGIN, this).execute(CLASSEVIVA_LOGIN+"login="+usr+"&password="+pws+"&custcode=PNIT0003&mode=custcode&lc=it");
    }
    public void getVotes(ClassevivaVoti callback){
        this.callbackVoti = callback;
        new JSOUP(VOTES, this).execute("https://web.spaggiari.eu/cvv/app/default/genitori_voti.php");
    }

    @Override
    public void onDataReceive(int type, String html) {
        System.out.println("Type: "+type);
        if(type==VOTES){
            Document doc = Jsoup.parse(html);
            Elements trs = doc.getElementsByTag("tr");
            List<Voto> votoList = new ArrayList<>();
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

                    Elements voti = tr.select("td.registro_voti_dettaglio_voto_piccolo");
                    for(int x = 0; x < voti.size(); x++){
                        if(voti.get(x).hasText()){
                            Element span = voti.get(x).getElementsByTag("span").first();
                            Element p = voti.get(x).getElementsByTag("p").first();
                            String data = span.text();
                            String voto = p.text();
                            Voto v = new Voto(voto, getMateria(materia) , data, "Orale");
                            votoList.add(v);
                        }
                    }
                }
            }
            callbackVoti.onVotiReceive(votoList);
        }else if(type==LOGIN){

        }
    }

    public Map<String, String> getSession(){
        return this.session;
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
        session = response.cookies();
        connection.cookies(session);
        return connection.get();
    }
    Document getPage(String url) throws IOException {
        Connection connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
        connection.cookies(session);
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
        else if(materia.startsWith("tecnologie")) return "Tecnica";
        else return null;
    }
}
interface insideCallback{
    void onDataReceive(int type, String html);
}
