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
               Grammar.getGrammarList();
               Grammar.fillGrammarListView();


               break;
           case Constants.INITSERVICE_ERROR_INITIALIZATION:
               context.onInitializationError();
               break;
           case Constants.RECOGNITION_WAS_STOPPED:
               if (context.stopWithoutResults) {
                   context.stopWithoutResults=false;
                   return;
               }
               if (SpitchMobileService.getLastErrorCode()!=0){
                   context.CurrentTextView.setText("Error message: "+ SpitchMobileService.getLastErrorMessage());
               } else {

                   String res = SpitchMobileService.getSpitchResult();
                   if (res.equals("")) {
                       context.CurrentTextView.setText("Server return empty result. Error message: " + SpitchMobileService.getLastErrorMessage());

                   } else {
                       //res = XmlParser.parse(res);
                       Log.d(LOG_TAG, res);
                       context.CurrentTextView.setText(res);
                   }
               }
               break;

       }



    }
}
