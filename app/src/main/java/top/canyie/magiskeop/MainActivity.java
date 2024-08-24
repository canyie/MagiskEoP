package top.canyie.magiskeop;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

/**
 * @author canyie
 */
public class MainActivity extends Activity {
    private EditText input;
    private TextView output;
    private Button execute;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Handler handler = new Handler();
        input = findViewById(R.id.cmd);
        output = findViewById(R.id.text);
        execute = findViewById(R.id.execute);
        output.append("- Current UID = " + Process.myUid() + " PID = " + Process.myPid() + "\n");
        output.append("- Go go go!\n");
        output.append("- Testing status...\n");

        if (MyProvider.out != null) {
            long delay = 1; // 5000
            handler.postDelayed(() -> {
                output.append("- Exploit success! Status: OK\n");
                output.append("- Received root shell!\n");
                Thread readThread = new Thread(() -> {
                    String line;
                    try {
                        while ((line = MyProvider.in.readLine()) != null) {
                            String finalLine = line;
                            handler.post(() -> output.append(finalLine + "\n"));
                        }
                    } catch (IOException e) {
                        handler.post(() -> {
                            output.append("! Error reading shell reply\n");
                            output.append(Log.getStackTraceString(e));
                            execute.setEnabled(false);
                        });
                    }
                });
                readThread.start();
                execute.setOnClickListener(v -> {
                    String command = input.getText().toString();
                    input.setText("");
                    try {
                        MyProvider.out.write(command + "\n");
                        MyProvider.out.flush();
                    } catch (IOException e) {
                        output.append("! Error writing command to shell\n");
                        output.append(Log.getStackTraceString(e));
                    }
                });
            }, delay);
        } else {
            output.append("- Exploit failed... Status: FAILED");
            execute.setEnabled(false);
        }
    }
}
