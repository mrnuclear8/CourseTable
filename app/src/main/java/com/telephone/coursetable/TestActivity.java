package com.telephone.coursetable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.telephone.coursetable.Database.AppDatabase;
import com.telephone.coursetable.Database.CETDao;
import com.telephone.coursetable.Database.ClassInfoDao;
import com.telephone.coursetable.Database.ExamInfoDao;
import com.telephone.coursetable.Database.GoToClassDao;
import com.telephone.coursetable.Database.GradesDao;
import com.telephone.coursetable.Database.GraduationScoreDao;
import com.telephone.coursetable.Database.LABDao;
import com.telephone.coursetable.Database.PersonInfoDao;
import com.telephone.coursetable.Database.TermInfoDao;
import com.telephone.coursetable.Database.UserDao;
import com.telephone.coursetable.LogMe.LogMe;

public class TestActivity extends AppCompatActivity {

    //DAOs of the database of the whole app
    private GoToClassDao gdao = null;
    private ClassInfoDao cdao = null;
    private TermInfoDao tdao = null;
    private UserDao udao = null;
    private PersonInfoDao pdao = null;
    private GraduationScoreDao gsdao = null;
    private GradesDao grdao = null;
    private ExamInfoDao edao = null;
    private CETDao cetDao = null;
    private LABDao labDao = null;
    private SharedPreferences pref = null;
    private SharedPreferences.Editor editor = null;

    private EditText editText;
    private TextView textView;
    private Button button1;
    private Button buttonClearOutput;
    private Button buttonClearInput;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        AppDatabase db = MyApp.getCurrentAppDB();

        gdao = db.goToClassDao();
        cdao = db.classInfoDao();
        tdao = db.termInfoDao();
        udao = db.userDao();
        pdao = db.personInfoDao();
        gsdao = db.graduationScoreDao();
        grdao = db.gradesDao();
        edao = db.examInfoDao();
        cetDao = db.cetDao();
        labDao = db.labDao();

        pref = MyApp.getCurrentSharedPreference();
        editor = pref.edit();

        editText = findViewById(R.id.test_activity_et);
        textView = findViewById(R.id.test_activity_tv);
        button1 = findViewById(R.id.test_activity_btn1);
        buttonClearOutput = findViewById(R.id.test_activity_btn_clear_output);
        buttonClearInput = findViewById(R.id.test_activity_btn_clear_input);
        scrollView = findViewById(R.id.test_activity_sv);

    }

    public void test_button1(View view){
        final String NAME = "test_button1()";
        String input = editText.getText().toString();
        print("输入：\n" + input + "\n================================");
        new Thread(()->{
            Login.deleteOldDataFromDatabase(gdao, cdao, tdao, pdao, gsdao, grdao, edao, cetDao, labDao);
            print("数据库已清空（除了用户数据库）");
            udao.deleteAll();
            print("用户数据库已清空");
            print("拉取数据中...");
            if (Login_vpn.fetch_merge(TestActivity.this, input, pdao, tdao, gdao, cdao, gsdao, grdao, edao, cetDao, labDao, editor)){
                print("拉取成功");
            }else {
                print("拉取失败");
            }
        }).start();
    }

    public void clearOutput(View view){
        final String NAME = "clearOutput()";
        textView.setText("");
    }

    public void clearInput(View view){
        final String NAME = "clearInput()";
        editText.setText("");
    }

    private void print(String text){
        runOnUiThread(()->{
            textView.setText(textView.getText() + text + "\n");
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }
}