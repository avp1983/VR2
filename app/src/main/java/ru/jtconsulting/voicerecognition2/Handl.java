package ru.jtconsulting.voicerecognition2;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bssys.spitchmobilesdk.*;

import java.util.ArrayList;

/**
 * Created by www on 11.01.2016.
 */
public class Handl extends Handler {
    private MainActivity context;






    public Handl(MainActivity a) {
        super();
        context=a;
        Log.d(LOG_TAG, "handleMessage:START");


    }


    private String getFirstGrammar(){
        ArrayList<String> grammars;
        String rez = null;
        grammars= SpitchMobileService.getGrammarList();
        if (grammars!=null){
            Log.d(LOG_TAG, "getGrammarList is successfull");
            if (grammars.size()>=1) rez=  grammars.get(0);

        }
        Log.d(LOG_TAG, "grammar is "+rez);
      //  if (rez==null) context.showAlert("Грамматика не найдена");
        return rez;
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
               Globals.grammar=getFirstGrammar();
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
