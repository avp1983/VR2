package ru.jtconsulting.voicerecognition2;

        import android.app.Activity;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.os.Handler;

        import android.text.method.ScrollingMovementMethod;
        import android.util.Log;

        import android.view.Menu;
        import android.view.MenuItem;

        import android.view.View;

        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.bssys.spitchmobilesdk.*;



public class MainActivity extends Activity  implements View.OnClickListener {
    Button btn;
    Button btn1;
    Button btn2;
    Button btn3;
    TextView txtOut;
    TextView txtOut1;
    TextView txtOut2;
    public ListView gramarsListView;
    final String LOG_TAG = "myLogs";
    public boolean isBlocked=false;
    public TextView CurrentTextView;
    public Button CurrentButton;

    public boolean errorOnBtnPress=false;
    public boolean stopWithoutResults =false;
    public boolean btnIsPressed=false;


    private void makeScroll(TextView t){
        t.setMovementMethod(new ScrollingMovementMethod());
    }
    private Handler handler=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        mainLayout.setOnTouchListener(new Slider(this));


        btn = (Button) findViewById(R.id.button);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        txtOut = (TextView) findViewById(R.id.txtOut);
        makeScroll(txtOut);
        txtOut1 = (TextView) findViewById(R.id.txtOut1);
        makeScroll(txtOut1);
        txtOut2 = (TextView) findViewById(R.id.txtOut2);
        makeScroll(txtOut2);
        // присваиваем обработчик кнопкам
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        gramarsListView = (ListView) findViewById(R.id.gramarsListView);
        Grammar.initGrammar(this, gramarsListView);
        Grammar.fillGrammarListView();
        //Grammar.markSelectedGrammar();
         handler= new Handl(this);

