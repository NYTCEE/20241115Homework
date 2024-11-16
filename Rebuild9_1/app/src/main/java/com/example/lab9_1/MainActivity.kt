package com.example.Rebuild9_1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.Rebuild9_1.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var raceJob: Job? = null

    companion object {
        private const val RACE_FINISH_LINE = 100
        private const val RABBIT_STEP = 3
        private const val TURTLE_STEP = 1
        private const val BASE_DELAY = 100L
        private const val RABBIT_SLEEP_DELAY = 300L
        private const val RABBIT_SLEEP_PROBABILITY = 0.66
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupRaceButton()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRaceButton() {
        binding.btnStart.setOnClickListener {
            startNewRace()
        }
    }

    private fun startNewRace() {
        binding.apply {
            btnStart.isEnabled = false
            sbRabbit.progress = 0
            sbTurtle.progress = 0
        }

        raceJob?.cancel()
        raceJob = lifecycleScope.launch {
            val rabbitJob = async { runRabbit() }
            val turtleJob = async { runTurtle() }

            // 使用 select 等待任一作業完成
            val winner = select<String> {
                rabbitJob.onAwait { it }
                turtleJob.onAwait { it }
            }

            // 終止比賽並更新 UI
            raceJob?.cancel()
            showWinner(winner)
            binding.btnStart.isEnabled = true
        }
    }

    private suspend fun runRabbit(): String = withContext(Dispatchers.Default) {
        var progress = 0
        while (progress < RACE_FINISH_LINE && isActive) {
            delay(BASE_DELAY)
            if (Random.nextDouble() < RABBIT_SLEEP_PROBABILITY) {
                delay(RABBIT_SLEEP_DELAY)
            }
            progress += RABBIT_STEP
            withContext(Dispatchers.Main) {
                binding.sbRabbit.progress = progress
            }
        }
        "兔子"
    }

    private suspend fun runTurtle(): String = withContext(Dispatchers.Default) {
        var progress = 0
        while (progress < RACE_FINISH_LINE && isActive) {
            delay(BASE_DELAY)
            progress += TURTLE_STEP
            withContext(Dispatchers.Main) {
                binding.sbTurtle.progress = progress
            }
        }
        "烏龜"
    }

    private fun showWinner(winner: String) {
        Toast.makeText(this, "${winner}勝利", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        raceJob?.cancel()
    }
}
