package com.example.kazan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kazan.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  IOTask{

    EditText edUsername, edPassword;
    Button btnOK;
    public static ArrayList<Employee> employees = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        requestListEmployee();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edUsername.getText().toString().trim().isEmpty()){
                    edUsername.setError("Fill Username textbox!");
                    return;
                }
                if(edPassword.getText().toString().trim().isEmpty()){
                    edPassword.setError("Fill Password textbox!");
                    return;
                }
                String username;
                String password;
                for (Employee employee: employees) {
                    username = edUsername.getText().toString();
                    password = edPassword.getText().toString();
                        if(username.equals(employee.getUsername())){
                            if(password.equals(employee.getPassword())){
                                Intent intent = new Intent(MainActivity.this, AvtMenu.class);
                                if(employee.isAdmin()){
                                    intent.putExtra("isAdmin", employee.isAdmin());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
                                    return;
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Only admin can use this app", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                edPassword.setError("Password incorrect");
                                return;
                            }
                        }
                }
                edUsername.setError("Account not exist!");
                edPassword.setError("Account not exist!");
            }
        });
    }

    private void requestListEmployee() {
        RequestAPI getEms = new RequestAPI(MainActivity.this,0, "employees","GET","");
        getEms.execute();
    }

    private void findView() {
        edPassword = findViewById(R.id.edit_password);
        edUsername = findViewById(R.id.edit_username);
        btnOK = findViewById(R.id.btn_ok);
    }


    @Override
    public void doIt(int requestcode, String jsonData, int resultCode) {
        if(requestcode==0){
            try {
                getListEmployee(jsonData);
            } catch (JSONException e) {
            }
        }
    }

    private void getListEmployee(String jsonData) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonData);
        for (int i  = 0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(!jsonObject.getString("Username").equals("null")){
                employees.add(new Employee(jsonObject));
            }
        }
    }
}