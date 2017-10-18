package com.bipinkh.secureqr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

//decryption
public class ScanActivity extends AppCompatActivity {
    public String scanresult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Bundle bundle = getIntent().getExtras();
        scanresult = bundle.getString("scanresult");
        TextView tv = (TextView) findViewById(R.id.scanResultText);
        tv.setText(scanresult);
        Log.d("datsun","scanresult:::"+scanresult);
        decodeWithOwnListener();
        decodeWithDifferentListener();
    }



    private void decodeWithOwnListener() {
        Button decryptbutton = (Button) findViewById(R.id.decodeWithOwn);
        decryptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String result = processing.decryption(scanresult, HomeActivity.getDefaultPrivateKey());  //decryption
                decrypt(HomeActivity.getDefaultPrivateKey());
//                if (result.startsWith("qr://")){
//                    TextView tv3 = (TextView) findViewById(R.id.decryptTopText);
//                    tv3.setText("Decrypted Result");
//                    TextView tv2 = (TextView) findViewById(R.id.decryptResulttext);
//                    tv2.setText(result);
//                    Toast.makeText(ScanActivity.this, "Encryption Successful", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
//                    builder.setTitle("Alert");
//                    builder.setMessage(" Decryption of this text doesn't match the structure. " +
//                            "It may be plain text or encrypted using different algorithm. \n\nDecrypt it any way ?");
//                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            TextView tv = (TextView) findViewById(R.id.scanResultText);
//                            TextView tv3 = (TextView) findViewById(R.id.decryptTopText);
//                            tv3.setText("Decrypted Result");
//                            TextView tv2 = (TextView) findViewById(R.id.decryptResulttext);
//                            tv2.setText(result+"\n\n");
//                            }});
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                                }
//                            }
//                    );
//                    AlertDialog alert = builder.create();
//                    alert.show();
//
//                }
//            }
//        });
            }
        });
    }

    private void decodeWithDifferentListener() {
        Button decryptbutton = (Button) findViewById(R.id.decodeWithDifferent);
        decryptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScanActivity.this,"check",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                builder.setTitle("Enter Base 64 Encrypted Private Key");
                final EditText input = new EditText(ScanActivity.this);
                input.setLines(3);
                input.setHint("(Key here)");
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            KeyFactory kf = KeyFactory.getInstance("RSA");
                            byte[] encodedPv = Base64.decode(input.getText().toString(), Base64.DEFAULT);
                            PKCS8EncodedKeySpec keySpecPv = new PKCS8EncodedKeySpec(encodedPv);
                            PrivateKey pk = kf.generatePrivate(keySpecPv);
                            decrypt(pk);
                        } catch (Exception e) {
                            Toast.makeText(ScanActivity.this, "InvalidKeySpecException. Please recheck.", Toast.LENGTH_LONG).show();
                            Log.d("datsun", "Error in input key:: " + e.toString());
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void decrypt(PrivateKey pk){
                final String result = processing.decryption(scanresult, pk);  //decryption
                if (result.startsWith("qr://")){
                    TextView tv3 = (TextView) findViewById(R.id.decryptTopText);
                    tv3.setText("Decrypted Result");
                    TextView tv2 = (TextView) findViewById(R.id.decryptResulttext);
                    tv2.setText(result);
                    Toast.makeText(ScanActivity.this, "Encryption Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                    builder.setTitle("Alert");
                    builder.setMessage(" Decryption of this text doesn't match the structure. " +
                            "It may be plain text or encrypted using different algorithm. \n\nDecrypt it any way ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TextView tv = (TextView) findViewById(R.id.scanResultText);
                            TextView tv3 = (TextView) findViewById(R.id.decryptTopText);
                            tv3.setText("Decrypted Result");
                            TextView tv2 = (TextView) findViewById(R.id.decryptResulttext);
                            tv2.setText(result+"\n\n");
                        }});
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }
                    );
                    AlertDialog alert = builder.create();
                    alert.show();
                }
    }

}
