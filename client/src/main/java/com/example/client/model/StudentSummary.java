package com.example.client.model;

public class StudentSummary {

    private Integer studentId;
    private int courseCount;
    private double average;
    private String rank;

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public int getCourseCount() { return courseCount; }
    public void setCourseCount(int courseCount) { this.courseCount = courseCount; }

    public double getAverage() { return average; }
    public void setAverage(double average) { this.average = average; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
}
