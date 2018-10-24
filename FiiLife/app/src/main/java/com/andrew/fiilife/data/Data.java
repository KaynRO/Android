package com.andrew.fiilife.data;

/**
 * Created by Grigoras on 01.04.2017.
 */

public class Data {

        public static class Student{
            public int ID;
            public String name;
            public String email;
            public String password;
            public String token;
        }

        public static class Course {

            public int ID;
            public String time;
            public String name;
            public String day;
        }

        public static class Comment {
            public int ID;
            public int postID;
            public int userID;
            public String content;
        }

        public static class Post {
            public int ID;
            public int courseID;
            public String courseName;
            public int userID ;
            public int type ;
            public String userName ;
            public String content;
            public String time;
        }

    public static class TableItem{
        public int ID;
        public int day;
        public String name;
        public String description;
    }




    }
