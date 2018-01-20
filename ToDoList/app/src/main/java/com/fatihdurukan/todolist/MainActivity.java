package com.fatihdurukan.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker;
import java.util.Calendar;
import java.util.ArrayList;


import static android.app.DatePickerDialog.*;
import static com.fatihdurukan.todolist.R.id.email;
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    FloatingActionButton addTask;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ToDoData> tdd = new ArrayList<>();
    SqliteHelper mysqlite;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "MainActivity";





    private ImageView mail,sms;
    private TextView mDisplayDate;
    private OnDateSetListener mDateSetListener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_s);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        addTask = (FloatingActionButton) findViewById(R.id.imageButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adapter = new ToDoListAdapter(tdd, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent), getResources().getColor(R.color.divider));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                updateCardView();
            }
        });


        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.popup);
                dialog.show();

                mail = (ImageView) dialog.findViewById(email);
                sms = (ImageView) dialog.findViewById(R.id.sms);
                mDisplayDate = (TextView) dialog.findViewById(R.id.tvDate);

                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
               // CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);


                TextView tvstatus = (TextView) dialog.findViewById(R.id.status);
              //  cb.setVisibility(View.GONE);
                tvstatus.setVisibility(View.GONE);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });



                mDisplayDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                MainActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.show();
                    }
                });


                mDateSetListener = new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        android.util.Log.d(TAG, "onDateSet: yyyy/mm/dd: " + year  + "/" + month  + "/" + day);

                        String date =  year + "-" + month  + "-" + day;
                        // tarh.set(year,month,day);

                        mDisplayDate.setText(date);

                    }
                };





                sms.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {

                        EditText todoNotes = (EditText) dialog.findViewById(R.id.input_task_notes);

                        android.net.Uri uri = android.net.Uri.parse("smsto:05071158040");
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", todoNotes.getText().toString());
                        startActivity(it);

                    }
                });




                mail.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View v) {

                        EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                        EditText todoNotes = (EditText) dialog.findViewById(R.id.input_task_notes);


                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"fatihdurukan42@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, todoText.getText().toString());
                        i.putExtra(Intent.EXTRA_TEXT   , todoNotes.getText().toString());
                        try {
                            startActivity(Intent.createChooser(i, "Mail gönder..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(MainActivity.this, "Gonderilecek kişi yok.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });










                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                        EditText todoNotes = (EditText) dialog.findViewById(R.id.input_task_notes);
                        TextView tarih = (TextView) dialog.findViewById(R.id.tvDate);
                        Toast.makeText(getApplicationContext(), tarih.getText().toString(), Toast.LENGTH_SHORT).show();
                        // dateInMilliSeconds = tarh.getTimeInMillis();



                        if (todoText.getText().length() >= 2) {
                            RadioGroup proritySelection = (RadioGroup) dialog.findViewById(R.id.toDoRG);
                            String RadioSelection = new String();
                            if (proritySelection.getCheckedRadioButtonId() != -1) {
                                int id = proritySelection.getCheckedRadioButtonId();
                                View radiobutton = proritySelection.findViewById(id);
                                int radioId = proritySelection.indexOfChild(radiobutton);
                                RadioButton btn = (RadioButton) proritySelection.getChildAt(radioId);
                                RadioSelection = (String) btn.getText();
                            }






                            ContentValues contentValues = new ContentValues();

                            contentValues.put("ToDoTaskDetails", todoText.getText().toString());
                            contentValues.put("ToDoTaskPrority", RadioSelection);
                            contentValues.put("ToDoTaskStatus", "Tamamlanmadı");
                            contentValues.put("ToDoNotes", todoNotes.getText().toString());


                            // contentValues.put("Date", System.currentTimeMillis());

                            mysqlite = new SqliteHelper(getApplicationContext());
                            Boolean b = mysqlite.insertInto(contentValues);
                            if (b) {
                                dialog.hide();
                                updateCardView();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ooops bişeyler yanlış gitti!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Boşlukları doldurunuz.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });
    }


    public void updateCardView() {
        swipeRefreshLayout.setRefreshing(true);
        mysqlite = new SqliteHelper(getApplicationContext());
        Cursor result = mysqlite.selectAllData();
        if (result.getCount() == 0) {
            tdd.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Liste boş.", Toast.LENGTH_SHORT).show();
        } else {
            tdd.clear();
            adapter.notifyDataSetChanged();
            while (result.moveToNext()) {
                ToDoData tddObj = new ToDoData();
                tddObj.setToDoID(result.getInt(0));
                tddObj.setToDoTaskDetails(result.getString(1));
                tddObj.setToDoTaskPrority(result.getString(2));
                tddObj.setToDoTaskStatus(result.getString(3));
                tddObj.setToDoNotes(result.getString(4));
                //tddObj.setDate(result.getInt(5));
               // tddObj.setRakam(5);
                tdd.add(tddObj);
            }
            adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }




    @Override
    public void onRefresh() {
        updateCardView();
    }



}




