package com.example.a7minutesworkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding : ActivityExerciseBinding? = null
    //TODO(Step 1 - Adding a variables for the 10 seconds REST timer.)
    //START
    private var restTimer : CountDownTimer? = null // Variable for Rest Timer and later on we will initialize it.
    private var restProgress = 0 // Variable for timer progress. As initial value the rest progress is set to 0. As we are about to start.
    private var restTimerDuration: Long = 5
    //END
    private var exerciseTimerDuration: Long = 30
    private var exerciseTimer : CountDownTimer? = null  // Variable for Exercise Timer and later on we will initialize it.
    private var exerciseProgress = 0  // Variable for the exercise timer progress. As initial value the exercise progress is set to 0. As we are about to start.

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1   // Current Position of Exercise.

    private var tts : TextToSpeech? = null  // variable for tts
    private var player : MediaPlayer? = null  // Declaring the variable of the media player for playing a notification sound when the exercise is about to start

   private var exerciseAdapter : ExerciseStatusAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // Disable screenshots and screen recording for this activity
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        // then set support action bar and get toolBarExcercise using the binding
        //variable
        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        //Initializing and Assigning a default exercise list to our list variable
        exerciseList = Constants.defaultExerciseList()

        /*
            Set a default failure listener that will be called if any of the setAnimation APIs fail for any reason.
            This can be used to replace the default behavior.
         */
        binding?.lottieAnimationView?.setFailureListener{
            Log.e("Lootle","Failed to load animation")
        }

        tts = TextToSpeech(this,this)

        //TODO(Step 4 - Calling the function to make it visible on screen.)-->
        //START
        setupRestView() // REST View is set in this function
        setupExerciseStatusRecycleView()

        // Add listeners for skip buttons
        binding?.ivSkipNext?.setOnClickListener {
            // Cancel the current timer if running
            if (exerciseTimer != null) {
                exerciseTimer?.cancel()
                exerciseProgress = 0
            }

            // Move to the next exercise if possible
            if (currentExercisePosition < exerciseList!!.size - 1) {
                // Mark the current exercise as completed
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()

                // Move to the next exercise
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                // Setup the view for the next exercise
                setupExerciseView()
            } else {
                // If no more exercises, go to finish screen
                finish()
                val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                startActivity(intent)
            }
        }
        binding?.ivSkipBackward?.setOnClickListener {
            // Cancel the current timer if running
            if (exerciseTimer != null) {
                exerciseTimer?.cancel()
                exerciseProgress = 0
            }

            // Move to the previous exercise if possible
            if (currentExercisePosition > 0) {
                // Mark the current exercise as not completed
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseAdapter!!.notifyDataSetChanged()

                // Move to the previous exercise
                currentExercisePosition--
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseList!![currentExercisePosition].setIsCompleted(false)
                exerciseAdapter!!.notifyDataSetChanged()

                // Setup the view for the previous exercise
                setupExerciseView()
            } else {
                // Optional: If at the beginning, stay on the first exercise or show a message
                Toast.makeText(this, "Already at the first exercise", Toast.LENGTH_SHORT).show()
            }
        }

    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        customDialogForBackButton()
        //super.onBackPressed()
    }

    private fun customDialogForBackButton(){
        val customDialog  = Dialog(this)
        val dialogBinding  = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecycleView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    //TODO(Step 3 - Setting up the Get Ready View with 10 seconds of timer.)-->
    //START
    /**
     * Function is used to set the timer for REST.
     */
    private fun setupRestView() {
        /*
            Playing a notification sound when the exercise is about to start when you are in the rest state
            the sound file is added in the raw folder as resource.
         */
        /**
         * Here the sound file is added in to "raw" folder in resources.
         * And played using MediaPlayer. MediaPlayer class can be used to control playback
         * of audio/video files and streams.
         */

            try {
                val soundURI =
                    Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
                player = MediaPlayer.create(applicationContext, soundURI)
                player?.isLooping = false// Sets the player to be looping or non-looping.
                player?.start() // start playback
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding?.flRestView?.visibility = View.VISIBLE
            binding?.tvTitle?.visibility = View.VISIBLE
            binding?.tvExerciseName?.visibility = View.INVISIBLE
            binding?.lottieAnimationView?.visibility = View.INVISIBLE
            binding?.flExerciseView?.visibility = View.INVISIBLE
            binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
            binding?.tvUpcomingLabel?.visibility = View.VISIBLE
            binding?.ivSkipNext?.visibility = View.INVISIBLE
            binding?.ivSkipBackward?.visibility = View.INVISIBLE

        /*The function first checks if there is an existing restTimer running (if(restTimer != null)).
        If a timer is already running, it cancels it using restTimer?.cancel().
        After canceling the running timer, it resets the restProgress variable to 0.
        This variable tracks the progress of the rest timer, and resetting it ensures that the progress starts fresh when the timer is restarted.
        */
            if (restTimer != null) {
                restTimer?.cancel()
                restProgress = 0
            }

            // Here we have set the upcoming exercise name to the text view
            // Here as the current position is -1 by default so to selected from the list it should be 0 so we have increased it by +1.
            binding?.tvUpcomingExerciseName?.text =
                exerciseList!![currentExercisePosition + 1].getName()

            // This function is used to set the progress details.
            setResetProgressBar()
        }

    /**
     * Function is used to set the progress of timer using the progress
     */
    private fun setResetProgressBar(){

        binding?.progressBar?.progress = restProgress // Sets the current progress to the specified value.
        // Here we have started a timer of 10 seconds so the 10000 is milliseconds is 10 seconds and the countdown interval is 1 second so it 1000.
        restTimer = object : CountDownTimer(restTimerDuration*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++ // It is increased by 1
                binding?.progressBar?.progress = 5 - restProgress // Indicates progress bar progress
                binding?.tvTimer?.text = (5 - restProgress).toString() // Current progress is set to text view in terms of seconds.
            }

            override fun onFinish() {
                // When the 10 seconds will complete this will be executed.
                    currentExercisePosition++
                    // When we are getting an updated position of exercise set that item in the list as selected and notify the adapter class.
                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupExerciseView()

            }

        }.start()
    }
    /**
     * Function is used to set the progress of the timer using the progress for Exercise View.
     */
    private fun setupExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.lottieAnimationView?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.ivSkipNext?.visibility = View.VISIBLE
        binding?.ivSkipBackward?.visibility = View.VISIBLE
        /**
         * Here firstly we will check if the timer is running and it is not null then cancel the running timer and start the new one.
         * And set the progress to the initial value which is 0.
         */
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        // set Animation in Exercise View when 10 sec is complete animation is displayed
        binding?.lottieAnimationView?.setAnimation(exerciseList!![currentExercisePosition].getLottieAnimation())
        binding?.lottieAnimationView?.playAnimation()
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()

    }

    /**
     * Function is used to set the progress of the timer using the progress for Exercise View for 30 Seconds
     */
    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++ // It is increased by 1
                binding?.progressBarExercise?.progress = 30 - exerciseProgress // Indicates progress bar progress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString() // Current progress is set to text view in terms of seconds.
            }

            override fun onFinish() {
                // Updating the view after completing the 30 seconds exercise
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    //We have changed the status of the selected item and updated the status of that, so that the position is set as completed in the exercise list
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else {
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)

                }
            }

        }.start()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.ENGLISH)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this,"Lang Not Supported",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"initialization failed",Toast.LENGTH_SHORT).show()
        }

    }

    private fun speakOut(text : String){
        tts?.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        if(player!= null){
            player?.stop()
        }

        binding = null
    }

}