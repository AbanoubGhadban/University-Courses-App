package com.golden.universitycourses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements JsonListFragment.OnJsonListItemSelected {
    private static final int PERMISSION_REQUEST = 3;
    private static final String FILE_PATH = "/storage/emulated/0/Download/txt.txt";
    private static final int PROGRAMS_LIST_ID = 1;
    private static final int COURSES_LIST_ID = 2;

    private JSONObject facultyJson;
    private TextView tvFaculty;
    private int selectedProgram;
    private int selectedCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFaculty = findViewById(R.id.tv_faculty);
        if (askForPermission())
            initActivity();
    }

    private boolean askForPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            return true;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            return true;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST);
        return false;
    }

    private void initActivity() {
        if (loadData()) {
            displayFacultyInfo();
            displayPrograms();
        }
    }

    private boolean loadData() {
        File file = new File(FILE_PATH);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Toast.makeText(this, "Couldn't open the file. " + e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            String s = text.toString();
            facultyJson = new JSONObject(text.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Invalid File Format", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    void displayFacultyInfo() {
        try {
            String title = "Faculty of " + facultyJson.getString("faculty") + " " +
                    facultyJson.getString("university") + " University";
            tvFaculty.setText(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void displayPrograms() {
        try {
            JSONArray programs = facultyJson.getJSONArray("programs");
            JsonListFragment programsFragment = JsonListFragment.newInstance("Programs", programs, PROGRAMS_LIST_ID);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, programsFragment)
                    .commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void displayCourses() {
        try {
            JSONArray programs = facultyJson.getJSONArray("programs");
            JSONObject program = programs.getJSONObject(selectedProgram);
            JSONArray courses = program.getJSONArray("courses");

            String title = program.getString("name") + " Courses";
            JsonListFragment coursesFragment = JsonListFragment.newInstance(title, courses, COURSES_LIST_ID);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, coursesFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void displayCourseDescription() {
        try {
            JSONArray programs = facultyJson.getJSONArray("programs");
            JSONObject program = programs.getJSONObject(selectedProgram);
            JSONArray courses = program.getJSONArray("courses");
            JSONObject course = courses.getJSONObject(selectedCourse);

            String title = course.getString("name") + " Course Description";
            DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(title, course.getString("description"));

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, descriptionFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initActivity();
            } else {
                Toast.makeText(this, "Couldn't open the file, as read access not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(int index, int listId) {
        if (listId == PROGRAMS_LIST_ID) {
            selectedProgram = index;
            displayCourses();
        } else {
            selectedCourse = index;
            displayCourseDescription();
        }
    }
}
