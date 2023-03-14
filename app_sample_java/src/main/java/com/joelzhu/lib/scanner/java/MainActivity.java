package com.joelzhu.lib.scanner.java;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.joelzhu.lib.scanner.java.utils.Constants;
import com.joelzhu.lib.scanner.runtime.Scanner;

/**
 * MainActivity.
 *
 * @author JoelZhu
 * @since 2023-01-27
 */
public final class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.tvHumanDoing)).setText(printHumanDoing());
        ((TextView) findViewById(R.id.tvManDoing)).setText(printManDoing());
        ((TextView) findViewById(R.id.tvWomanDoing)).setText(printWomanDoing());
    }

    private String printHumanDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final ICommand[] commands = Scanner.getAnnotatedInstances(ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String printManDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final ICommand[] commands = Scanner.getAnnotatedInstances(Constants.TAG_MAN, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String printWomanDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final ICommand[] commands = Scanner.getAnnotatedInstances(Constants.TAG_WOMAN, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }
}