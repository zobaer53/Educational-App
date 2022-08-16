package com.educational_app.model;

public class CourseListModel {
    private String courseID;
    private  String courseName;
    private  String courseDetails;

    public CourseListModel(String courseID, String courseName, String courseDetails) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseDetails = courseDetails;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(String courseDetails) {
        this.courseDetails = courseDetails;
    }
}
