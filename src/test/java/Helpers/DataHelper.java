package Helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Helpers.UserGenerator.fakerRU;

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

    public static EditingQuestionInfo getEditInfo(String question, String answer, String id) {
        return new EditingQuestionInfo("", "version", id,
                new LTP(new LTPdata("", "", List.of(), List.of(), "", List.of(),
                        question, List.of(), "", answer, List.of(), List.of(), "", id)),
                new VersionDetails(0, 0, 1, "1.0.0"));
    }

    @AllArgsConstructor
    public static class EditingQuestionInfo {
        public String currentLTS;
        public String changeKey;
        public String question;
        @JsonProperty("LTP")
        public LTP ltp;
        public VersionDetails versionDetails;
    }

    @AllArgsConstructor
    public static class LTP {
        public LTPdata data;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LTPdata {
        public String jsDetails;
        public String comment;
        public List<Object> quizes;
        public List<Object> hints;
        public String type;
        public List<Object> videos;
        public String name;
        public List<Object> hashTags;
        public String title;
        public String answer;
        public List<Object> facts;
        public List<Object> useCases;
        public String originalDuplicateId;
        public String questionId;
    }

    @AllArgsConstructor
    public static class VersionDetails {
        public int patch;
        public int subVersion;
        public int version;
        public String versionStr;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Question {
        public int _id;
        public ArrayList<Object> quizes;
        @JsonIgnore
        public ArrayList<Object> __names;
        public String name;
        public ArrayList<Owner> contributors;
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
        public int __v;
        public int quizCount;
        public Object jsDetails;
        public String comment;
        public String type;
        public String title;
        public String answer;
        public Object originalDuplicateId;
        public Owner owner;
        public int contributorsScore;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Owner{
        public int grade;
        public int user;
        public String name;
        public long cd;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EditingQuestion {
        public questionDB data;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class questionDB {
        public Question questionDB;
        public VersionDB versionDB;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VersionDB{
        public String source;
        public String sourceId;
        public ArrayList<Object> suggests;
        public ArrayList<Object> approvers;
        public ArrayList<Object> decliners;
        public ArrayList<Owner> contributors;
        public ArrayList<Object> prevApprovers;
        public Date cd;
        public int _id;
        public int approveScore;
        public int version;
        public int subVersion;
        public int patch;
        public String versionStr;
        public int versionSort;
        public String name;
        public String dataStr;
        public Object letters;
        public String versionStrUniq;
        public int __v;
        public LTPdata data;
        public boolean isLTS;
        public Owner owner;
        public Owner firstOwner;
        public int contributorsScore;
    }

    @AllArgsConstructor
    public static class QuizInfo {
        public String answerType;
        public boolean isValid;
        public String name;
        public List<Object> files;
        public List<Variation> variations;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Variation {
        public String name;
        public boolean isCorrect;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddingQuiz {
        public Quiz data;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Quiz {
        public int _id;
        public ArrayList<Object> correctAnswers;
        public ArrayList<Object> files;
        public ArrayList<Object> names;
        public ArrayList<Object> audioNames;
        public String answerType;
        public boolean isValid;
        public String name;
        public ArrayList<Variation> variations;
        public ArrayList<Object> correctCodeVariations;
        public int user;
        public Date cd;
        public boolean isAudio;
        public boolean isCode;
    }

    public static QuizInfo getQuizInfo() {
        return new QuizInfo("quiz", true, fakerRU.internet().emailSubject() + "?", List.of(),
                List.of(
                        new Variation(fakerRU.internet().emailSubject(), true),
                        new Variation(fakerRU.internet().emailSubject(), false),
                        new Variation(fakerRU.internet().emailSubject(), false)));
    }
}
