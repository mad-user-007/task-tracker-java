package com.dilawar.tasktracker.json;

import com.dilawar.tasktracker.task.Task;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Json {

  private ArrayList<Task> tasks = new ArrayList<>();
  private Path filePath;
  private String data;
  private String fileName;
  private int newId;

  // Handle File
  public Json(){
    this.fileName = "data.json";

    this.filePath = Path.of(fileName);

    if(Files.exists(filePath)){
      try {
        String content = Files.readString(filePath);

        if(content.length() == 0){
          System.out.println("Warning: Empty data file");
          Files.writeString(filePath, "[]");
          this.data = "[]";
        } else {
          this.data = content;
        }
      } catch (IOException e) {
        System.out.println("Error: Unable to read file");
        System.exit(1);
      }
    } else {
      System.out.println("File does not exist. Creating...");
      try {
        Files.createFile(filePath);
        Files.writeString(filePath, "[]");
        this.data = "[]";
      } catch (IOException e) {
        System.out.println("Error: Unable to create file");
        System.exit(1);
      }
    }
  }

  // Convert all tasks to string and write
  private String tasksToStr(){
    String tasksStr = "[";
    int noOfTasks = tasks.size(), i = 1;

    if(noOfTasks == 1){
      tasksStr += tasks.getFirst().toString() + "]";
    } else {
      for(Task t: tasks){
        if(i == noOfTasks){
          tasksStr += t.toString() + "]";
        } else {
          tasksStr += t.toString() + ",";
        }
        i++;
      }
    }

    return tasksStr;
  }

  // Parse File: Convert into ArrayList
  public boolean parseFile(){

    Pattern idPat = Pattern.compile("\s*\"id\"\s*:\s*(?<id>\\d+)");
    Pattern descPat = Pattern.compile("\s*\"description\"\s*:\s*\"(?<description>[^\"]+)\"");
    Pattern statusPat = Pattern.compile("\s*\"status\"\s*:\s*\"(?<status>todo|in-progress|done)\"");
    Pattern createdAtPat = Pattern.compile("\s*\"createdAt\"\s*:\s*\"(?<createdAt>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?)\"");
    Pattern updatedAtPat = Pattern.compile("\s*\"updatedAt\"\s*:\s*\"(?<updatedAt>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,9})?)\"");

    Matcher idMat = idPat.matcher(this.data);
    Matcher descMat = descPat.matcher(this.data);
    Matcher statusMat = statusPat.matcher(this.data);
    Matcher createdAtMat = createdAtPat.matcher(this.data);
    Matcher updatedAtMat = updatedAtPat.matcher(this.data);

    while(true){
      int id;
      String desc, status, createdAt, updatedAt;

      // Catch ID
      if(idMat.find()){
        id = Integer.parseInt(idMat.group("id"));
      } else {
        break;
      }

      // Catch desc
      if(descMat.find()){
        desc = descMat.group("description");
      } else {
        System.out.println("Inconsistent data.");
        return false;
      }

      // Catch status
      if(statusMat.find()){
        status = statusMat.group("status");
      } else {
        System.out.println("Inconsistent data.");
        return false;
      }
      
      // Catch createdAt
      if(createdAtMat.find()){
        createdAt = createdAtMat.group("createdAt");
      } else {
        System.out.println("Inconsistent data.");
        return false;
      }
      
      // Catch updatedAt
      if(updatedAtMat.find()){
        updatedAt = updatedAtMat.group("updatedAt");
      } else {
        System.out.println("Inconsistent data.");
        return false;
      }

      newId = id + 1;
      tasks.add(new Task(id, desc, status, createdAt, updatedAt));
    }

    return true;
  }

  // Add Task
  // 1. Parse
  // 2. Add
  public boolean addTask(String description){

    if(!parseFile()){
      System.out.println("Parsing file failed.");
      return false;
    }

    // Get New Id
    int id = this.newId;

    String time = LocalDateTime.now().toString();

    Task t = new Task(id, description, "todo", time, time);

    // Add at end
    if(tasks.isEmpty()){
      try {
        Files.writeString(filePath, "[" + t.toString() + "]");
        return true;
      } catch (IOException e) {
        return false;
      }
    } else {
      try(RandomAccessFile filePointer = new RandomAccessFile(filePath.toFile(), "rw")) {
        long size = filePointer.length();
        filePointer.seek(size - 1);
        filePointer.writeBytes("," + t.toString() + "]");
        return true;

      } catch (FileNotFoundException e) {
        System.out.println("File does not exist.");
        return false;
      } catch(IOException e){
        return false;
      }
    }
  }

  // Update Task
  // 1. Parse
  // 2. Update
  // 3. Write
  public boolean updateTask(int id, String newDesc){
    // Parse
    if(!parseFile()){
      System.out.println("Parsing file failed.");
      return false;
    }

    // Search Task
    Task foundTask = tasks.stream().filter(task -> task.getId() == id).findFirst().orElse(null);

    if(foundTask == null){
      System.out.println("Task with ID " + id + " not found!");
      return false;
    }

    foundTask.updateDescription(newDesc);

    // Convert all tasks to string
    String tasksStr = tasksToStr();

    // Write to File
    try {
      Files.writeString(filePath, tasksStr);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  // Delete task
  public boolean removeTask(int id){

    // Parse
    if(!parseFile()){
      System.out.println("Parsing file failed.");
      return false;
    }

    boolean removed = tasks.removeIf(t -> t.getId() == id);
    if(!removed){
      System.out.println("Task with ID " + id + " not found!");
      return false;
    }
    
    String tasksStr = tasksToStr();

    // Write to String
    try {
      Files.writeString(filePath, tasksStr);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  // Mark a task progress
  // Same as update, just update status
  public boolean updateStatus(int id, String newStatus){
    // Parse
    if(!parseFile()){
      System.out.println("Parsing file failed.");
      return false;
    }

    // Update
    Task foundTask = tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    if(foundTask == null){
      System.out.println("Task with ID " + id + " not found!");
      return false;
    }

    if(!foundTask.updateStatus(newStatus)){
      System.out.println("Invalid status.");
      return false;
    }

    String tasksStr = tasksToStr();

    try {
      Files.writeString(filePath, tasksStr);
      return true;
    } catch (IOException e) {
      return false;
    }
  }
  

  // Display Tasks
  // -- Filter
  //    0: All
  //    1: Done
  //    2: Todo
  //    3: In-Progress
  public void listTasks(int filter){

    if(!parseFile()){
      System.out.println("Parsing file failed.");
      return;
    }

    switch(filter){
      case 1 -> tasks.removeIf(t -> !t.getStatus().equals("done"));
      case 2 -> tasks.removeIf(t -> !t.getStatus().equals("todo"));
      case 3 -> tasks.removeIf(t -> !t.getStatus().equals("in-progress"));
    }

    if(tasks.isEmpty()){
      System.out.println("No tasks with the given status.");
      return;
    }

    // Line Width, and print header
    int maxWidth = 40;
    System.out.printf("%n%-10s %-" + maxWidth + "s %-14s %-25s %-25s %n", "ID", "Description", "Status", "Created At", "Updated At");
    StringBuilder separator = new StringBuilder();
    for(int i = 0; i<maxWidth + 74; i++){
      separator.append("-");
    }

    System.out.println(separator.toString());

    tasks.forEach(t -> {
      t.display(maxWidth);
    });
  }
}
