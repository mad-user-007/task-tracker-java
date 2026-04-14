package com.dilawar.tasktracker.main;

import com.dilawar.tasktracker.task.Tasks;

public class Main {
  public static void main(String[] args) {
    int args_len = args.length;
    int id;

    if (args_len == 0){
      System.out.println("Missing paramteres...");
      System.out.println("1. add 'description'");
      System.out.println("2. update 'id' 'new description'");
      System.out.println("3. delete 'id'");
      System.out.println("4. mark-in-progress 'id'");
      System.out.println("5. mark-done 'id'");
      System.out.println("6. list");
      System.out.println("7. list done");
      System.out.println("8. list todo");
      System.out.println("9. list in-progress");
      
      return;
    }

    switch(args[0]){
      case "add" -> {
        if (args_len != 2){
          System.out.println("Missing/Invalid parameters, requires 'description'");
          System.out.println("Command: add 'description'");
          return;
        }

        String description = args[1];
        Tasks.add_task(description);    
      }

      case "list" -> {
        switch (args_len) {
            case 1 -> Tasks.display_tasks("all");
            case 2 -> Tasks.display_tasks(args[1]);
            default -> {
                System.out.println("Invalid paramters for list!");
                System.out.println("1. list");
                System.out.println("2. list done");
                System.out.println("3. list todo");
                System.out.println("4. list in-progress");
            }
        }
      }

      case "update" -> {
        if(args_len != 3){
          System.out.println("Invalid paramters for update. Requires 'id', 'new description'");
          System.out.println("E.g., update 3 \"New description\"");
          return;
        }

        id = Integer.parseInt(args[1]);
        String newDesc = args[2];

        Tasks.update_task(id, newDesc);
      }

      case "delete" -> {
        if(args_len != 2){
          System.out.println("Invalid parameters for delete. Requires 'id'");
          System.out.println("eg., delete 2");
          return;
        }

        id = Integer.parseInt(args[1]);
        Tasks.delete_task(id);
      }

      case "mark-in-progress" -> {
        if(args_len != 2){
          System.out.println("Invalid parameters for mark-in-progress.");
          System.out.println("Command: mark-in-progress 'id'");
          return;
        }

        id = Integer.parseInt(args[1]);
        Tasks.mark_in_progress(id);
      }

      case "mark-done" -> {
        if(args_len != 2){
          System.out.println("Invalid parameters for mark-in-progress.");
          System.out.println("Command: mark-done 'id'");
          return;
        }

        id = Integer.parseInt(args[1]);
        Tasks.mark_done(id);
      }
      
      default -> {
        System.out.println("Invalid command!");
        System.out.println("Commands: add, update, delete, list, mark-in-progress, mark-done");
      }
    }
  }
}