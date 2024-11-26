package Helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class DataHelper {

    @Value
    public static class AuthInfo {
        public String username;
        public String password;
    }

    @Value
    public static class AddingInfo {
        public CustomData customData;
        public String first_name;
        public String surname;
        public String email;
        public String username;
        public String plain_password;
        public String roles;
    }


    public static class LoginUser {
        public String token;
        @JsonIgnore
        public String refresh_token;
        public User user;
    }

    public static class User {
        public int _id;
        @JsonIgnore
        public ArrayList teams;
        public String surname;
        public String first_name;
        public String username;
        public ArrayList<String> roles;
        @JsonIgnore
        public ArrayList<Object> positions;
        @JsonIgnore
        public String plain_password;
        @JsonIgnore
        public ArrayList<Object> cities;
        @JsonIgnore
        public ArrayList<Object> companies;
        @JsonIgnore
        public ArrayList<Object> work_history;
        @JsonIgnore
        public ArrayList<Object> edu_history;
        @JsonIgnore
        public String cd;
        @JsonIgnore
        public String password;
        public String name;
        public String email;
        @JsonIgnore
        public int __v;
        public CustomData customData;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomData {
        public boolean isCV;
        public String salesOpenTime; //DateTime
        public String salesStatus; //enum
    }

    public static class AddingUser {
        public User data;
    }

    @AllArgsConstructor
    public static class QuestionInfo {
        public String name;
    }

    public static class AddingQuestion {
        public Question data;
    }


    public static class Question {
        public int _id;
        public ArrayList<Object> quizes;
        @JsonIgnore
        public ArrayList<Object> __names;
        public String name;
        public ArrayList<Object> contributors;
        public ArrayList<Object> decliners;
        public ArrayList<Object> hints;
        public ArrayList<Object> additionalQuestionsArr;
        public ArrayList<Object> useCases;
        public ArrayList<Object> videos;
        public ArrayList<Object> facts;
        public ArrayList<Object> interviews;
        public ArrayList<Object> hashTags;
        public ArrayList<Object> shortAnswers;
        public ArrayList<Object> detailedAnswers;
        public Date cd;
        public boolean isDuplicated;
        public int useCasesLength;
        public int factsLength;
        public int answerProgressCount;
    }
}
