package yongjin.lastchallenge.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import yongjin.lastchallenge.R;
import yongjin.lastchallenge.utils.AppSettings;

/**
 * Created by jeonyongjin on 2016. 12. 22..
 */
public class LLSettingsFragment extends Fragment {
    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;

//    private CheckBox mCheckBackground;
//    private TextView mTextIot;

    private DatePicker datePicker;
    private EditText reservehour;
    private EditText reservemin;
    private Button setcomplete;

    private String year;
    private String month;
    private String dayofmonth;

    private int todayyear;
    private int todaymonth;
    private int todayday;

    private int nowhour;
    private int nowminute;

    private int hour;
    private int minute;

    public static String fmessage;

    public LLSettingsFragment(Context c, IFragmentListener l) {
        mContext = c;
        mFragmentListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        AppSettings.initializeAppSettings(mContext);

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        datePicker = (DatePicker)rootView.findViewById(R.id.datePicker);
        reservehour = (EditText)rootView.findViewById(R.id.reservehour);
        reservemin = (EditText)rootView.findViewById(R.id.reservemin);
        setcomplete = (Button)rootView.findViewById(R.id.buttonset);



        datePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                year = String.valueOf(datePicker.getYear());
                year = year.substring(2);
                Log.d("valueyear", String.valueOf(year));
                month = String.valueOf(datePicker.getMonth()+1);
                Log.d("valuemonth", String.valueOf(month));
                dayofmonth = String.valueOf(datePicker.getDayOfMonth());
                Log.d("valuedayr", String.valueOf(dayofmonth));
            }
        });


        setcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.performClick();


                String h = "";
                String m = "";
                if(reservehour.getText()!=null) {
//                    hour = Integer.getInteger(String.valueOf(reservehour.getText()));
                    h = String.valueOf(reservehour.getText());
                }
                if(reservemin.getText()!=null) {
//                    minute = Integer.getInteger(String.valueOf(reservemin.getText()));
                    m = String.valueOf(reservemin.getText());
                }
//                Log.d("Currenttime : ", strCurhour +":"+ strCurmin);
                Log.d("reservetime : ", h + ":" + m);
                Log.d("Pickeddate : ", year + "," + month + "," + dayofmonth);
//                Log.d("today : ", strCurYear + "," + strCurMonth + "," + strCurDay);

                fmessage = "4,";
                fmessage = fmessage.concat(year+","+month+","+dayofmonth+","+h+","+m+"z");
                Log.d("messagetimevalue",fmessage);
                Toast.makeText(getActivity().getApplicationContext(),"알람시간설정이 되었습니다",Toast.LENGTH_LONG).show();
//                if(message!=null && message.length()>0){
//                    sendMessage(message);
//                    Toast.makeText(getActivity().getApplicationContext(),"알람이 설정되었습니다",Toast.LENGTH_LONG).show();
//                }
//                Bundle extra = new Bundle();
//                String fragmentdata = new String();
//                fragmentdata = (String) extra.get(message);
            }
        });
        return rootView;
    }
    public void sendMessage(String message) {
        if(message == null || message.length() < 1)
            return;
        // send to remote
        if(mFragmentListener != null)
            mFragmentListener.OnFragmentCallback(IFragmentListener.CALLBACK_SEND_MESSAGE, 0, 0, message, null,null);
        else
            return;
    }
}
