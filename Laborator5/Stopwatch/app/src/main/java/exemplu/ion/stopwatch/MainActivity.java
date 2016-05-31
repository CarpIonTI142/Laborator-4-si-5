package exemplu.ion.stopwatch;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTime;
    private EditText mEtLaps;
    private Button mBtnStart;
    private Button mBtnLap;
    private Button mBtnStop;

    private ScrollView mSvLaps;
    private int mLaps = 1;

    private Context mContext;
    private Chronometer mChronometer;
    private Thread mThreadChrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mContext = this;

        mTvTime = (TextView) findViewById(R.id.tv_time);
        mEtLaps = (EditText) findViewById(R.id.et_laps);
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnLap = (Button) findViewById(R.id.btn_lap);
        mSvLaps = (ScrollView) findViewById(R.id.sv_laps);
        mBtnStop = (Button) findViewById(R.id.btn_stop);

        mEtLaps.setEnabled(false);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                if(mChronometer == null) {
                       mChronometer = new Chronometer(mContext);
                        mThreadChrono = new Thread(mChronometer);
                       mThreadChrono.start();
                     mChronometer.start();
                    mLaps = 1;
                    mEtLaps.setText("");


                }
            }
        });
      mBtnStop.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
              if(mChronometer != null){
                  mChronometer.stop();
                  mThreadChrono.interrupt();
                  mThreadChrono = null;
                  mChronometer = null;
              }
          }
      });

        mBtnLap.setOnClickListener(new View.OnClickListener(){
          @Override
            public void onClick(View v){
              if(mChronometer == null){return;}
            mEtLaps.append("LAP"+String.valueOf(mLaps++)+ "   " + String.valueOf(mTvTime.getText()) + "\n");

              mSvLaps.post(new Runnable() {
                  @Override
                  public void run() {
                      mSvLaps.smoothScrollTo(0,mEtLaps.getBottom());
                  }
              });
          }



        });

    }

    public void updateTimerText(final String time){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvTime.setText(time);
            }
        });
    }
}
