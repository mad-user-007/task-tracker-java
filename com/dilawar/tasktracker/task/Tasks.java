package com.dilawar.tasktracker.task;

import com.dilawar.tasktracker.json.Json;

public class Tasks {

  static Json file;

  static {
    file = new Json();
  }

  // Add Task
  public static void add_task(String description){
    Boolean result = file.addTask(description);
    if(result){
      System.out.println("Task added!");
    } else {
      System.out.println("Failed to add task!");
    }
  }

  // Update Task
  public static void update_task(int id, String newDesc){
    if(file.updateTask(id, newDesc)){
      System.out.println("Task updated!");
    } else {
      System.out.println("Failed to update task!");
    }
  }

  // Delete task
  public static void delete_task(int id){
    if(file.removeTask(id)){
      System.out.println("Task removed!");
    } else {
      System.out.println("Failed to remove task!");
    }
  }

  // Mark In Progress
  public static void mark_in_progress(int id){
    if(file.updateStatus(id, "in-progress")){
      System.out.println("Task updated!");
    } else {
      System.out.println("Failed to update task!");
    }
  }

  // Mark Done
  public static void mark_done(int id){
    if(file.updateStatus(id, "done")){
      System.out.println("Task updated!");
    } else {
      System.out.println("Failed to update task!");
    }
  }

  // Display tasks
  public static void display_tasks(String filter){
    int filterInt;

    switch(filter){
      case "all":
        filterInt = 0;
        break;
      case "done":
        filterInt = 1;
        break;
      case "todo":
        filterInt = 2;
        break;
      case "in-progress":
        filterInt = 3;
        break;
      default:
        System.out.println("Invalid argument for list. Showing all tasks!");
        filterInt = 0;
        break;
    }

    file.listTasks(filterInt);

  }
}
