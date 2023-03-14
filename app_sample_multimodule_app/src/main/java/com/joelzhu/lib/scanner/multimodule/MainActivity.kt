package com.joelzhu.lib.scanner.multimodule

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.joelzhu.lib.scanner.runtime.Scanner

/**
 * MainActivity.
 *
 * @author JoelZhu
 * @since 2023-03-13
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (findViewById<View>(R.id.tvContentClasses) as TextView).text = printAllAnnotatedClasses()
    }

    private fun printAllAnnotatedClasses(): String {
        val stringBuilder = StringBuilder()
        val classes = Scanner.getAnnotatedClasses()
        for (clazz in classes) {
            if (!ICommand::class.java.isAssignableFrom(clazz)) {
                continue
            }
            try {
                val command = clazz.newInstance() as ICommand
                stringBuilder.append(command.execute()).append("\n")
            } catch (exception: IllegalAccessException) {
                exception.printStackTrace()
            } catch (exception: InstantiationException) {
                exception.printStackTrace()
            }
        }
        return stringBuilder.toString()
    }
}