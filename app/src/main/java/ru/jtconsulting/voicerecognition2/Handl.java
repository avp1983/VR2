package ru.jtconsulting.voicerecognition2;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bssys.spitchmobilesdk.*;

import java.util.ArrayList;

/**
 * Created by www on 11.01.2016.
 */
public class Handl extends Handler {
    private MainActivity context;


    public MainActivity getContext() {
        return context;
    }

    public void setContext(MainActivity context) {
        this.context = context;
    }

    public Handl(MainActivity a) {
        super();
        context=a;
        Log.d(LOG_TAG, "handleMessage:START");


    }


    private ArrayList<String> getGrammarList(){
        ArrayList<String> grammars;
        String rez = null;
        grammars= SpitchMobileService.getGrammarList();
        if (grammars!=null){
            Log.d(LOG_TAG, "getGrammarList is successfull");
            if (grammars.size()>=1) rez=  grammars.get(0);

        }
        Log.d(LOG_TAG, "grammar is "+rez);
        //if (rez==null) context.showAlert("Грамматика не найдена");
        return grammars;
    }
    final String LOG_TAG = "Handl";
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        Log.d(LOG_TAG, "handleMessage:"+msg.what);
        int serviceState = SpitchMobileService.getServiceState();
        Log.d(LOG_TAG, "serviceState="+String.valueOf(serviceState));



        switch(msg.what){
           case Constants.INITSERVICE_INITIALIZATION_COMPLETED:
               context.disableEnableButtons(true);
               context.isBlocked=false;
               context.pd.setMessage("ИНИЦИАЛИЗАЦИЯ УСПЕШНА");
               context.pd.dismiss();
               Globals.grammarsList=getGrammarList();
               for(int i=0;i<Globals.grammarsList.size();i++){
                   Log.d(LOG_TAG, "Gramar= "+Globals.grammarsList.get(i));
               }
               ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                       R.layout.my_list_item,
                       Globals.grammarsList
               );
               context.gramarsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
               context.gramarsListView.setAdapter(adapter);
               context.gramarsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                       for (int j = 0; j < parent.getChildCount(); j++)
                           parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                       // change the background color of the selected element
                       view.setBackgroundColor(Color.LTGRAY);
                       // ListView Clicked item value
                       String itemValue = (String) context.gramarsListView.getItemAtPosition(position);
                       Globals.grammar=itemValue;
                       Log.d(LOG_TAG, "SELECTED:  " + itemValue);
                   }
               });
               break;
           case Constants.INITSERVICE_ERROR_INITIALIZATION:
               context.onInitializationError();
               break;
           case Constants.RECOGNITION_WAS_STOPPED:
               if (context.stopWithoutResults) {
                   context.stopWithoutResults=false;
                   return;
               }
               String res =  SpitchMobileService.getSpitchResult();
               //res = XmlParser.parse(res);
               Log.d(LOG_TAG,res);
               context.CurrentTextView.setText(res);

               break;

       }



    }
}
