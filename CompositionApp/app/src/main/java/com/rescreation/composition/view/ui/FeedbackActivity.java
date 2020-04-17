package com.rescreation.composition.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.rescreation.composition.R;
import com.rescreation.composition.model.Config;
import com.rescreation.composition.model.FeedbackResponse;
import com.rescreation.composition.model.FkeyEntryResponse;
import com.rescreation.composition.model.repository.RetrofitClient;
import com.rescreation.composition.model.repository.RetrofitService;

public class FeedbackActivity extends AppCompatActivity {


    Context mContext;

    ImageButton backImageButton;
    TextView titleTextView,errorTextView;

    Button saveButton;
    EditText customerName_editText,email_editText,phone_editText,query_editText;

    String name,email,phone,query;
    String APPS_VERSION_URL,phpFileName="user_feedback.php";

    ProgressBar LoadingprogressBar;
    String status,status_message;
    String regId;
    RetrofitService retrofitService;

    SharedPreferences pref;
    SharedPreferences pref_firebase;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor fkeyEditor;
    public String BASE_URL = "",newToken="",fkey="";

    Config config;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        config=new Config();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backImageButton=findViewById(R.id.backImageButton);
        titleTextView=findViewById(R.id.title);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContext = FeedbackActivity.this;
        pd = new ProgressDialog(mContext);

        customerName_editText=findViewById(R.id.customerName_editText);
        email_editText=findViewById(R.id.email_editText);
        phone_editText=findViewById(R.id.phone_editText);
        query_editText=findViewById(R.id.query_editText);
        saveButton=findViewById(R.id.saveButton);
        errorTextView=findViewById(R.id.errorTextView);
        LoadingprogressBar = (ProgressBar)findViewById(R.id.loadingprogressBar);
        LoadingprogressBar.setVisibility(View.INVISIBLE);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        pref_firebase = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        editor = pref.edit();
        fkeyEditor = pref_firebase.edit();
        BASE_URL=pref.getString("baseURL", null);
        retrofitService = RetrofitClient.getClient(BASE_URL).create(RetrofitService.class);

        regId = pref_firebase.getString("newToken", null);
        OnClickSaveButton();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void OnClickSaveButton() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=customerName_editText.getText().toString();
                phone=phone_editText.getText().toString();
                email=email_editText.getText().toString();
                query=query_editText.getText().toString();

                if (name.isEmpty()){
                    Toast.makeText(mContext,"Enter Name First",Toast.LENGTH_LONG).show();
                    return;
                }
                if (phone.isEmpty()){
                    Toast.makeText(mContext,"Enter Phone Number First",Toast.LENGTH_LONG).show();
                    return;
                }
                if (query.isEmpty()){
                    Toast.makeText(mContext,"Query field can not be empty",Toast.LENGTH_LONG).show();
                    return;
                }
                if (query.contains("'")) {

                    query_editText.setError("Can not use special characters ");
                    Toast.makeText(mContext, "Can not use special characters in remarks", Toast.LENGTH_LONG).show();
                    return;

                }

                if (!config.isInternetAvailable()){
                    config.ShowAlert(mContext,"Failed", "Internet Connection Not Working Properly , Check your mobile Data or WIFI");
                    return;
                }


                InsertData(name,phone,email,query);
            }

        });
    }


    private void InsertData(String name, String phone, String email, String query) {

        final KProgressHUD progressDialog=  KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Call<FeedbackResponse> call3 =  retrofitService.SaveFeedback(name,phone,email,query,regId);
        call3.enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                int count=0;
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    FeedbackResponse loginResponse = response.body();
                    String message = loginResponse.getMessage();
                    String status = loginResponse.getStatus();
                    if (status.equals("1")){
                        customerName_editText.setText("");
                        phone_editText.setText("");
                        email_editText.setText("");
                        query_editText.setText("");
                        // Toast.makeText(mContext,jsonObject.getString(status_message),Toast.LENGTH_LONG).show();
                        errorTextView.setText("Feedback Submitted Successfully");
                        errorTextView.setTextColor(Color.BLUE);

                    }
                    else{
                        //  mMaster.ShowAlert(mContext, "Failed", message);
                        count=-1;
                        errorTextView.setText("Something went wrong, Feedback Submission Failed");
                        errorTextView.setTextColor(Color.RED);
                        progressDialog.dismiss();

                    }


                }else{
                    FeedbackResponse loginResponse = response.body();
                    String message = loginResponse.getMessage();
                    String status = loginResponse.getStatus();

                    progressDialog.dismiss();

                }


            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                call.cancel();
                LoadingprogressBar.setVisibility(View.GONE);


            }
        });

    }
}
