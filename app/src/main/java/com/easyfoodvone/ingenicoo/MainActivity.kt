package com.ingenico.pushyclient

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.pushy.sdk.Pushy


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Pushy.listen(this)
    /*    setContentView(R.layout.activity_main)

        val b1 = findViewById<Button>(R.id.button)
        val b2 = findViewById<Button>(R.id.button2)
        val b3 = findViewById<Button>(R.id.button3)
        val b4 = findViewById<Button>(R.id.button4)

        b1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (!Pushy.isRegistered(this@MainActivity)) {
                        val deviceToken = Pushy.register(this@MainActivity)
                        withContext(Dispatchers.Main) {
                            try {
                                onPostExecute(
                                    this@MainActivity,
                                    "register",
                                    deviceToken
                                );
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        dialogMessage(
                            this@MainActivity,
                            getString(R.string.unregister),
                            getString(R.string.term_registered)
                        )
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        b2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (Pushy.isRegistered(this@MainActivity)) {
                        Pushy.unregister(this@MainActivity)
                        withContext(Dispatchers.Main) {
                            try {
                                dialogMessage(
                                    this@MainActivity, getString(R.string.unregister), getString(
                                        R.string.term_unregistered
                                    )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        dialogMessage(
                            this@MainActivity,
                            getString(R.string.unregister),
                            getString(R.string.term_not_registered)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        b3.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (Pushy.isRegistered(this@MainActivity)) {
                        Pushy.subscribe("news", this@MainActivity)
                        withContext(Dispatchers.Main) {
                            try {
                                dialogMessage(
                                    this@MainActivity, getString(R.string.subscribe), getString(
                                        R.string.term_subscribed
                                    )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        dialogMessage(
                            this@MainActivity,
                            getString(R.string.unregister),
                            getString(R.string.term_not_registered)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        b4.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (Pushy.isRegistered(this@MainActivity)) {
                        Pushy.unsubscribe("news", this@MainActivity)
                        withContext(Dispatchers.Main) {
                            try {
                                dialogMessage(
                                    this@MainActivity, getString(R.string.unsubscribe), getString(
                                        R.string.term_unsubscribed
                                    )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        dialogMessage(
                            this@MainActivity,
                            getString(R.string.unregister),
                            getString(R.string.term_not_registered)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    */}

    fun onPostExecute(activity: Activity, title: String, result: Any) {
        var message: String

        if (result is Exception) {
            message = result.message.toString()
        }
        else {
            message = "Pushy device token: " + result.toString()
        }
        dialogMessage(activity, title, message)
    }

    fun dialogMessage(activity: Activity, title: String, message: String) {
        activity.runOnUiThread {
            android.app.AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
    }
}
