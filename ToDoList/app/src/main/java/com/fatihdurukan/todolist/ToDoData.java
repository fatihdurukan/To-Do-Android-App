package com.fatihdurukan.todolist;

/**
 * Created by fatihdurukan on 20.12.2017.
 */

public class ToDoData {
    int ToDoID;
    String ToDoTaskDetails;
    String ToDoTaskPrority;
    String ToDoTaskStatus;
    String ToDoNotes;

    public int getRakam() {
        return Rakam;
    }

    public void setRakam(int rakam) {
        Rakam = rakam;
    }

    int Rakam;


    public int getToDoID() {
        return ToDoID;
    }

    public void setToDoID(int toDoID) {
        ToDoID = toDoID;
    }

    public String getToDoTaskDetails() {
        return ToDoTaskDetails;
    }

    public void setToDoTaskDetails(String toDoTaskDetails) {
        ToDoTaskDetails = toDoTaskDetails;
    }

    public String getToDoTaskPrority() {
        return ToDoTaskPrority;
    }

    public void setToDoTaskPrority(String toDoTaskPrority) {
        ToDoTaskPrority = toDoTaskPrority;
    }

    public String getToDoTaskStatus() {
        return ToDoTaskStatus;
    }

    public void setToDoTaskStatus(String toDoTaskStatus) {
        ToDoTaskStatus = toDoTaskStatus;
    }

    public String getToDoNotes() {
        return ToDoNotes;
    }

    public void setToDoNotes(String toDoNotes) {
        ToDoNotes = toDoNotes;
    }

  //  @Override
   // public String toString() {
    //    return "ToDoData {id-" + ToDoID + ", taskDetails-" + ToDoTaskDetails + ", propity-" + ToDoTaskPrority + ", status-" + ToDoTaskStatus + ", notes-" + ToDoNotes + "}";
    //}

}
