package com.joelzhu.lib.scanner.java;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        ((TextView) findViewById(R.id.tvContentClasses)).setText(printHumanLifeInADayByClasses());
        ((TextView) findViewById(R.id.tvContentInstances)).setText(printHumanLifeInADayByInstances());
    }

    private String printHumanLifeInADayByClasses() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Class<?>[] classes = Scanner.getAnnotatedClasses();
        for (final Class<?> clazz : classes) {
            if (!ICommand.class.isAssignableFrom(clazz)) {
                continue;
            }

            try {
                final ICommand command = (ICommand) clazz.newInstance();
                stringBuilder.append(command.execute()).append("\n");
            } catch (IllegalAccessException | InstantiationException exception) {
                exception.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    private String printHumanLifeInADayByInstances() {
        final StringBuilder stringBuilder = new StringBuilder();
        final ICommand[] commands = Scanner.getAnnotatedInstances("", ICommand.class);
        for (final ICommand command : commands) {
            stringBuilder.append(command.execute()).append("\n");
        }
        return stringBuilder.toString();
    }
}