package com.easyfoodvone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easyfoodvone.fragments.LoginFragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeviceList  extends AppCompatActivity {
    private static String TAG = "---DeviceList";
    public static final int REQUEST_COARSE_LOCATION = 200;
    public static final int REQUEST_BLUETOOTH_SCAN = 201;

    static public final int REQUEST_CONNECT_BT = 0*2300;
    static private final int REQUEST_ENABLE_BT = 0*1000;
    static private BluetoothAdapter mBluetoothAdapter = null;

    static private ArrayAdapter<BluetoothDevice> btDevices = null;
    ArrayList<Names> namesArray = new ArrayList<>();
    NameAdapter nameAdapter;
    private static final UUID SPP_UUID = UUID
            .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    // UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    static private BluetoothSocket mbtSocket = null;
    ImageView backButton,refreshIcon;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_devices);
        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
        refreshIcon = findViewById(R.id.refreshIcon);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDevicesList();
            }
        });
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#ff5200"));
        }
*/
        try {
            if (initDevicesList() != 0) {
                finish();
            }

        } catch (Exception ex) {
            finish();
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(DeviceList.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }else if (ContextCompat.checkSelfPermission(DeviceList.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(DeviceList.this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        REQUEST_BLUETOOTH_SCAN);
            }
        }else{
            proceedDiscovery();
        }




    }


    protected void proceedDiscovery() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBTReceiver, filter);

        mBluetoothAdapter.startDiscovery();
    }

    public static BluetoothSocket getSocket() {
        return mbtSocket;
    }

    private void flushData() {
        try {
            if (mbtSocket != null) {
                mbtSocket.close();
                mbtSocket = null;
            }

            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.cancelDiscovery();
            }

            if (btDevices != null) {
                btDevices.clear();
                btDevices = null;
            }


            if (nameAdapter != null) {
                nameAdapter.clear();
                nameAdapter.notifyDataSetChanged();
                nameAdapter.notifyDataSetInvalidated();
                nameAdapter = null;
            }

            //finalize();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

    }
    private int initDevicesList() {
        flushData();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "Bluetooth not supported!!", Toast.LENGTH_LONG).show();
            return -1;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }


        nameAdapter = new NameAdapter(this,namesArray);
        listView.setAdapter(nameAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBluetoothAdapter == null) {
                    return;
                }

                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }

                Toast.makeText(
                        getApplicationContext(),
                        "Connecting to " + btDevices.getItem(position).getName() + ","
                                + btDevices.getItem(position).getAddress(),
                        Toast.LENGTH_SHORT).show();

                Thread connectThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            boolean gotuuid = btDevices.getItem(position)
                                    .fetchUuidsWithSdp();
                            if(btDevices.getItem(position)!=null) {
                                if(btDevices.getItem(position).getUuids()!=null) {
                                    if (btDevices.getItem(position).getUuids()[0] != null) {
                                        UUID uuid = btDevices.getItem(position).getUuids()[0]
                                                .getUuid();
                                        mbtSocket = btDevices.getItem(position)
                                                .createRfcommSocketToServiceRecord(uuid);

                                        mbtSocket.connect();
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            runOnUiThread(socketErrorRunnable);
                            try {
                                mbtSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mbtSocket = null;
                        } finally {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    finish();

                                }
                            });
                        }
                    }
                });

                connectThread.start();
            }
        });

        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        try {
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } catch (Exception ex) {

            return -2;
        }

        Toast.makeText(getApplicationContext(),
                "Getting all available Bluetooth Devices", Toast.LENGTH_SHORT)
                .show();

        return 0;

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent intent) {
        super.onActivityResult(reqCode, resultCode, intent);

        switch (reqCode) {
            case REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {
                    Set<BluetoothDevice> btDeviceList = mBluetoothAdapter
                            .getBondedDevices();
                    try {
                        if (btDeviceList.size() > 0) {

                            for (BluetoothDevice device : btDeviceList) {
                                if (btDeviceList.contains(device) == false) {

                                    btDevices.add(device);
                                    Names names = new Names(device.getName(),device.getAddress());
                                    namesArray.add(names);
                                    nameAdapter.notifyDataSetChanged();

                                }
                            }
                            if(namesArray.size()==0){
                                alertDialog("No Printer Available",DeviceList.this);

                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }


                }

                break;
        }
        mBluetoothAdapter.startDiscovery();

    }
    public static void alertDialog(String msg, Activity activity) {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View mDialogView = factory.inflate(R.layout.alert_dialog, null);
        final AlertDialog mDialog = new AlertDialog.Builder(activity).create();
        mDialog.setView(mDialogView);
        TextView msgText = mDialogView.findViewById(R.id.txt_message);
        msgText.setText(msg);
        mDialogView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mDialog.dismiss();
//your business logic
            }
        });

        mDialog.show();
    }

    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                try {
                    if (btDevices == null) {
                        btDevices = new ArrayAdapter<BluetoothDevice>(
                                getApplicationContext(), R.layout.layout_list);
                    }

                    if (btDevices.getPosition(device) < 0) {
                        btDevices.add(device);
                        Names names = new Names(device.getName(),device.getAddress());
                        namesArray.add(names);
                        nameAdapter.notifyDataSetChanged();

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            }
        }
    };


    private Runnable socketErrorRunnable = new Runnable() {

        @Override
        public void run() {
            Toast.makeText(getApplicationContext(),
                    "Cannot establish connection", Toast.LENGTH_SHORT).show();
            mBluetoothAdapter.startDiscovery();

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedDiscovery();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Permission is not granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case REQUEST_BLUETOOTH_SCAN: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedDiscovery();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Permission is not granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }





    @Override
    protected void onStop()
    {
        super.onStop();
        try {
            unregisterReceiver(mBTReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class NameAdapter extends ArrayAdapter<Names> {

        private Context mContext;
        private List<Names> moviesList = new ArrayList<>();

        public NameAdapter(@NonNull Context context,@NotNull ArrayList<Names> list) {
            super(context, 0 , list);
            mContext = context;
            moviesList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

            Names currentMovie = moviesList.get(position);



            TextView name = (TextView) listItem.findViewById(R.id.textView_name);

            if(currentMovie.getmName()!=null)
                name.setText(currentMovie.getmName());
            else
                name.setText("Unknown");
            TextView address = (TextView) listItem.findViewById(R.id.textView_address);
            address.setText(currentMovie.getmAddress());

            return listItem;
        }
    }

    public class Names {

        // Store the id of the  movie poster
        // Store the name of the movie
        private String mName;
        // Store the release date of the movie
        private String mAddress;

        // Constructor that is used to create an instance of the Movie object
        public Names(String mName, String mAddress) {
            this.mName = mName;
            this.mAddress = mAddress;
        }


        public String getmName() {
            return mName;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }

        public String getmAddress() {
            return mAddress;
        }

        public void setmAddress(String mAddress) {
            this.mAddress = mAddress;
        }
    }
}