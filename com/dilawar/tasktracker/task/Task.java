package com.dilawar.tasktracker.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Task {
  int id;
  String description, status, createdAt, updatedAt;

  public Task(int id, String description, String status, String createdAt, String updatedAt){
    this.id = id;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public int getId(){
    return this.id;
  }

  public String getStatus(){
    return this.status;
  }

  // Update Description
  public void updateDescription(String newDesc){
    this.description = newDesc;
    this.updateTime();
  }

  // Update status
  public boolean updateStatus(String newStatus){
    Pattern statusPat = Pattern.compile("(todo|in-progress|done)");
    if(statusPat.matcher(newStatus).matches()){
      this.status = newStatus;
      this.updateTime();
      return true;
    } else {
      System.out.println("Invalid status");
      return false;
    }
  }

  // Update updatedAt
  public void updateTime(){
    this.updatedAt = LocalDateTime.now().toString();
  }

  @Override
  public String toString(){
    return "{" + "\"id\":" + id + ",\"description\":\"" + description + "\",\"status\":\"" + status + "\",\"createdAt\":\"" + createdAt + "\",\"updatedAt\":\"" + updatedAt + "\"}";
  }

  // Wrap description into lines
  private List<String> wrapToLines(String text, int width){
    String[] words = text.split(" ");
    StringBuilder line = new StringBuilder();
    List<String> lines = new ArrayList<>();

    for(String word: words){
      if(line.length() + word.length() + 1 > width){  
        lines.add(line.toString());
        line = new StringBuilder();
      } else {
        if (line.length() > 0) line.append(" ");
        line.append(word);
      }
    }

    lines.add(line.toString());
    return lines;
  }

  // Display task
  public void display(int maxWidth){
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a, d MMM yyyy");

    // String to Date
    LocalDateTime createdAtLDT = LocalDateTime.parse(createdAt);
    LocalDateTime updatedAtLDT = LocalDateTime.parse(updatedAt);

    // Get Description lines in array
    // Each item of array is displayed on each line
    List<String> lines = wrapToLines(description, maxWidth - 5);
    System.out.printf("%-10d %-" + maxWidth + "s %-14s %-25s %-25s%n", id, lines.getFirst(), status, dtf.format(createdAtLDT), dtf.format(updatedAtLDT));

    // Print remaining desc if any
    if(lines.size() > 1){
      lines.remove(0);
      lines.forEach(descLine -> {
        System.out.printf("%-10s %-" + maxWidth + "s %-14s %-25s %-25s%n", " ", descLine, " ", " ", " ");
      });

      System.out.println();
    }
  }
}
