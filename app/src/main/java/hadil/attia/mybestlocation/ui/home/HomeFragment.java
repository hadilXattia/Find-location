package hadil.attia.mybestlocation.ui.home;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hadil.attia.mybestlocation.Config;
import hadil.attia.mybestlocation.JSONParser;
import hadil.attia.mybestlocation.Position;
import hadil.attia.mybestlocation.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
ArrayList<Position> data=new ArrayList<Position>();

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);//inflate :parsing de xml a java
        View root = binding.getRoot();
binding.btnLoad.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View view){
     Load l = new Load();
     l.execute();
    }
});

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    class Load extends AsyncTask{
        AlertDialog alert;
        @Override
        protected void onPreExecute() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Loading");
            dialog.setMessage("please wait .. almost there !");

            alert= dialog.create();
            alert.show();

        }

        @Override
        protected void onPostExecute(Object o) {
          alert.dismiss();
            ArrayAdapter ad =new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,data);
          binding.lvLocation.setAdapter(ad);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //code de second thread
            JSONParser parser=new JSONParser();
            JSONObject response=parser.makeRequest(Config.URL_GETALL);
            try {
                int success=response.getInt("success");

                if (success==0){
                    String msg=response.getString("message");
                }else {
                    JSONArray tableau=response.getJSONArray("positions");
                    data.clear();
                    for (int i = 0; i < tableau.length(); i++) {
                        JSONObject ligne=tableau.getJSONObject(i);
                        int idposition= ligne.getInt("idposition");
                        String pseudo = ligne.getString("pseudo");
                        String numero = ligne.getString("numero");
                        String longitude = ligne.getString("longitude");
                        String latitude = ligne.getString("latitude");

                        Position p =new Position(idposition,pseudo,numero,longitude,latitude);
                        data.add(p);

                    }
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}