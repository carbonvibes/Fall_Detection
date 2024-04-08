package com.example.Parent;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

   private OnFragmentInteractionListener mListener;

   // Network info
   private boolean networkState;
   private String networkName;
   private String networkIP;

   // Device info
   private String deviceSerial;
   private String deviceUnsupportedSensors;
   private ArrayList<String> listOfUnsupportedSensorsInModel;

   // Model info
   private String modelName;
   private String awaitedModelInfo="";

    public InfoFragment() {}

    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

       // Initialize properties when view is created
       setNetworkInfo();
       setDeviceInfo();
       setModelInfo();

       // Display the appropriate text sections
       displayNetworkInfo((TextView)rootView.findViewById(R.id.InfoTab_Network_Data));
       displayDeviceInfo((TextView)rootView.findViewById(R.id.InfoTab_Device_Data));
       displayModelInfo((TextView)rootView.findViewById(R.id.InfoTab_Model_Data));

        return rootView;
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       try {
           mListener = (OnFragmentInteractionListener) activity;
       } catch (ClassCastException e) {
           throw new ClassCastException(activity.toString()
           + " must implement OnFragmentInteractionListener");
       }
   }*/

   @Override
   public void onDetach() {
       super.onDetach();
       mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListener == null) {
            try {
                 mListener = (OnFragmentInteractionListener) getActivity();
            } catch (ClassCastException e) {
                 throw new ClassCastException(getActivity().toString()
              + " must implement OnFragmentInteractionListener");
            }
        }
       mListener.onFragmentStart("Info");
    }
    @Override
    public void onResume() {
        super.onResume();
        mListener.onFragmentResume("Info");
    }


    private void setNetworkInfo() {
       WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
       networkState = wifiManager.isWifiEnabled();

       if(networkState == true) {
           networkName     = wifiManager.getConnectionInfo().getSSID();
           networkName     = networkName.replace("\"","");
           int ipAddress   = wifiManager.getConnectionInfo().getIpAddress();
           networkIP       = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
       }
   }

   private void setDeviceInfo() {
       deviceSerial = Build.SERIAL;
            }

   private void setModelInfo() {
       modelName = "Parent";
        }

   private void displayNetworkInfo(TextView tv) {
       // Network section displays: Title, Network name, ip address
       tv.setText("");

       if (networkState != true) {// wifi disabled
           tv.append("Wifi is not enabled");
           return;
       }
       if (networkName != null && !networkName.isEmpty() && !networkName.equals("<unknown ssid>")) {
       tv.append("Name:      "+ networkName + "\n\n");
       }
       tv.append("IP Address:      "+ networkIP + "\n\n");
   }

   private void displayDeviceInfo(TextView tv) {
       // Device section displays: Title, serial num, list of unsupported sensors (if used)
       tv.setText("");

       tv.append("Serial:     "+ deviceSerial + "\n\n");
   }

   private void displayModelInfo(TextView tv) {
       // Model section displays: Title, model name, camera resolutions (if used)
       tv.setText("");
       tv.append("Name:      " + modelName+ "\n\n");
       if (awaitedModelInfo!= null && !awaitedModelInfo.isEmpty())
           tv.append(awaitedModelInfo);
   }

   public void updateModelInfo(String msg) {
        TextView textView = (TextView) getActivity().findViewById(R.id.InfoTab_Model);
        if(textView == null) {
            if(!awaitedModelInfo.contains(msg))
                awaitedModelInfo += (msg + "\n\n");
            return;
        }
        String str = textView.getText().toString();
        if(!str.contains(msg)) {
            textView.append( msg + "\n\n");
        }
   }

   public void setFragmentInteractionListener(Activity activity) {
       try {
           mListener = (OnFragmentInteractionListener) activity;
       } catch (ClassCastException e) {
           throw new ClassCastException(activity.toString()
                   + " must implement OnFragmentInteractionListener");
       }
   }
}
