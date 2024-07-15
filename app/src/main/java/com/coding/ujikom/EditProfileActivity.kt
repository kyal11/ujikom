package com.coding.ujikom

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.coding.ujikom.databinding.ActivityEditProfileBinding
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.edtDate.setOnClickListener {
            showDatePicker()
        }
        binding.switchEdit.isChecked = false
        lockProfile(false)
        binding.switchEdit.setOnCheckedChangeListener { _, isChecked ->
            lockProfile(isChecked)
        }
        val profile = intent.getParcelableExtra<Profile>("profile")
        profile?.let { showProfile(it) }
    }

    private fun showProfile(profile: Profile) {
        binding.edtNama.setText(profile.name)
        binding.edtNim.setText(profile.nim)
        binding.edtDate.setText(profile.date)
        binding.edtPlace.setText(profile.place)

        if (profile.gender == "Laki-laki") {
            binding.radioGroup.check(R.id.rb_male)
        } else {
            binding.radioGroup.check(R.id.rb_female)
        }

        if (!profile.imageUri.isNullOrEmpty()) {
            binding.ivProfile.setImageURI(Uri.parse(profile.imageUri))
        }
    }
    private fun lockProfile(isChecked: Boolean) {
        if (isChecked) {
            binding.edtNama.isEnabled = true
            binding.edtNim.isEnabled = true
            binding.edtDate.isEnabled = true
            binding.edtPlace.isEnabled = true
            binding.radioGroup.isEnabled = true
            binding.rbMale.isEnabled = true
            binding.rbFemale.isEnabled = true
        } else {
            binding.edtNama.isEnabled = false
            binding.edtNim.isEnabled = false
            binding.edtDate.isEnabled = false
            binding.edtPlace.isEnabled = false
            binding.radioGroup.isEnabled = false
            binding.rbMale.isEnabled = false
            binding.rbFemale.isEnabled = false
        }
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth+1, selectedYear)
                binding.edtDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}
