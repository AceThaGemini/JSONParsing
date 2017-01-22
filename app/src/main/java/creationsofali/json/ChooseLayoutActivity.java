package creationsofali.json;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseLayoutActivity extends AppCompatActivity {

    String CODE = "viewCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_layout);

        findViewById(R.id.buttonListView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listView = new Intent(ChooseLayoutActivity.this, MainActivity.class);
                listView.putExtra(CODE, "l");
                startActivity(listView);
            }
        });

        findViewById(R.id.buttonRecyclerView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recyclerView = new Intent(ChooseLayoutActivity.this, MainActivity.class);
                recyclerView.putExtra(CODE, "r");
                startActivity(recyclerView);
            }
        });
    }
}
