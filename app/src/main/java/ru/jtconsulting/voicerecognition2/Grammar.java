package ru.jtconsulting.voicerecognition2;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bssys.spitchmobilesdk.*;
import java.util.ArrayList;

/**
 * Created by www on 18.01.2016.
 */
public class Grammar {
    final static String LOG_TAG = "Grammar";
    final static int SELECTION_COLOR = Color.LTGRAY;
    final static int BCKGRND_COLOR = Color.TRANSPARENT;


    /**
     * Выбранная грамматика (название)
     */
    public static String selectedGrammarName;

    /**
     * Выбранная грамматика (номер позиции в списке)
     */
    public static int selectedGrammarPosNum;

    /**
     * Список граматик
     */
    public static ArrayList<String> grammarsList;

    private static  ListView listViewGrammar;
    private static  MainActivity context;
    private static boolean initState=false;
   public static void initGrammar (MainActivity c, ListView gramarsListView){
       context=c;
       listViewGrammar=gramarsListView;
       initState=true;   }

    /**
     * Получить список грамматик с сервера
     */
    public static void getGrammarList(){
        ArrayList<String> grammars;
        String rez = null;
        grammarsList= SpitchMobileService.getGrammarList();
        if (grammarsList==null) Log.d(LOG_TAG, "getGrammarList: grammarsList==null");
    }

    /**
     * Заполнить ListView полученными граматиками
     */
    public static void fillGrammarListView(){
        if (!initState) {
            Log.d(LOG_TAG, "fillGrammarListView()  Need run Grammar.initGrammar first");
            return;
        }
        if (grammarsList==null) {
            Log.d(LOG_TAG, "fillGrammarListView()    grammarsList==null");
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.my_list_item,
                grammarsList
        );
        listViewGrammar.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewGrammar.setAdapter(adapter);
        listViewGrammar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(BCKGRND_COLOR);
                // change the background color of the selected element
                view.setBackgroundColor(SELECTION_COLOR);
                // ListView Clicked item value
                Grammar.selectedGrammarName = (String) Grammar.listViewGrammar.getItemAtPosition(position);
                Grammar.selectedGrammarPosNum = position;
                        Log.d(LOG_TAG, "SELECTED:  " +  Grammar.selectedGrammarName);
            }
        });
    }



    /**
     * Выделить выбранную грамматику (в случае выхода приложения из фона)
     */
   /* public static void markSelectedGrammar(){
        if (selectedGrammarName!=null){
            for (int i = 0; i < listViewGrammar.getChildCount(); i++) {
                View v =listViewGrammar.getChildAt(i);
                if (i==selectedGrammarPosNum){
                    v.setBackgroundColor(SELECTION_COLOR);
                } else {
                    v.setBackgroundColor(BCKGRND_COLOR);
                }
            }
        }
    }*/

}
