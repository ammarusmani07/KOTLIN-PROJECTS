package com.example.apiwork;




import com.google.gson.annotations.SerializedName;

public class Model {
        @SerializedName("userId")
        private int userId;

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("body")
        private String body;

        // Constructor
        public Model(int userId, int id, String title, String body) {
                this.userId = userId;
                this.id = id;
                this.title = title;
                this.body = body;
        }

        // Getter methods
        public int getUserId() {
                return userId;
        }

        public int getId() {
                return id;
        }

        public String getTitle() {
                return title;
        }

        public String getBody() {
                return body;
        }
}