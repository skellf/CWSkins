package me.skellf.cwskins.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class VersionChecker {

    public static class ReleaseInfo {
        private final String version;
        private final String downloadUrl;

        public ReleaseInfo(String version, String downloadUrl) {
            this.version = version;
            this.downloadUrl = downloadUrl;
        }

        public String getVersion() {
            return version;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }

    public static ReleaseInfo getLatestVersion(String user, String repo) throws Exception {
        String url = "https://api.github.com/repos/" + user + "/" + repo + "/releases/latest";
        JSONObject json = getJsonObject(url);
        String version = json.getString("tag_name");

        JSONArray assets = json.getJSONArray("assets");
        String downloadUrl = null;
        if (!assets.isEmpty()) {
            downloadUrl = assets.getJSONObject(0).getString("browser_download_url");
        }

        return new ReleaseInfo(version, downloadUrl);
    }

    @NotNull
    private static JSONObject getJsonObject(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();

        JSONObject json = new JSONObject(sb.toString());
        return json;
    }
}
