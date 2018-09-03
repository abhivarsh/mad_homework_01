/*a. Assignment HW1.
b. File Name : MainActivity.java
c. Full name of all students in your group:(GRP-17)
        1. Naga Vamsi Abhivarsh Peddireddy
        2. Siva Kumar Reddy Vayyeti*/

package com.example.pabhivarshnv.baccalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    EditText weight;
    Switch gender;
    RadioGroup drinksRgp;
    RadioButton drinkSizeRb;
    SeekBar alcoholPercentage;
    TextView bacValue;
    TextView status;
    TextView alcoholIndicator;
    ProgressBar progressBar;
    CharSequence genderValue = "F";
    Double weightValue;
    int drinkSize = 1;
    int alcoholPercentageIndicator;
    double calBacValue;
    double val = 0.00;

    Double numerator = 0.0;
    Double denominator = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.glass_icon);
        actionBar.setTitle(R.string.app_name);

        weight = findViewById(R.id.input_weight);
        gender = (Switch) findViewById(R.id.gender);
        drinksRgp = (RadioGroup) findViewById(R.id.alcohol_size);
        drinkSizeRb = (RadioButton) findViewById(drinksRgp.getCheckedRadioButtonId());
        alcoholPercentage = (SeekBar) findViewById(R.id.seekBar_alcohol);
        alcoholPercentage.setMin(5);
        bacValue = findViewById(R.id.bac_value);
        status = findViewById(R.id.status_value);
        alcoholIndicator = findViewById(R.id.alcohol_indicator);
        progressBar = findViewById(R.id.progressBar_bac);
        alcoholIndicator.setText("5%");
        bacValue.setText("0.0");
        status.setText(R.string.safe_limit);

        gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    genderValue = gender.getTextOn();
                }
                else{
                    genderValue = gender.getTextOff();
                }
            }
        });

        drinksRgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                drinkSizeRb = findViewById(checkedId);
                drinkSize = Integer.parseInt(drinkSizeRb.getText().toString().split(" ")[0]);
            }
        });

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(weight.getText().toString().equalsIgnoreCase("")
                        ||Double.parseDouble(weight.getText().toString()) <= 0.0){
                    Toast.makeText(MainActivity.this, R.string.hint_enter_weight, Toast.LENGTH_SHORT).show();
                    weight.setText("");
                }else {
                    weightValue = Double.parseDouble(weight.getText().toString());
                    double genderConstant = genderValue.equals("F")?0.55:0.68;
                    Log.d("demo", "weight : "+weightValue+",gender : "+genderValue);
                    Toast.makeText(MainActivity.this, R.string.hint_saved, Toast.LENGTH_SHORT).show();
                    denominator = weightValue*genderConstant;
                    calculateBac();

                }
            }
        });

        alcoholPercentage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("demo", "onProgressChanged: "+progress);
                alcoholPercentageIndicator=progress;
                alcoholIndicator.setText(progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.add_drink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setError(this)){
                    Double newDrink = drinkSize*alcoholPercentageIndicator*6.24;
                    numerator += newDrink;
                    calculateBac();
                }

            }
        });

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weight.setEnabled(true);
                gender.setClickable(true);
                findViewById(R.id.shot).setClickable(true);
                findViewById(R.id.glass).setClickable(true);
                findViewById(R.id.can).setClickable(true);
                findViewById(R.id.add_drink).setEnabled(true);
                findViewById(R.id.save_button).setEnabled(true);
                findViewById(R.id.seekBar_alcohol).setEnabled(true);
                progressBar.setProgress(0);
                bacValue.setText("0.0");
                status.setText(R.string.safe_limit);
                status.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));

                weight.setText("");
                weightValue=0.0;
                numerator=0.0;
                denominator=0.0;
                gender.setChecked(false);
                drinksRgp.check(R.id.shot);
                alcoholPercentage.setProgress(5);

            }
        });

    }

    private void calculateBac() {

        //calBacValue = (drinkSize*(alcoholPercentageIndicator)*6.24/(weightValue*g))/100;

        calBacValue = numerator/denominator/100;
        val = (double) Math.round(calBacValue*1000);
        bacValue.setText(Double.toString(val/1000));
        Log.d("demo", "onClick: "+drinkSize+" : "+alcoholPercentageIndicator+" : "+
                weightValue+" : "+numerator+" : "+val+" : "+calBacValue);
        progressBar.setProgress((int) Math.round(val/10));

        if(val/1000>0.08 && val/1000<0.20){
            status.setText(R.string.normal_limit);
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        }
        else if(val/1000>=0.20){
            status.setText(R.string.over_limit);
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        else{
            status.setText(R.string.safe_limit);
            status.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        if((val/10)>=25.0){
            weight.setEnabled(false);
            gender.setClickable(false);
            findViewById(R.id.shot).setClickable(false);
            findViewById(R.id.glass).setClickable(false);
            findViewById(R.id.can).setClickable(false);
            findViewById(R.id.seekBar_alcohol).setEnabled(false);
            findViewById(R.id.add_drink).setEnabled(false);
            findViewById(R.id.save_button).setEnabled(false);

            Toast.makeText(this, R.string.hint_over_limit, Toast.LENGTH_SHORT).show();

        }
    }

    public boolean setError(View.OnClickListener view) {

        try{
            Log.d("demo", "setError: "+weightValue);
            if (weightValue.equals("") || weightValue.equals(" ") || weightValue<=0.0){
                Toast.makeText(this, R.string.hint_enter_weight, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception e){
            Toast.makeText(this, R.string.hint_enter_weight, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
