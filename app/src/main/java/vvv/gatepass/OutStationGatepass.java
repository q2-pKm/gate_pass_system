package vvv.gatepass;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OutStationGatepassInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OutStationGatepass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutStationGatepass extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OutStationGatepassInteractionListener mListener;

    AutoCompleteTextView purpose, destination;
    String out_time_sel, in_time_sel, in_date_sel, out_date_sel;
    public Button out_time, in_time, request, in_date, out_date;

    public String rUserName, rEnrollKey, rFullName, rBranch, rRoom, rContact, rDestination, rPurpose;


    public OutStationGatepass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OutStationGatepass.
     */
    // TODO: Rename and change types and number of parameters
    public static OutStationGatepass newInstance(String param1, String param2) {
        OutStationGatepass fragment = new OutStationGatepass();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.out_station_gatepass_fragment, container, false);

        purpose = (AutoCompleteTextView) view.findViewById(R.id.purpose);
        destination = (AutoCompleteTextView) view.findViewById(R.id.destination);

        AppData.LoggedInUser = PreferenceManager.getDefaultSharedPreferences(getActivity());
        rUserName = AppData.LoggedInUser.getString("rUserName", "");
        rEnrollKey = AppData.LoggedInUser.getString("rEnrollKey", "");
        rFullName = AppData.LoggedInUser.getString("rFullName", "");
        rBranch = AppData.LoggedInUser.getString("rBranch", "");
        rRoom = AppData.LoggedInUser.getString("rRoom", "");
        rContact = AppData.LoggedInUser.getString("rContact", "");
        Log.d("Bundle : ", "Restored.");

        out_time = (Button) view.findViewById(R.id.out_time);
        out_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        out_time.setText(selectedHour + ":" + selectedMinute);
                        out_time_sel = selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select OUT TIME: ");
                mTimePicker.show();
            }
        });

        in_time = (Button) view.findViewById(R.id.in_time);
        in_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        in_time.setText(selectedHour + ":" + selectedMinute);
                        in_time_sel = selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select IN TIME: ");
                mTimePicker.show();
            }
        });

        in_date = (Button) view.findViewById(R.id.in_date);
        in_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;
                        in_date.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        in_date_sel = "" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select IN Date:");
                mDatePicker.show();
            }
        });

        out_date = (Button) view.findViewById(R.id.out_date);
        out_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        selectedmonth = selectedmonth + 1;
                        out_date.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                        out_date_sel = "" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select OUT Date:");
                mDatePicker.show();
            }
        });

        request = (Button) view.findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rPurpose = purpose.getText().toString();
                rDestination = destination.getText().toString();
                AddRequest addRequest = new AddRequest(getActivity());
                addRequest.execute();
            }
        });

        return view;
    }

    public void getTime(Context ctx, final Button button){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                button.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OutStationGatepassInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OutStationGatepassInteractionListener) {
            mListener = (OutStationGatepassInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LocalGatepassInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OutStationGatepassInteractionListener {
        // TODO: Update argument type and name
        void OutStationGatepassInteraction(Uri uri);
    }


    class AddRequest extends AsyncTask<String, String, JSONObject> {

        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
        Context ctxt;

        AddRequest(Context ctx){
            ctxt = ctx;
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sending Request...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("student_name", rFullName);
                params.put("user_name", rUserName);
                params.put("purpose", rPurpose);
                params.put("request_status", "Pending");
                params.put("request_to", "dummy.warden");
                params.put("enrollment_no", rEnrollKey);

                params.put("out_date", out_date_sel);
                params.put("out_time", out_time_sel);
                params.put("in_date", in_date_sel);
                params.put("in_time", in_time_sel);

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int seconds = mcurrentTime.get(Calendar.SECOND);
                params.put("request_time", String.valueOf(hour) + " : " + String.valueOf(minute) + " : " + String.valueOf(seconds));
                params.put("approved_time", "Not Required");
                params.put("visit_place", rDestination);
                params.put("visit_type", "Others");
                params.put("contact_number", rContact);

                JSONObject json = jsonParser.makeHttpRequest(AppData.ULRAddRequests, "GET", params);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {

            pDialog.setMessage("Sending Request...");

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            try {
                if (json.getBoolean("result")) {
                    Toast.makeText(ctxt, "Request made.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctxt, "Request Failed.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
            }
        }
    }
}
