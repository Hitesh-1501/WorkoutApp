package com.example.auraFitAI.presentation.dashboard

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.auraFitAI.presentation.WorkOutApplication
import com.example.auraFitAI.data.local.HistoryDao
import com.example.auraFitAI.data.local.HistoryEntity
import com.example.auraFitAI.databinding.ActivityFinishBinding
import com.example.auraFitAI.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinishActivity : AppCompatActivity() {
    private var binding : ActivityFinishBinding? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener{
            finish()
        }

        try {
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.press_start)
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val historyDao = (application as WorkOutApplication).db.historyDao()
        addDateToDatabase(historyDao)
    }

    private fun addDateToDatabase(historyDao: HistoryDao) {
        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date : ", "" + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted Date : ", "" + date)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
            Log.e("Date : ", "Added locally to Room...")

            if (uid != null) {
                try {
                    val workoutRecord = hashMapOf(
                        "date" to date,
                        "timestamp" to System.currentTimeMillis()
                    )
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .collection("workouts")
                        .add(workoutRecord)
                        .await()
                    Log.e("Firestore", "Workout synced successfully to cloud")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        binding = null
    }
}
