package Helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Helpers.UserGenerator.faker;
import static Helpers.UserGenerator.fakerRU;

public class DataHelper {

    @Value
    public static class AuthInfo {
        public String username;
        public String password;
    }

    @Value
    public static class AddingInfo implements Info {
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

    @AllArgsConstructor
    public static class QuestionInfo implements Info {
        public String name;
    }

    public static EditingQuestionInfo getEditInfo(int id) {
        return new EditingQuestionInfo("", "version", Integer.toString(id),
                new LTP(new LTPdata("", "", List.of(), List.of(), "", List.of(),
                        fakerRU.internet().emailSubject() + "?", List.of(), "",
                        fakerRU.internet().emailSubject(), List.of(), List.of(), "", Integer.toString(id))),
                new VersionDetails(0, 0, 1, "1.0.0"));
    }

    @AllArgsConstructor
    public static class EditingQuestionInfo implements Info {
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

    @AllArgsConstructor
    public static class QuizInfo implements Info {
        public String answerType;
        public boolean isValid;
        public String name;
        public List<Object> files;
        public List<Variation> variations;
    }

    public static class UpdateQuizInfo extends QuizInfo implements Info {
        public int _id;

        public UpdateQuizInfo(int id, String answerType, boolean isValid, String name,
                              List<Object> files, List<Variation> variations) {
            super(answerType, isValid, name, files, variations);
            this._id = id;
        }
    }
    public static UpdateQuizInfo getUpdateQuizInfo(int id) {
        return new UpdateQuizInfo(id, "quiz", true, fakerRU.word().noun() + "?", List.of(),
                List.of(
                        new Variation(fakerRU.internet().emailSubject(), true),
                        new Variation(fakerRU.internet().emailSubject(), false),
                        new Variation(fakerRU.internet().emailSubject(), false)));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Variation {
        public String name;
        public boolean isCorrect;
    }

    public static QuizInfo getQuizInfo() {
        return new QuizInfo("quiz", true, fakerRU.internet().emailSubject() + "?", List.of(),
                List.of(
                        new Variation(fakerRU.internet().emailSubject(), true),
                        new Variation(fakerRU.internet().emailSubject(), false),
                        new Variation(fakerRU.internet().emailSubject(), false)));
    }
    @AllArgsConstructor
    public static class ModuleInfo implements Info{
        public String name;
        public List<Integer> questions;
    }

    public static ModuleInfo getModuleInfo() {
        return new ModuleInfo(fakerRU.science().element(),
                List.of(faker.random().nextInt(),
                        faker.random().nextInt(),
                        faker.random().nextInt()));
    }

    public static class UpdateModuleInfo extends ModuleInfo implements Info {
        public int _id;

        public UpdateModuleInfo(int id, String name, List<Integer> questions) {
            super(name, questions);
            this._id = id;
        }
    }
    public static UpdateModuleInfo getUpdateModuleInfo(int id) {
        return new UpdateModuleInfo(id, fakerRU.science().element(),
                List.of(faker.random().nextInt(),
                        faker.random().nextInt(),
                        faker.random().nextInt()));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CourseInfo implements Info {
        public String name;
        public List<Modules> modules;
    }
    @AllArgsConstructor
    public static class Modules {
        public String module;
        public String name;
    }

    public static CourseInfo getCourseInfo() {
        return new CourseInfo(fakerRU.word().noun(),
                List.of(new Modules(fakerRU.word().noun(), fakerRU.word().noun())));
    }

    public static class UpdateCourseInfo extends CourseInfo implements Info {
        public int _id;

        public UpdateCourseInfo(String name, List<Modules> modules, int id) {
            super(name, modules);
            this._id = id;
        }
    }
    public static UpdateCourseInfo getUpdateCourseInfo(int id) {
        return new UpdateCourseInfo(fakerRU.word().noun(),
                List.of(new Modules(fakerRU.word().noun(), fakerRU.word().noun())), id);
    }

    @AllArgsConstructor
    public static class ExamInfo implements Info {
        public String name;
        public String minutesStr;
        public List<Object> potQuizes;
    }

    public static ExamInfo getExamInfo() {
        return new ExamInfo(fakerRU.word().noun(), Integer.toString(faker.random().nextInt()), List.of());
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class TemplateInfo implements Info {
        public String name;
        public String desc;
        public List<Source> exams;
        public List<Source> courses;
    }

    @NoArgsConstructor
    public static class Source {
        public String sourceId;
    }

    public static class UpdateExamInfo extends ExamInfo implements Info {
        public int _id;

        public UpdateExamInfo(String name, String minutesStr, List<Object> potQuizes, int id) {
            super(name, minutesStr, potQuizes);
            this._id = id;
        }
    }
    public static UpdateExamInfo getUpdateExamInfo(int id) {
        return new UpdateExamInfo(fakerRU.word().noun(), Integer.toString(faker.random().nextInt()), List.of(), id);
    }
    public static TemplateInfo getTemplateInfo() {
        return new TemplateInfo(fakerRU.word().noun(), fakerRU.word().noun(),
                List.of(), List.of());
    }
}
