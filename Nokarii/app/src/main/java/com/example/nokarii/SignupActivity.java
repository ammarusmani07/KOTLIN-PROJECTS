package com.example.nokarii;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB,
            editTextRegisterPwd, editTextRegisterConfirmPwd;

    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender, radioGroupRegisterJobType;
    private RadioButton radioButtonRegisterGenderSelected, radioButtonRegisterJobTypeSelected;

    private DatePickerDialog picker;

    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button buttonRegister = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confrim_password);


        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        radioGroupRegisterJobType = findViewById(R.id.radio_group_register_job_type);
        radioGroupRegisterJobType.clearCheck();
        AutoCompleteTextView autoCompleteTextViewProfession = findViewById(R.id.autoCompleteTextViewProfession);
        AutoCompleteTextView autoCompleteTextViewExperience = findViewById(R.id.autoCompleteTextView_experience);
        AutoCompleteTextView autoCompleteTextViewqual = findViewById(R.id.autoCompleteTextViewe_qual);

 AutoCompleteTextView autoCompleteTextViewSalary = findViewById(R.id.autoCompleteTextViewSalary);
        AutoCompleteTextView autoCompleteTextViewMobile = findViewById(R.id.autoCompleteTextViewMobile);
// List of professions
        String[] professions = { "Software Engineer",
                "Web Developer",
                "Data Scientist",
                "Network Engineer",
                "Database Administrator",
                "System Administrator",
                "DevOps Engineer",
                "UI/UX Designer",
                "Security Analyst",
                "IT Manager",
                "Quality Assurance (QA) Engineer",
                "Business Analyst",
                "Project Manager (IT)",
                "Technical Support Specialist",
                "Cloud Architect",
                "Mobile App Developer", // Added "Mobile App Developer"
                "Game Developer",
                "Artificial Intelligence (AI) Engineer",

                "Doctor",
                "Teacher",
                "Designer",
                "Engineer",
                "Manager",
                "Artist",
                "Lawyer",
                "Accountant",
                "Marketing Specialist",
                "Human Resources (HR) Manager",
                "Chef",
                "Writer",
                "Photographer",
                "Police Officer",
                "Firefighter",
                "Pilot",
                "Athlete",
                "Musician"};
        String[] salaryExpectations = {"15000k - 30,000k", "30,000k - 50,000k", "50,000k - 70,000k", "70,000k - 100,000k", "Over 100,000k"};
        String[] Qualification = {"8th","Matric","2ndyear","undergraduate"};
        String[] Experience = {"1month - 6month", "6month - 1year", "1year - 18months", "2year - 3year", "Over 4years"};
        String[]  Mobile = {"030","031","032","033","034","0355"};

// Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, professions);
// Set the adapter to the AutoCompleteTextView
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, salaryExpectations);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Mobile);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Experience);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, Qualification);
        autoCompleteTextViewProfession.setAdapter(adapter);
        autoCompleteTextViewMobile.setAdapter(adapter3);
        autoCompleteTextViewSalary.setAdapter(adapter2);
        autoCompleteTextViewExperience.setAdapter(adapter4);
        autoCompleteTextViewqual.setAdapter(adapter5);
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                picker.show();
            }
        });

        buttonRegister.setOnClickListener(view -> {
            int selectGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
            radioButtonRegisterGenderSelected = findViewById(selectGenderId);

            int selectJobTypeId = radioGroupRegisterJobType.getCheckedRadioButtonId();
            radioButtonRegisterJobTypeSelected = findViewById(selectJobTypeId);

            String textProfession = autoCompleteTextViewProfession.getText().toString();
            String textSalary = autoCompleteTextViewSalary.getText().toString();
            String textFullName = editTextRegisterFullName.getText().toString();
            String textEmail = editTextRegisterEmail.getText().toString();
            String textDob = editTextRegisterDoB.getText().toString();
            String textMobile = autoCompleteTextViewMobile.getText().toString();
            String textPwd = editTextRegisterPwd.getText().toString();
            String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
            String textGender;
            String textJobType;
            String textExperience=autoCompleteTextViewExperience.getText().toString();
            String textQualification=autoCompleteTextViewqual.getText().toString();

            String mobileRegex = "03[0-9]{9}";
            Matcher mobileMatcher;
            Pattern mobilePattern = Pattern.compile(mobileRegex);
            mobileMatcher = mobilePattern.matcher(textMobile);

            if (TextUtils.isEmpty(textFullName) || TextUtils.isEmpty(textEmail) || !Patterns.EMAIL_ADDRESS.matcher(textEmail).matches() ||
                    TextUtils.isEmpty(textDob) || TextUtils.isEmpty(textMobile) || !mobileMatcher.find() || textMobile.length() != 11 ||
                    TextUtils.isEmpty(textPwd) || textPwd.length() < 6 || TextUtils.isEmpty(textConfirmPwd) || !textConfirmPwd.equals(textPwd)) {
                // Your existing validation logic
                Toast.makeText(SignupActivity.this, "Validation failed. Please check your inputs.", Toast.LENGTH_SHORT).show();
            } else {
                textGender = radioButtonRegisterGenderSelected.getText().toString();
                textJobType = radioButtonRegisterJobTypeSelected.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                registerUser(textFullName, textEmail, textDob, textGender, textMobile, textPwd, textJobType, textProfession, textSalary, textExperience,textQualification);
            }
        });
    }

    private void registerUser(String textFullName, String textEmail, String textDob, String textGender, String textMobile, String textPwd, String textJobType, String textProfession, String textSalary ,String textExperience,String textQualification) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDob, textGender, textMobile, textFullName, textEmail, "", textJobType, textProfession, textSalary,textExperience,textQualification);

                    DatabaseReference referenceProfile;
                    if ("Job Seeker".equals(textJobType)) {
                        referenceProfile = FirebaseDatabase.getInstance().getReference("JobSeekerUsers");
                    } else {
                        referenceProfile = FirebaseDatabase.getInstance().getReference("JobGiverUsers");
                    }

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(SignupActivity.this, "User registered successfully, please verify your email", Toast.LENGTH_SHORT).show();

                                // Save user type to SharedPreferences
                                saveUserType(textJobType);

                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);

                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "User registration failed, try again", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    firebaseUser.sendEmailVerification();
                } else {
                    // Handle registration failure
                    Toast.makeText(SignupActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void saveUserType(String userType) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userType", userType);
        editor.apply();
    }
}