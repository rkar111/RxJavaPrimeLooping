package xyz.arkarhein.rxjavaasynchronous;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_ok)
    Button btnOK;

    @BindView(R.id.tv_input)
    EditText etInput;

    @BindView(R.id.tv_result)
    TextView tvResult;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick(R.id.btn_ok)
    public void onTapBtnOk(View view) {
        operationExecution();
    }

    private void operationExecution() {
        tvResult.setText("");
        Single<int[]> integerSingle = Single.fromCallable(new Callable<int[]>() {
            @Override
            public int[] call() throws Exception {
                return calculatePrime();
            }
        });

        integerSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<int[]>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull int[] integer) {
                        for (int i = 0; i < count; i++)
                            tvResult.setText(tvResult.getText().toString() + ", " + integer[i]);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private int[] calculatePrime() {
        int b = 0;
        int[] k = new int[1000];
        for (int i = 3; i <= 1000; i++) {
            boolean isPrime = true;
            for (int j = 2; j <= i / 2 && isPrime; j++) {
                isPrime = i % j > 0;
            }
            if (isPrime) {
                k[b] = i;
                b++;
            }
        }
        Log.d("RxJava", "Array Size" + k[0]);
        return k;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
