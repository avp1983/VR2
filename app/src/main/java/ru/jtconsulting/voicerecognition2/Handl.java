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

        context.pd.setMessage("Инициализация");
    }

    final String LOG_TAG = "Handl";
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        ArrayList<String> grammars;
        Log.d(LOG_TAG, "handleMessage:"+msg.what);
        int serviceState = SpitchMobileService.getServiceState();
        Log.d(LOG_TAG, "serviceState="+String.valueOf(serviceState));



        switch(msg.what){
           case Constants.INITSERVICE_INITIALIZATION_COMPLETED:
               context.disableEnableButtons(true);
               context.isBlocked=false;
               context.pd.setMessage("ИНИЦИАЛИЗАЦИЯ УСПЕШНА");
               context.pd.dismiss();
               grammars= SpitchMobileService.getGrammarList();
               if (grammars!=null){
                   Log.d(LOG_TAG, "getGrammarList is successfull");
                   //Log.d(LOG_TAG, "getGrammarList[0] is "+grammars.get(0));
                   for (int i=0;i<grammars.size();i++){
                       Log.d(LOG_TAG,grammars.get(i));
                   }
               } else {
                   Log.d(LOG_TAG, "getGrammarList is NOT successfull");
               }
               break;
           case Constants.INITSERVICE_ERROR_INITIALIZATION:
               context.setTextToAll(context.getString(R.string.INITSERVICE_ERROR_INITIALIZATION));
               context.pd.setMessage(context.getString(R.string.INITSERVICE_ERROR_INITIALIZATION));
             //  context.initMenuitem.setEnabled(true);
               break;
           case Constants.RECOGNITION_WAS_STOPPED:
               String res =  SpitchMobileService.getSpitchResult();
               new XmlParser(res);
               Log.d(LOG_TAG,res);
               context.txtOut.setText(res);
               break;

       }



        /*Toast toast = Toast.makeText(context,
                "Статус: "+ String.valueOf(msg.what),
                Toast.LENGTH_SHORT);
       // toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
       // context.pd.setMessage("handleMessage:"+String.valueOf(msg.what));
    }
}
