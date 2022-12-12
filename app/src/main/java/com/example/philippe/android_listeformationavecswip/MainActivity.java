package com.example.philippe.android_listeformationavecswip;

import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import metier.Formation;

public class MainActivity extends AppCompatActivity
{
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static ArrayList<Formation> lesFormations = new ArrayList<Formation>();

    // L'ADRESSE IP SERA A REMPLACER PAR L'IP DU POSTE CONTENANT LE WEB SERVICE
    //String lien = "http://192.168.1.20:8080/WebServiceFormation/webresources/formation";
    String lien = "http://192.168.1.90:8080/WebServiceFormation/webresources/formation";
    URL urlCon;
    HttpURLConnection urlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Paramétrage du ViewPager avec l'adaptateur
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /* Lancement de la tache asynchrone (obligatoire car l'appel à un webservice est une "tache longue")*/
        AccesWebServices accesWS = new AccesWebServices();
        BufferedReader rd = null;
        String retourWS = "";
        try
        {
            //Récupération, conversion en String et exploitation de la valeur de retour
            InputStream rep = accesWS.execute().get();

            rd = new BufferedReader(new InputStreamReader(rep));
            retourWS = rd.readLine();

            //System.out.println("RETOUR:" + retourWS);
        }
        catch(Exception e)
        {
            System.out.println("ERREUR APPEL DU WEBSERVICE : " + e.getMessage());
        }
        /* PARSING DU TEXTE (retourWS) RETOURNE (format JSON) */
        // Création du tableau JSON
        JSONArray jTab = null;
        try
        {
            jTab = new JSONArray(retourWS);
        }
        catch (Exception e)
        {
            System.out.println("ERREUR TABLEAU JSON : " + e.getMessage());
        }
        Formation uneFormation = null;
        String libelle, niveau, type, description;
        int coutRevient, diplomante, duree;
        //MODIFICATIONS A FINIR
        // Pour exploiter le tableau JSON
        System.out.println("TAILLE TABLEAU:" + jTab.length());
        for (int i=0; i < jTab.length(); i++)
        {
            try
            {
                libelle = jTab.getJSONObject(i).getString("libelle");
                niveau = jTab.getJSONObject(i).getString("niveau");
                type = jTab.getJSONObject(i).getString("type");
                description = jTab.getJSONObject(i).getString("description");
                coutRevient = jTab.getJSONObject(i).getInt("coutRevient");
                diplomante = jTab.getJSONObject(i).getInt("diplomante");
                duree = jTab.getJSONObject(i).getInt("duree");
                uneFormation = new Formation(libelle, niveau, type, description, coutRevient, diplomante, duree);
            }
            catch (JSONException jse)
            {
                System.out.println("ERREUR OBJET JSON : " + jse.getMessage());
            }
            lesFormations.add(uneFormation);
        }
        /* FIN DU PARSING */
        // On va compter le nombre d'écrans sachant que nombre de formations = nombre d'écrans.
        int max = lesFormations.size();
        mSectionsPagerAdapter.setCount(max);

    }
    private class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        int nbPages;
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem est appelé pour instancier le fragment pour la page donnée.
            // Retourne un GestionFragment (défini comme une classe interne plus bas).
            return GestionFragment.newInstance(position + 1);
        }

        @Override
        public int getCount()
        {
            // Show nbPages total pages.
            return nbPages;
        }
        public void setCount(int nb)
        {
            nbPages = nb;
            notifyDataSetChanged();
        }
    }
    //private class AccesWebServices extends AsyncTask<Void, Void, HttpResponse>
    private class AccesWebServices extends AsyncTask<Void, Void, InputStream>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();
        }
        @Override
        //protected InputStream doInBackground(Void... params)
        protected InputStream doInBackground(Void... params)
        {
            try
            {
                System.out.println("MESSAGE1");
                urlCon = new URL(lien);
                System.out.println("MESSAGE2");
                urlConnection = (HttpURLConnection) urlCon.openConnection();
                System.out.println("MESSAGE3");
                InputStream in = urlConnection.getInputStream();
                System.out.println("MESSAGE4");
                return in;
            }
            catch (Exception ex)
            {
                System.out.println("ERREUR ASYNCTASK : " + ex.getMessage());
                return null;
            }
        }
        /*
        @Override
        protected void onPostExecute(OutputStreamWriter resultat)
        {
            Toast.makeText(getApplicationContext(), "Fin du traitement asynchrone", Toast.LENGTH_LONG).show();
        }
        */
    }

    public static class GestionFragment extends Fragment
    {
        EditText edtLibelle, edtNiveau, edtType, edtDescription;
        int positionCourante=0;

        public GestionFragment()
        {
        }

        /**
         * Retourne une instance de ce fragment pour le n° de section demandé
         */
        public static GestionFragment newInstance(int sectionNumber)
        {
            GestionFragment fragment = new GestionFragment();
            Bundle args = new Bundle();
            args.putInt("num_etape", sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            // Pour faire le lien entre le layout fragment_main et le conteneur de l'activity_main
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            // Récupération du numéro d'écran
            int numEcran = getArguments().getInt("num_etape");
            System.out.println("NUM ECRAN:" + numEcran);
            positionCourante = numEcran - 1; // -1 car la position courante commence à 0 et pas numEcran

            initialisations(rootView);
            affichage();

            return rootView;
        }
        public void initialisations(View rootView)
        {
            edtLibelle = rootView.findViewById(R.id.edtLibelle);
            edtNiveau = rootView.findViewById(R.id.edtNiveau);
            edtType = rootView.findViewById(R.id.edtType);
            edtDescription = rootView.findViewById(R.id.edtDescription);
        }

        public void affichage()
        {
            Formation uneFormation = lesFormations.get(positionCourante);
            edtLibelle.setText(uneFormation.getLibelle());
            edtNiveau.setText(uneFormation.getNiveau());
            edtType.setText(uneFormation.getType());
            edtDescription.setText(uneFormation.getDescription());
        }
    }
}