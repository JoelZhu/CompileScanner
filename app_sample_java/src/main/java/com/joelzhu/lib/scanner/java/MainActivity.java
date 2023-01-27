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
        final String humanLifeInADay = printHumanLifeInADay();
        ((TextView) findViewById(R.id.tvContent)).setText(humanLifeInADay);
    }

    private String printHumanLifeInADay() {
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
}