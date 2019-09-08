package com.laioffer.botlogistics;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;

        import android.os.Bundle;

public class MainActivity extends AppCompatActivity  implements TransactionManager{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doTransctinonFragment(new DeliveryFragment());

    }

    @Override
    public void doTransctinonFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "").commit();
    }
}
