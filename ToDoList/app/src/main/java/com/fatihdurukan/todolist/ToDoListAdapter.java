package com.fatihdurukan.todolist;

/**
 * Created by fatihdurukan on 20.12.2017.
 */


import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fatihdurukan.todolist.R;
import com.fatihdurukan.todolist.ToDoData;
import com.fatihdurukan.todolist.SqliteHelper;


import java.util.ArrayList;
import java.util.List;

import static com.fatihdurukan.todolist.R.id.email;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder> {
    List<ToDoData> ToDoDataArrayList = new ArrayList<ToDoData>();
    Context context;
    private ImageView mail,sms;

    public ToDoListAdapter(String details) {
        ToDoData toDoData = new ToDoData();
        toDoData.setToDoTaskDetails(details);
        ToDoDataArrayList.add(toDoData);
    }

    public ToDoListAdapter(ArrayList<ToDoData> toDoDataArrayList, Context context) {
        this.ToDoDataArrayList = toDoDataArrayList;
        this.context = context;
    }

    @Override
    public ToDoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        ToDoListViewHolder toDoListViewHolder = new ToDoListViewHolder(view, context);
        return toDoListViewHolder;
    }

    @Override
    public void onBindViewHolder(ToDoListViewHolder holder, final int position) {


        final ToDoData td = ToDoDataArrayList.get(position);
        holder.todoDetails.setText(td.getToDoTaskDetails());
        holder.todoNotes.setText(td.getToDoNotes());
        String tdStatus = td.getToDoTaskStatus();




        String type = td.getToDoTaskPrority();
        int color = 0;
        if (type.matches("Normal")) {
            color = Color.parseColor("#FFFF00");
        } else if (type.matches("Düşük")) {
            color = Color.parseColor("#40FF00");
        } else {
            color = Color.parseColor("#FF0000");
        }
        ((GradientDrawable) holder.proprityColor.getBackground()).setColor(color);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = td.getToDoID();
                SqliteHelper mysqlite = new SqliteHelper(view.getContext());
                Cursor b = mysqlite.deleteTask(id);
                if (b.getCount() == 0) {
                    Toast.makeText(view.getContext(), "Silindi", Toast.LENGTH_SHORT).show();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } else {
                    Toast.makeText(view.getContext(), "Silinemedi!", Toast.LENGTH_SHORT).show();
                }


            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.popup);
                dialog.show();
                EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                EditText todoNote = (EditText) dialog.findViewById(R.id.input_task_notes);
                CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                RadioButton rbHigh = (RadioButton) dialog.findViewById(R.id.high);
                RadioButton rbNormal = (RadioButton) dialog.findViewById(R.id.normal);
                RadioButton rbLow = (RadioButton) dialog.findViewById(R.id.low);


                mail = (ImageView) dialog.findViewById(email);
                sms = (ImageView) dialog.findViewById(R.id.sms);
                mail.setVisibility(View.GONE);
                sms.setVisibility(View.GONE);









                if (td.getToDoTaskPrority().matches("Normal")) {
                    rbNormal.setChecked(true);
                } else if (td.getToDoTaskPrority().matches("Düşük")) {
                    rbLow.setChecked(true);
                } else {
                    rbHigh.setChecked(true);
                }
                if (td.getToDoTaskStatus().matches("Tamamlandı")) {
                    cb.setChecked(true);
                }
                todoText.setText(td.getToDoTaskDetails());
                todoNote.setText(td.getToDoNotes());
                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                        EditText todoNote = (EditText) dialog.findViewById(R.id.input_task_notes);
                       // CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
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
                            ToDoData updateTd = new ToDoData();
                            updateTd.setToDoID(td.getToDoID());
                            updateTd.setToDoTaskDetails(todoText.getText().toString());
                            updateTd.setToDoTaskPrority(RadioSelection);
                            updateTd.setToDoNotes(todoNote.getText().toString());





                            SqliteHelper mysqlite = new SqliteHelper(view.getContext());


                            Cursor b = mysqlite.updateTask(updateTd);
                            ToDoDataArrayList.set(position, updateTd);
                            if (b.getCount() == 0) {

                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {

                                        notifyDataSetChanged();
                                    }
                                });
                                dialog.hide();
                            } else {


                                dialog.hide();

                            }

                        } else {
                            Toast.makeText(view.getContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


    @Override
    public int getItemCount() {
        return ToDoDataArrayList.size();
    }

    public class ToDoListViewHolder extends RecyclerView.ViewHolder {
        TextView todoDetails, todoNotes;
        ImageButton proprityColor;
        ImageView edit, deleteButton;
        ToDoData toDoData;

        public ToDoListViewHolder(View view, final Context context) {
            super(view);
            todoDetails = (TextView) view.findViewById(R.id.toDoTextDetails);
            todoNotes = (TextView) view.findViewById(R.id.toDoTextNotes);
            proprityColor = (ImageButton) view.findViewById(R.id.typeCircle);
            edit = (ImageView) view.findViewById(R.id.edit);
            deleteButton = (ImageView) view.findViewById(R.id.delete);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}

