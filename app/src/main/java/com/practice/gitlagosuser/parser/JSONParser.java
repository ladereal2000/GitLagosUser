package com.practice.gitlagosuser.parser;

import com.practice.gitlagosuser.utils.Keys;
import com.practice.gitlagosuser.models.GitUsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by David on 8/11/2017.
 */

public class JSONParser {

    /**
     *
     * @param JSONString
     * @return
     */
    public static ArrayList<GitUsers> getGitUsersFromJSON(String JSONString){
        ArrayList<GitUsers> list = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(JSONString);
            JSONArray jsonArray = rootObject
                    .getJSONArray(Keys.UserKeys.KEY_USER_ROOT_ARRAY);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt(Keys.UserKeys.KEY_ID);
                String username = jsonObject.getString(Keys.UserKeys.KEY_LOGIN);
                String score = jsonObject.getString(Keys.UserKeys.KEY_SCORE);
                String avata_url = jsonObject.getString(Keys.UserKeys.KEY_AVATAR_URL);
                String html_url = jsonObject.getString(Keys.UserKeys.KEY_HTML_URL);
                String profile_url = jsonObject.getString(Keys.UserKeys.KEY_URL);

                GitUsers gitUsers = new GitUsers(id, username, score, avata_url, html_url, profile_url);
                list.add(gitUsers);
            }
            //return list
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
