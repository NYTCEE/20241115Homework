package com.example.Rebuild9_2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.Rebuild9_2.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    data class HealthMetrics(
        val standardWeight: Double,
        val bodyFat: Double,
        val bmi: Double
    )

    companion object {
        private const val PROGRESS_DELAY = 50L
        private const val MAX_PROGRESS = 100
        private const val MALE_WEIGHT_FACTOR = 0.7
        private const val FEMALE_WEIGHT_FACTOR = 0.6
        private const val MALE_HEIGHT_ADJUSTMENT = 80
        private const val FEMALE_HEIGHT_ADJUSTMENT = 70
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupCalculateButton()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupCalculateButton() {
        binding.btnCalculate.setOnClickListener {
            if (validateInputs()) {
                startCalculation()
            }
        }
    }

    private fun validateInputs() = when {
        binding.edHeight.text.isEmpty() -> {
            showToast("請輸入身高")
            false
        }
        binding.edWeight.text.isEmpty() -> {
            showToast("請輸入體重")
            false
        }
        binding.edAge.text.isEmpty() -> {
            showToast("請輸入年齡")
            false
        }
        else -> true
    }

    private fun startCalculation() {
        resetResults()
        showProgressBar()

        lifecycleScope.launch {
            try {
                updateProgress()
                val metrics = calculateHealthMetrics()
                displayResults(metrics)
            } finally {
                binding.llProgress.visibility = View.GONE
            }
        }
    }

    private fun resetResults() = binding.apply {
        tvWeightResult.text = "標準體重\n無"
        tvFatResult.text = "體脂肪\n無"
        tvBmiResult.text = "BMI\n無"
        progressBar.progress = 0
        tvProgress.text = "0%"
    }

    private fun showProgressBar() {
        binding.llProgress.visibility = View.VISIBLE
    }

    private suspend fun updateProgress() {
        for (progress in 1..MAX_PROGRESS) {
            delay(PROGRESS_DELAY)
            withContext(Dispatchers.Main) {
                binding.progressBar.progress = progress
                binding.tvProgress.text = "$progress%"
            }
        }
    }

    private suspend fun calculateHealthMetrics(): HealthMetrics = withContext(Dispatchers.Default) {
        val height = binding.edHeight.text.toString().toDouble()
        val weight = binding.edWeight.text.toString().toDouble()
        val age = binding.edAge.text.toString().toDouble()
        val bmi = weight / ((height / 100).pow(2))

        if (binding.btnBoy.isChecked) {
            HealthMetrics(
                standardWeight = (height - MALE_HEIGHT_ADJUSTMENT) * MALE_WEIGHT_FACTOR,
                bodyFat = 1.39 * bmi + 0.16 * age - 19.34,
                bmi = bmi
            )
        } else {
            HealthMetrics(
                standardWeight = (height - FEMALE_HEIGHT_ADJUSTMENT) * FEMALE_WEIGHT_FACTOR,
                bodyFat = 1.39 * bmi + 0.16 * age - 9,
                bmi = bmi
            )
        }
    }

    private fun displayResults(metrics: HealthMetrics) = binding.apply {
        tvWeightResult.text = "標準體重\n${String.format("%.2f", metrics.standardWeight)}"
        tvFatResult.text = "體脂肪\n${String.format("%.2f", metrics.bodyFat)}"
        tvBmiResult.text = "BMI\n${String.format("%.2f", metrics.bmi)}"
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}