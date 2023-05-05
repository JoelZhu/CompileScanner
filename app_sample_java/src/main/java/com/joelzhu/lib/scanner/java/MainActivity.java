package com.joelzhu.lib.scanner.java;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.joelzhu.lib.scanner.java.utils.Constants;
import com.joelzhu.lib.scanner.runtime.Scanner;
import com.joelzhu.lib.scanner.runtime.core.Options;

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
        final Options options = new Options.Builder().listAllTags().listAllGroups().create();
        final ICommand[] commands = Scanner.getAnnotatedInstances(options, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String printManDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Options options = new Options.Builder().tag(Constants.TAG_MAN).listAllGroups().create();
        final ICommand[] commands = Scanner.getAnnotatedInstances(options, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String printWomanDailyDoing() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Options options = new Options.Builder().tag(Constants.TAG_WOMAN).group(Constants.GROUP_DAILY).create();
        final ICommand[] commands = Scanner.getAnnotatedInstances(options, ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }
}