package com.example.a7minutesworkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.BuildConfig
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null
    private var directDownloadLink : String = "https://drive.google.com/file/d/1jpRUFgtFIC9i1rL4y6a6c8V2pRh65B2q/view?usp=sharing"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.flStart?.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBMI?.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }


        binding?.flHistory?.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // Initially hide other views
        binding?.flBMI?.visibility = View.INVISIBLE
        binding?.flStart?.visibility = View.INVISIBLE
        binding?.flHistory?.visibility = View.INVISIBLE
        binding?.tvHistory?.visibility = View.INVISIBLE
        binding?.tvCalculator?.visibility = View.INVISIBLE
        binding?.flShare?.visibility = View.INVISIBLE
        binding?.tvShare?.visibility = View.INVISIBLE

        // Load the fade-in animation (you can create custom animations as needed)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        fadeInAnimation.duration = 2000 // Set duration for the animation

        // Start animation on the ImageView
        binding?.myImageView?.startAnimation(fadeInAnimation)

        // Set listener to show other views when the animation ends
        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Animation has started
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Animation has ended, now show the other views
                binding?.flStart?.visibility = View.VISIBLE
                binding?.flBMI?.visibility = View.VISIBLE
                binding?.flHistory?.visibility = View.VISIBLE
                binding?.flShare?.visibility = View.VISIBLE
                binding?.tvHistory?.visibility = View.VISIBLE
                binding?.tvCalculator?.visibility = View.VISIBLE
                binding?.tvShare?.visibility = View.VISIBLE

                // You can also animate the showing of these views if needed
                binding?.flStart?.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, android.R.anim.fade_in))
                binding?.flBMI?.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, android.R.anim.fade_in))
                binding?.flHistory?.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, android.R.anim.fade_in))
                binding?.flShare?.startAnimation(AnimationUtils.loadAnimation(this@MainActivity, android.R.anim.fade_in))
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Handle animation repeat if needed
            }
        })

        binding?.flShare?.setOnClickListener {
            shareAPK()        }

    }

    private fun shareAPK() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
        var shareMessage = "7minutesWorkout app,Download the app Now: "
        shareMessage = shareMessage + directDownloadLink + "\n\n"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}