        initSpService();

    }



    public void initSpService(){
            int state;
            try {
                 state = SpitchMobileService.getServiceState();
            }
            catch(Exception e){
                state=Constants.GETSERVICESTATE_SERVICE_NOT_INITED;
            }
            if (state==Constants.GETSERVICESTATE_SERVICE_NOT_INITED||state==Constants.GETSERVICESTATE_SERVICE_ERROR_INIT) {
                isBlocked=true;

                showInitializationDialog();
                disableEnableButtons(false);


                SpitchMobileService.initService("111", null, null, null, handler, this);
                Log.d(LOG_TAG, "getStateMes=" + String.valueOf(SpitchMobileService.getStateMes()));
            }
    }

    public void showAlert(String txt){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Важное сообщение!")
                .setMessage(txt)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }



    public ProgressDialog  pd;
    private void showInitializationDialog(){
        pd = new ProgressDialog(this);
        pd.setTitle("Старт.");
        pd.setMessage("Инициализация");
        // добавляем кнопку
        pd.setButton(Dialog.BUTTON_NEGATIVE, "Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //System.exit(0);
            }
        });
        pd.show();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        setVisible(true);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        setVisible(false);
        resetButtons();
    }

    public MenuItem initMenuitem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        initMenuitem = menu.findItem(R.id.action_init);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_exit:
                finish();
                System.exit(0);
                break;
            case  R.id.action_init:
                initSpService();
                break;
        }




        return super.onOptionsItemSelected(item);
    }

    private void startFreeVoiceRecognition(){
        startVoceRecognition(null);

    }
    private void startGrammarVoiceRecognition(){
        Log.d(LOG_TAG,"startGrammarVoiceRecognition grammar="+Grammar.selectedGrammarName);
        if(Grammar.selectedGrammarName==null) Log.d(LOG_TAG, "startGrammarVoiceRecognition  grammar not selected");
        startVoceRecognition(Grammar.selectedGrammarName);

    }
    private  void startVoceRecognition(String grammar){
        Log.d(LOG_TAG, "start startVoceRecognition");
        Log.d(LOG_TAG, "getStateMes1="+String.valueOf(SpitchMobileService.getStateMes()));
        if (SpitchMobileService.getServiceState()==Constants.GETSERVICESTATE_SERVICE_BUSY) {
            Log.d(LOG_TAG, "startFreeVoiceRecognition FAIL");

            errorOnBtnPress=true;
            stopRecognition();
            showAlert("Сервис занят. Попробуйте через секундочку...");

            return;
        }
        if (!SpitchMobileService.startRecognition(grammar, handler  )){
            Log.d(LOG_TAG, "startFreeVoiceRecognition FAIL");
            showAlert("ОШИБКА: "+SpitchMobileService.getLastErrorMessage());

        } else {
            CurrentTextView.setText("Чтобы получить результаты распознавания, нажмите ВЫКЛЮЧИТЬ ЗАПИСЬ");
        }
    }


    private void stopRecognition(){
        Log.d(LOG_TAG, "stop FreeVoiceRecognition");

        int serviceState = SpitchMobileService.getServiceState();
        Log.d(LOG_TAG, "stop getServiceState="+String.valueOf(serviceState));
        if (!stopWithoutResults) CurrentTextView.setText("Ожидается ответ с сервера...");
        if (serviceState==Constants.GETSERVICESTATE_SERVICE_BUSY) {
            SpitchMobileService.stopRecognition();
        } else {

            Log.d(LOG_TAG, "stop FreeVoiceRecognition trying stop NULL object");
            stopWithoutResults=false;
        }
       // String res =  SpitchMobileService.getSpitchResult();
        //Log.d(LOG_TAG, "getSpitchResult = "+res);
        //showAlert(res);
    }
    private void invertBtn( int id){
        if (errorOnBtnPress){
            errorOnBtnPress=false;
            return;
        }

        Button b = (Button) findViewById(id);
        if (btnIsPressed) {
            unpressButton(b);

        } else {
            pressButton(b);
        }
        btnIsPressed = !btnIsPressed;
    }

    private void switchBtn(View v) {



        int btnId=v.getId();
        switch (btnId){
            case R.id.button: // свободное распознование
                CurrentTextView=txtOut;
                CurrentButton=btn;

                if (btnIsPressed)  {
                    stopRecognition();
                } else {

                    startFreeVoiceRecognition();
                }

                invertBtn(btnId);
                break;

            case R.id.button1: // распознование по грамматике
                CurrentTextView=txtOut1;
                CurrentButton=btn1;

                if (btnIsPressed) {
                    stopRecognition();
                } else {

                    startGrammarVoiceRecognition();
                }

                invertBtn(btnId);
                break;

            case R.id.button2: // слепок голоса
                CurrentTextView=txtOut2;
                CurrentButton=btn2;
                CurrentTextView.setText("Это еще не готово.");
                invertBtn(btnId);
                break;

            case R.id.button3: // проверка по слепку
                CurrentTextView=txtOut2;
                CurrentButton=btn3;
                CurrentTextView.setText("Это еще не готово.");
                invertBtn(btnId);
                break;
        }


    }
    @Override
    public void onClick(View v) {


        switchBtn(v);



    }
    public void unpressButton(Button b){
        if (b==null) return;
        b.setText(R.string.btnON);
        b.setBackgroundResource(R.drawable.button);
    }
    public void pressButton(Button b){
        b.setText(R.string.btnOFF);
        b.setBackgroundResource(R.drawable.button_off);
    }

    private void unpressButtons(){
        unpressButton(btn);
        unpressButton(btn1);
        unpressButton(btn2);
        unpressButton(btn3);
    }

    private void  resetButtons(){
        btnIsPressed=false;
        unpressButtons();

        disableEnableButtons(true);
    }
    public void disableEnableButtons(boolean enable){
        btn.setEnabled(enable);
        btn1.setEnabled(enable);
        btn2.setEnabled(enable);
        btn3.setEnabled(enable);
    }

    public void setTextToAll(String s){
        txtOut.setText(s);
        txtOut1.setText(s);
        txtOut2.setText(s);
    }


    public void  resetButtonsAndTxt(){
        btnIsPressed=false;

        unpressButtons();
        setTextToAll("");

        disableEnableButtons(true);
    }

    /**
     *  Выполняем при перелистывании..
     */
    public void onSliding(){
        resetButtonsAndTxt();
        disableEnableButtons(!isBlocked);

        stopWithoutResults=true;
        stopRecognition();
    }

    public void onInitializationError(){
        setTextToAll(this.getString(R.string.INITSERVICE_ERROR_INITIALIZATION));
        pd.setMessage(this.getString(R.string.INITSERVICE_ERROR_INITIALIZATION));
        initMenuitem.setEnabled(true);
    }



}
