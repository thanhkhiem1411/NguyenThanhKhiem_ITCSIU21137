package com.student.model;

public class Student {
    private int id;
    private String studentCode;
    private String fullName;
    private String email;
    private String major;
    private String photo; 

    public Student() {
    }

    public Student(int id, String studentCode, String fullName, String email, String major, String photo) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.major = major;
        this.photo = photo;
    }

    public Student(String studentCode, String fullName, String email, String major, String photo) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.major = major;
        this.photo = photo;
    }

    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", major='" + major + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}




  