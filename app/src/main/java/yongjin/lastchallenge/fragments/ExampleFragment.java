package yongjin.lastchallenge.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;

import java.text.SimpleDateFormat;
import java.util.Date;

import yongjin.lastchallenge.R;

/**
 * Created by jeonyongjin on 2016. 12. 22..
 */
public class ExampleFragment extends Fragment implements View.OnClickListener{
    private SeekArc mSeekArc;
    private TextView output;
    private Button LEDCOLOR;
    private Button SOUNDVOLUME;
    private Button BRIGHTNESS;
    private Button ONOFF;
    private Button Play;
    private Button Pause;
    private Button nextsong;
    private Button previoussong;

    private Button sendtime;

    private int flag = 0;
    private int lightflag = 0;

    private int progressv = 0;
    private int progresss = 0;

    private int rgbhex;
    private int r;
    private int g;
    private int b;

    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;
    private Handler mActivityHandler = null;

    TextView mTextChat;
    EditText mEditChat;
    Button mBtnSend;


    public ExampleFragment(Context c, IFragmentListener l, Handler h) {
        mContext = c;
        mFragmentListener = l;
        mActivityHandler = h;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
        mSeekArc = (SeekArc)rootView.findViewById(R.id.threshold);
        output = (TextView)rootView.findViewById(R.id.output);
        LEDCOLOR = (Button)rootView.findViewById(R.id.ledcolor);
        BRIGHTNESS = (Button)rootView.findViewById(R.id.ledbrightness);
        SOUNDVOLUME = (Button)rootView.findViewById(R.id.soundvolume);
        ONOFF = (Button)rootView.findViewById(R.id.onoff);
        Play = (Button)rootView.findViewById(R.id.play);
        Pause = (Button)rootView.findViewById(R.id.pause);
        nextsong = (Button)rootView.findViewById(R.id.nextsong);
        previoussong = (Button)rootView.findViewById(R.id.previoussong);
        sendtime = (Button)rootView.findViewById(R.id.alarm);
        mSeekArc.setEnabled(false);
//        mTextChat = (TextView) rootView.findViewById(R.id.text_chat);
//        mTextChat.setMaxLines(1000);
//        mTextChat.setVerticalScrollBarEnabled(true);
//        mTextChat.setMovementMethod(new ScrollingMovementMethod());
//
//        mEditChat = (EditText) rootView.findViewById(R.id.edit_chat);
//        mEditChat.setOnEditorActionListener(mWriteListener);
//
//        mBtnSend = (Button) rootView.findViewById(R.id.button_send);
//        mBtnSend.setOnClickListener(this);
        mSeekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            // control seekArc
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser){

                if(flag==1) {

                    rgbhex = interpolateColor(Color.RED,
                            Color.MAGENTA, mSeekArc.getProgress() / 100f);
                    // change top left rectange background color according
                    // to seekbar progress from red to blue
                    Log.d("messagecolorvalue", String.valueOf(rgbhex));
                    mSeekArc.setBackgroundColor(rgbhex);
                    r = (rgbhex & 0xFF0000) >> 16;
                    Log.d("messageRvalue", String.valueOf(r));
                    g = (rgbhex & 0xFF00) >> 8;
                    Log.d("messageGvalue", String.valueOf(g));
                    b = (rgbhex & 0xFF);
                    Log.d("messageBvalue", String.valueOf(b));
                    String message = "2,";
                    message = message.concat(String.valueOf(r)+","+String.valueOf(g)+","+String.valueOf(b)+"z");
                    Log.d("messagecolortoarduino",message);
                    if(message!=null && message.length()>0){
                        sendMessage(message);
                    }

                }
                else if(flag==2) {
                    output.setText(String.valueOf(progress));
                    progressv = (int) (progress*2.55);
                    String message = "1,";
                    message = message.concat(String.valueOf(progressv)+"z");
                    Log.d("messagebrightvalue", message);
                    if(message!=null && message.length()>0){
                        sendMessage(message);
                    }

                }
                else if(flag==3) {
                    output.setText(String.valueOf(progress/2));
                    String message = "3,";
                    Log.d("messagesoundvalue",message);
                    if((progress/2)-progresss!=0){
                        if((progress/2)-progresss>0){
                            message = message.concat("uz");
                            Log.d("messagesoundvalue", message);
                            if(message!=null && message.length()>0){
                                sendMessage(message);
                            }
                            progresss = progress/2;
                        }
                        else if((progress/2)-progresss<0){
                            message = message.concat("dz");
                            Log.d("messagesoundvalue", message);
                            if(message!=null && message.length()>0){
                                sendMessage(message);
                            }
                            progresss = progress/2;
                        }
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }
            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });
        // buttonlistener for change led color
        LEDCOLOR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSeekArc.setEnabled(true);
                output.setTextColor(Color.WHITE);
                output.setText("Color control");
                if(flag!=1) {
                    BRIGHTNESS.setTextColor(Color.BLACK);
                    SOUNDVOLUME.setTextColor(Color.BLACK);
                    LEDCOLOR.setTextColor(Color.WHITE);
                    flag = 1;
                }
                else if(flag==1){
                    LEDCOLOR.setTextColor(Color.BLACK);
                    mSeekArc.setBackgroundColor(Color.WHITE);
                    mSeekArc.setEnabled(false);
                    flag=0;
                    String message = "2,0,0,0z";
                    if(message!=null&&message.length()>0) {
                        sendMessage(message);
                    }
                }
            }
        });

        // buttonlistener for change led brightness
        BRIGHTNESS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSeekArc.setEnabled(true);
                output.setTextColor(Color.BLACK);
                mSeekArc.setBackgroundColor(Color.WHITE);
                output.setText("0");
                if(flag!=2) {
                    LEDCOLOR.setTextColor(Color.BLACK);
                    SOUNDVOLUME.setTextColor(Color.BLACK);
                    BRIGHTNESS.setTextColor(Color.WHITE);
                    mSeekArc.setProgress(0);
                    flag = 2;
                }
                else if(flag==2){
                    BRIGHTNESS.setTextColor(Color.BLACK);
                    mSeekArc.setEnabled(false);
                    flag=0;
                }
            }
        });

        // buttonlistener for change sound volume of music
        SOUNDVOLUME.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mSeekArc.setEnabled(true);
                output.setTextColor(Color.BLACK);
                mSeekArc.setBackgroundColor(Color.WHITE);
                output.setText("0");
                if(flag!=3) {
                    LEDCOLOR.setTextColor(Color.BLACK);
                    BRIGHTNESS.setTextColor(Color.BLACK);
                    SOUNDVOLUME.setTextColor(Color.WHITE);
                    mSeekArc.setProgress(0);
                    flag = 3;
                }
                else if(flag==3){
                    SOUNDVOLUME.setTextColor(Color.BLACK);
                    mSeekArc.setEnabled(false);
                    flag=0;
                }
            }
        });

        ONOFF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                long now = System.currentTimeMillis();
                Date date = new Date(now);

                SimpleDateFormat CurYearFormat = new SimpleDateFormat("yy");
                SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
                SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
                SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
                SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

                String strCurYear = CurYearFormat.format(date);
                String strCurMonth = CurMonthFormat.format(date);
                String strCurDay = CurDayFormat.format(date);
                String strCurhour = CurHourFormat.format(date);
                String strCurmin = CurMinuteFormat.format(date);

                if(lightflag == 0){
                    String message = "6,1,";
                    if(message!=null && message.length()>0){
                        message = message.concat(strCurYear+","+strCurMonth+","+strCurDay+","+strCurhour+","+strCurmin+"z");
                        sendMessage(message);
                        Log.d("messageonoff", message);
                        lightflag = 1;
                    }
                }
                else if(lightflag == 1){
                    String message = "6,0,";
                    if(message!=null && message.length()>0){
                        message = message.concat(strCurYear+","+strCurMonth+","+strCurDay+","+strCurhour+","+strCurmin+"z");
                        sendMessage(message);
                        Log.d("messageonoff", message);
                        mSeekArc.setProgress(0);
                        lightflag = 0;
                    }
                }
            }
        });

        Play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = "5,pz";
                if(message!=null && message.length()>0){
                    Log.d("messageplay", message);
                    sendMessage(message);
                }
            }
        });

        Pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = "5,sz";
                if(message!=null && message.length()>0){
                    Log.d("messagepause", message);
                    sendMessage(message);
                }
            }
        });

        nextsong.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = "5,nz";
                if(message!=null && message.length()>0){
                    Log.d("messagenext", message);
                    sendMessage(message);
                }
            }
        });

        previoussong.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String message = "5,bz";
                if(message!=null && message.length()>0){
                    Log.d("messageprevious", message);
                    sendMessage(message);
                }
            }
        });

        sendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = LLSettingsFragment.fmessage;
                if(message!=null && message.length()>0){
                    Log.d("messagetimevalue",message);
                    sendMessage(message);
                    Toast.makeText(getActivity().getApplicationContext(),"알람세팅이 완료되었습니다",Toast.LENGTH_LONG).show();
                }

            }
        });
        return rootView;
    }

    // used to take colors mix according to proportion
    private int interpolateColor(final int a, final int b,
                                 final float proportion) {
        final float[] hsva = new float[3];
        final float[] hsvb = new float[3];
        Color.colorToHSV(a, hsva);
        Color.colorToHSV(b, hsvb);
        for (int i = 0; i < 3; i++) {
            hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
        }
        return Color.HSVToColor(hsvb);
    }

    private float interpolate(final float a, final float b,
                              final float proportion) {
        return (a + ((b - a) * proportion));
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
//            case R.id.button_send:
//                String message = mEditChat.getText().toString();
//                if(message != null && message.length() > 0)
//                    sendMessage(message);
//                break;
            case R.id.onoff:
                String message = "1";
                if(message!=null && message.length() >0)
                    sendMessage(message);
                break;
        }
    }


    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        if(message != null && message.length() > 0)
                            sendMessage(message);
                    }
                    return true;
                }
            };

    // Sends user message to remote
    public void sendMessage(String message) {
        if(message == null || message.length() < 1)
            return;
        // send to remote
        if(mFragmentListener != null)
            mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, 0, 0, message, null,null);
        else
            return;
    }

    private static final int NEW_LINE_INTERVAL = 1000;
    private long mLastReceivedTime = 0L;

    // Show messages from remote
    public void showMessage(String message) {
        if(message != null && message.length() > 0) {
            long current = System.currentTimeMillis();

            if(current - mLastReceivedTime > NEW_LINE_INTERVAL) {
                mTextChat.append("\nRcv: ");
            }
            mTextChat.append(message);
            int scrollamout = mTextChat.getLayout().getLineTop(mTextChat.getLineCount()) - mTextChat.getHeight();
            if (scrollamout > mTextChat.getHeight())
                mTextChat.scrollTo(0, scrollamout);

            mLastReceivedTime = current;
        }
    }

}
