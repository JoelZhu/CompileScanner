package com.joelzhu.lib.scanner.java;

import com.joelzhu.lib.scanner.java.utils.Constants;
import com.joelzhu.lib.scanner.runtime.Scanner;
import com.joelzhu.lib.scanner.runtime.core.Options;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        ((TextView) findViewById(R.id.tvWomanDoing)).setText(printWomanDailyDoing());
    }

    private String printHumanDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Options options = new Options.Builder().create();
        final ICommand[] commands = Scanner.getAnnotatedInstances(options, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String printManDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Options options = new Options.Builder().category(Constants.TAG_MAN).create();
        final ICommand[] commands = Scanner.getAnnotatedInstances(options, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String printWomanDailyDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Options options = new Options.Builder().category(Constants.TAG_WOMAN).create();
        final ICommand[] commands = Scanner.getAnnotatedInstances(options, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }
}