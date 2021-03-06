package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Респонс от сервера - данные пользователя
 */
public class UserModelRes {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * геттер для данных о пользователе
     * @return
     */
    public Data getData() {
        return data;
    }

    /**
     * субкласс контактные данные ВК, телефон, почта
     */
    public class Contacts {

        @SerializedName("vk")
        @Expose
        private String vk;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("updated")
        @Expose
        private String updated;

        public String getVk() {
            return vk;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

    }

    /**
     * субкласс данные пользователя - пользователь + токен
     */
    public class Data {

        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("token")
        @Expose
        private String token;

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }

    /**
     * субкласс - Значения рейтинговых полей -
     * домашних заданий,
     * проектов,
     * строк кода,
     * рейтинг
     *
     */
    public class ProfileValues {

        @SerializedName("homeTask")
        @Expose
        private int homeTask;
        @SerializedName("projects")
        @Expose
        private int projects;
        @SerializedName("linesCode")
        @Expose
        private int linesCode;
        @SerializedName("rait")
        @Expose
        private int rait;
        @SerializedName("updated")
        @Expose
        private String updated;

        public int getProjects() {
            return projects;
        }

        public int getLinesCode() {
            return linesCode;
        }

        public int getRaiting() {
            return rait;
        }
    }

    /**
     * субкласс - публичная информация
     * О пользователе
     * аватарка
     * фото
     */
    public class PublicInfo {

        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("updated")
        @Expose
        private String updated;

        public String getBio() {
            return bio;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getPhoto() {
            return photo;
        }
    }

    /**
     * субклас - репозиторий на гитхабе
     * ид,
     * путь к гиту,
     * название проекта
     */
    public class Repo {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("git")
        @Expose
        private String git;
        @SerializedName("title")
        @Expose
        private String title;

        public String getGit() {
            return git;
        }

        public String getTitle() {
            return title;
        }

        public String getId() {
            return id;
        }
    }

    /**
     * Субкласс - список репозиториев гита пользователя
     */
    public class Repositories {

        @SerializedName("repo")
        @Expose
        private List<Repo> repo = new ArrayList<Repo>();
        @SerializedName("updated")
        @Expose
        private String updated;

        public List<Repo> getRepo() {
            return repo;
        }
    }

    /**
     * Информация о пользователе
     * Имя
     * Фамилия
     * список репозиториев
     * контакты
     * значения профиля
     * публичная информация
     * специализация
     * роль
     */
    public class User {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("second_name")
        @Expose
        private String secondName;
        @SerializedName("__v")
        @Expose
        private int v;
        @SerializedName("repositories")
        @Expose
        private Repositories repositories;
        @SerializedName("contacts")
        @Expose
        private Contacts contacts;
        @SerializedName("profileValues")
        @Expose
        private ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        private PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("updated")
        @Expose
        private String updated;

        public String getId() {
            return id;
        }

        public ProfileValues getProfileValues() {
            return profileValues;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSecondName() {
            return secondName;
        }

        public Repositories getRepositories() {
            return repositories;
        }

        public Contacts getContacts() {
            return contacts;
        }

        public PublicInfo getPublicInfo() {
            return publicInfo;
        }
    }


}
