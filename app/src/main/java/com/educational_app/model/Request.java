package com.educational_app.model;

import java.util.List;

public class Request {
 private  String price;
 private String phone;
 private List<Order> courses;

 public Request() {
 }

 public Request(String price, String phone, List<Order> courses) {
  this.price = price;
  this.phone = phone;
  this.courses = courses;
 }

 public String getPrice() {
  return price;
 }

 public void setPrice(String price) {
  this.price = price;
 }

 public String getPhone() {
  return phone;
 }

 public void setPhone(String phone) {
  this.phone = phone;
 }

 public List<Order> getCourses() {
  return courses;
 }

 public void setCourses(List<Order> courses) {
  this.courses = courses;
 }
}
